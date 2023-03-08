package it.tndigitale.a4g.proxy.dto.persona;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dati di una persona")
public class PersonaDto {
    @ApiModelProperty(value = "Codice fiscale della persona", required = true)
    private String codiceFiscale;
    
    @ApiModelProperty(value = "Dati iscrizione sezione della persona")
    private List<IscrizioneSezioneDto> iscrizioniSezione;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

	public List<IscrizioneSezioneDto> getIscrizioniSezione() {
		return iscrizioniSezione;
	}

	public void setIscrizioniSezione(List<IscrizioneSezioneDto> iscrizioniSezione) {
		this.iscrizioniSezione = iscrizioniSezione;
	}
}
