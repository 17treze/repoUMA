package it.tndigitale.a4gutente.api;

import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.dto.UtentiFilter;
import it.tndigitale.a4gutente.exception.ValidationException;
import it.tndigitale.a4gutente.service.IDomandaRegistrazioneService;
import it.tndigitale.a4gutente.service.IUtenteService;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class UtenteControllerJTest {

    private UtenteController utenteController;
    private IUtenteService utenteService;
    private IDomandaRegistrazioneService domandaService;

    public UtenteControllerJTest() {
        utenteService = mock(IUtenteService.class);
        domandaService = mock(IDomandaRegistrazioneService.class);

        utenteController = new UtenteController().setComponents(utenteService, domandaService);
    }

    @Test
    public void forRicercaUtentiIfFilterNullThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> utenteController.ricerca(null));
        verify(utenteService, never()).ricerca(any());
    }

    @Test
    public void forRicercaUtentiIfFilterWithNullPropertiesThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> utenteController.ricerca(new UtentiFilter()));
        verify(utenteService, never()).ricerca(any());
    }

    @Test
    public void forRicercaUtentiIfFilterWithEmptyPropertiesThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                utenteController.ricerca(new UtentiFilter().setCodiceFiscale("").setCognome("").setNome("")));
        verify(utenteService, never()).ricerca(any());
    }

    @Test
    public void forRicercaUtentiIfFilterWithPropertiesWithOnlySpaceBlankThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                utenteController.ricerca(new UtentiFilter().setCodiceFiscale("   ").setCognome("   ").setNome("   ")));
        verify(utenteService, never()).ricerca(any());
    }

    @Test
    public void forRicercaUtentiIfFilterOkThenCallService() throws Exception {
        when(utenteService.ricerca(aUtentiRicerca())).thenReturn(singletonList(new Utente()));

        List<Utente> utenti = utenteController.ricerca(aUtentiRicerca());

        assertThat(utenti).hasSize(1);
        verify(utenteService).ricerca(aUtentiRicerca());
    }

    private UtentiFilter aUtentiRicerca() {
        return new UtentiFilter().setNome("xxxx");
    }
}
