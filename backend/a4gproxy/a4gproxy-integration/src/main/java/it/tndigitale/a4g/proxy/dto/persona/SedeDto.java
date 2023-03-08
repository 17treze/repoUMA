package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "Unita locale o sede legale di un'impresa")
public class SedeDto implements Serializable {
    private static final long serialVersionUID = -2673650331834232095L;

    @ApiModelProperty(value = "Indirizzo della sede", required = true)
    private IndirizzoDto indirizzo;

    @ApiModelProperty(value = "Attivita ateco della ditta")
    private List<AttivitaDto> attivitaAteco;

    @ApiModelProperty(value = "Dati di registrazione al registro imprese")
    private IscrizioneRepertorioEconomicoDto iscrizioneRegistroImprese;

    @ApiModelProperty(value = "Telefono della sede")
    private String telefono;

    @ApiModelProperty(value = "Email della sede")
    private String indirizzoPec;

    public IndirizzoDto getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(IndirizzoDto indirizzo) {
        this.indirizzo = indirizzo;
    }

    public List<AttivitaDto> getAttivitaAteco() {
        return this.attivitaAteco;
    }

    public void setAttivitaAteco(List<AttivitaDto> attivitaAteco) {
        this.attivitaAteco = attivitaAteco;
    }

    public IscrizioneRepertorioEconomicoDto getIscrizioneRegistroImprese() {
        return this.iscrizioneRegistroImprese;
    }

    public void setIscrizioneRegistroImprese(IscrizioneRepertorioEconomicoDto iscrizioneRegistroImprese) {
        this.iscrizioneRegistroImprese = iscrizioneRegistroImprese;
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
}
