package it.tndigitale.a4g.framework.exception;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ValidationExceptionTest {

    @Test
    public void creaValidationException() {
        ValidationException validationException = new ValidationException();

        assertThat(validationException).isNotNull();
        assertThat(validationException).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void creaValidationExceptionByMessage() {
        ValidationException validationException = new ValidationException("message");

        assertThat(validationException).isNotNull();
        assertThat(validationException.getMessage()).isEqualTo("message");
        assertThat(validationException).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void creaValidationExceptionByMessageAndThrowable() {
        Throwable exception = mock(Throwable.class);

        ValidationException validationException = new ValidationException("message", exception);

        assertThat(validationException).isNotNull();
        assertThat(validationException.getMessage()).isEqualTo("message");
        assertThat(validationException.getCause()).isEqualTo(exception);
        assertThat(validationException).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void creaValidationExceptionByThrowable() {
        Throwable exception = mock(Throwable.class);

        ValidationException validationException = new ValidationException(exception);

        assertThat(validationException).isNotNull();
        assertThat(validationException.getCause()).isEqualTo(exception);
        assertThat(validationException).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void creaValidationExceptionAndSettingCode() {
        ValidationException validationException = new ValidationException().setCode(100);


        assertThat(validationException).isNotNull();
        assertThat(validationException.getCode()).isEqualTo(100);
        assertThat(validationException).isInstanceOf(RuntimeException.class);
    }
}
