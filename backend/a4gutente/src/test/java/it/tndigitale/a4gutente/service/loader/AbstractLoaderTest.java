package it.tndigitale.a4gutente.service.loader;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import javax.validation.ValidationException;

import org.junit.Test;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class AbstractLoaderTest {

    @Test
    public void forCheckIfIdNullThenThrowing() {
        AbstractLoader loader = getLoader();
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> loader.checkId(null));
    }

    @Test
    public void forCheckIfIdNullThenNotThrowing() {
        AbstractLoader loader = getLoader();
        loader.checkId(1L);
    }

    private AbstractLoader getLoader() {
        return new AbstractLoader() {
            @Override
            public <T extends EntitaDominio> T load(Long id) {
                return null;
            }
        };
    }

}
