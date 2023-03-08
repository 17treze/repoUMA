package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AreaDiLavoroModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.AvvioLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputCopiaLavorazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;

@Component("COPIA")
public class CopiaLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, InputCopiaLavorazione> {
	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;
	@Autowired
	private UtilsFme utilsFme;
	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;
	private static final Logger log = LoggerFactory.getLogger(CopiaLavorazioneBusiness.class);

	@Override
	protected LavorazioneSuoloModel eseguiAzione(InputCopiaLavorazione inputCopiaLavorazione) {
		return copiaLavorazioneSuolo(inputCopiaLavorazione.getUtente(), inputCopiaLavorazione.getAnnoCampagna(), inputCopiaLavorazione.getIdLavorazionePadre());
	}

	public LavorazioneSuoloModel copiaLavorazioneSuolo(String username, Integer annoCampagna, Long idLavorazionePadre) {

		log.debug("START - insertDaCopiaLavorazioneSuolo ");
		LavorazioneSuoloModel newLavorazioneSuoloModel = new LavorazioneSuoloModel();
		LavorazioneSuoloModel oldLavorazione = lavorazioneSuoloService.ricercaLavorazioneUtenteModel(idLavorazionePadre, username);
		List<WorkspaceLavSuoloModel> workspaceList = oldLavorazione.getListaLavorazioneWorkspaceModel();
		List<AreaDiLavoroModel> adl = oldLavorazione.getListaAreadiLavoro();
		newLavorazioneSuoloModel.setUtente(username);
		newLavorazioneSuoloModel.setIdLavorazionePadre(idLavorazionePadre);
		newLavorazioneSuoloModel.setNote("Lavorazione copiata da: " + newLavorazioneSuoloModel.getIdLavorazionePadre());
		newLavorazioneSuoloModel.setDataInizioLavorazione(getClock().now());
		newLavorazioneSuoloModel.setDataUltimaModifica(getClock().now());
		newLavorazioneSuoloModel.setStato(StatoLavorazioneSuolo.IN_MODIFICA);
		newLavorazioneSuoloModel.setCampagna(annoCampagna);
		newLavorazioneSuoloModel.setModalitaAdl(oldLavorazione.getModalitaADL());
		newLavorazioneSuoloModel.setxUltimoZoom(oldLavorazione.getxUltimoZoom());
		newLavorazioneSuoloModel.setyUltimoZoom(oldLavorazione.getyUltimoZoom());
		newLavorazioneSuoloModel.setScalaUltimoZoom(oldLavorazione.getScalaUltimoZoom());
		newLavorazioneSuoloModel.setModalitaAdl(oldLavorazione.getModalitaADL());

		// WORKSPACE
		workspaceList.forEach(suoloClip -> {
			WorkspaceLavSuoloModel newWorkspace = new WorkspaceLavSuoloModel();
			newWorkspace.setCodUsoSuoloWorkspaceLavSuolo(suoloClip.getCodUsoSuoloWorkspaceLavSuolo());
			newWorkspace.setNote(suoloClip.getNote());
			newWorkspace.setStatoColtWorkspaceLavSuolo(suoloClip.getStatoColtWorkspaceLavSuolo());
			newWorkspace.setShape(suoloClip.getShape());
			newWorkspace.setDataUltimaModifica(getClock().now());
			newWorkspace.setArea(suoloClip.getShape().getArea());
			newWorkspace.setIstatp(suoloClip.getIstatp());
			newWorkspace.setIdGridWorkspace(suoloClip.getIdGridWorkspace());
			newLavorazioneSuoloModel.addWorkspaceLavSuoloModel(newWorkspace);
		});

		// ADL
		if (newLavorazioneSuoloModel.getModalitaADL().equals(ModalitaADL.DISEGNO_ADL)) {
			adl.forEach(poligonoAdl -> {
				AreaDiLavoroModel newAdl = new AreaDiLavoroModel();
				newAdl.setShape(poligonoAdl.getShape());
				newAdl.setVersion(poligonoAdl.getVersion());
				newLavorazioneSuoloModel.addAdlLavSuoloModel(newAdl);
			});
		}

		LavorazioneSuoloModel responseLavorazione = getLavorazioneSuoloDao().save(newLavorazioneSuoloModel);
		getEventoPublisher().notificaEvento(new AvvioLavorazioneEvento(newLavorazioneSuoloModel));
		log.debug("END - insertLavorazioneSuolo - Copia e Insert Lavorazione con id # {}", responseLavorazione.getId());
		return responseLavorazione;
	}

}
