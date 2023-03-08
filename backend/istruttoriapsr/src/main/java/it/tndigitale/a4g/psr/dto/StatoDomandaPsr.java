package it.tndigitale.a4g.psr.dto;

import java.time.LocalDate;

public class StatoDomandaPsr {

    private Long idDomanda;
    private String codOperazione;
    private String interventoPat;
    private LocalDate dataUltimoMovimento;
    private String tipoPagamento;
    private String stato;
    private Boolean campione;

    public Long getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
    }

    public String getCodOperazione() {
        return codOperazione;
    }

    public void setCodOperazione(String codOperazione) {
        this.codOperazione = codOperazione;
    }

    public String getInterventoPat() {
        return interventoPat;
    }

    public void setInterventoPat(String interventoPat) {
        this.interventoPat = interventoPat;
    }

    public LocalDate getDataUltimoMovimento() {
        return dataUltimoMovimento;
    }

    public void setDataUltimoMovimento(LocalDate dataUltimoMovimento) {
        this.dataUltimoMovimento = dataUltimoMovimento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Boolean getCampione() {
        return campione;
    }

    public void setCampione(Boolean campione) {
        this.campione = campione;
    }
}
