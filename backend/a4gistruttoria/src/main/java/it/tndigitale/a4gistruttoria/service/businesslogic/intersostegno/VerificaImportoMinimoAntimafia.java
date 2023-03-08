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
public class VerificaImportoMinimoAntimafia extends ControlloImporto {

	@Value("${a4gistruttoria.pac.intersostegno.sogliaMinimaAntimafia}")
	private Double soglia;

	private static Map<CodiceEsito, EsitoControllo> ctrlImpMinimoAntimafia = new EnumMap<>(CodiceEsito.class);
	static {
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_061, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, false, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_056, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, false, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_058, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, false, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_069, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, false, OpzioniImportoMinimo.RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_057, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_059, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_063, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_064, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_060, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
		ctrlImpMinimoAntimafia.put(CodiceEsito.DUT_062, new EsitoControllo(TipoControllo.BRIDUSDS049_importoMinimoAntimafia, true, OpzioniImportoMinimo.NON_RAGGIUNTO.name()));
	}
	
	private Function<CodiceEsito, EsitoControllo> trasformaEsitoInRisultatoEsito = (esito) -> {
		return ctrlImpMinimoAntimafia.get(esito);
	};
	
	/**
	 * Function
	 */
	private final Function<DatiLavorazioneControlloIntersostegno, CodiceEsito> checkSostegno2 = datiIntersostegno ->
	// sommo l'importo del sostegno con l'importo del primo sostegno aggiuntivo, dopodichè viene apppurato se la somma dei due importi raggiunge la soglia minima
	sum.apply(
			datiIntersostegno
					.getImportoSostegno(),
			importoSostegno2
					.apply(datiIntersostegno))
			.filter(sogliaMinima::test).map(x -> CodiceEsito.DUT_056).orElse(
					// se non raggiunta la soglia minima con il primo sostegno aggiuntivo, viene appurata l'esistenza del secondo ed ultimo sostegno aggiuntivo.
					Optional.of(!datiIntersostegno.getHasSostegno3()).filter(nonEsisteTerzoSostegno -> nonEsisteTerzoSostegno).map(b -> CodiceEsito.DUT_063).orElse(Optional
							.of(CaricaPremioSostegno.isStatoInAttesa(datiIntersostegno.getStatoLavorazioneSostegno3())).filter(sostegno3NonLavorato -> sostegno3NonLavorato) // il terzo sostegno è ancora in
																																										// lavorazione
							.map(v -> CodiceEsito.DUT_057) // sostegno non autorizzato
							// terzo sostegno lavorato con importo definitivo => sommo gli importi dei 3 sostegni e controllo la soglia minima
							.orElse(sum.apply(importoSostegno3.apply(datiIntersostegno), sum.apply(datiIntersostegno.getImportoSostegno(), importoSostegno2.apply(datiIntersostegno)).orElse(0D))
									.filter(sogliaMinima::test) // soglia raggiunta
									.map(x -> CodiceEsito.DUT_058)
									// i 3 sostegni non raggiungono la soglia
									.orElse(CodiceEsito.DUT_059) // antimafia non obbligatoria
					)));

	private final Function<DatiLavorazioneControlloIntersostegno, CodiceEsito> checkSostegno3 = importoMinimoHandler ->
	// sommo l'importo del sostegno con l'unico sostegno aggiuntivo, dopodichè viene apppurato se la somma dei due importi raggiunge la soglia minima
	sum.apply(importoMinimoHandler.getImportoSostegno(), importoMinimoHandler.getImportoSostegno3()).filter(sogliaMinima::test).map(x -> CodiceEsito.DUT_069).orElseGet(() -> CodiceEsito.DUT_064);

	@Override
	@Transactional
	public CodiceEsito apply(DatiLavorazioneControlloIntersostegno DatiLavorazioneControlloIntersostegno) {
		// controllo che il sostegno richiesto abbia un importo maggiore di zero e che raggiunga la soglia minima
		return Optional.of(DatiLavorazioneControlloIntersostegno.getImportoSostegno()).filter(isNotZero::test).filter(sogliaMinima::test).map(x -> CodiceEsito.DUT_061).orElseGet(() -> {
			return Optional.of(DatiLavorazioneControlloIntersostegno).filter(notHasSostegniAggiuntivi::test).map(m -> CodiceEsito.DUT_062)
					// altrimenti valuto la bridusdi044: se il primo sostegno aggiuntivo non e' in lavorazione vengono sommati i due importi per appurare il raggiungimento della soglia
					.orElseGet(() -> Optional.of(bridu_044.test(DatiLavorazioneControlloIntersostegno.getHasSostegno2(), DatiLavorazioneControlloIntersostegno.getStatoLavorazioneSostegno2()))
							.filter(Boolean::booleanValue)
							.map(x -> checkSostegno2.apply(DatiLavorazioneControlloIntersostegno))
							// altrimenti viene appurata l'esistenza del secondo sostegno aggiuntivo e sommato ancora l'importo per il raggiungimento della sogli minima
							.orElseGet(() -> Optional.of(bridu_045.test(DatiLavorazioneControlloIntersostegno.getHasSostegno3(), DatiLavorazioneControlloIntersostegno.getStatoLavorazioneSostegno3()))
									.filter(Boolean::booleanValue)
									.map(x -> checkSostegno3.apply(DatiLavorazioneControlloIntersostegno)).orElse(CodiceEsito.DUT_060)));
		});

	}

	protected EsitoControllo trasformaEsitoInRisultatoEsito(CodiceEsito esito) {
		return trasformaEsitoInRisultatoEsito.apply(esito);
	}

	@Override
	protected Double getSoglia() {
		return soglia;
	}
}
