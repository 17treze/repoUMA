package it.tndigitale.a4gistruttoria.service.businesslogic.processo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.strategy.processi.ProcessoSyncronizeStatus;

public abstract class ElaboraProcesso<T extends Processo, S> {

	private static final Logger logger = LoggerFactory.getLogger(ElaboraProcesso.class);
	
	@Autowired
	private ProcessoSyncronizeStatus sync;

	public void avvia(T datiInputProcesso) {
		Long idProcesso = datiInputProcesso.getIdProcesso();
		S datiProcesso = istanziaDatiProcesso();
		try {
			inizializzaAvanzamentoProcesso(datiInputProcesso, datiProcesso);
			elabora(datiInputProcesso, datiProcesso, idProcesso);
			elaborazioneTerminataConSuccesso(datiInputProcesso);
			processoConclusoConSuccesso(datiInputProcesso, datiProcesso);
		} catch (Exception e) {
			logger.error("avvia: errore generico", e);
			sync.aggiornaProcesso(idProcesso, 100, StatoProcesso.KO, datiProcesso);
		}
	
	}

	protected void elaborazioneTerminataConSuccesso(T datiInputProcesso) throws Exception {
	}

	protected void processoConclusoConSuccesso(T datiInputProcesso, S datiProcesso) {
		sync.aggiornaProcesso(datiInputProcesso.getIdProcesso(), 100, StatoProcesso.OK, datiProcesso);
	}

	protected void inizializzaAvanzamentoProcesso(T datiInputProcesso, S datiProcesso) {
		inizializzaDatiProcesso(datiInputProcesso, datiProcesso);

		avanzamentoProcesso(datiInputProcesso.getIdProcesso(), 0, datiProcesso);
	}
	
	protected abstract void elabora(T datiInputProcesso, S datiProcesso, Long idProcesso) throws Exception;

	protected abstract void inizializzaDatiProcesso(T datiInputProcesso, S datiProcesso);
	
	protected abstract S istanziaDatiProcesso();
	
	protected void avanzamentoProcesso(Long idProcesso, int avanzamento, S datiProcesso) {
		sync.aggiornaProcesso(idProcesso, avanzamento, StatoProcesso.RUN, datiProcesso);
	}
}
