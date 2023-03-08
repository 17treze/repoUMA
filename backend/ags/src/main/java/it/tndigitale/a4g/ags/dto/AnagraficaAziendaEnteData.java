package it.tndigitale.a4g.ags.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;

public class AnagraficaAziendaEnteData {

    private final String tipoDetentore;
    private final String detentore;
    private final Date dataSottMandato;

    public AnagraficaAziendaEnteData(String tipoDetentore, String detentore, Date dataSottMandato) {
        this.tipoDetentore = tipoDetentore;
        this.detentore = detentore;
        this.dataSottMandato = dataSottMandato;
    }

    public String getTipoDetentore() {
        return tipoDetentore;
    }

    public String getDetentore() {
        return detentore;
    }

    public Date getDataSottMandato() {
        return dataSottMandato;
    }
}
