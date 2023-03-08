package it.tndigitale.a4g.soc.business.dto;

import org.junit.jupiter.api.Test;

import it.tndigitale.a4g.framework.exception.ValidationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ImportoLiquidatoFilterTest {

    @Test
    public void forIsValidIfNumeroDomandaNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() ->
                new ImportoLiquidatoFilter().setAnno(2019)
                                            .setIdElencoLiquidazione(10L)
                                            .setTipoDomanda(TipoDomanda.DOMANDA_UNICA)
                                            .isValid()
                );
    }

    @Test
    public void forIsValidIfAnnoNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() ->
                        new ImportoLiquidatoFilter().setNumeroDomanda(BigDecimal.ONE)
                                                    .setIdElencoLiquidazione(10L)
                                                    .setTipoDomanda(TipoDomanda.DOMANDA_UNICA)
                                                    .isValid()
                );
    }

    @Test
    public void forIsValidIfIdElencoLiquidazioneNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() ->
                        new ImportoLiquidatoFilter().setNumeroDomanda(BigDecimal.ONE)
                                                    .setTipoDomanda(TipoDomanda.DOMANDA_UNICA)
                                                    .isValid()
                );
    }


    @Test
    public void forIsValidIfAllParametersAreDefinedThenNotThrowing() {
        new ImportoLiquidatoFilter().setNumeroDomanda(BigDecimal.ONE)
                                    .setIdElencoLiquidazione(100L)
                                    .setCuaa("abc123")
                                    .setAnno(2019)
                                    .setTipoDomanda(TipoDomanda.DOMANDA_UNICA)
                                    .isValid();
    }

}
