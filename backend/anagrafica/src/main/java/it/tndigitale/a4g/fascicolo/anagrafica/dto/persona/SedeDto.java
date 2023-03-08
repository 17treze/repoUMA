package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SedeDto implements Serializable {
	private static final long serialVersionUID = 1080776504957236469L;

	@ApiModelProperty(value = "Indirizzo della sede", required = true)
    private IndirizzoDto indirizzo;

	@ApiModelProperty(value = "Indirizzo della sede (Camera Di Commercio)")
    private IndirizzoDto indirizzoCameraCommercio;
	
    @ApiModelProperty(value = "Attivita ateco della ditta")
    private List<AttivitaDto> attivitaAteco;

    @ApiModelProperty(value = "Dati di registrazione al registro imprese")
    private IscrizioneRepertorioEconomicoDto iscrizioneRegistroImprese;

    @ApiModelProperty(value = "Telefono della sede")
    private String telefono;

    @ApiModelProperty(value = "Email della sede")
    private String indirizzoPec;

	public IndirizzoDto getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoDto indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIndirizzoPec() {
		return indirizzoPec;
	}

	public void setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
	}

	public IscrizioneRepertorioEconomicoDto getIscrizioneRegistroImprese() {
		return iscrizioneRegistroImprese;
	}

	public void setIscrizioneRegistroImprese(IscrizioneRepertorioEconomicoDto iscrizioneRegistroImprese) {
		this.iscrizioneRegistroImprese = iscrizioneRegistroImprese;
	}

	public List<AttivitaDto> getAttivitaAteco() {
		return attivitaAteco;
	}

	public void setAttivitaAteco(List<AttivitaDto> attivitaAteco) {
		this.attivitaAteco = attivitaAteco;
	}

	public IndirizzoDto getIndirizzoCameraCommercio() {
		return indirizzoCameraCommercio;
	}

	public void setIndirizzoCameraCommercio(IndirizzoDto indirizzoCameraCommercio) {
		this.indirizzoCameraCommercio = indirizzoCameraCommercio;
	}
}
