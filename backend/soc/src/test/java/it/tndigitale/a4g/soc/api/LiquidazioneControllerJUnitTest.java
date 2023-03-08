package it.tndigitale.a4g.soc.api;

import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.service.LiquidazioneService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.tndigitale.a4g.soc.business.dto.TipoDomanda.DOMANDA_UNICA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LiquidazioneControllerJUnitTest {

    private LiquidazioneController liquidazioneController;
    private LiquidazioneService liquidazioneService;

    public LiquidazioneControllerJUnitTest() {
        liquidazioneService = mock(LiquidazioneService.class);

        liquidazioneController = new LiquidazioneController()
                .setLiquidazioneService(liquidazioneService);
    }

    @Test
    public void forGetImportiLiquidazioneItReturnImportoLiquidazione() {
        when(liquidazioneService.caricaImportoLiquidazione(importoLiquidatoFilter())).thenReturn(importoLiquidato());

        List<ImportoLiquidato> inportoLiquidato = liquidazioneController.getImportiLiquidazione(importoLiquidatoFilter());

        assertThat(inportoLiquidato).isNotNull();
        assertThat(inportoLiquidato).isEqualTo(importoLiquidato());
        verify(liquidazioneService).caricaImportoLiquidazione(importoLiquidatoFilter());
    }

    private ImportoLiquidatoFilter importoLiquidatoFilter() {
        return new ImportoLiquidatoFilter().setTipoDomanda(DOMANDA_UNICA)
                                           .setAnno(2019)
                                           .setIdElencoLiquidazione(100L)
                                           .setNumeroDomanda(BigDecimal.ONE);
    }

    private List<ImportoLiquidato> importoLiquidato() {
        return new ArrayList<ImportoLiquidato>();
    }
}
