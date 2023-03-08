package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.AvvioLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneErrorDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("AVVIO")
public class AvvioLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(AvvioLavorazioneBusiness.class);

	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;

	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		return insertLavorazioneInWorkspace(inputData.getIdLavorazione(), inputData.getUtente());
	}

	public LavorazioneSuoloModel insertLavorazioneInWorkspace(Long idLavorazione, String utente) {
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(idLavorazione, utente);

		chechValidState(lavorazione);

		ValidazioneLavorazioneErrorDto validazione = lavorazioneSuoloService.validaLavorazione(idLavorazione);
		if (validazione.getCode() != null || validazione.getMessage() != null) {
			log.error("Lavorazione con id {} non rispetta i criteri di validazione ", lavorazione.getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nell'avvio della lavorazione");

		}

		Clock clock = getClock();

		lavorazione.setStato(StatoLavorazioneSuolo.IN_CORSO);
		lavorazione.setDataUltimaModifica(clock.now());

		if (lavorazione.getModalitaADL().equals(ModalitaADL.DISEGNO_ADL)) {
			List<TempClipSuADLModel> listSuoliClippedLavorazione = getListSuoliClippedInside(lavorazione);
			listSuoliClippedLavorazione.forEach(suoloClip -> {
				WorkspaceLavSuoloModel workspace = new WorkspaceLavSuoloModel();
				workspace.setIdLavorazioneWorkspaceLavSuolo(lavorazione);
				workspace.setCodUsoSuoloWorkspaceLavSuolo(suoloClip.getCodUsoSuolo());
				workspace.setNote(suoloClip.getNote());
				workspace.setStatoColtWorkspaceLavSuolo(suoloClip.getStatoColt());
				workspace.setShape(suoloClip.getShape());
				workspace.setDataUltimaModifica(clock.now());
				workspace.setArea(suoloClip.getShape().getArea());
				workspace.setIstatp(suoloClip.getIstatp());
				workspace.setIdGridWorkspace(suoloClip.getIdGrid());

				lavorazione.addWorkspaceLavSuoloModel(workspace);
			});
		} else if (lavorazione.getModalitaADL().equals(ModalitaADL.POLIGONI_INTERI)) {
			lavorazione.getListaSuoloInCorsoModel().forEach(suolo -> {
				WorkspaceLavSuoloModel workspace = new WorkspaceLavSuoloModel();
				workspace.setIdLavorazioneWorkspaceLavSuolo(lavorazione);
				workspace.setCodUsoSuoloWorkspaceLavSuolo(suolo.getCodUsoSuoloModel());
				workspace.setNote(suolo.getNote());
				workspace.setStatoColtWorkspaceLavSuolo(suolo.getStatoColtSuolo());
				workspace.setShape(suolo.getShape());
				workspace.setDataUltimaModifica(clock.now());
				workspace.setArea(suolo.getShape().getArea());

				workspace.setIstatp(suolo.getIstatp());
				workspace.setIdGridWorkspace(suolo.getIdGrid());
				lavorazione.addWorkspaceLavSuoloModel(workspace);
			});

			calcolareAreaDiLavoro(lavorazione);
		}

		getLavorazioneSuoloDao().save(lavorazione);

		getEventoPublisher().notificaEvento(new AvvioLavorazioneEvento(lavorazione));

		return lavorazione;
	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per passaggio di stato a IN_CORSO {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente il passaggio a IN_CORSO "))));

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
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nell'avvio della lavorazione");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME".concat("responseCode= ").concat(String.valueOf(responseFme.getStatusCodeValue())).concat(" nel calcolo dell'area di lavoro"));
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nell'avvio della lavorazione");
		}
	}
}
