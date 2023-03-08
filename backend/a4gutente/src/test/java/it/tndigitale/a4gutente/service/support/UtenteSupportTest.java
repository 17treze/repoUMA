package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import it.tndigitale.a4gutente.service.loader.PersonaLoader;
import it.tndigitale.a4gutente.service.loader.UtenteLoader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UtenteSupportTest {

    private UtenteLoader utenteLoader;
    private PersonaLoader personaLoader;
    private UtenteSupport support;

    private static final Long ID_UTENTE = 1L;
    private static final String CODICE_FISCALE = "STRFBA66H02E406Y";
    private static final String NOME = "NOME";
    private static final String COGNOME = "COGNOME";

    public UtenteSupportTest() {
        utenteLoader = mock(UtenteLoader.class);
        personaLoader = mock(PersonaLoader.class);

        support = new UtenteSupport().setUtenteLoader(utenteLoader, personaLoader);
    }

    @Test
    public void forCaricaUtenteConAnagraficaItReturnUtente() {
        when(utenteLoader.load(ID_UTENTE)).thenReturn(utente());
        when(personaLoader.loadBy(CODICE_FISCALE)).thenReturn(persona());

        Utente utente = support.caricaUtenteConAnagrafica(ID_UTENTE);

        assertThat(utente).isNotNull();
        assertThat(utente.getNome()).isEqualTo(NOME);
        assertThat(utente.getCognome()).isEqualTo(COGNOME);
        assertThat(utente.getCodiceFiscale()).isEqualTo(CODICE_FISCALE);
        assertThat(utente.getIdentificativo()).isEqualTo(CODICE_FISCALE);
        verify(utenteLoader).load(ID_UTENTE);
        verify(personaLoader).loadBy(CODICE_FISCALE);
    }

    private A4gtUtente utente() {
        A4gtUtente utente = new A4gtUtente();
        utente.setId(ID_UTENTE);
        utente.setCodiceFiscale(CODICE_FISCALE);
        utente.setIdentificativo(CODICE_FISCALE);
        return utente;
    }

    private PersonaEntita persona() {
        PersonaEntita persona = new PersonaEntita();
        persona.setNome(NOME);
        persona.setCognome(COGNOME);
        persona.setCodiceFiscale(CODICE_FISCALE);
        return persona;
    }
}
