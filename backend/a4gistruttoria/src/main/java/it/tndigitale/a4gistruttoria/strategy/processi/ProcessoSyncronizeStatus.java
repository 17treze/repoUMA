package it.tndigitale.a4gistruttoria.strategy.processi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;


/**
 * Gestisce la persistenza dell'avanzamento del processo.
 * @author Lorenzo Martinelli
 */
@Component
public class ProcessoSyncronizeStatus {
	private static final Logger logger = LoggerFactory.getLogger(ProcessoSyncronizeStatus.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProcessoDao processoDao;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <T> void aggiornaProcesso(Long idProcesso, int avanzamento, StatoProcesso statoProcesso,
			T datiProcesso) {
		try {
			String dati = objectMapper.writeValueAsString(datiProcesso);
			aggiornaProcesso(idProcesso, avanzamento, statoProcesso, dati);
		} catch (JsonProcessingException e) {
			logger.error("Errore durante l'aggiornamento dei dati elaborazione del processo");
		}
	}
	
	void aggiornaProcesso(Long idProcesso, int avanzamento, StatoProcesso statoProcesso,
			String datiElaborazione) {
		A4gtProcesso processoModel;

		Optional<A4gtProcesso> opt = processoDao.findById(idProcesso);
		if (opt.isPresent()) {
			processoModel = opt.get();
			processoModel.setPercentualeAvanzamento(new BigDecimal(avanzamento));
			processoModel.setStato(statoProcesso);
			processoModel.setDatiElaborazione(datiElaborazione);
			if (avanzamento == 100 || statoProcesso.equals(StatoProcesso.KO)) {
				processoModel.setDtFine(new Date());
			}
			processoDao.saveAndFlush(processoModel);
		} else {
			logger.warn("Processo da aggiornare non trovato: {}", idProcesso);
		}
	}
	

	/**
	 * Metodo che crea un nuovo processo dopo aver verificato che non ci sia un processo dello stesso tipo non parallelizzabili per lo stesso settore in esecuzione
	 */
	public Long creaNuovoProcesso(Processo processo) throws Exception {
		logger.debug("creaNuovoProcesso: " + processo.getTipoProcesso());
		if (processoDao.countByProcessoInEsecuzione(
				processo.getTipoProcesso(),
				StatoProcesso.RUN) > 0) {
			return null;
		}

		A4gtProcesso processoModel = new A4gtProcesso();
		processoModel.setStato(StatoProcesso.START);
		processoModel.setTipo(processo.getTipoProcesso());
		processoModel.setDtInizio(new Date());
		processoModel.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.saveAndFlush(processoModel);
		return processoModel.getId();
	}
}
