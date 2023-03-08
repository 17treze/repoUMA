package it.tndigitale.a4gistruttoria.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.IstruttoriaFilter;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiPagamento;
import it.tndigitale.a4gistruttoria.service.DomandaUnicaService;

public class DomandaUnicaControllerJTest {

    private DomandaUnicaController controller;
    private DomandaUnicaService domandaUnicaService;

    public DomandaUnicaControllerJTest() {
        domandaUnicaService = mock(DomandaUnicaService.class);

        controller = new DomandaUnicaController()
            .setComponents(domandaUnicaService);
    }

    @Test
    public void itReturnIstruttorieByIdDomanda() throws Exception {
        when(domandaUnicaService.getIstruttorie(1L)).thenReturn(Arrays.asList(new Istruttoria()));

        List<Istruttoria> istruttorie = controller.getIstruttorie(1L);

        assertThat(istruttorie).hasSize(1);
        verify(domandaUnicaService).getIstruttorie(1L);
    }

    @Test
    public void itReturnIstruttorieByNumeroDomanda() throws Exception {
        when(domandaUnicaService.getIstruttorie(BigDecimal.ONE)).thenReturn(Arrays.asList(new Istruttoria()));

        List<Istruttoria> istruttorie = controller.getIstruttorie(BigDecimal.ONE);

        assertThat(istruttorie).hasSize(1);
        verify(domandaUnicaService).getIstruttorie(BigDecimal.ONE);
    }

    @Test
    public void itReturnIstruttorieByFilter() throws Exception {
        when(domandaUnicaService.getIstruttorie(istruttoriaFilter())).thenReturn(Arrays.asList(new Istruttoria()));

        List<Istruttoria> istruttorie = controller.getIstruttorie(istruttoriaFilter());

        assertThat(istruttorie).hasSize(1);
        verify(domandaUnicaService).getIstruttorie(istruttoriaFilter());
    }

    @Test
    public void itReturnSintesiPagamentiyIdDomanda() throws Exception {
        when(domandaUnicaService.getSintesiPagamento(1L)).thenReturn(new SintesiPagamento(1d, 1d));

        SintesiPagamento sintesiPagamento = controller.getSintesiPagamenti(1L);

        assertThat(sintesiPagamento).isNotNull();
        assertThat(sintesiPagamento.getImportoCalcolato()).isEqualTo(1d);
        assertThat(sintesiPagamento.getImportoLiquidato()).isEqualTo(1d);
        verify(domandaUnicaService).getSintesiPagamento(1L);
    }

    @Test
    public void itReturnSintesiPagamentiyNumerDomanda() throws Exception {
        when(domandaUnicaService.getSintesiPagamento(BigDecimal.ONE)).thenReturn(new SintesiPagamento(1d, 1d));

        SintesiPagamento sintesiPagamento = controller.getSintesiPagamenti(BigDecimal.ONE);

        assertThat(sintesiPagamento).isNotNull();
        assertThat(sintesiPagamento.getImportoCalcolato()).isEqualTo(1d);
        assertThat(sintesiPagamento.getImportoLiquidato()).isEqualTo(1d);
        verify(domandaUnicaService).getSintesiPagamento(BigDecimal.ONE);
    }
    
    private IstruttoriaFilter istruttoriaFilter() {
        return new IstruttoriaFilter()
                .setCampagna(1990)
                .setCuaa("XXXX");
    }
}
