package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PsrFatturaDto {
    private Integer idDomandaPagamento;
    private LocalDate dataFattura;
    private String codifica;
    private String tipoFattura;
    private String numeroFattura;
    private String oggetto;
    private BigDecimal quotaContributoRichiesto;
    private BigDecimal valoreFattura;
    private BigDecimal spesaRichiesta;
    private BigDecimal contributoRichiesto;
    private BigDecimal spesaAmmessa;
    private BigDecimal contributoAmmesso;

    public Integer getIdDomandaPagamento() {
        return idDomandaPagamento;
    }

    public void setIdDomandaPagamento(Integer idDomandaPagamento) {
        this.idDomandaPagamento = idDomandaPagamento;
    }

    public LocalDate getDataFattura() {
        return dataFattura;
    }

    public void setDataFattura(LocalDate dataFattura) {
        this.dataFattura = dataFattura;
    }

    public String getCodifica() {
        return codifica;
    }

    public void setCodifica(String codifica) {
        this.codifica = codifica;
    }

    public String getTipoFattura() {
        return tipoFattura;
    }

    public void setTipoFattura(String tipoFattura) {
        this.tipoFattura = tipoFattura;
    }

    public String getNumeroFattura() {
        return numeroFattura;
    }

    public void setNumeroFattura(String numeroFattura) {
        this.numeroFattura = numeroFattura;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public BigDecimal getQuotaContributoRichiesto() {
        return quotaContributoRichiesto;
    }

    public void setQuotaContributoRichiesto(BigDecimal quotaContributoRichiesto) {
        this.quotaContributoRichiesto = quotaContributoRichiesto;
    }

    public BigDecimal getValoreFattura() {
        return valoreFattura;
    }

    public void setValoreFattura(BigDecimal valoreFattura) {
        this.valoreFattura = valoreFattura;
    }

    public BigDecimal getSpesaRichiesta() {
        return spesaRichiesta;
    }

    public void setSpesaRichiesta(BigDecimal spesaRichiesta) {
        this.spesaRichiesta = spesaRichiesta;
    }

    public BigDecimal getContributoRichiesto() {
        return contributoRichiesto;
    }

    public void setContributoRichiesto(BigDecimal contributoRichiesto) {
        this.contributoRichiesto = contributoRichiesto;
    }

    public BigDecimal getSpesaAmmessa() {
        return spesaAmmessa;
    }

    public void setSpesaAmmessa(BigDecimal spesaAmmessa) {
        this.spesaAmmessa = spesaAmmessa;
    }

    public BigDecimal getContributoAmmesso() {
        return contributoAmmesso;
    }

    public void setContributoAmmesso(BigDecimal contributoAmmesso) {
        this.contributoAmmesso = contributoAmmesso;
    }
}
