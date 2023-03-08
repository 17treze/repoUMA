package it.tndigitale.a4g.richiestamodificasuolo.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	private static Map<SuoloException.ExceptionType, HttpStatus> toHttpStatus;

    static{
        toHttpStatus = new HashMap<SuoloException.ExceptionType, HttpStatus>();
        toHttpStatus.put(SuoloException.ExceptionType.SECURITY_EXCEPTION, HttpStatus.FORBIDDEN);
        toHttpStatus.put(SuoloException.ExceptionType.NOT_FOUND_EXCEPTION, HttpStatus.NO_CONTENT);
        toHttpStatus.put(SuoloException.ExceptionType.CONFLICT_EXCEPTION, HttpStatus.CONFLICT);
        toHttpStatus.put(SuoloException.ExceptionType.GENERIC_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        toHttpStatus.put(SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }
    
    
    @ExceptionHandler(value = { Throwable.class})
    @ResponseBody
    protected ResponseEntity<String> handle(Throwable  t, WebRequest request) {
    	if (t instanceof SuoloException) {
    		log.error("Riscontrato errore {} per la richiesta {}", t.getMessage(), request);
    		return convertSuoloException((SuoloException)t);
    	}
    	Throwable cause = t.getCause();
    	if (cause != null && cause instanceof SuoloException) {
    		log.error("Riscontrato errore {} per la richiesta {}", cause.getMessage(), request);
    		return convertSuoloException((SuoloException)cause);
    	}
    	if (t instanceof ObjectOptimisticLockingFailureException) {
    		log.error("Errore Lock Ottimistico {} per la richiesta {}", t.getMessage(), request);
    		return convertOptimisticLockingFailure((ObjectOptimisticLockingFailureException)t);
    	}
    	if (t instanceof AccessDeniedException) {
    		log.error("Errore abilitazioni {} per la richiesta {}", t.getMessage(), request);
    		return convertAccessDeniedException((AccessDeniedException)t);
    	}
    	log.error("Errore generico per la richiesta {}", request, t);
    	return convertGeneric(t);
    }

	protected ResponseEntity<String> convertGeneric(Throwable t) {
		return new ResponseEntity<String>("Errore generico",  getBasicHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected ResponseEntity<String> convertSuoloException(SuoloException e) {
		return new ResponseEntity<String>(e.getMessage(),  getBasicHeaders(), status(e));
	}
    
	protected ResponseEntity<String> convertOptimisticLockingFailure(ObjectOptimisticLockingFailureException e) {
		return new ResponseEntity<String>("Dati aggiornati da altro utente",  getBasicHeaders(), HttpStatus.CONFLICT);
	}
	protected ResponseEntity<String> convertAccessDeniedException(AccessDeniedException e) {
		return new ResponseEntity<String>(e.getMessage(),  getBasicHeaders(), HttpStatus.FORBIDDEN);
	}
	
	protected HttpHeaders getBasicHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
	
    static private HttpStatus status(SuoloException ex) {
        return toHttpStatus.getOrDefault(ex.getType(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
