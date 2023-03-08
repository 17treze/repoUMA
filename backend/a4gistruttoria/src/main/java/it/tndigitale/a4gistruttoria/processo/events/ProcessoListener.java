package it.tndigitale.a4gistruttoria.processo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.processo.exceptions.ProcessoInEsecuzioneException;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Component
public class ProcessoListener implements ApplicationListener<ProcessoEventHandler> {

	@Autowired
	private ProcessoDao daoProcesso;

	@Autowired
	private ObjectMapper objectMapper;

	private static Logger logger = LoggerFactory.getLogger(ProcessoListener.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void onApplicationEvent(ProcessoEventHandler event) {
		ProcessoEvent processoEvent = event.geProcessoEvent();
		A4gtProcesso a4gtProcesso = new A4gtProcesso();
		boolean creazione = Objects.isNull(processoEvent.getId());
		try {
			if (!creazione) {
				A4gtProcesso a4gtProcessoFiltro = new A4gtProcesso();
				a4gtProcessoFiltro.setId(processoEvent.getId());
				a4gtProcesso = daoProcesso.findOne(Example.of(a4gtProcessoFiltro))
						.orElseThrow(() -> new EntityNotFoundException(String.format("Nessun processo trovato con ID: %d", a4gtProcessoFiltro.getId())));
				a4gtProcesso.setDtFine(processoEvent.getDtFine());
			}
			if (Objects.nonNull(processoEvent.getStatoProcesso())) {
				a4gtProcesso.setStato(processoEvent.getStatoProcesso());
			}
			if (Objects.nonNull(processoEvent.getTipoProcesso())) {
				a4gtProcesso.setTipo(processoEvent.getTipoProcesso());
			}
			if (creazione && daoProcesso.exists(Example.of(a4gtProcesso))) {
				// se esiste un processo attivo per tipologia blocco l'esecuzione lanciando un messaggio d'errore come da BR "BRIAMPRT001"
				String error = String.format("Esiste gia' un processo attivo per tipo %s e stato %s.", processoEvent.getTipoProcesso(), processoEvent.getStatoProcesso());
				logger.warn(error);
				throw new ProcessoInEsecuzioneException();
			}
			if (!creazione) {
				if (processoEvent.getDatiElaborazioneProcesso() != null) {
					DatiElaborazioneProcesso datiElaborazioneProcesso = objectMapper.readValue(a4gtProcesso.getDatiElaborazione(), DatiElaborazioneProcesso.class);
					BeanUtils.copyProperties(processoEvent.getDatiElaborazioneProcesso(), datiElaborazioneProcesso, "gestite", "conProblemi");
					if (processoEvent.getDatiElaborazioneProcesso().getConProblemi() != null) {
						datiElaborazioneProcesso.getConProblemi().addAll(processoEvent.getDatiElaborazioneProcesso().getConProblemi());
					}
					if (processoEvent.getDatiElaborazioneProcesso().getGestite() != null) {
						datiElaborazioneProcesso.getGestite().addAll(processoEvent.getDatiElaborazioneProcesso().getGestite());
					}
					a4gtProcesso.setDatiElaborazione(objectMapper.writeValueAsString(datiElaborazioneProcesso));
				}
			} else {
				a4gtProcesso.setDatiElaborazione(objectMapper.writeValueAsString(processoEvent.getDatiElaborazioneProcesso()));
				a4gtProcesso.setDtInizio(processoEvent.getDtInizio());
			}
			a4gtProcesso.setPercentualeAvanzamento(Objects.isNull(processoEvent.getPercentualeAvanzamento()) ? a4gtProcesso.getPercentualeAvanzamento() : processoEvent.getPercentualeAvanzamento());
			a4gtProcesso = Objects.requireNonNull(daoProcesso.save(a4gtProcesso), () -> {
				String error = String.format("Impossibile %s il processo con tipo %s e stato %s.", creazione ? "creare" : "aggiornare", processoEvent.getTipoProcesso(),
						processoEvent.getStatoProcesso());
				logger.error(error);
				return error;
			});
			CustomThreadLocal.addVariable("idProcesso", a4gtProcesso.getId());
		} catch (Exception e) {
			logger.error(getErroreGenerico(creazione, a4gtProcesso.getId()));
			try {
				throw e;
			} catch (Exception e1) {
				logger.error(getErroreGenerico(creazione, a4gtProcesso.getId()));
			}
		}

	}

	private String getErroreGenerico(boolean creazione, Long idProcesso) {
		StringBuilder stringBuilder = new StringBuilder();
		// switch per diminuire la complessità ciclomatica
		switch (Boolean.toString(creazione)) {
		case "true":
			stringBuilder.append("Errore nella creazione del processo.");
			break;
		case "false":
			stringBuilder.append(String.format("Errore nell'aggiornamento del processo con id %d", idProcesso));
			break;
		default:
			break;
		}
		return stringBuilder.toString();
	}
}