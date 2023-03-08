package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneErrorDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("RIPRENDI_LAVORAZIONE")
public class RiprendiLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(RiprendiLavorazioneBusiness.class);

	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;

	@Value("${it.tndigit.serverFme.calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme}")
	private String calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme;

	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		return riprendiLavorazione(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente());
	}

	private LavorazioneSuoloModel riprendiLavorazione(Long idLavorazione, Integer versione, String utente) {
		log.debug("START - riprendiLavorazione");

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);

		chechValidState(lavorazione);
		ValidazioneLavorazioneErrorDto validazione = lavorazioneSuoloService.validaLavorazione(idLavorazione);
		if (validazione.getCode() != null || validazione.getMessage() != null) {
			log.error("Lavorazione con id {} non rispetta i criteri di validazione ", lavorazione.getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nella ripresa della lavorazione");

		}

		calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspace(lavorazione);

		if (lavorazione.getModalitaADL().equals(ModalitaADL.POLIGONI_INTERI)) {
			calcolareAreaDiLavoro(lavorazione);
		}

		lavorazione.setStato(StatoLavorazioneSuolo.IN_CORSO);

		getLavorazioneSuoloDao().save(lavorazione);
		log.debug("END - riprendiLavorazione");
		return lavorazione;
	}

	/**
	 * calcolare i poligoni di suolo che non sono coperti da workspace e salvare i nuovi poligoni nel workspace
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspace(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFme(lavorazione.getId(), calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme);
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME nel calcolare i poligoni di suolo che non sono coperti da workspace e salvare i nuovi poligoni nel workspace", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nella ripresa della lavorazione");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue()))
					.concat("nel calcolare i poligoni di suolo che non sono coperti da workspace e salvare i nuovi poligoni nel workspace"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nella ripresa della lavorazione");
		}
	}

	/**
	 * calcolare l'area di lavoro
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void calcolareAreaDiLavoro(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFme(lavorazione.getId(), creaAreaDiLavoro);
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME nel calcolo dell'area di lavoro", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nella ripresa della lavorazione");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue())).concat(" nel calcolo dell'area di lavoro"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nella ripresa della lavorazione");
		}
	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("Lavorazione con id {} non in stato corretto per riprendere la lavorazione {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente la ripresa della lavorazione"))));

		}

	}
}