package it.tndigitale.a4gutente.service.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.Test;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;

public class UtenteLoaderTest {

    private IUtenteCompletoDao utenteCompletoDao;
    private UtenteLoader utenteLoader;
    private UtenteComponent utenteComponent;
    private static final Long ID_UTENTE = 1L;
    private static final A4gtUtente UTENTE = new A4gtUtente();

    public UtenteLoaderTest() {
        utenteCompletoDao = mock(IUtenteCompletoDao.class);
        utenteComponent = mock(UtenteComponent.class);

        utenteLoader = new UtenteLoader().setComponents(utenteCompletoDao,
                                                        utenteComponent);
    }

    @Test
    public void forLoadIfNotExistUtenteThenThrowing() {
        when(utenteCompletoDao.findById(ID_UTENTE)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> utenteLoader.load(ID_UTENTE));
        verify(utenteCompletoDao).findById(ID_UTENTE);
    }

    @Test
    public void forLoadIfExistUtenteThenReturnUtente() {
        when(utenteCompletoDao.findById(ID_UTENTE)).thenReturn(Optional.of(UTENTE));

        A4gtUtente utente = utenteLoader.load(ID_UTENTE);

        assertThat(utente).isNotNull();
        verify(utenteCompletoDao).findById(ID_UTENTE);
    }

}
