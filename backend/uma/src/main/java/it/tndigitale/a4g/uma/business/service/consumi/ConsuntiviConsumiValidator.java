package it.tndigitale.a4g.uma.business.service.consumi;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.MotivazioneConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.consumi.builder.CarburanteCompletoDtoBuilder;

@Component
public class ConsuntiviConsumiValidator {

	private static final String NO_CONSUMATO_DICHIARATO = "E’ necessario compilare il record consumato";
	private static final String NO_ALLEGATI = "Non ci sono allegati per il campo %s %s";
	private static final String NO_MOTIVAZIONE = "Nessuna Motivazione espressa per il campo %s %s";
	private static final String DATI_INCONSISTENTI = "Errore Salvataggio consuntivo %s %s";
	private static final String CONSUNTIVI_MAGGIORI_DISPONIBILE = "Attenzione! Le quantità digitate eccedono il carburante Disponibile";
	@Autowired
	private CarburanteHelper carburanteHelper;


	private Predicate<AllegatoConsuntivoModel> datiAllegatiAmmissibileNull = a -> a.getDescrizione() == null && a.getTipoAllegato() == null;

	private Predicate<ConsuntivoConsumiModel> allegatiNull = c -> CollectionUtils.isEmpty(c.getAllegati());

	private Predicate<ConsuntivoConsumiModel> motivazioneNull = c -> c.getMotivazione() == null;

	// UMA-03-05-H - Validazione *a valle* del salvataggio dichiarazione consumi - Prevede il passaggio di tutti i consuntivi prima della commit a db
	@Transactional(propagation = Propagation.MANDATORY)
	public void validaConsuntivi(DichiarazioneConsumiModel dichiarazione) {
		esisteAlmenoUnConsuntivoModel
		.andThen(valoriMinoriDelDisponibile)
		.accept(dichiarazione);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void validaAllegati(DichiarazioneConsumiModel dichiarazione) {
		checkAllegatiRecuperoModel
		.andThen(checkAllegatiAmmissibileModel)
		.andThen(consistenzaTipoRimanenzaEConsumatoModel)
		.accept(dichiarazione);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void validaAllegatiAmmissibile(DichiarazioneConsumiModel dichiarazione) {
		checkAllegatiAmmissibileModel.accept(dichiarazione);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void validaAllegatiRecupero(DichiarazioneConsumiModel dichiarazione) {
		checkAllegatiRecuperoModel.accept(dichiarazione);
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public void esisteConsumato(DichiarazioneConsumiModel dichiarazione) {
		esisteConsuntivoTipoConsumatoModel.accept(dichiarazione);
	}

	// Esiste Almeno un consuntivo
	private Consumer<DichiarazioneConsumiModel> esisteAlmenoUnConsuntivoModel = dichiarazione -> Assert.isTrue(!CollectionUtils.isEmpty(dichiarazione.getConsuntivi()), NO_CONSUMATO_DICHIARATO);
	// Esiste almeno un consuntivo per il tipo CONSUMATO
	private Consumer<DichiarazioneConsumiModel> esisteConsuntivoTipoConsumatoModel = dichiarazione -> Assert.isTrue(!dichiarazione.getConsuntivi().stream().noneMatch(c -> TipoConsuntivo.CONSUMATO.equals(c.getTipoConsuntivo())), NO_CONSUMATO_DICHIARATO);

	// Tutti i consuntivi di tipo RECUPERO hanno almeno un allegato E una motivazione FURTO oppure UTILIZZO_IMPROPRIO - Se la quantità è 0 non vanno caricati allegati
	private Consumer<DichiarazioneConsumiModel> checkAllegatiRecuperoModel = dichiarazione -> dichiarazione.getConsuntivi().stream().filter(c -> TipoConsuntivo.RECUPERO.equals(c.getTipoConsuntivo())).forEach(c -> {
		var msgNoAllegati = String.format(NO_ALLEGATI, c.getTipoConsuntivo(), c.getTipoCarburante());
		var msgNoMotivazione = String.format(NO_MOTIVAZIONE, c.getTipoConsuntivo(), c.getTipoCarburante());
		var msgDatiInconsistenti = String.format(DATI_INCONSISTENTI, c.getTipoConsuntivo().name(), c.getTipoCarburante().name());

		if (BigDecimal.ZERO.compareTo(c.getQuantita()) < 0) {
			Assert.isTrue(allegatiNull.negate().test(c), msgNoAllegati);
			Assert.isTrue(c.getAllegati().stream().allMatch(datiAllegatiAmmissibileNull), msgDatiInconsistenti);
			Assert.isTrue(MotivazioneConsuntivo.FURTO.equals(c.getMotivazione()) || MotivazioneConsuntivo.UTILIZZO_IMPROPRIO.equals(c.getMotivazione()), msgNoMotivazione);
		} else {
			Assert.isTrue(allegatiNull.or(motivazioneNull).test(c), msgDatiInconsistenti);
		}
	});

	// L'unico consuntivo di tipo AMMISSIBILE (se esiste) ha almeno un allegato se e solo se la quantità non coincide con il valore GASOLIO CONTO TERZI - ASSEGNATO/RICHIESTO della richiesta di carburante
	// inoltre deve avere una motivazione di tipo ALTRO oppure ASSEGNAZIONE_SVINCOLATA - Se la quantità è 0 non vanno caricati allegati
	private Consumer<DichiarazioneConsumiModel> checkAllegatiAmmissibileModel = dichiarazione -> {

		Optional<ConsuntivoConsumiModel> ammissibileOpt = dichiarazione.getConsuntivi().stream().filter(c -> TipoConsuntivo.AMMISSIBILE.equals(c.getTipoConsuntivo())).collect(CustomCollectors.collectOne());

		if (ammissibileOpt.isPresent()) {
			var ammissibile = ammissibileOpt.get();
			var msgDatiInconsistenti = String.format(DATI_INCONSISTENTI, ammissibile.getTipoConsuntivo().name(), ammissibile.getTipoCarburante().name());
			var msgNoAllegati = String.format(NO_ALLEGATI, ammissibile.getTipoConsuntivo(), ammissibile.getTipoCarburante());
			var msgNoMotivazione = String.format(NO_MOTIVAZIONE, ammissibile.getTipoConsuntivo(), ammissibile.getTipoCarburante());

			Integer gasolioTerzi = ammissibile.getDichiarazioneConsumi().getRichiestaCarburante().getGasolioTerzi();
			Assert.isTrue(TipoCarburanteConsuntivo.GASOLIO_TERZI.equals(ammissibile.getTipoCarburante()), msgDatiInconsistenti);

			// quantità diversa da 0 oppure dal valore di default => devono esserci allegati
			if (gasolioTerzi.compareTo(ammissibile.getQuantita().intValue()) != 0 && BigDecimal.ZERO.compareTo(ammissibile.getQuantita()) < 0) {
				Assert.isTrue(MotivazioneConsuntivo.ALTRO.equals(ammissibile.getMotivazione()) || MotivazioneConsuntivo.ASSEGNAZIONE_SVINCOLATA.equals(ammissibile.getMotivazione()), msgNoMotivazione); 
				Assert.isTrue(allegatiNull.negate().test(ammissibile), msgNoAllegati);
				Assert.isTrue(ammissibile.getAllegati().stream().allMatch(datiAllegatiAmmissibileNull.negate()), msgDatiInconsistenti);
			} else if (gasolioTerzi.compareTo(ammissibile.getQuantita().intValue()) == 0 && !CollectionUtils.isEmpty(ammissibile.getAllegati())) { // l'utente ha re-inserito il valore di default dopo aver caricato degli allegati
				throw new IllegalArgumentException("Consuntivo Ammissibile Gasolio conto terzi: E' necessario modificare la quantità oppure cancellare gli allegati");
			} else {	// quantità 0 oppure uguale al valore di defalt => non devono esserci allegati 
				Assert.isTrue(motivazioneNull.or(allegatiNull).test(ammissibile), msgDatiInconsistenti);
			}
		}
	};

	// Tutti i consuntivi di tipo RIMANENZA e CONSUMATO non devono avere motivazioni e non devono avere allegati
	private Consumer<DichiarazioneConsumiModel> consistenzaTipoRimanenzaEConsumatoModel = dichiarazione -> dichiarazione.getConsuntivi().stream().filter(c -> TipoConsuntivo.RIMANENZA.equals(c.getTipoConsuntivo()) || TipoConsuntivo.CONSUMATO.equals(c.getTipoConsuntivo())).forEach(c -> {
		var msgDatiInconsistenti = String.format(DATI_INCONSISTENTI, c.getTipoConsuntivo().name(), c.getTipoCarburante().name());
		Assert.isTrue(motivazioneNull.or(allegatiNull).test(c), msgDatiInconsistenti);
	});

	// in input dovrebbe avere cuaa e campagna per calcolare il disponibile 
	private Consumer<DichiarazioneConsumiModel> valoriMinoriDelDisponibile = dichiarazione -> {
		CarburanteCompletoDtoBuilder builder = new CarburanteCompletoDtoBuilder();
		List<ConsuntivoConsumiModel> consuntivi = dichiarazione.getConsuntivi();
		RichiestaCarburanteModel richiesta = dichiarazione.getRichiestaCarburante();

		var disponibile = carburanteHelper.calcolaDisponibile(richiesta);
		var consumato = builder.newDto().from(consuntivi, TipoConsuntivo.CONSUMATO).build().toCarburanteDto();
		var rimanenza = builder.newDto().from(consuntivi, TipoConsuntivo.RIMANENZA).build().toCarburanteDto();
		var recupero = builder.newDto().from(consuntivi, TipoConsuntivo.RECUPERO).build().toCarburanteDto();

		Assert.isTrue(new CarburanteDtoBuilder().add(consumato).add(rimanenza).add(recupero).isLesserOrEqual(disponibile), CONSUNTIVI_MAGGIORI_DISPONIBILE);
	};
}
