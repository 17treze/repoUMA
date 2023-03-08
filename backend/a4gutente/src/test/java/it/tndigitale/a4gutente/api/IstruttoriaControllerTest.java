package it.tndigitale.a4gutente.api;

import it.tndigitale.a4gutente.dto.*;
import it.tndigitale.a4gutente.service.IstruttoriaService;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class IstruttoriaControllerTest {

    private IstruttoriaService istruttoriaService;
    private IstruttoriaController controller;

    private static final Long ID_DOMANDA = 1L;
    private static final Long ID_ISTRUTTORIA = 10L;
    private static final Long ID_UTENTE = 100L;

    public IstruttoriaControllerTest() {
        istruttoriaService = mock(IstruttoriaService.class);

        controller = new IstruttoriaController().setComponents(istruttoriaService);
    }

    @Test
    public void findByIdDomanda() throws Exception {
        when(istruttoriaService.findByIdDomanda(ID_DOMANDA)).thenReturn(aIstruttoria());

        Istruttoria istruttoria = controller.findByIdDomanda(ID_DOMANDA);

        assertThat(istruttoria).isNotNull();
        assertThat(istruttoria).isEqualTo(aIstruttoria());
        verify(istruttoriaService).findByIdDomanda(ID_DOMANDA);
    }

    @Test
    public void creaIstruttoria() {
        when(istruttoriaService.crea(aIstruttoria())).thenReturn(ID_ISTRUTTORIA);

        Long idIstruttoria = controller.crea(aIstruttoria());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(istruttoriaService).crea(aIstruttoria());
    }

    @Test
    public void aggiornaIstruttoria() {
        when(istruttoriaService.aggiorna(aIstruttoria())).thenReturn(ID_ISTRUTTORIA);

        Long idIstruttoria = controller.aggiorna(aIstruttoria());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(istruttoriaService).aggiorna(aIstruttoria());
    }

    @Test
    public void creaIstruttoriaSenzaDomanda() {
        when(istruttoriaService.creaSenzaDomanda(istruttoriaSenzaDomanda())).thenReturn(ID_ISTRUTTORIA);

        Long idIstruttoria = controller.creaSenzaDomanda(ID_UTENTE, new IstruttoriaSenzaDomanda());

        assertThat(idIstruttoria).isEqualTo(ID_ISTRUTTORIA);
        verify(istruttoriaService).creaSenzaDomanda(istruttoriaSenzaDomanda());
    }

    @Test
    public void ritornaStoricoUtente() {
        when(istruttoriaService.listaStorico(ID_UTENTE)).thenReturn(storico());

        StoricoIstruttorie storico = controller.storico(ID_UTENTE);

        assertThat(storico).isEqualTo(storico());
        verify(istruttoriaService).listaStorico(ID_UTENTE);
    }

    private Istruttoria aIstruttoria() {
        return new Istruttoria().setIdDomanda(ID_DOMANDA);
    }

    private IstruttoriaSenzaDomanda istruttoriaSenzaDomanda() {
        return new IstruttoriaSenzaDomanda().setIdUtente(ID_UTENTE);
    }

    private StoricoIstruttorie storico() {
        return new  StoricoIstruttorie().setIstruttorie(Arrays.asList(new IstruttoriaPerStorico()))
                                        .setUtente(new Utente());
    }
}
