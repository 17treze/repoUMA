package it.tndigitale.a4gutente.service.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.Test;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;

public class IstruttoriaLoaderTest {

    private DomandaLoader domandaLoader;
    private EnteLoader enteLoader;
    private ProfiloLoader profiloLoader;
    private IIstruttoriaDao istruttoriaDao;
    private UtenteLoader utenteLoader;
    private Clock clock;

    private IstruttoriaLoader loader;

    private static final Long ID_ISTRUTTORIA = 1L;

    public IstruttoriaLoaderTest() {
        domandaLoader = mock(DomandaLoader.class);
        enteLoader = mock(EnteLoader.class);
        profiloLoader = mock(ProfiloLoader.class);
        istruttoriaDao = mock(IIstruttoriaDao.class);
        utenteLoader = mock(UtenteLoader.class);
        clock = mock(Clock.class);

        loader = new IstruttoriaLoader()
                .setComponents(domandaLoader, enteLoader,
                               profiloLoader, istruttoriaDao, utenteLoader,
                               clock);
    }

    @Test
    public void forLoadifIstruttoriaNotExistThenThrowing() {
        when(istruttoriaDao.findById(ID_ISTRUTTORIA)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> loader.load(ID_ISTRUTTORIA));
        verify(istruttoriaDao).findById(ID_ISTRUTTORIA);
    }

    @Test
    public void forLoadIfIstruttoriaExistThenReturnEntity() {
        when(istruttoriaDao.findById(ID_ISTRUTTORIA)).thenReturn(Optional.of(new IstruttoriaEntita()));

        IstruttoriaEntita entita = loader.load(ID_ISTRUTTORIA);

        assertThat(entita).isEqualTo(new IstruttoriaEntita());
        verify(istruttoriaDao).findById(ID_ISTRUTTORIA);
    }
}
