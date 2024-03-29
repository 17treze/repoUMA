package it.tndigitale.a4g.framework.ext.validazione.repository.handler;

import java.time.LocalDateTime;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;

import org.springframework.core.annotation.AnnotationUtils;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.repository.handler.LoggingUser;
import it.tndigitale.a4g.framework.security.configuration.AutowireSecurityContext;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

public class PersistenzaEntitaFascicoloHandler<T extends EntitaDominioFascicolo> {

	@PostPersist
	public void afterPersist(T entita) throws Exception {
		logDbEvent("INSERT", entita);
	}

	@PostUpdate
	public void afterUpdate(T entita) throws Exception {
		logDbEvent("UPDATE", entita);
	}

	@PostRemove
	public void afterRemove(T entita) throws Exception {
		logDbEvent("DELETE", entita);
	}
	
	protected LoggingValidazioneUser prepareInstance(
			final LocalDateTime timestamp,
			final T entita,
			final String tabella,
			final String currentUser,
			final String tipoEvento) {
		LoggingValidazioneUser loggingUser = new LoggingValidazioneUser();
		loggingUser.setDtEvento(timestamp);
		loggingUser.setIdEntita(entita.getId());
		loggingUser.setIdValidazione(entita.getIdValidazione());
		loggingUser.setTabella(tabella);
		loggingUser.setUtente(currentUser);
		loggingUser.setTipoEvento(tipoEvento);
		return loggingUser;
	}
	
	/**
	 * Scrive l'evento di modifica dati occorso all'interno della tabella di logging del sistema.
	 * 
	 * @param tipoEvento
	 *            tipo di evento di modifica occorso (INSERT, DELETE, UPDATE).
	 * @param entita
	 *            record oggetto della modifica.
	 */
	protected void logDbEvent(String tipoEvento, T entita) throws Exception {
			// utente invoca operazione, i servizi REST dovrebbero estrarlo dall'header http
			// e fornirlo allo strato sottostante di service e dao
			AutowireSecurityContext securityContext = AutowireSecurityContext.build();
			
			UtenteComponent utenteComp = securityContext.newObjectBy(UtenteComponent.class);
			String currentUser = utenteComp.utenza();
			LocalDateTime timestamp = LocalDateTime.now(); // now nel nostro zone id
			String tabella = AnnotationUtils.findAnnotation(entita.getClass(), Table.class).name();
			
			LoggingValidazioneUser logging = prepareInstance(
					timestamp,
					entita,
					tabella,
					currentUser,
					tipoEvento);
			/**
			 * TODO devo fare insert sulla tabella dei log dei dati (A4GT_LOGGING)
			 */
			@SuppressWarnings("unchecked")
			LoggingValidazioneDao loggingValidazioneDAO = securityContext.newObjectBy(LoggingValidazioneDao.class);
			loggingValidazioneDAO.save(logging);
	}
}
