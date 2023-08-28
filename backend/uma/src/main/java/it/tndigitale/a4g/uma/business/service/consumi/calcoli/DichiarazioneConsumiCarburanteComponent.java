package it.tndigitale.a4g.uma.business.service.consumi.calcoli;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoFabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.lavorazioni.RecuperaLavorazioniSuperficie;
import it.tndigitale.a4g.uma.business.service.richiesta.CarburanteConverter;
import it.tndigitale.a4g.uma.dto.aual.FabbricatoAualDto;
import it.tndigitale.a4g.uma.dto.aual.TerritorioAualDto;

@Component
public class DichiarazioneConsumiCarburanteComponent {

	private static final Logger logger = LoggerFactory.getLogger(DichiarazioneConsumiCarburanteComponent.class);

	@Autowired
	private UmaTerritorioClient territorioClient;
	@Autowired
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@Autowired
	private RecuperaLavorazioniSuperficie recuperaLavorazioniSuperficie;

	public CarburanteDecimal calcolaSuperfici(RichiestaCarburanteModel richiesta, LocalDateTime dataConduzione) {

		// Interrogare il piano colturale alla data di protocollazione della richiesta
		List<TerritorioAualDto> pcOld = territorioClient.getColtureFromAual(richiesta.getCuaa(), richiesta.getDataProtocollazione());

		// Interrogare il piano colturale al primo novembre dell'anno di campagna
		List<TerritorioAualDto> pcNew = territorioClient.getColtureFromAual(richiesta.getCuaa(), dataConduzione);

		if (CollectionUtils.isEmpty(pcOld) || CollectionUtils.isEmpty(pcNew)) {
			return new CarburanteDecimal();
		}

		// Scartare le particelle presenti al primo di novembre ma non alla data di protocollazione della Richiesta di carburante.
		List<TerritorioAualDto> particelle = pcNew.stream()
				.filter(p -> contains(p, pcOld))
				.collect(Collectors.toList());

		// Calcolo la superficie massima
		Map<GruppoLavorazioneModel, Integer> mappaSuperficieMassima = recuperaLavorazioniSuperficie.calcolaSuperficieMassima(particelle);

		// Per ogni lavorazione che ho dichiarato verifico che ci sia almeno una coltura in qualche particella  che giustifica il gruppo a cui appartiene la lavorazione; se non ce ne sono il sistema le scarta.
		// Calcolo il minimo tra la superficie massima e la superficie dichiarata in Richiesta di carburante per quella lavorazione
		List<FabbisognoModel> fabbisogni = richiesta.getFabbisogni().stream()
				.filter(f -> mappaSuperficieMassima.containsKey(f.getLavorazioneModel().getGruppoLavorazione()))
				.map(fab -> fab.setQuantita(fab.getQuantita().min(new BigDecimal(mappaSuperficieMassima.get(fab.getLavorazioneModel().getGruppoLavorazione())))))
				.collect(Collectors.toList());
		// Sommo i vari valori di tutte le lavorazioni moltiplicando la superficie calcolata per il coefficiente della lavorazione e sommando tutte le quantità ottenute.

		CarburanteDecimal carburanteDecimal = new CarburanteConverter(richiesta.getCampagna()).calcola(fabbisogni);
		logger.info("Calcolo carburante ammissibile Supefici Dichiarazione Consumi: Richiesta {} - Carburante {}", richiesta.getId() , carburanteDecimal);
		return carburanteDecimal;
	}

	public CarburanteDecimal calcolaFabbricati(RichiestaCarburanteModel richiesta, LocalDateTime dataConduzione) {

		// leggere i fabbricati presenti nella Richiesta di carburante per i quali sono state dichiarate lavorazioni
		List<FabbricatoModel> fabbricatiOld = richiesta.getFabbricati();

		// Leggere i fabbricati presenti nel fascicolo e con titolo di conduzione pari a proprietà, affitto, comodato al primo di novembre dell'anno di campagna.
		List<FabbricatoAualDto> fabbricatiNew = dotazioneTecnicaClient.getFabbricati(richiesta.getCuaa());

		if (CollectionUtils.isEmpty(fabbricatiNew)) {return new CarburanteDecimal();}

		// Dalla lista ottenuta al passo 2 togliere i fabbricati  che non sono presenti in Richiesta di carburante (passo 1)
		List<FabbricatoModel> fabbricati = fabbricatiOld.stream()
				.filter(f -> fabbricatiNew.stream().anyMatch(fab -> fab.getCodiFabb().equals(f.getIdentificativoAgs())))
				.collect(Collectors.toList());

		// Sommo i vari valori di tutte le lavorazioni rimaste moltiplicando la quantità dichiarata per il coefficiente della lavorazione e sommando tutte le quantità ottenute.
		List<FabbisognoModel> fabbisogni = richiesta.getFabbisogni().stream()
				.filter(fabbisogno -> {
					var ambito = fabbisogno.getLavorazioneModel().getGruppoLavorazione().getAmbitoLavorazione();
					return (AmbitoLavorazione.FABBRICATI.equals(ambito) || AmbitoLavorazione.SERRE.equals(ambito));
				})
				.map(FabbisognoFabbricatoModel.class::cast)
				.filter(f -> fabbricati.stream().anyMatch(fab -> fab.getId().equals(f.getFabbricatoModel().getId())))
				.collect(Collectors.toList());
		var carburanteDecimal = new CarburanteConverter(richiesta.getCampagna()).calcola(fabbisogni);
		logger.info("Calcolo carburante ammissibile Fabbricati Dichiarazione Consumi: Richiesta {} - Carburante {}", richiesta.getId() , carburanteDecimal);
		return carburanteDecimal;
	}

	public CarburanteDecimal calcolaAltre(RichiestaCarburanteModel richiesta) {
		List<FabbisognoModel> fabbisogni = richiesta.getFabbisogni().stream()
				.filter(fabbisogno -> {
					var ambito = fabbisogno.getLavorazioneModel().getGruppoLavorazione().getAmbitoLavorazione();
					return (AmbitoLavorazione.ALTRO.equals(ambito) || AmbitoLavorazione.ZOOTECNIA.equals(ambito));
				})
				.collect(Collectors.toList());

		var carburanteDecimal = new CarburanteConverter(richiesta.getCampagna()).calcola(fabbisogni);
		logger.info("Calcolo carburante ammissibile Altre e Zootecnia Dichiarazione Consumi: Richiesta {} - Carburante {}", richiesta.getId() , carburanteDecimal);
		return carburanteDecimal;
	}

	// restituisce true se l'elemento è contenuto nella lista, false altrimenti
	private boolean contains(TerritorioAualDto p, List<TerritorioAualDto> particelle) {
		return particelle.stream().anyMatch(particella -> 
			p.getCodiProv().equals(particella.getCodiProv()) && 
			p.getCodiComu().equals(particella.getCodiComu()) && 
			(p.getDescSezi() == null ? particella.getDescSezi() == null : p.getDescSezi().equals(particella.getDescSezi())) &&
			p.getDescFogl().equals(particella.getDescFogl()) &&
			p.getDescPart().equals(particella.getDescPart()) &&
			(p.getDescSuba() == null ? particella.getDescSuba() == null : p.getDescSuba().equals(particella.getDescSuba()))
		);
	}
}
