package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Impresa sia essa individuale oppure no.")
public interface Impresa extends Serializable {
    @ApiModelProperty(value = "Partita iva della persona", required = true)
    public String getPartitaIva();

    @ApiModelProperty(value = "Denominazione della persona", required = true)
    public String getDenominazione();

    @ApiModelProperty(value = "Forma giuridica dell'azienda", required = true)
    public String getFormaGiuridica();

    @ApiModelProperty(value = "Sede legale della ditta", required = true)
    public SedeDto getSedeLegale();
    
    @ApiModelProperty(value = "Oggetto sociale della ditta", required = true)
    public String getOggettoSociale();
}
