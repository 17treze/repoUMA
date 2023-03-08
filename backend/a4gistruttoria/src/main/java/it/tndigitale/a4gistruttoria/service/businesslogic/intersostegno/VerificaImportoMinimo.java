package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.OpzioniImportoMinimo;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;

@Component
public class VerificaImportoMinimo extends ControlloImporto {

	@Value("${a4gistruttoria.pac.intersostegno.sogliaMinima}")
	private Double soglia;

	private static Map<CodiceEsito, EsitoControllo> ctrlImpMinimo = new EnumMap<>(CodiceEsito.class);
	static {
		ctrlImpMinimo.put(CodiceEsito.DUT_042, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, true, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_043, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_044, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, true, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_049, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, true, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_047, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_DETERMINABILE.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_050, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_051, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_045, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, true, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_048, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_DETERMINABILE.name()));
		ctrlImpMinimo.put(CodiceEsito.DUT_046, new EsitoControllo(TipoControllo.BRIDUSDS040_importoMinimo, false, OpzioniImportoMinimo.NON_DETERMINABILE.name()));
	}
	
	private Function<CodiceEsito, EsitoControllo> trasformaEsitoInRisultatoEsito = (esito) -> {
		return ctrlImpMinimo.get(esito);
	};
	
	
	private final Function<DatiLavorazioneControlloIntersostegno, CodiceEsito> checkSostegno2 = importoMinimoHandler ->
	// sommo l'importo del sostegno con l'importo del primo sostegno aggiuntivo, dopodichè viene apppurato se la somma dei due importi raggiunge la soglia minima
	sum.apply(
			importoMinimoHandler
					.getImportoSostegno(),
			importoSostegno2
					.apply(importoMinimoHandler))
			.filter(sogliaMinima::test)
			.map(x -> CodiceEsito.DUT_044)
			.orElse(
					// se non raggiunta la soglia minima con il primo sostegno aggiuntivo, viene appurata l'esistenza del secondo ed ultimo sostegno aggiuntivo.
					Optional.of(!importoMinimoHandler.getHasSostegno3())
						.filter(Boolean::booleanValue)
						.map(b -> CodiceEsito.DUT_051)
						.orElse(Optional
							.of(CaricaPremioSostegno.isStatoInAttesa(importoMinimoHandler.getStatoLavorazioneSostegno3()))
							.filter(sostegno3NonLavorato -> sostegno3NonLavorato) // il terzo sostegno è ancora in																																										// lavorazione
							.map(v -> CodiceEsito.DUT_047) // sostegno non autorizzato
							// terzo sostegno lavorato con importo definitivo => sommo gli importi dei 3 sostegni e controllo la soglia minima
							.orElse(sum.apply(importoSostegno3.apply(importoMinimoHandler), sum.apply(importoMinimoHandler.getImportoSostegno(), importoSostegno2.apply(importoMinimoHandler)).get())
									.filter(sogliaMinima::test) // soglia raggiunta
									.map(x -> CodiceEsito.DUT_049)
									// i 3 sostegni non raggiungono la soglia
									.orElse(CodiceEsito.DUT_050) // non liquidabile
					)));

	private final Function<DatiLavorazioneControlloIntersostegno, CodiceEsito> checkSostegno3 = importoMinimoHandler ->
	// sommo l'importo del sostegno con l'unico sostegno aggiuntivo, dopodichè viene apppurato se la somma dei due importi raggiunge la soglia minima
	sum.apply(importoMinimoHandler.getImportoSostegno(), importoMinimoHandler.getImportoSostegno3()).filter(sogliaMinima::test).map(x -> CodiceEsito.DUT_045).orElseGet(() -> CodiceEsito.DUT_048);

	@Override
	@Transactional
	public CodiceEsito apply(DatiLavorazioneControlloIntersostegno datiLavorazioneControlloIntersostegno) {
		// controllo che il sostegno richiesto abbia un importo maggiore di zero e che raggiunga la soglia minima
		return Optional.of(datiLavorazioneControlloIntersostegno.getImportoSostegno()).filter(isNotZero::test).filter(sogliaMinima::test).map(x -> CodiceEsito.DUT_042).orElseGet(() -> {
			return Optional.of(datiLavorazioneControlloIntersostegno).filter(notHasSostegniAggiuntivi::test).map(m -> CodiceEsito.DUT_043)
					// altrimenti valuto la bridusdi044: se il primo sostegno aggiuntivo non e' in lavorazione vengono sommati i due importi per appurare il raggiungimento della soglia
					.orElseGet(() -> Optional.of(bridu_044.test(datiLavorazioneControlloIntersostegno.getHasSostegno2(), datiLavorazioneControlloIntersostegno.getStatoLavorazioneSostegno2()))
							.filter(Boolean::booleanValue)
							.map(x -> checkSostegno2.apply(datiLavorazioneControlloIntersostegno))
							// altrimenti viene appurata l'esistenza del secondo sostegno aggiuntivo e sommato ancora l'importo per il raggiungimento della sogli minima
							.orElseGet(() -> Optional.of(bridu_045.test(datiLavorazioneControlloIntersostegno.getHasSostegno3(), datiLavorazioneControlloIntersostegno.getStatoLavorazioneSostegno3()))
									.filter(Boolean::booleanValue)
									.map(x -> checkSostegno3.apply(datiLavorazioneControlloIntersostegno)).orElse(CodiceEsito.DUT_046)));
		});

		// return esito;

	}
	
	protected EsitoControllo trasformaEsitoInRisultatoEsito(CodiceEsito esito) {
		return trasformaEsitoInRisultatoEsito.apply(esito);
	}

	@Override
	protected Double getSoglia() {
		return soglia;
	}

}
