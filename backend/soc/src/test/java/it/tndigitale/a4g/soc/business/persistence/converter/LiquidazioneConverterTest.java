package it.tndigitale.a4g.soc.business.persistence.converter;

import it.tndigitale.a4g.soc.business.dto.CapitoloDisciplina;
import it.tndigitale.a4g.soc.business.dto.Debito;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.TipoDomanda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import static it.tndigitale.a4g.soc.business.persistence.converter.LiquidazioneConverter.debitoFrom;
import static it.tndigitale.a4g.soc.business.persistence.converter.LiquidazioneConverter.importoLiquidatoFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LiquidazioneConverterTest {

    private ResultSet resultSet;

    public LiquidazioneConverterTest() {
        resultSet = mock(ResultSet.class);
    }

    @Test
    public void forImportoLiquidatoFromItConverts() throws Exception {
        when(resultSet.getInt("annoEsercizio")).thenReturn(2019);
        when(resultSet.getBigDecimal("incassatoNetto")).thenReturn(BigDecimal.valueOf(100L));
        when(resultSet.getLong("progressivoCredito")).thenReturn(200L);
        when(resultSet.getString("tipoBilancio")).thenReturn("TIPOBILANCIO");
        when(resultSet.getString("numeroDomanda")).thenReturn("S100010392");

        ImportoLiquidato importoLiquidato = importoLiquidatoFrom(resultSet, new ArrayList<CapitoloDisciplina>(), TipoDomanda.DOMANDA_PSR_STRUTTURALE);

        assertThat(importoLiquidato).isNotNull();
        assertThat(importoLiquidato.getAnno()).isEqualTo(2019);
        assertThat(importoLiquidato.getIncassatoNetto()).isEqualTo(BigDecimal.valueOf(100L));
        assertThat(importoLiquidato.getProgressivo()).isEqualTo(200L);
        assertThat(importoLiquidato.getTipoBilancio()).isEqualTo("TIPOBILANCIO");
    }

    @Test
    public void forImportoLiquidatoFromIfDataAutorizzaNullAndNumeroAutorizzaNullItConverts() throws Exception {
        when(resultSet.getInt("annoEsercizio")).thenReturn(2019);
        when(resultSet.getBigDecimal("incassatoNetto")).thenReturn(BigDecimal.valueOf(100L));
        when(resultSet.getLong("progressivoCredito")).thenReturn(200L);
        when(resultSet.getString("tipoBilancio")).thenReturn("TIPOBILANCIO");
        when(resultSet.getBigDecimal("importoTotale")).thenReturn(BigDecimal.valueOf(300L));
        when(resultSet.getString("numeroDomanda")).thenReturn("SF00010586");

        ImportoLiquidato importoLiquidato = importoLiquidatoFrom(resultSet, new ArrayList<CapitoloDisciplina>(), TipoDomanda.DOMANDA_PSR_STRUTTURALE);

        assertThat(importoLiquidato).isNotNull();
        assertThat(importoLiquidato.getAnno()).isEqualTo(2019);
        assertThat(importoLiquidato.getIncassatoNetto()).isEqualTo(BigDecimal.valueOf(100L));
        assertThat(importoLiquidato.getProgressivo()).isEqualTo(200L);
        assertThat(importoLiquidato.getTipoBilancio()).isEqualTo("TIPOBILANCIO");
    }

    @Test
    public void forDebitoFromItConverts() throws Exception  {
        when(resultSet.getBigDecimal("importIncasso")).thenReturn(BigDecimal.valueOf(1239L));
        when(resultSet.getString("descrizioneCapitolo")).thenReturn("DESCRIZIONE");

        Debito debito = debitoFrom(resultSet);

        assertThat(debito).isNotNull();
        assertThat(debito.getImporto()).isEqualTo(BigDecimal.valueOf(1239L));
        assertThat(debito.getDescrizioneCapitolo()).isEqualTo("DESCRIZIONE");
    }
}
