package it.tndigitale.a4g.soc.business.persistence.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ImportLiquidatoPO {

    private String cuaa;
    private String numeroDomanda;
    private Long numeroElenco;
    private String tipoBilancio;
    private Integer annoEsercizio;
    private Long progressivoCredito;
    private String tipoPagamento;
    private BigDecimal incassatoNetto;

    public String getCuaa() {
        return cuaa;
    }

    public ImportLiquidatoPO setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public String getNumeroDomanda() {
        return numeroDomanda;
    }

    public ImportLiquidatoPO setNumeroDomanda(String numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
        return this;
    }

    public Long getNumeroElenco() {
        return numeroElenco;
    }

    public ImportLiquidatoPO setNumeroElenco(Long numeroElenco) {
        this.numeroElenco = numeroElenco;
        return this;
    }

    public String getTipoBilancio() {
        return tipoBilancio;
    }

    public ImportLiquidatoPO setTipoBilancio(String tipoBilancio) {
        this.tipoBilancio = tipoBilancio;
        return this;
    }

    public Integer getAnnoEsercizio() {
        return annoEsercizio;
    }

    public ImportLiquidatoPO setAnnoEsercizio(Integer annoEsercizio) {
        this.annoEsercizio = annoEsercizio;
        return this;
    }

    public Long getProgressivoCredito() {
        return progressivoCredito;
    }

    public ImportLiquidatoPO setProgressivoCredito(Long progressivoCredito) {
        this.progressivoCredito = progressivoCredito;
        return this;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public ImportLiquidatoPO setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
        return this;
    }

    public BigDecimal getIncassatoNetto() {
        return incassatoNetto;
    }

    public ImportLiquidatoPO setIncassatoNetto(BigDecimal incassatoNetto) {
        this.incassatoNetto = incassatoNetto;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportLiquidatoPO that = (ImportLiquidatoPO) o;
        return Objects.equals(cuaa, that.cuaa) &&
                Objects.equals(numeroDomanda, that.numeroDomanda) &&
                Objects.equals(numeroElenco, that.numeroElenco) &&
                Objects.equals(tipoBilancio, that.tipoBilancio) &&
                Objects.equals(annoEsercizio, that.annoEsercizio) &&
                Objects.equals(progressivoCredito, that.progressivoCredito) &&
                Objects.equals(tipoPagamento, that.tipoPagamento) &&
                Objects.equals(incassatoNetto, that.incassatoNetto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuaa, numeroDomanda, numeroElenco, tipoBilancio, annoEsercizio, progressivoCredito, tipoPagamento, incassatoNetto);
    }
}
