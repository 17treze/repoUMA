package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AreaDiLavoroModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoLavorazioneDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoAnomaliaValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoJobFME;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceTmpModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AnomaliaValidazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AreaDiLavoroDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.TempClipSuADLDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.LavorazioneSuoloEventoPublisher;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaPoligoniWithRootWorkspaceDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaValidazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.AnomaliaValidazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

public abstract class AzioneLavorazioneBase<T, I> {

	private static final Logger log = LoggerFactory.getLogger(AzioneLavorazioneBase.class);

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private SuoloDichiaratoDao suoloDichiaratoDao;

	@Autowired
	private WorkspaceLavorazioneDao workspaceLavorazioneDao;

	@Autowired
	private AreaDiLavoroDao areaDiLavoroDao;

	@Autowired
	private SuoloDao suoloDao;

	@Autowired
	private TempClipSuADLDao tempClipSuADLDao;

	@Autowired
	private Clock clock;

	@Autowired
	private LavorazioneSuoloEventoPublisher eventoPublisher;

	@Autowired
	private AnomaliaValidazioneDao anomaliaValidazioneDao;

	@Autowired
	private AnomaliaValidazioneMapper anomaliaValidazioneMapper;

	protected abstract T eseguiAzione(I inputData);

	protected LavorazioneSuoloModel ricercaLavorazioneUtenteModel(Long idLavorazione, String utente) {
		log.debug("START - ricercaLavorazione ");
		LavorazioneSuoloModel lavorazioneModel = null;
		if (Objects.nonNull(idLavorazione)) {
			Optional<LavorazioneSuoloModel> responseLavorazione = lavorazioneSuoloDao.findByIdAndUtente(idLavorazione, utente);
			if (!responseLavorazione.isPresent()) {
				log.warn("LavorazioneSuoloModel non trovata per idLavorazione {} e utente {}", idLavorazione, utente);
			} else {
				lavorazioneModel = responseLavorazione.get();
			}
		}
		log.debug("END - ricercaLavorazione");
		return lavorazioneModel;
	}

	protected LavorazioneSuoloModel getUtenteLavorazioneValidoOrInvalidArgument(Long idLavorazione, String utente) {
		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);
		if (lavorazione == null) {
			log.error("LavorazioneSuoloModel non trovata per  {} o per utente {}", idLavorazione, utente);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Entita non trovata per lavorazione ".concat(String.valueOf(idLavorazione).concat(" per utente ").concat(utente)));
		}
		return lavorazione;
	}

	protected LavorazioneSuoloModel getUtenteLavorazioneValidoNotFoundOrUnauthorized(Long idLavorazione, String utente) {
		LavorazioneSuoloModel lavorazione = null;
		Optional<LavorazioneSuoloModel> responseLavorazione = lavorazioneSuoloDao.findById(idLavorazione);

		if (!responseLavorazione.isPresent()) {
			log.error("LavorazioneSuoloModel non trovata per  {} ", idLavorazione);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per lavorazione ".concat(String.valueOf(idLavorazione)));
		} else {
			lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);
			if (lavorazione == null) {
				log.error("LavorazioneSuoloModel non trovata per  {} per utente {}", idLavorazione, utente);
				throw SuoloException.ExceptionType.SECURITY_EXCEPTION
						.newSuoloExceptionInstance("Entita non trovata per lavorazione ".concat(String.valueOf(idLavorazione).concat(" per utente ").concat(utente)));
			}
		}

		return lavorazione;
	}

	protected LavorazioneSuoloModel getUtenteLavorazioneValidoOrNotFound(Long idLavorazione, String utente) {
		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);
		if (lavorazione == null) {
			log.error("LavorazioneSuoloModel non trovata per  {} o per utente {}", idLavorazione, utente);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION
					.newSuoloExceptionInstance("Entita non trovata per lavorazione ".concat(String.valueOf(idLavorazione).concat(" o per utente ").concat(utente)));
		}
		return lavorazione;
	}

	protected SuoloDichiaratoModel getDichiaratoOrNotFound(Long idSuoloDichiarato) {
		Optional<SuoloDichiaratoModel> suolo = suoloDichiaratoDao.findById(idSuoloDichiarato);
		if (!suolo.isPresent()) {
			log.error("SuoloDichiaratoModel non trovata per  {}", idSuoloDichiarato);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per suolo ".concat(String.valueOf(idSuoloDichiarato)));
		}
		return suolo.get();
	}

	protected List<TempClipSuADLModel> getListSuoliClippedInside(LavorazioneSuoloModel lavorazione) {
		return tempClipSuADLDao.findByLavorazioneSuoloAndPosizionePoligono(lavorazione, "IN");
	}

	protected LavorazioneSuoloDao getLavorazioneSuoloDao() {
		return lavorazioneSuoloDao;
	}

	protected WorkspaceLavorazioneDao getWorkspaceLavorazioneDao() {
		return workspaceLavorazioneDao;
	}

	protected AreaDiLavoroDao getAreaDiLavoroDao() {
		return areaDiLavoroDao;
	}

	protected SuoloDao getSuoloDao() {
		return suoloDao;
	}

	protected SuoloDichiaratoDao getSuoloDichiaratoDao() {
		return suoloDichiaratoDao;
	}

	public TempClipSuADLDao getTempClipSuADLDao() {
		return tempClipSuADLDao;
	}

	protected Clock getClock() {
		return clock;
	}

	protected LavorazioneSuoloEventoPublisher getEventoPublisher() {
		return eventoPublisher;
	}

	public static class BaseInputData {
		private Long idLavorazione;
		private Integer versione;
		private String utente;

		public BaseInputData(Long idLavorazione, Integer versione, String utente) {
			super();
			this.idLavorazione = idLavorazione;
			this.versione = versione;
			this.utente = utente;
		}

		public Long getIdLavorazione() {
			return idLavorazione;
		}

		public void setIdLavorazione(Long idLavorazione) {
			this.idLavorazione = idLavorazione;
		}

		public Integer getVersione() {
			return versione;
		}

		public void setVersione(Integer versione) {
			this.versione = versione;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}
	}

	public static class InputDataGeoJson extends BaseInputData {

		private String geoJson;

		public InputDataGeoJson(Long idLavorazione, Integer versione, String utente, String geoJson) {
			super(idLavorazione, versione, utente);
			this.geoJson = geoJson;
		}

		public String getGeoJson() {
			return geoJson;
		}

		public void setGeoJson(String geoJson) {
			this.geoJson = geoJson;
		}

	}

	public static class InputDataFme {
		private Long idLavorazione;
		private String utente;
		private TipoJobFME tipoJobFME;

		public InputDataFme(Long idLavorazione, String utente, TipoJobFME tipoJobFME) {
			super();
			this.idLavorazione = idLavorazione;
			this.utente = utente;
			this.tipoJobFME = tipoJobFME;
		}

		public Long getIdLavorazione() {
			return idLavorazione;
		}

		public void setIdLavorazione(Long idLavorazione) {
			this.idLavorazione = idLavorazione;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}

		public TipoJobFME getTipoJobFME() {
			return tipoJobFME;
		}

		public void setTipoJobFME(TipoJobFME tipoJobFME) {
			this.tipoJobFME = tipoJobFME;
		}
	}

	public static class InputCreazioneLavorazione {

		private String utente;
		private Integer annoCampagna;

		public InputCreazioneLavorazione(String utente, Integer annoCampagna, Long idLavorazionePadre) {
			this.utente = utente;
			this.annoCampagna = annoCampagna;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}

		public Integer getAnnoCampagna() {
			return annoCampagna;
		}

		public void setAnnoCampagna(Integer annoCampagna) {
			this.annoCampagna = annoCampagna;
		}
	}

	public static class InputCopiaLavorazione {

		private String utente;
		private Integer annoCampagna;
		private Long idLavorazionePadre;

		public InputCopiaLavorazione(String utente, Integer annoCampagna, Long idLavorazionePadre) {
			this.utente = utente;
			this.annoCampagna = annoCampagna;
			this.idLavorazionePadre = idLavorazionePadre;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}

		public Integer getAnnoCampagna() {
			return annoCampagna;
		}

		public void setAnnoCampagna(Integer annoCampagna) {
			this.annoCampagna = annoCampagna;
		}

		public Long getIdLavorazionePadre() {
			return idLavorazionePadre;
		}

		public void setId(Long idLavorazionePadre) {
			this.idLavorazionePadre = idLavorazionePadre;
		}
	}

	public List<AnomaliaPoligoniWithRootWorkspaceDto> searchAnomaliaWithRootWorkspace(LavorazioneSuoloModel lavorazione, List<TipoAnomaliaValidazione> tipiAnomalia) {

		List<AnomaliaValidazioneModel> anomaliaValidazione = new ArrayList<>();
		for (TipoAnomaliaValidazione tipoAnomaliaValidazione : tipiAnomalia) {
			anomaliaValidazione.addAll(anomaliaValidazioneDao.findByLavorazioneSuoloInAnomaliaValidazioneAndTipoAnomalia(lavorazione, tipoAnomaliaValidazione));
		}

		Map<WorkspaceLavSuoloModel, List<AnomaliaValidazioneModel>> trasposta = new HashMap<WorkspaceLavSuoloModel, List<AnomaliaValidazioneModel>>();
		for (AnomaliaValidazioneModel anomalia : anomaliaValidazione) {
			for (WorkspaceLavSuoloModel workspaceLavSuoloModel : anomalia.getAnomaliaWorkspaceRel()) {
				if (!trasposta.containsKey(workspaceLavSuoloModel)) {
					List<AnomaliaValidazioneModel> listAnomalia = new ArrayList<>();
					listAnomalia.add(anomalia);
					trasposta.put(workspaceLavSuoloModel, listAnomalia);
				} else {
					trasposta.get(workspaceLavSuoloModel).add(anomalia);
				}
			}
		}

		List<AnomaliaPoligoniWithRootWorkspaceDto> anomaliaResult = new ArrayList<>();

		Iterator<Entry<WorkspaceLavSuoloModel, List<AnomaliaValidazioneModel>>> iteratorMap = trasposta.entrySet().iterator();
		while (iteratorMap.hasNext()) {
			Entry<WorkspaceLavSuoloModel, List<AnomaliaValidazioneModel>> entry = iteratorMap.next();

			WorkspaceLavSuoloModel workspace = entry.getKey();

			AnomaliaPoligoniWithRootWorkspaceDto a = new AnomaliaPoligoniWithRootWorkspaceDto();
			a.setId(workspace.getId());
			a.setExtent(GisUtils.calculateExtent(workspace.getShape()));
			a.setArea(workspace.getArea());
			a.setIdLavorazione(workspace.getIdLavorazioneWorkspaceLavSuolo().getId());

			Set<AnomaliaValidazioneDto> anomaliaDto = new HashSet<>();
			for (AnomaliaValidazioneModel anomaliaValue : entry.getValue()) {

				anomaliaDto.add(anomaliaValidazioneMapper.convertToDto(anomaliaValue));
				// sono tutte uguali
				a.setAnomaliaDescrizione(anomaliaValue.getDettaglioAnomalia());
			}
			a.setAnomaliaDto(anomaliaDto);
			anomaliaResult.add(a);
		}

		return anomaliaResult;

	}

	protected LavorazioneSuoloModel cleanLavorazione(LavorazioneSuoloModel lavorazione, boolean cancellaSeModalitaAdl, boolean cancellaWorkspace) {
		rollbackEsitoDichiarato(lavorazione);
		LavorazioneSuoloModel lavorazionTmp = dissociaSuoloDichiaratoDaLavorazione(lavorazione);
		lavorazionTmp = dissociaSuoloDaLavorazione(lavorazionTmp);

		lavorazionTmp = cancellaAreaDiLavoro(lavorazionTmp, cancellaSeModalitaAdl);
		if (cancellaWorkspace) {
			lavorazionTmp = cancellaWorkspace(lavorazionTmp);
		}
		lavorazionTmp = cancellaTmpWorkspace(lavorazionTmp);
		lavorazionTmp = cancellaAnomalie(lavorazionTmp);

		return lavorazionTmp;
	}

	protected void rollbackEsitoDichiarato(LavorazioneSuoloModel lavorazione) {
		List<SuoloDichiaratoModel> suoliDichiaratiLavorazione = lavorazione.getSuoloDichiaratoModel();
		for (SuoloDichiaratoModel suoloDichiarato : new ArrayList<>(suoliDichiaratiLavorazione)) {
			suoloDichiarato.setEsito(EsitoLavorazioneDichiarato.DA_LAVORARE);
		}
	}

	protected LavorazioneSuoloModel dissociaSuoloDichiaratoDaLavorazione(LavorazioneSuoloModel lavorazione) {
		List<SuoloDichiaratoModel> suoliDichiaratiLavorazione = lavorazione.getSuoloDichiaratoModel();
		for (SuoloDichiaratoModel suoloDichiarato : new ArrayList<>(suoliDichiaratiLavorazione)) {
			lavorazione.removeSuoloDichiaratoModel(suoloDichiarato);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected LavorazioneSuoloModel dissociaSuoloDaLavorazione(LavorazioneSuoloModel lavorazione) {
		List<SuoloModel> suoloLavorazione = lavorazione.getListaSuoloInCorsoModel();
		for (SuoloModel suolo : new ArrayList<>(suoloLavorazione)) {
			lavorazione.removeSuoloInCorsoModel(suolo);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected LavorazioneSuoloModel cancellaAreaDiLavoro(LavorazioneSuoloModel lavorazione, boolean cancellaSeModalitaAdl) {

		if (cancellaSeModalitaAdl || lavorazione.getModalitaADL() == ModalitaADL.POLIGONI_INTERI) {
			List<AreaDiLavoroModel> areeeDiLavoro = lavorazione.getListaAreadiLavoro();
			for (AreaDiLavoroModel areeaDiLavoro : new ArrayList<>(areeeDiLavoro)) {
				lavorazione.removeAreaDiLavoroModel(areeaDiLavoro);
			}
		}

		List<TempClipSuADLModel> clipAdls = lavorazione.getListaTempClipSuADL();
		for (TempClipSuADLModel clipAdl : new ArrayList<>(clipAdls)) {
			lavorazione.removeTempClipSuADLModel(clipAdl);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected LavorazioneSuoloModel cancellaWorkspace(LavorazioneSuoloModel lavorazione) {
		List<WorkspaceLavSuoloModel> workspaces = lavorazione.getListaLavorazioneWorkspaceModel();
		for (WorkspaceLavSuoloModel workspace : new ArrayList<>(workspaces)) {
			lavorazione.removeWorkspaceLavSuoloModel(workspace);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected LavorazioneSuoloModel cancellaTmpWorkspace(LavorazioneSuoloModel lavorazione) {
		List<WorkspaceTmpModel> workspaceTmpModels = lavorazione.getListaLavorazioneWorkspaceTmpModel();
		for (WorkspaceTmpModel workspaceTmpModel : new ArrayList<>(workspaceTmpModels)) {
			lavorazione.removeWorkspaceTmpModel(workspaceTmpModel);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	protected LavorazioneSuoloModel cancellaAnomalie(LavorazioneSuoloModel lavorazione) {
		List<AnomaliaValidazioneModel> anomalieValidazione = lavorazione.getListaAnomalieValidazione();
		for (AnomaliaValidazioneModel anomaliaValidazione : new ArrayList<>(anomalieValidazione)) {
			lavorazione.removeAnomaliaLavSuoloModel(anomaliaValidazione);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
	}

	public static enum TipologiaAzione {
		CREAZIONE, MODIFICA, AVVIO, COLLEGA_DICHIARATO, RIMUOVI_DICHIARATO, ASSOCIAZIONE_POLIGONI_VIGENTE, DISSOCIA_POLIGONO_VIGENTE, CANCELLAZIONE, AGGIORNA_WORKSPACE, CERCA_POLIGONI_VIGENTE_DA_CLICK_IN_MAPPA, CERCA_POLIGONI_DICHIARATO_DA_CLICK_IN_MAPPA, AGGIORNA_ESITO_DICHIARATO, VALIDA_LAVORAZIONE_IN_CORSO, CREA_BUCHI_LAVORAZIONE, CONSOLIDA_IN_A4S, CONSOLIDA_IN_AGS, RIPRENDI_LAVORAZIONE, RITAGLIA_SU_ADL, REFRESH_STATO_JOB_FME_LAVORAZIONE, AGGIORNA_ADL, COPIA, CAMBIA_CAMPAGNA
	}

}
