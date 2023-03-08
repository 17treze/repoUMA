package it.tndigitale.a4g.soc.business.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class ImportoLiquidatoTest {

    @Test
    public void forCalcolaTotaleRecuperatoIfNullListDebitiThenImportoTotaleNull() {
        ImportoLiquidato importoLiquidato = new ImportoLiquidato();

        assertThat(importoLiquidato.getTotaleRecuperato()).isNull();
    }

    @Test
    public void forCalcolaTotaleRecuperatoIfEmptyListDebitiThenImportoTotaleNull() {
        ImportoLiquidato importoLiquidato = new ImportoLiquidato().setDebiti(emptyList());

        assertThat(importoLiquidato.getTotaleRecuperato()).isNull();
    }

    @Test
    public void forCalcolaTotaleRecuperatoIfNotEmptyListDebitiThenImportoTotaleNotNull() {
        ImportoLiquidato importoLiquidato = 
        				new ImportoLiquidato().setDebiti(
        								Arrays.asList(
        										new Debito().setImporto(BigDecimal.TEN),
        										new Debito().setImporto(BigDecimal.TEN))
        								);

        assertThat(importoLiquidato.getTotaleRecuperato()).isEqualTo(BigDecimal.valueOf(20L));
    }
}
