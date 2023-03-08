package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Codice fiscale del rappresentante legale")
public class RappresentanteLegaleDto {
    @ApiModelProperty(value = "Codice fiscale del rappresentante legale", required = true)
    private String codiceFiscale;

    @ApiModelProperty(value = "Nominativo del rappresentante legale", required = true)
    private String nominativo;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }
}
