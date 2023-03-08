package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempPoligoniInOutAdlModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.TempPoligoniInOutAdlDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.UtenteControllerClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness.TipoValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.config.ErroriOracle;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneInCorsoDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.WorkspaceLavSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@Component("CONSOLIDA_IN_A4S")
public class ConsolidaInA4sBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(ConsolidaInA4sBusiness.class);

	@Autowired
	private ValidLavorazioneInCorsoBusiness validLavorazioneInCorsoBusiness;

	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private SuoloDao suoloDao;

	@Autowired
	private TempPoligoniInOutAdlDao tempPoligoniInOutAdlDao;

	@Autowired
	private UtenteControllerClient utenteControllerClient;

	@Value("${it.tndigit.serverFme.consolidaLavorazioneInA4S}")
	private String consolidaLavorazioneInA4S;

	@Value("${it.tndigit.serverFme.elaboraWsPerConsolidamentoLavorazioneInA4S_ADL}")
	private String elaboraWsPerConsolidamentoLavorazioneInA4S_ADL;

	@Value("${it.tndigit.serverFme.pathSalvataggio}")
	private String pathSalvataggio;

	@Value("${it.tndigit.serverFme.buchiPerimetroPrevalente}")
	private Long buchiPerimetroPrevalente;

	@Value("${it.tndigit.oracle.scostamentoAreaAccettato}")
	private int scostamentoAreaAccettato;

	@Value("${it.tndigit.oracle.tolleranza}")
	public Double tolleranza;

	@Autowired
	private WorkspaceLavSuoloMapper workspaceLavSuoloMapper;

	@Value("${it.tndigit.oracle.percentualeScostamentoAreaAccettato}")
	private Double percentualeScostamentoAreaAccettato;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		return consolidaLavorazioneInA4s(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente());
	}

	private LavorazioneSuoloModel consolidaLavorazioneInA4s(Long idLavorazione, Integer versione, String utente) {
		log.debug("START - consolidaLavorazioneInA4s");

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);

		checkValidState(lavorazione);

		// unisce poligoni workspace adiacenti con stesso cod_uso, stato colt
		// Splitta i poligoni di workspace sulla griglia

		dissolveWorkspaceERitagliaSuGrigliaFme(lavorazione);
		/*
		 * Riesegue la validazione della lavorazione considerando i poligoni di workspace splittati su griglia, ed in caso positivo: In una transazione: Aggiorna i poligoni di suolo vigente che hanno
		 * id_lavorazione_in_corso = idLavorazione con: id_lavorazione_fine = idLavorazione data_fine_validita=sysdate ( salvare in variabile tmp e usarla in tutte le future date = sysdate)
		 * id_lavorazione_in_corso = null crea nuovi record di suolo vigente copiandoli dal workspace con id_lavorazione_inizio = idLavorazione, gli attributi definiti sul ws e id_lavorazione_corrente
		 * = idLavorazione (tengo temporaneamente i poligoni nuovi occupati fino a quando non consolido in AGS in modo che non ci siano altri che li possono lavorare nel frattempo e creare conflitti);
		 * Data_inizio_validita= sysdate ( vedi sopra); Data_fine_validita = 31/12/999
		 */

		ValidazioneLavorazioneInCorsoDto res = validLavorazioneInCorsoBusiness.validLavorazioneInCorso(idLavorazione, TipoValidazione.TUTTI);

		if (res.isValidazioneCorretta()) {
			// chiama nuova trasformata
			// valida
			// se valido scrivi in suolo
			// se non valida manda messaggio di errore

			LavorazioneSuoloModel lavorazioneAggiornata = null;

			if (lavorazione.getModalitaADL() == ModalitaADL.DISEGNO_ADL) {
				elaboraWorkspaceInternoConEsternoFme(lavorazione);

				boolean validAdl = checkPoligoniAdlAnomalie(lavorazione);

				if (validAdl) {
					List<TempPoligoniInOutAdlModel> tmpPoligoni = tempPoligoniInOutAdlDao.findByLavorazioneSuolo(lavorazione);
					List<WorkspaceLavSuoloModel> listWorkspace = new ArrayList<>();
					for (TempPoligoniInOutAdlModel tempPoligoniInOutAdlModel : tmpPoligoni) {
						listWorkspace.add(workspaceLavSuoloMapper.convertTempPoligoniInOutAdlModelToModel(tempPoligoniInOutAdlModel));
					}
					lavorazioneAggiornata = aggiornaSuoloDaWorkspacePerConsolidareLavorazione(lavorazione, listWorkspace);
				} else {
					log.debug("END - consolidaLavorazioneInA4s Validazione Non Corretta");
					throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
							.newSuoloExceptionInstance("Consolidamento su suolo A4Gis fallito a causa di errori di validazione su unione suoli interni e esterni all'ADL.");
				}
			} else {
				List<WorkspaceLavSuoloModel> listWorkspace = lavorazione.getListaLavorazioneWorkspaceModel();
				lavorazioneAggiornata = aggiornaSuoloDaWorkspacePerConsolidareLavorazione(lavorazione, listWorkspace);
			}

			if (lavorazioneAggiornata != null) {
				String utenteAgs = utente;

				try {
					RappresentaIlModelloPerRappresentareUnUtenteDelSistema datiUtente = utenteControllerClient.getUtente(utente);
					String nomeUtente = datiUtente.getNome();
					String cognomeUtente = datiUtente.getCognome();

					if (!(nomeUtente == null || nomeUtente.isEmpty() || cognomeUtente == null || cognomeUtente.isEmpty())) {
						utenteAgs = Character.toString(nomeUtente.toUpperCase().trim().charAt(0)).concat(cognomeUtente.toUpperCase());
					}
				} catch (HttpClientErrorException | NullPointerException | StringIndexOutOfBoundsException e) {
					log.debug("Errore nel recupero dati utente.");
				}
				lavorazioneAggiornata.setUtenteAgs(utenteAgs.concat("_").concat(String.valueOf(lavorazioneAggiornata.getId())));

				LavorazioneSuoloDao lavorazioneSuoloDao = getLavorazioneSuoloDao();
				lavorazioneSuoloDao.save(lavorazioneAggiornata);
			}
			log.debug("END - consolidaLavorazioneInA4s");
			return null;
		} else {
			log.debug("END - consolidaLavorazioneInA4s Validazione Non Corretta");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Consolidamento su suolo A4Gis fallito a causa di errori di validazione.");

		}
	}

	public boolean checkPoligoniAdlAnomalie(LavorazioneSuoloModel lavorazione) {
		List<TempPoligoniInOutAdlModel> tmpPoligoni = tempPoligoniInOutAdlDao.findByLavorazioneSuolo(lavorazione);

		if (!tmpPoligoni.isEmpty()) {
			for (TempPoligoniInOutAdlModel poligonoTmp : tmpPoligoni) {
				String validazione = validateGeometry(poligonoTmp.getId());
				if (!validazione.equals("TRUE")) {
					String descrizioneAnomalia = validazione + ErroriOracle.getMap().getOrDefault(validazione, "");
					String validazioneLightFixed = validateFixedGeometry(poligonoTmp.getId());
					if (validazioneLightFixed.equals("TRUE")) {
						Double areaFixed = getAreaFixed(poligonoTmp.getId());
						Double scostamentoArea = Math.abs(areaFixed - poligonoTmp.getArea());
						Long getNumElemFixed = getNumElemFixed(poligonoTmp.getId());
						if (getNumElemFixed == 1 && scostamentoArea <= scostamentoAreaAccettato && scostamentoArea / poligonoTmp.getArea() <= percentualeScostamentoAreaAccettato) {
							// aggiorna
							fixData(poligonoTmp.getId());
							fixArea(poligonoTmp.getId());

						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			return true;
		} else {
			return true;
		}
	}

	public Long getNumElemFixed(Long id) {
		return tempPoligoniInOutAdlDao.getNumElemFixed(id, tolleranza);
	}

	public Double getAreaFixed(Long id) {
		return tempPoligoniInOutAdlDao.getAreaFixed(id, tolleranza);
	}

	public String validateFixedGeometry(Long id) {
		return tempPoligoniInOutAdlDao.validateFixedGeometry(id, tolleranza);
	}

	public String validateGeometry(Long id) {
		return tempPoligoniInOutAdlDao.validateGeometry(id, tolleranza);
	}

	// estratto ai fini di h2
	public boolean fixData(Long id) {
		tempPoligoniInOutAdlDao.fixData(id, tolleranza);
		return true;
	}

	// estratto ai fini di h2
	public boolean fixArea(Long id) {
		tempPoligoniInOutAdlDao.fixArea(id, tolleranza);
		return true;
	}

	private LavorazioneSuoloModel aggiornaSuoloDaWorkspacePerConsolidareLavorazione(LavorazioneSuoloModel lavorazione, List<WorkspaceLavSuoloModel> listWorkspace) {
		LocalDateTime dataAttuale = getClock().now();
		LocalDateTime dataFine = LocalDateTime.of(9999, 12, 31, 00, 00);

		List<SuoloModel> listSuoliInCorso = suoloDao.findByIdLavorazioneInCorso(lavorazione);
		for (SuoloModel suoloModel : listSuoliInCorso) {
			suoloModel.setIdLavorazioneFine(lavorazione);
			suoloModel.setIdLavorazioneInCorso(null);
			suoloModel.setDataFineValidita(dataAttuale);
			for (WorkspaceLavSuoloModel workspaceLavSuoloModel : listWorkspace) {
				suoloModel.setNote(workspaceLavSuoloModel.getNote());
			}
		}
		suoloDao.saveAll(listSuoliInCorso);

		for (WorkspaceLavSuoloModel workspaceLavSuoloModel : listWorkspace) {
			SuoloModel suolo = new SuoloModel();
			suolo.setIdLavorazioneInizio(workspaceLavSuoloModel.getIdLavorazioneOrigWorkspaceLavSuolo() == null ? lavorazione : workspaceLavSuoloModel.getIdLavorazioneWorkspaceLavSuolo());
			suolo.setIdLavorazioneInCorso(lavorazione);

			if (workspaceLavSuoloModel.getSorgente() != null)
				suolo.setSorgente(workspaceLavSuoloModel.getSorgente());
			else
				suolo.setSorgente("LAVORAZIONE SUOLO");

			suolo.setCodUsoSuoloModel(workspaceLavSuoloModel.getCodUsoSuoloWorkspaceLavSuolo());
			suolo.setStatoColtSuolo(workspaceLavSuoloModel.getStatoColtWorkspaceLavSuolo());
			suolo.setDataInizioValidita(dataAttuale);
			suolo.setDataFineValidita(dataFine);

			suolo.setShape(workspaceLavSuoloModel.getShape());
			suolo.setAreaColt(workspaceLavSuoloModel.getArea());
			suolo.setIdGrid(workspaceLavSuoloModel.getIdGridWorkspace());
			suolo.setNote(workspaceLavSuoloModel.getNote());
			if (workspaceLavSuoloModel.getIstatp() != null) {
				suolo.setIstatp(workspaceLavSuoloModel.getIstatp());
			} else {
				suolo.setIstatp("022");
			}

			suolo.setCampagna(lavorazione.getCampagna());

			suoloDao.save(suolo);
		}

		lavorazione.setStato(StatoLavorazioneSuolo.CONSOLIDATA_SU_A4S);
		return lavorazione;

	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per consolidamento in A4S {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente il consolidamento in A4GIS"))));

		}

	}

	/**
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void dissolveWorkspaceERitagliaSuGrigliaFme(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFmeSync(consolidaLavorazioneInA4S,
					utilsFme.generateBodyTrasformataConsolidamentoA4S(lavorazione.getId(), lavorazione.getCampagna(), buchiPerimetroPrevalente, pathSalvataggio));
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo A4Gis");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME ");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo A4Gis");
		}
	}

	/**
	 * 
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void elaboraWorkspaceInternoConEsternoFme(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFmeSync(elaboraWsPerConsolidamentoLavorazioneInA4S_ADL,
					utilsFme.generateBodyTrasformataConsolidamentoA4S_ADL(lavorazione.getId(), buchiPerimetroPrevalente));
		} catch (URISyntaxException e) {
			log.error("Errore trasformata FME", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo A4Gis");
		}

		if (responseFme.getStatusCodeValue() != 200) {
			log.error("Errore trasformata FME ");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo A4Gis");
		}
	}

}
