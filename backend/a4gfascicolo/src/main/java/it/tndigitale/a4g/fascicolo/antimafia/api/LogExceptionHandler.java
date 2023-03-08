package it.tndigitale.a4g.fascicolo.antimafia.api;

import it.tndigitale.a4g.fascicolo.antimafia.exceptions.UtenteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LogExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

	@ExceptionHandler(value = { Throwable.class })
	@ResponseBody
    protected ResponseEntity<String> handleConflict(Throwable t, WebRequest request) {
        String errorResponse = t.getMessage();
        if (t instanceof UtenteException) {
            return new ResponseEntity<String>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (t instanceof HttpClientErrorException ) {
            HttpClientErrorException errore = (HttpClientErrorException)t;
            return new ResponseEntity<String>(errorResponse, errore.getStatusCode());
        }
        if (t instanceof AccessDeniedException) {
            return new ResponseEntity<String>(errorResponse, HttpStatus.FORBIDDEN);
        }
        log.error("Errore generico per la request " + request, t);

        return new ResponseEntity<String>("Si e' verificato un errore non previsto: " + errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
