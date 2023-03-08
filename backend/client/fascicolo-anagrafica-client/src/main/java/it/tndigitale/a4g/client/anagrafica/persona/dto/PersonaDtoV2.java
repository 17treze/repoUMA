package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 
 * Nuovi Persona*DTOV2 temporanei da unificare con PersonaDto in progetto anagrafica
 * 
 * @author m.dallatorre
 *
 */
@ApiModel(description = "Dati di una persona")
public class PersonaDtoV2 implements Serializable {
    @ApiModelProperty(value = "Codice fiscale della persona", required = true)
    private String codiceFiscale;

    // @ApiModelProperty(value = "Domicilio fiscale della persona", required = true)
    // private IndirizzoDto domicilioFiscale;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    /* public IndirizzoDto getDomicilioFiscale() {
        return domicilioFiscale;
    }

    public void setDomicilioFiscale(IndirizzoDto domicilioFiscale) {
        this.domicilioFiscale = domicilioFiscale;
    } */
}
