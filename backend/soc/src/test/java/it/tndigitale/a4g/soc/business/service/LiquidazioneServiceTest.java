package it.tndigitale.a4g.soc.business.service;

import it.tndigitale.a4g.soc.business.dto.Debito;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.dto.TipoDomanda;
import it.tndigitale.a4g.soc.business.persistence.repository.LiquidazioneCustomDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LiquidazioneServiceTest {

    private LiquidazioneService liquidazioneService;
    private LiquidazioneCustomDao liquidazioneCustomDao;

    public LiquidazioneServiceTest() {
        liquidazioneCustomDao = Mockito.mock(LiquidazioneCustomDao.class);
        liquidazioneService = new LiquidazioneService()
                .setLiquidazioneCustomDao(liquidazioneCustomDao);
    }

    @Test
    public void itReturnImportiLiquidazione() {
        when(liquidazioneCustomDao.calcolaImportoLiquidato(importoLiquidatoFilter())).thenReturn(importoLiquidatoSenzaDebiti());
        when(liquidazioneCustomDao.calcolaDebitiImportoLiquidato(importoLiquidatoSenzaDebiti().get(0))).thenReturn(debiti());

        List<ImportoLiquidato> importoLiquidato = liquidazioneService.caricaImportoLiquidazione(importoLiquidatoFilter());

        assertThat(importoLiquidato.get(0)).isNotNull();
        assertThat(importoLiquidato.get(0)).isEqualTo(importoLiquidatoConDebiti());
        verify(liquidazioneCustomDao).calcolaImportoLiquidato(importoLiquidatoFilter());
        verify(liquidazioneCustomDao).calcolaDebitiImportoLiquidato(any());
    }

    private ImportoLiquidatoFilter importoLiquidatoFilter() {
        return new ImportoLiquidatoFilter()
        		.setNumeroDomanda(BigDecimal.ONE)
        		.setIdElencoLiquidazione(1L)
                .setTipoDomanda(TipoDomanda.DOMANDA_UNICA)
                .setCuaa("abc123")
                .setAnno(2019);
    }

    private List<ImportoLiquidato> importoLiquidatoSenzaDebiti() {
    	ArrayList<ImportoLiquidato> list = new ArrayList<ImportoLiquidato>();
        ImportoLiquidato importoLiquidato = new ImportoLiquidato().setTipoBilancio("SALDO")
                                     .setIncassatoNetto(BigDecimal.valueOf(10.0))
                                     .setProgressivo(239L)
                                     .setAnno(2019);
        list.add(importoLiquidato);
		return list;
    }

    private List<Debito> debiti() {
        return Arrays.asList(new Debito().setDescrizioneCapitolo("Descrizione").setImporto(BigDecimal.TEN));
    }

    private ImportoLiquidato importoLiquidatoConDebiti() {
        return importoLiquidatoSenzaDebiti().get(0).setDebiti(debiti());
    }
}
