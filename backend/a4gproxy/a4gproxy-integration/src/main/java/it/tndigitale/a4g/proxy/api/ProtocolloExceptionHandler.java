package it.tndigitale.a4g.proxy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProtocolloExceptionHandler extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(ProtocolloExceptionHandler.class);
	
	@ExceptionHandler(value = { PiTreException.class })
	protected ResponseEntity<Object> handlePiTreException(PiTreException ex, WebRequest request) {
		log.error("Errore eseguento la request " + request, ex);
		return handleExceptionInternal(ex, ex.getExtendedMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}
}
