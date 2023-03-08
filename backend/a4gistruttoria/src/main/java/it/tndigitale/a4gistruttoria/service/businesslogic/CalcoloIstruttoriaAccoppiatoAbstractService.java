package it.tndigitale.a4gistruttoria.service.businesslogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.dao.AnomDomandaSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CalcoloIstruttoriaAccoppiatoAbstractService<T> implements ElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloIstruttoriaAccoppiatoAbstractService.class);

	@Autowired
	protected TransizioneIstruttoriaService transizioneService;

	@Autowired
	protected StatoSostegnoDomandaService statoDomandaService;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private PassoTransizioneDao passiLavorazioneSostegnoDao;

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	
	@Autowired
	private AnomDomandaSostegnoDao anomaliaDao;

	protected abstract Boolean eseguiControlliPreliminari(IstruttoriaModel istruttoria) throws Exception;

	protected abstract Sostegno getIdentificativoSostegno();

	protected abstract DatiSintesi calcolaDatiSintesiLavorazione(T esito, CalcoloAccoppiatoHandler handler) throws Exception;

	protected abstract TipologiaPassoTransizione getPassoLavorazione();

	protected abstract CalcoloAccoppiatoHandler calcolo(IstruttoriaModel istruttoria, MapVariabili inputListaVariabiliCalcolo);

	protected abstract T calcoloEsito(CalcoloAccoppiatoHandler handler) throws Exception;

	protected abstract MapVariabili initVariabiliInput(IstruttoriaModel istruttoria) throws Exception;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
		eseguiCalcolo(istruttoria);
	}


	protected void eseguiCalcolo(IstruttoriaModel istruttoria) throws ElaborazioneIstruttoriaException {
		try {
			// Verifico se esistono condizioni che devono precludere il calcolo
			if (!eseguiControlliPreliminari(istruttoria))
				return;

			if  (logger.isDebugEnabled()) {
				logger.debug("eseguiCalcolo: avvio transizione per l'istruttoria con identificativo {} e stato {}",
							 istruttoria.getId(), istruttoria.getA4gdStatoLavSostegno().getIdentificativo());
			}
			resetAnomalie(istruttoria);
			TransizioneIstruttoriaModel transizione = transizioneService.avviaTransizioneCalcolo(istruttoria);
			if  (logger.isDebugEnabled()) {
				logger.debug("eseguiCalcolo: avviata transizione {} per istruttoria {} ", transizione.getId(), istruttoria.getId());
			}

			// Inizializzo le variabili di input
			MapVariabili inputListaVariabiliCalcolo = initVariabiliInput(istruttoria);
			CalcoloAccoppiatoHandler handler = calcolo(istruttoria, inputListaVariabiliCalcolo);

			calcoloEsito(handler, transizione, istruttoria);
		} catch (Exception e) {
			throw new ElaborazioneIstruttoriaException(
					"Errore durante l'esecuzione del metodo eseguiCalcolo" ,e);
		}
	}
	
	protected String serializza(DatiSintesi datiSintesi) throws Exception {
		if (datiSintesi != null) {
			return objectMapper.writeValueAsString(datiSintesi);		
		}
		return "";
	}
	protected void resetAnomalie(IstruttoriaModel istruttoria) {
		istruttoria.getTransizioni().forEach(t -> t.getPassiTransizione().stream()
				.filter(p -> p.getCodicePasso().equals(getPassoLavorazione().getDescrizione()))
				.forEach(p ->  anomaliaDao.deleteAll(p.getAnomalie()))
				);
	}

	protected void calcoloEsito(CalcoloAccoppiatoHandler handler, TransizioneIstruttoriaModel transizione, IstruttoriaModel statoSostegnoIniziale) throws Exception {
		T esito = calcoloEsito(handler);
		logger.debug("eseguiCalcolo: domanda {} -> esito = {} ", transizione.getIstruttoria().getDomandaUnicaModel().getId(), esito);
		FoglieAlgoritmoWorkflow foglia = calcoloFoglia(esito);
		logger.debug("eseguiCalcolo: domanda {} -> foglia = {} ", transizione.getIstruttoria().getDomandaUnicaModel().getId(), foglia);
		// aggiorno lo stato del sostegno della domanda
		StatoIstruttoria codiceStatoLavorazioneFinale = foglia.getStatoWF();
		A4gdStatoLavSostegno statoLavorazioneSostegnoObject = 
				statoDomandaService.caricaStatoLavorazioneSostegno(codiceStatoLavorazioneFinale);
		statoSostegnoIniziale.setA4gdStatoLavSostegno(statoLavorazioneSostegnoObject);
		statoDomandaService.aggiornaLavorazioneSostegnoDellaDomanda(statoSostegnoIniziale);
		// salvo il passo di lavorazione
		salvaPassoLavorazione(transizione, foglia, esito, handler);
		// aggiorno (chiudo) la transizione
		transizione.setA4gdStatoLavSostegno1(statoLavorazioneSostegnoObject);
		transizione.setDataEsecuzione(new Date());
		transizioneService.aggiornaTransizione(transizione);
	}
	
	protected abstract FoglieAlgoritmoWorkflow calcoloFoglia(T esito) throws Exception;
	
	protected void salvaPassoLavorazione(TransizioneIstruttoriaModel transizione,
			FoglieAlgoritmoWorkflow foglia, T esito, CalcoloAccoppiatoHandler handler) throws Exception {
		// Passo lavorazione
		PassoTransizioneModel passoTransizione = new PassoTransizioneModel();
		passoTransizione.setTransizioneIstruttoria(transizione);
		passoTransizione.setCodiceEsito(foglia.getCodiceEsito());
		passoTransizione.setCodicePasso(getPassoLavorazione());
		passoTransizione.setEsito(foglia.isEsitoOK() ? "OK" : "KO");
		DatiSintesi datisintesi = calcolaDatiSintesiLavorazione(esito, handler);
		passoTransizione.setDatiSintesiLavorazione(serializza(datisintesi));
		passoTransizione.setDatiInput(calcolaDatiInput(handler));
		passoTransizione.setDatiOutput(calcolaDatiOutput(handler));
		
		passoTransizione = passiLavorazioneSostegnoDao.save(passoTransizione);
		
		if (datisintesi != null) {
			salvaAnomaliePasso(passoTransizione, datisintesi.getEsitiControlli());
		}
	}
	
	protected void salvaAnomaliePasso(PassoTransizioneModel passo, List<EsitoControllo> esiti) {
		if (esiti != null && passo != null) {
			esiti.forEach(controllo -> {
				if (controllo.getEsito() != null) {
					A4gtAnomDomandaSostegno anom = new A4gtAnomDomandaSostegno();
					anom.setPassoLavorazione(passo);
					if (controllo.getTipoControllo().equals(TipoControllo.BRIDUSDC021_idDomandaSanzioni) && !controllo.getValString().equals("false")) {
						anom.setCodiceAnomalia(controllo.getTipoControllo().getCodice().concat("_").concat(controllo.getValString().toUpperCase()));
					} else if (controllo.getTipoControllo().equals(TipoControllo.BRIDUSDC025_impegniGreening)) {
						anom.setCodiceAnomalia(controllo.getTipoControllo().getCodice().concat("_").concat(controllo.getValString().toUpperCase()));
					} else {
						anom.setCodiceAnomalia(controllo.getTipoControllo().getCodice());
					}
					anom.setLivelloAnomalia(controllo.getLivelloControllo().name());
					anom.setEsito(controllo.getEsito() ? "SI" : "NO");
					anomaliaDao.save(anom);
				}
			});
			
		}
	}

	protected EsitoControllo trasformaEsito(TipoControllo tipo, Boolean check) {
		return new EsitoControllo(tipo, check);
	}

	protected String calcolaDatiInput(CalcoloAccoppiatoHandler handler) throws Exception {
		DatiInput di = new DatiInput();
		if (handler != null && handler.hasVariabiliInput()) {
			di.setVariabiliCalcolo(new ArrayList<>(handler.getVariabiliInput().getVariabiliCalcolo().values()));
			return objectMapper.writeValueAsString(di);
		} else return "";
	}

	protected String calcolaDatiOutput(CalcoloAccoppiatoHandler handler) throws Exception {
		DatiOutput dati = new DatiOutput();
		if (handler != null && handler.hasVariabiliOutput()) {
			dati.setVariabiliCalcolo(new ArrayList<>(handler.getVariabiliOutput().getVariabiliCalcolo().values()));
			return objectMapper.writeValueAsString(dati);
		} else return "";
	}

	
}
