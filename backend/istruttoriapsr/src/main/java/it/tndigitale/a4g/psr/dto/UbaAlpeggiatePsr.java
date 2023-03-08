package it.tndigitale.a4g.psr.dto;

import java.time.LocalDate;

public class UbaAlpeggiatePsr {

    private Long idDomanda;
    private Long idModulo;
    private LocalDate dataPres;
    private String codDestinazione;
    private String dsDestinazione;
    private Long ubaAlpeggiate;
    private Long supUbaAlpeggiate;

    public Long getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
    }

    public Long getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Long idModulo) {
        this.idModulo = idModulo;
    }

    public LocalDate getDataPres() {
        return dataPres;
    }

    public void setDataPres(LocalDate dataPres) {
        this.dataPres = dataPres;
    }

    public String getCodDestinazione() {
        return codDestinazione;
    }

    public void setCodDestinazione(String codDestinazione) {
        this.codDestinazione = codDestinazione;
    }

    public String getDsDestinazione() {
        return dsDestinazione;
    }

    public void setDsDestinazione(String dsDestinazione) {
        this.dsDestinazione = dsDestinazione;
    }

    public Long getUbaAlpeggiate() {
        return ubaAlpeggiate;
    }

    public void setUbaAlpeggiate(Long ubaAlpeggiate) {
        this.ubaAlpeggiate = ubaAlpeggiate;
    }

    public Long getSupUbaAlpeggiate() {
        return supUbaAlpeggiate;
    }

    public void setSupUbaAlpeggiate(Long supUbaAlpeggiate) {
        this.supUbaAlpeggiate = supUbaAlpeggiate;
    }
}
