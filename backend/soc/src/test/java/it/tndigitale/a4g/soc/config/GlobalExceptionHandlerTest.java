package it.tndigitale.a4g.soc.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.WebRequest;

import it.tndigitale.a4g.framework.exception.ValidationException;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private static final String MESSAGE = "MESSAGE";

    private GlobalExceptionHandler handler;
    private WebRequest request;

    public GlobalExceptionHandlerTest() {
        request = mock(WebRequest.class);

        handler = new GlobalExceptionHandler();
    }

    @Test
    public void forHandleValidationException() {
        ValidationException validationException = new ValidationException(MESSAGE);

        ResponseEntity<String> responseEntity =  handler.handle(validationException, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(MESSAGE);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void forHandleEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException(MESSAGE);

        ResponseEntity<String> responseEntity =  handler.handle(entityNotFoundException, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(MESSAGE);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void forHandleAccessDeniedException() {
        AccessDeniedException accessDeniedException = new AccessDeniedException(MESSAGE);

        ResponseEntity<String> responseEntity =  handler.handle(accessDeniedException, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(MESSAGE);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
