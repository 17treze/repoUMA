package it.tndigitale.a4g.proxy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

//@ControllerAdvice
//@Order(Ordered.LOWEST_PRECEDENCE)
public class LogExceptionHandler { //extends ResponseEntityExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

//	@ExceptionHandler(value = { Throwable.class })
//	@ResponseBody
    protected ResponseEntity<? extends Object> handleConflict(Throwable t, WebRequest request) {
        log.error("Errore generico per la request " + request, t);
        String errorResponse = t.getMessage();
        if (t instanceof Exception) {
            try {
            	//return handleException((Exception)t, request);
            } catch (Exception e) {
                return new ResponseEntity<String>("Si e' verificato un errore non previsto: " + errorResponse,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<String>("Si e' verificato un errore non previsto: " + errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
