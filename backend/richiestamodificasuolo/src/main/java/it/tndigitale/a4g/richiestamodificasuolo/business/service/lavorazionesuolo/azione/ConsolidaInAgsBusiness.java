package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.JobFmeLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoJobFME;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.JobFmeLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.dto.fme.ResponseBodyFmeDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CONSOLIDA_IN_AGS")
public class ConsolidaInAgsBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(ConsolidaInAgsBusiness.class);

	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private JobFmeLavorazioneDao jobFmeLavorazioneDao;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${it.tndigit.serverFme.consolidaLavorazioneInAGS}")
	private String consolidaLavorazioneInAGS;

	@Value("${it.tndigit.serverFme.sogliaClipperConsolidamentoAGS}")
	private Long sogliaClipperConsolidamentoAGS;

	@Value("${it.tndigit.serverFme.scostamentoAreaGruppo}")
	private Long scostamentoAreaGruppo;

	@Value("${it.tndigit.serverFme.bufferGruppo}")
	private Long bufferGruppo;

	@Autowired
	private Clock clock;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		return consolidaLavorazioneInAGs(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente());
	}

	private LavorazioneSuoloModel consolidaLavorazioneInAGs(Long idLavorazione, Integer versione, String utente) {
		log.debug("START - consolidaLavorazioneInA4s");

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);

		chechValidState(lavorazione);

		consolidaInAGS(lavorazione);
		/*
		 * trasforma in GB il workspace della Lavorazione identifica in AGS le particelle (in tutta la sitipart) che sono in relazione con i miei poligoni trasformati (tipo sdo_relate) input: poligoni
		 * ws in GB tutti i poligoni vigenti della sitipart in GB (sysdate between data_inizio_val and data_fine_val) output: lista di particelle in GB che "sono interessate" dal workspace
		 */
		lavorazione.setStato(StatoLavorazioneSuolo.CONSOLIDAMENTO_IN_CORSO);

		getLavorazioneSuoloDao().save(lavorazione);
		return lavorazione;

	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.CONSOLIDATA_SU_A4S.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.ERRORE_CONSOLIDAMENTO.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per consolidamento in AGS {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente il consolidamento in AGS"))));

		}

	}

	/**
	 *
	 * @param LavorazioneSuoloModel
	 * @throws URISyntaxException
	 * @throws SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
	 */
	private void consolidaInAGS(LavorazioneSuoloModel lavorazione) {

		ResponseEntity<String> responseFme;
		try {
			responseFme = utilsFme.callProcedureFmeAsync(consolidaLavorazioneInAGS, utilsFme.generateBodyTrasformataConsolidamentoAGS(lavorazione.getId(), lavorazione.getCampagna(), clock.now(),
					lavorazione.getUtenteAgs(), sogliaClipperConsolidamentoAGS, scostamentoAreaGruppo, bufferGruppo));
			JobFmeLavorazioneModel jobFmeLavorazioneModel = new JobFmeLavorazioneModel();

			jobFmeLavorazioneModel.setTipoJobFme(TipoJobFME.CONSOLIDAMENTO_AGS);
			jobFmeLavorazioneModel.setIdJobFme(objectMapper.readValue(responseFme.getBody(), ResponseBodyFmeDto.class).getId());
			jobFmeLavorazioneModel.setRelLavorazioneSuolo(lavorazione);
			jobFmeLavorazioneModel.setDataInizioEsecuzione(LocalDateTime.now());
			jobFmeLavorazioneDao.save(jobFmeLavorazioneModel);

		} catch (URISyntaxException | JsonProcessingException e) {
			log.error("Errore trasformata FME", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo AGS");
		}

		if (responseFme.getStatusCodeValue() != 202) {
			log.error("Errore trasformata FME ");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore nel consolidamento della lavorazione su suolo AGS");
		}

	}
}
