package it.tndigitale.a4gutente.config;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

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

import it.tndigitale.a4gutente.exception.UtenteException;

@ControllerAdvice
public class LogExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

	@ExceptionHandler(value = { Throwable.class })
	@ResponseBody
	protected ResponseEntity<String> handleConflict(Throwable t, WebRequest request) {
		String errorResponse = t.getMessage();
		if (t instanceof UtenteException) {
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		if (t instanceof EntityNotFoundException) {
			log.error(t.getMessage());
			return new ResponseEntity<>((String)null, HttpStatus.NOT_FOUND);
		}
		if (t instanceof ValidationException) {
			log.error(t.getMessage());
			return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (t instanceof AccessDeniedException) {
			log.error(t.getMessage());
			return  new ResponseEntity<>(t.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		log.error("Errore generico per la request " + request, t);

		return new ResponseEntity<String>("Si e' verificato un errore non previsto: " + errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
