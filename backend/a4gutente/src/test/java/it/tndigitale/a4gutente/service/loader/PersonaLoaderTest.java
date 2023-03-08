package it.tndigitale.a4gutente.service.loader;

import it.tndigitale.a4gutente.repository.dao.IPersonaDao;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import org.junit.Test;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class PersonaLoaderTest {

    private PersonaLoader personaLoader;
    private IPersonaDao personaDao;

    private static final Long ID_PERSONA = 1L;
    private static final String CODICE_FISCALE = "CODICE_FISCALE";

    public PersonaLoaderTest() {
        personaDao = mock(IPersonaDao.class);

        personaLoader = new PersonaLoader().setPersonaDao(personaDao);
    }

    @Test
    public void forLoadByIdIfIdNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> personaLoader.load(null));
        verify(personaDao, never()).findById(ID_PERSONA);
    }

    @Test
    public void forLoadByIdIfPersonWithIdNotExistThenThrowing() {
        when(personaDao.findById(ID_PERSONA)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> personaLoader.load(ID_PERSONA));
        verify(personaDao).findById(ID_PERSONA);
    }

    @Test
    public void forLoadByIdIfPersonWithIdExistThenReturnPerson() {
        when(personaDao.findById(ID_PERSONA)).thenReturn(Optional.of(new PersonaEntita()));

        PersonaEntita personaEntita = personaLoader.load(ID_PERSONA);

        assertThat(personaEntita).isNotNull();
        verify(personaDao).findById(ID_PERSONA);
    }

    @Test
    public void forLoadByFiscalCodeIfFiscalCodeNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> personaLoader.loadBy(null));
        verify(personaDao, never()).findOneByCodiceFiscale(any());
    }

    @Test
    public void forLoadByFiscalCodeIfFiscalCodeEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> personaLoader.loadBy(""));
        verify(personaDao, never()).findOneByCodiceFiscale(any());
    }

    @Test
    public void forLoadByFiscalCodeIfPersonNotExistThenThrowing() {
        when(personaDao.findOneByCodiceFiscale(CODICE_FISCALE)).thenReturn(null);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> personaLoader.loadBy(CODICE_FISCALE));
        verify(personaDao).findOneByCodiceFiscale(CODICE_FISCALE);
    }

    @Test
    public void forLoadByFiscalCodeIfExistPersonThenReturnPerson() {
        when(personaDao.findOneByCodiceFiscale(CODICE_FISCALE)).thenReturn(new PersonaEntita());

        PersonaEntita persona = personaLoader.loadBy(CODICE_FISCALE);

        assertThat(persona).isNotNull();
        verify(personaDao).findOneByCodiceFiscale(CODICE_FISCALE);
    }
}
