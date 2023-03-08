package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;

public class PsrInterventoDettaglioDto {

    private Integer idIntervento;
    private Integer idInvestimentoOriginale;
    private String codifica;
    private String dettaglio;
    private BigDecimal costoInvestimentoRichiesto;
    private BigDecimal contributoRichiesto;
    private BigDecimal contributoAmmesso;

    public Integer getIdIntervento() {
        return idIntervento;
    }

    public void setIdIntervento(Integer idIntervento) {
        this.idIntervento = idIntervento;
    }

    public Integer getIdInvestimentoOriginale() {
        return idInvestimentoOriginale;
    }

    public void setIdInvestimentoOriginale(Integer idInvestimentoOriginale) {
        this.idInvestimentoOriginale = idInvestimentoOriginale;
    }

    public String getCodifica() {
        return codifica;
    }

    public void setCodifica(String codifica) {
        this.codifica = codifica;
    }

    public String getDettaglio() {
        return dettaglio;
    }

    public void setDettaglio(String dettaglio) {
        this.dettaglio = dettaglio;
    }

    public BigDecimal getCostoInvestimentoRichiesto() {
        return costoInvestimentoRichiesto;
    }

    public void setCostoInvestimentoRichiesto(BigDecimal costoInvestimentoRichiesto) {
        this.costoInvestimentoRichiesto = costoInvestimentoRichiesto;
    }

    public void addCostoInvestimentoRichiesto(BigDecimal costoInvestimentoRichiesto) {
        this.costoInvestimentoRichiesto = this.costoInvestimentoRichiesto.add(costoInvestimentoRichiesto);
    }

    public BigDecimal getContributoRichiesto() {
        return contributoRichiesto;
    }

    public void setContributoRichiesto(BigDecimal contributoRichiesto) {
        this.contributoRichiesto = contributoRichiesto;
    }

    public void addContributoRichiesto(BigDecimal contributoRichiesto) {
        this.contributoRichiesto = this.contributoRichiesto.add(contributoRichiesto);
    }

    public BigDecimal getContributoAmmesso() {
        return contributoAmmesso;
    }

    public void setContributoAmmesso(BigDecimal contributoAmmesso) {
        this.contributoAmmesso = contributoAmmesso;
    }

    public void addContributoAmmesso(BigDecimal contributoAmmesso) {
        this.contributoAmmesso = this.contributoAmmesso.add(contributoAmmesso);
    }

}
