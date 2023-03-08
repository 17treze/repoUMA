package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.util.TipoControllo;

public abstract class PassoDatiElaborazioneIstruttoria extends ElaboraPassoIstruttoria {
	
	private static final Logger logger = LoggerFactory.getLogger(PassoDatiElaborazioneIstruttoria.class);

	public PassoTransizioneModel eseguiPasso(DatiElaborazioneIstruttoria dati) throws Exception {
		logger.debug("eseguiPasso: inizio");
		MapVariabili variabiliCalcolo = initVariabiliCalcolo(dati);
		HashMap<TipoControllo, EsitoControllo> mappaEsiti = initMappaEsiti(dati);
		DatiPassoLavorazione risultatoElaborazione = elaboraPasso(dati, variabiliCalcolo, mappaEsiti);
		compilaDatiInput(risultatoElaborazione, dati);
		puliziaEsiti(risultatoElaborazione, dati);
		
		return salvaPassoLavorazioneSostegno(risultatoElaborazione);
	}
	
	protected void compilaDatiInput(DatiPassoLavorazione risultatoElaborazione, DatiElaborazioneIstruttoria dati) {
		risultatoElaborazione.getDatiInput().setVariabiliCalcolo(dati.getVariabiliInputNext());
	}
	
	protected void puliziaEsiti(DatiPassoLavorazione risultatoElaborazione, DatiElaborazioneIstruttoria dati) {
		DatiSintesi datiSintesi = risultatoElaborazione.getDatiSintesi();
		List<TipoControllo> tipiInput = dati.getEsitiInputNext().stream().map(EsitoControllo::getTipoControllo).collect(Collectors.toList());
		List<EsitoControllo> esitiControlliout = datiSintesi.getEsitiControlli().stream().filter(x -> !tipiInput.contains(x.getTipoControllo())).collect(Collectors.toList());
		datiSintesi.setEsitiControlli(esitiControlliout);
	}

	protected abstract DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti);
	
	protected MapVariabili initVariabiliCalcolo(DatiElaborazioneIstruttoria dati) {
		MapVariabili variabiliCalcolo = new MapVariabili();
		dati.getVariabiliInputNext().forEach(v -> variabiliCalcolo.add(v.getTipoVariabile(), v));
		return variabiliCalcolo;
	}
	
	protected HashMap<TipoControllo, EsitoControllo> initMappaEsiti(DatiElaborazioneIstruttoria dati) {
		HashMap<TipoControllo, EsitoControllo> mapEsiti = new HashMap<TipoControllo, EsitoControllo>();
		dati.getEsitiInputNext().forEach(e -> mapEsiti.put(e.getTipoControllo(), e));
		return mapEsiti;
	}
}
