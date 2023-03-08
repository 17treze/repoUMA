package it.tndigitale.a4g.psr.dto;

import java.time.LocalDate;

public class ImpegnoZooPascoloPsr {

    private Long idDomanda;
    private Long idModulo;
    private LocalDate dataPresentazione;
    private Long grafica;
    private String codAzione;
    private String deAzione;
    private Long quantitaImp;

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

    public LocalDate getDataPresentazione() {
        return dataPresentazione;
    }

    public void setDataPresentazione(LocalDate dataPresentazione) {
        this.dataPresentazione = dataPresentazione;
    }

    public Long getGrafica() {
        return grafica;
    }

    public void setGrafica(Long grafica) {
        this.grafica = grafica;
    }

    public String getCodAzione() {
        return codAzione;
    }

    public void setCodAzione(String codAzione) {
        this.codAzione = codAzione;
    }

    public String getDeAzione() {
        return deAzione;
    }

    public void setDeAzione(String deAzione) {
        this.deAzione = deAzione;
    }

    public Long getQuantitaImp() {
        return quantitaImp;
    }

    public void setQuantitaImp(Long quantitaImp) {
        this.quantitaImp = quantitaImp;
    }
}
