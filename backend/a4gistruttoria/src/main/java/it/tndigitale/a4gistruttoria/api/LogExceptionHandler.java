package it.tndigitale.a4gistruttoria.api;

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

@ControllerAdvice
public class LogExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

	@ExceptionHandler(value = { AccessDeniedException.class })
	@ResponseBody
    protected ResponseEntity<String> handleConflict(AccessDeniedException t, WebRequest request) {
        String errorResponse = t.getMessage();
        log.error("Access denied per la req " + request, t);
        return new ResponseEntity<String>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
