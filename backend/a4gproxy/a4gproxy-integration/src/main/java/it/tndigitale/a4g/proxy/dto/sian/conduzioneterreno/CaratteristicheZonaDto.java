package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "DTO della dei dati della particella del terreno recuperato dal SIAN")
public class CaratteristicheZonaDto implements Serializable{

    @ApiModelProperty(value = "Codice dei Casi particolari")
    private String casiParticolari;
    @ApiModelProperty(value = "Descrizione dei Casi particolari")
    private String casiParticolariDescrizione;
    @ApiModelProperty(value = "Flag di presenza Documentazione giustificativa")
    private String flagGiust;
    @ApiModelProperty(value = "Descrizione del flag di Presenza Documentazione giustificativa")
    private String flagGiustDescrizione;
    @ApiModelProperty(value = "Valore di Zone vulnerabili ai nitrati di origine agricola (SOLO CONSULTAZIONE)")
    private String codiZVN;
    @ApiModelProperty(value = "Descrizione del valore di Zone vulnerabili ai nitrati di origine agricola (SOLO CONSULTAZIONE)")
    private String codiZVNDescrizione;

    public String getCasiParticolari() {
        return casiParticolari;
    }

    public void setCasiParticolari(String casiParticolari) {
        this.casiParticolari = casiParticolari;
    }

    public String getCasiParticolariDescrizione() {
        return casiParticolariDescrizione;
    }

    public void setCasiParticolariDescrizione(String casiParticolariDescrizione) {
        this.casiParticolariDescrizione = casiParticolariDescrizione;
    }

    public String getFlagGiust() {
        return flagGiust;
    }

    public void setFlagGiust(String flagGiust) {
        this.flagGiust = flagGiust;
    }

    public String getFlagGiustDescrizione() {
        return flagGiustDescrizione;
    }

    public void setFlagGiustDescrizione(String flagGiustDescrizione) {
        this.flagGiustDescrizione = flagGiustDescrizione;
    }

    public String getCodiZVN() {
        return codiZVN;
    }

    public void setCodiZVN(String codiZVN) {
        this.codiZVN = codiZVN;
    }

    public String getCodiZVNDescrizione() {
        return codiZVNDescrizione;
    }

    public void setCodiZVNDescrizione(String codiZVNDescrizione) {
        this.codiZVNDescrizione = codiZVNDescrizione;
    }
}
