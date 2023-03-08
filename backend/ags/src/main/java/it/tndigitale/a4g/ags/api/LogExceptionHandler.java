package it.tndigitale.a4g.ags.api;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(assignableTypes = FascicoloRestController.class)
public class LogExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

	@ExceptionHandler(value = { Throwable.class })
	@ResponseBody
	protected ResponseEntity<String> handleConflict(Throwable t, WebRequest request) {
		String errorResponse = t.getMessage();
		if (t instanceof NoResultException) {
			return new ResponseEntity<String>((String)null, HttpStatus.NOT_FOUND);
		} 
		log.error("Errore generico per la request " + request, t);

		return new ResponseEntity<String>("Si e' verificato un errore non previsto: " + errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseBody
	protected ResponseEntity<String> handleAccessDenied(AccessDeniedException ade, WebRequest request) {
		log.error("Utente non abilitato alla request " + request, ade);
		String errorResponse = ade.getMessage();

		return new ResponseEntity<String>("Utente non abilitato: " + errorResponse, HttpStatus.FORBIDDEN);
	}
}
