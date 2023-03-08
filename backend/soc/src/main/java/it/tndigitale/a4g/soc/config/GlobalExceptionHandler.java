package it.tndigitale.a4g.soc.config;

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

import it.tndigitale.a4g.framework.exception.ValidationException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = { ValidationException.class })
    @ResponseBody
    protected ResponseEntity<String> handle(ValidationException validationException, WebRequest request) {
        log.error("Message: {}. Code: {}", validationException.getMessage(), validationException.getCode());
        return new ResponseEntity<>(validationException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    @ResponseBody
    protected ResponseEntity<String> handle(EntityNotFoundException entityNotFoundException, WebRequest request) {
        log.error(entityNotFoundException.getMessage());
        return new ResponseEntity<>(entityNotFoundException.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    @ResponseBody
    protected ResponseEntity<String> handle(AccessDeniedException accessDeniedException, WebRequest request) {
        log.error(accessDeniedException.getMessage());
        return new ResponseEntity<>(accessDeniedException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
