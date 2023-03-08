package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoLavorazioneDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoAnomaliaValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AnomaliaValidazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness.ValidLavorazioneInCorsoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.config.ErroriOracle;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaPoligoniWithRootWorkspaceDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneInCorsoDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.WorkspaceLavSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.AnomaliaPoligoniWithRootWorkspaceMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.AnomaliaValidazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.SuoloDichiaratoLavorazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.WorkspaceLavSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("VALIDA_LAVORAZIONE_IN_CORSO")
public class ValidLavorazioneInCorsoBusiness extends AzioneLavorazioneBase<ValidazioneLavorazioneInCorsoDto, ValidLavorazioneInCorsoInputData> {

	private static final Logger log = LoggerFactory.getLogger(ValidLavorazioneInCorsoBusiness.class);

	@Value("${it.tndigit.oracle.scostamentoAreaAccettato}")
	private int scostamentoAreaAccettato;

	@Value("${it.tndigit.oracle.percentualeScostamentoAreaAccettato}")
	private Double percentualeScostamentoAreaAccettato;

	@Value("${it.tndigit.oracle.tolleranza}")
	public Double tolleranza;

	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private SuoloDichiaratoLavorazioneMapper suoloDichiaratoLavorazioneMapper;

	@Autowired
	private WorkspaceLavSuoloMapper workspaceLavSuoloMapper;

	@Autowired
	private AnomaliaValidazioneMapper anomaliaValidazioneMapper;

	@Autowired
	private AnomaliaPoligoniWithRootWorkspaceMapper anomaliaPoligoniWithRootWorkspaceMapper;

	@Autowired
	private SuoloDichiaratoLavorazioneDao suoloDichiaratoLavorazioneDao;

	@Autowired
	private AnomaliaValidazioneDao anomaliaValidazioneDao;

	@Autowired
	private WorkspaceLavorazioneDao workspaceLavorazioneDao;
	@Value("${it.tndigit.serverFme.workspaceValidazioneLavorazioneInCorso}")
	private String workspaceValidazioneSovrapposizioni;

	@Autowired
	private UtilsFme utilsFme;

	@Override
	protected ValidazioneLavorazioneInCorsoDto eseguiAzione(ValidLavorazioneInCorsoInputData input) {
		return validLavorazioneInCorso(input.idLavorazione, input.tipoValidazione);
	}

	public ValidazioneLavorazioneInCorsoDto validLavorazioneInCorso(Long idLavorazione, TipoValidazione tipoValidazione) {
		log.debug("START - validLavorazioneInCorso {}", idLavorazione);

		ValidazioneLavorazioneInCorsoDto res = new ValidazioneLavorazioneInCorsoDto();
		res.setIdLavorazione(idLavorazione);

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(idLavorazione, utenteComponent.username());

		checkValidStateLavorazione(lavorazione);

		// Elimina le vecchie anomalie
		anomaliaValidazioneDao.deleteAnomalieLavorazione(lavorazione);

		res.setPoligoniAnomalieOracle(checkPoligoniWorkspaceAnomalieOracle(lavorazione));
		// ricarica la lavorazione
		lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);

		if (tipoValidazione.equals(TipoValidazione.TUTTI)) {
			try {
				checkAnomaliaPoligoniFme(res, lavorazione);
			} catch (Exception e) {
				log.error("Errore validazione trasformata FME", e);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore validazione lavorazione");
			}
			lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
			res.setPoligoniDichiaratoSenzaEsito(checkPoligoniDichiaratoSenzaEsito(lavorazione));
			res.setPoligoniDichiaratoRichiestaCancellata(checkPoligoniDichiaratoRichiestaCancellata(lavorazione));

			List<WorkspaceLavSuoloModel> workspaceLavorazione = workspaceLavorazioneDao.findByIdLavorazioneWorkspaceLavSuolo(lavorazione);

			res.setPoligoniSuoloAttributiMancanti(checkPoligoniSuoloAttributiMancanti(workspaceLavorazione));

			// Filter DTO

		}

		log.debug("END - validLavorazioneInCorso ");
		return res;
	}

	protected void checkValidStateLavorazione(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per l'operazione previsto IN_CORSO ottenuto {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente la validazione "))));
		}
	}

	public List<AnomaliaPoligoniWithRootWorkspaceDto> checkPoligoniWorkspaceAnomalieOracle(LavorazioneSuoloModel lavorazione) {
		List<AnomaliaPoligoniWithRootWorkspaceDto> listaAnomalie = new ArrayList<>();
		if (!lavorazione.getListaLavorazioneWorkspaceModel().isEmpty()) {
			for (WorkspaceLavSuoloModel poligonoWorkspace : lavorazione.getListaLavorazioneWorkspaceModel()) {
				String validazione = validateGeometry(poligonoWorkspace.getId());
				if (!validazione.equals("TRUE")) {
					String descrizioneAnomalia = validazione + ErroriOracle.getMap().getOrDefault(validazione, "");
					String validazioneLightFixed = validateFixedGeometry(poligonoWorkspace.getId());
					if (validazioneLightFixed.equals("TRUE")) {
						Double areaFixed = getAreaFixed(poligonoWorkspace.getId());
						Double scostamentoArea = Math.abs(areaFixed - poligonoWorkspace.getArea());
						Long getNumElemFixed = getNumElemFixed(poligonoWorkspace.getId());
						if (getNumElemFixed == 1 && scostamentoArea <= scostamentoAreaAccettato && scostamentoArea / poligonoWorkspace.getArea() <= percentualeScostamentoAreaAccettato) {
							// aggiorna
							fixWorkspace(poligonoWorkspace.getId());
						} else {
							// salva come proposta
							insertFixedWorkspaceAnomalies(poligonoWorkspace.getId(), descrizioneAnomalia);
						}
					} else { // salva come non correggibile
						insertNotFixedWorkspaceErrors(poligonoWorkspace.getId(), descrizioneAnomalia);
					}
				}
			}

			List<TipoAnomaliaValidazione> tipiAnomalia = new ArrayList<>();
			tipiAnomalia.add(TipoAnomaliaValidazione.ERRORI_ORACLE_CORRETTI_CON_SCOSTAMENTO_AREA);
			tipiAnomalia.add(TipoAnomaliaValidazione.ERRORI_ORACLE);

			return searchAnomaliaWithRootWorkspace(lavorazione, tipiAnomalia);

		} else

		{
			return new ArrayList<>();
		}
	}

	public Long getNumElemFixed(Long idWorkspace) {
		return workspaceLavorazioneDao.getNumElemFixed(idWorkspace, tolleranza);
	}

	public Double getAreaFixed(Long idWorkspace) {
		return workspaceLavorazioneDao.getAreaFixed(idWorkspace, tolleranza);
	}

	public String validateFixedGeometry(Long idWorkspace) {
		return workspaceLavorazioneDao.validateFixedGeometry(idWorkspace, tolleranza);
	}

	public String validateGeometry(Long idWorkspace) {
		return workspaceLavorazioneDao.validateGeometry(idWorkspace, tolleranza);
	}

	// estratto ai fini di h2
	public boolean insertNotFixedWorkspaceErrors(Long workspaceId, String descrizioneAnomalia) {
		anomaliaValidazioneDao.insertNotFixedWorkspaceErrors(workspaceId, TipoAnomaliaValidazione.ERRORI_ORACLE_NON_CORREGGIBILI.toString(), descrizioneAnomalia, tolleranza);
		return true;
	}

	// estratto ai fini di h2
	public boolean insertFixedWorkspaceAnomalies(Long workspaceId, String descrizioneAnomalia) {
		anomaliaValidazioneDao.insertFixedWorkspaceAnomalies(workspaceId, TipoAnomaliaValidazione.ERRORI_ORACLE_CORRETTI_CON_SCOSTAMENTO_AREA.toString(), descrizioneAnomalia, tolleranza);
		return true;
	}

	// estratto ai fini di h2
	public boolean fixWorkspace(Long workspaceId) {
		workspaceLavorazioneDao.fixWorkspace(workspaceId, tolleranza);
		return true;
	}

	protected List<SuoloDichiaratoLavorazioneDto> checkPoligoniDichiaratoSenzaEsito(LavorazioneSuoloModel lavorazione) {
		if (!lavorazione.getSuoloDichiaratoModel().isEmpty()) {
			List<SuoloDichiaratoModel> poligonoSenzaEsito = lavorazione.getSuoloDichiaratoModel().stream()
					.filter(t -> !t.getRichiestaModificaSuolo().getStato().equals(StatoRichiestaModificaSuolo.CANCELLATA) && t.getEsito().equals(EsitoLavorazioneDichiarato.DA_LAVORARE))
					.collect(Collectors.toList());

			List<SuoloDichiaratoLavorazioneModel> poligonoSenzaEsitoLavorazione = new ArrayList<>();

			poligonoSenzaEsito.forEach(x -> poligonoSenzaEsitoLavorazione.add(suoloDichiaratoLavorazioneDao.getOne(x.getId())));

			return suoloDichiaratoLavorazioneMapper.fromList(poligonoSenzaEsitoLavorazione);
		} else {
			return new ArrayList<>();
		}
	}

	protected List<SuoloDichiaratoLavorazioneDto> checkPoligoniDichiaratoRichiestaCancellata(LavorazioneSuoloModel lavorazione) {
		if (!lavorazione.getSuoloDichiaratoModel().isEmpty()) {
			List<SuoloDichiaratoModel> poligonoSenzaEsito = lavorazione.getSuoloDichiaratoModel().stream()
					.filter(t -> t.getRichiestaModificaSuolo().getStato().equals(StatoRichiestaModificaSuolo.CANCELLATA)).collect(Collectors.toList());

			List<SuoloDichiaratoLavorazioneModel> poligonoSenzaEsitoLavorazione = new ArrayList<>();

			poligonoSenzaEsito.forEach(x -> poligonoSenzaEsitoLavorazione.add(suoloDichiaratoLavorazioneDao.getOne(x.getId())));

			return suoloDichiaratoLavorazioneMapper.fromList(poligonoSenzaEsitoLavorazione);
		} else {
			return new ArrayList<>();
		}
	}

	private List<WorkspaceLavSuoloDto> checkPoligoniSuoloAttributiMancanti(List<WorkspaceLavSuoloModel> workspaceLavorazione) {
		if (!workspaceLavorazione.isEmpty()) {
			List<WorkspaceLavSuoloModel> poligoniSenzaAttributi = workspaceLavorazione.stream().filter(t -> checkPoligonoSuoloAttributiMancanti(t) == true).collect(Collectors.toList());

			return workspaceLavSuoloMapper.fromList(poligoniSenzaAttributi);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * 
	 * @param ValidazioneLavorazioneInCorsoDto
	 * @param LavorazioneSuoloModel
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void checkAnomaliaPoligoniFme(ValidazioneLavorazioneInCorsoDto res, LavorazioneSuoloModel lavorazione) throws Exception {

		ResponseEntity<String> responseFme = utilsFme.callProcedureFme(lavorazione.getId(), workspaceValidazioneSovrapposizioni);

		if (responseFme.getStatusCodeValue() == 200) {
			List<AnomaliaValidazioneModel> anomaliaValidazioneSovrapposizione = anomaliaValidazioneDao.findByLavorazioneSuoloInAnomaliaValidazioneAndTipoAnomalia(lavorazione,
					TipoAnomaliaValidazione.SOVRAPPOSIZIONE_POLIGONI_WORKSPACE);

			res.setPoligoniAnomaliaSovrapposizioni(anomaliaValidazioneMapper.fromList(anomaliaValidazioneSovrapposizione));

			res.setPoligoniAnomaliaDebordanoAreaDiLavoro(searchAnomaliaWithRootWorkspace(lavorazione, TipoAnomaliaValidazione.POLIGONI_DEBORDANO_AREA_DI_LAVORO));

		} else if (responseFme.getStatusCode().is5xxServerError()) {
			log.error("Errore validazione trasformata FME ");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore validazione trasformata FME");
		}
	}

	private boolean checkPoligonoSuoloAttributiMancanti(WorkspaceLavSuoloModel workspaceLavSuolo) {
		if (workspaceLavSuolo.getCodUsoSuoloWorkspaceLavSuolo().getCodUsoSuolo().equals(LavorazioneSuoloService.USO_SUOLO_NON_DEFINITO)) {
			return true;
		} else {
			return false;
		}
	}

	public static class ValidLavorazioneInCorsoInputData {
		private Long idLavorazione;
		private TipoValidazione tipoValidazione;

		public ValidLavorazioneInCorsoInputData(Long idLavorazione, TipoValidazione tipoValidazione) {
			super();
			this.idLavorazione = idLavorazione;
			this.tipoValidazione = tipoValidazione;
		}
	}

	public enum TipoValidazione {
		TUTTI
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @param lavorazione
	 * @param tipoAnomalia
	 * @return la trasposta dei dati da Anomalia a Workspace
	 */
	private List<AnomaliaPoligoniWithRootWorkspaceDto> searchAnomaliaWithRootWorkspace(LavorazioneSuoloModel lavorazione, TipoAnomaliaValidazione tipoAnomalia) {
		List<TipoAnomaliaValidazione> tipiAnomalia = new ArrayList<>();
		tipiAnomalia.add(tipoAnomalia);
		return searchAnomaliaWithRootWorkspace(lavorazione, tipiAnomalia);
	}

}
