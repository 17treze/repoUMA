package it.tndigitale.a4g.framework.pagination.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaginazioneTest {

    private static final Paginazione PAGINAZIONE_DEFAULT = new Paginazione(10, 0);
    private static final Integer MY_PAGE_NUMBER = 3;
    private static final Integer MY_NUMERO_ELEMENTI_PAGINA_DEFAULT = 13;
    private static final Integer NUMERO_ELEMENTI_PAGINA_DEFAULT = 10;
    private static final Integer PAGINA_DEFAULT = 0;

    @Test
    public void ifPaginazioneNullThenReturnPaginazioneDefault() {
        Paginazione paginazione = Paginazione.getOrDefault(null);

        assertThat(paginazione).isEqualTo(PAGINAZIONE_DEFAULT);
    }

    @Test
    public void ifPaginazioneNotNullAndNumeroRecordXPaginaNullThenReturnPaginazioneWithDefaultValue() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(null, MY_PAGE_NUMBER));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(MY_PAGE_NUMBER);
    }

    @Test
    public void ifPaginazioneNotNullAndNumeroRecordXPaginaMinoreZeroTHenDefaultValue() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(-1, MY_PAGE_NUMBER));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(MY_PAGE_NUMBER);
    }

    @Test
    public void ifPaginazioneNotNullAndNumeroRecordXPaginaEqualZeroTHenDefaultValue() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(0, MY_PAGE_NUMBER));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(MY_PAGE_NUMBER);
    }

    @Test
    public void ifPaginazioneNotNullAndNumeroPaginaNullThenDefaultValue() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT, null));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(PAGINA_DEFAULT);
    }

    @Test
    public void ifPaginazioneNotNullAndNumeroPaginaMinoreZeroThenDefaultValue() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT, -1));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(PAGINA_DEFAULT);
    }

    @Test
    public void ifPaginazioneNotNullAndParametersOKThenReturnPagination() {
        Paginazione paginazione = Paginazione.getOrDefault(new Paginazione(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT, MY_PAGE_NUMBER));

        assertThat(paginazione.getNumeroElementiPagina()).isEqualTo(MY_NUMERO_ELEMENTI_PAGINA_DEFAULT);
        assertThat(paginazione.getPagina()).isEqualTo(MY_PAGE_NUMBER);
    }

}
