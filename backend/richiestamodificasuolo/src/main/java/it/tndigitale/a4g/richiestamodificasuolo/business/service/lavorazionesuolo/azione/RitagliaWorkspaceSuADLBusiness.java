package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("RITAGLIA_SU_ADL")
public class RitagliaWorkspaceSuADLBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(RitagliaWorkspaceSuADLBusiness.class);

	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.ritagliaWorkspaceSuAreaDiLavoro}")
	private String ritagliaWorkspaceSuADL;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		return ritagliaSuADL(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente());
	}

	private LavorazioneSuoloModel ritagliaSuADL(Long idLavorazione, Integer versione, String utente) {
		log.debug("START - ritagliaSuADL");

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);

		chechValidState(lavorazione);

		ritagliaWorkspaceSuAreaDiLavoro(lavorazione);

		log.debug("END - ritagliaSuADL");
		return lavorazione;
	}

	/**
	 * Ritaglia i poligoni di workspace sull'area di lavoro
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void ritagliaWorkspaceSuAreaDiLavoro(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFme(lavorazione.getId(), ritagliaWorkspaceSuADL);
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME nel ritagliare i poligoni di workspace sull'area di lavoro", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore trasformata FME nel ritagliare i poligoni di workspace sull'area di lavoro");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue())).concat(" nel ritagliare i poligoni di workspace sull'area di lavoro"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore trasformata FME nel ritagliare i poligoni di workspace sull'area di lavoro");
		}
	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {
			log.error("Lavorazione con id {} non in stato corretto per ritagliare il workspace sull'area di lavoro ", lavorazione.getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente di ritagliare il workspace sull'area di lavoro"))));

		}

	}
}
