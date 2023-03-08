package it.tndigitale.a4g.soc.business.persistence.builder;

import it.tndigitale.a4g.framework.exception.ValidationException;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static it.tndigitale.a4g.soc.business.dto.TipoDomanda.DOMANDA_UNICA;
import static it.tndigitale.a4g.soc.business.persistence.builder.LiquidazioneBuilder.paramsOfCalcoloDebitiXImportoLiquidato;
import static it.tndigitale.a4g.soc.business.persistence.builder.LiquidazioneBuilder.paramsOfCalcoloImportoLiquidato;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LiquidazioneBuilderTest {

    @Test
    public void forParamsOfCalcoloImportoLiquidatoItReturnsParams() {
        Map<String, Object> params =  paramsOfCalcoloImportoLiquidato(importoLiquidatoFilter());

        assertThat(params).hasSize(4);
        assertThat(params.get("cuaa")).isEqualTo("abc123");
        assertThat(params.get("anno")).isEqualTo(2019);
        assertThat(params.get("idElencoLiquidazione")).isEqualTo(100L);
        assertThat(params.get("numeroDomanda")).isEqualTo("0092019.*1234");
    }

    @Test
    public void forParamsOfCalcoloImportoLiquidatoIfTipoDomandaDifferenteOfDomandaUnicaThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> paramsOfCalcoloImportoLiquidato(
    		new ImportoLiquidatoFilter()
    		.setAnno(2019)
    		.setCuaa("abc123")
        	.setIdElencoLiquidazione(100L)
        	.setNumeroDomanda(BigDecimal.valueOf(1234L))
        ));
    }

    @Test
    public void forParamsOfCalcoloDebitiXImportoLiquidatoItreturnParams() {
        Map<String, Object> params =  paramsOfCalcoloDebitiXImportoLiquidato(importoLiquidato());

        assertThat(params).hasSize(3);
        assertThat(params.get("annoEsercizio")).isEqualTo(2019);
        assertThat(params.get("tipoBilancio")).isEqualTo("FF");
        assertThat(params.get("progressivoCredito")).isEqualTo(200L);
    }

    private ImportoLiquidatoFilter importoLiquidatoFilter() {
        return new ImportoLiquidatoFilter().setAnno(2019)
                                           .setTipoDomanda(DOMANDA_UNICA)
                                           .setCuaa("abc123")
                                           .setIdElencoLiquidazione(100L)
                                           .setNumeroDomanda(BigDecimal.valueOf(1234L));
    }

    private ImportoLiquidato importoLiquidato() {
        return new ImportoLiquidato().setAnno(2019)
                                     .setProgressivo(200L)
                                     .setTipoBilancio("FF");
    }
}
