package it.tndigitale.a4g.uma.business.service.logging;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.LoggingModel;
import it.tndigitale.a4g.uma.business.persistence.repository.UmaLoggingDao;

@Service
public class LoggingService {

	private static final String INSERT = "INSERT";
	private static final String UPDATE = "UPDATE";
	private static final String USER_SCHEDULER = "USER_SCHEDULER_EVENT";

	@Autowired
	private UmaLoggingDao umaLoggingDao;

	// Utile al reperimento dell'operatore che ha effettuato la creazione/inserimento della tabella indicata
	public String getOperatoreCreazione(Class<?> clazz, Long idEntita) {
		final String tabella = clazz.getAnnotation(Table.class).name();
		// Cerco il log di insert. Ne sarà sempre 1 e 1 solo
		List<LoggingModel> logs = umaLoggingDao.findByTabellaAndTipoEventoAndIdEntita(tabella, INSERT, idEntita);
		// Verifico che la lista non sia vuota e che ci sia un solo elemento
		if (CollectionUtils.isEmpty(logs) && logs.size() != 1) {
			return null;
		}
		// Prendo il log
		Optional<LoggingModel> log = logs.stream().findFirst();

		return log.isPresent() ? log.get().getUtente() : null;
	}

	// Utile al reperimento dell'operatore che ha effettuato l'operazione di update più recente della tabella indicata
	public String getOperatoreUltimoAggiornamento(Class<?> clazz, Long idEntita) {
		final String tabella = clazz.getAnnotation(Table.class).name();
		// Cerco i log di update
		List<LoggingModel> logs = umaLoggingDao.findByTabellaAndTipoEventoAndIdEntita(tabella, UPDATE, idEntita);
		// Verifico che la lista non sia vuota
		if (CollectionUtils.isEmpty(logs)) {
			return null;
		}
		// Prendo il log più recente escludendo l'utenza USER_SCHEDULER_EVENT
		Optional<LoggingModel> log = logs.stream()
				.filter(logToFilter -> !logToFilter.getUtente().equals(USER_SCHEDULER))
				.max(Comparator.comparing(LoggingModel::getDtEvento));

		return log.isPresent() ? log.get().getUtente() : null;
	}
}
