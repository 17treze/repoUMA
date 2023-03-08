package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "Unita locale o sede legale di un'impresa")
public class SedeDtoV2 implements Serializable {
    private static final long serialVersionUID = -2673650331834232095L;

    @ApiModelProperty(value = "Indirizzo della sede", required = true)
    private IndirizzoDtoV2 indirizzo;

    @ApiModelProperty(value = "Attivita ateco della ditta")
    private List<AttivitaDtoV2> attivitaAteco;

    @ApiModelProperty(value = "Dati di registrazione al registro imprese")
    private IscrizioneRepertorioEconomicoDtoV2 iscrizioneRegistroImprese;

    @ApiModelProperty(value = "Telefono della sede")
    private String telefono;

    @ApiModelProperty(value = "Email della sede")
    private String indirizzoPec;

    public IndirizzoDtoV2 getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(IndirizzoDtoV2 indirizzo) {
        this.indirizzo = indirizzo;
    }

    public List<AttivitaDtoV2> getAttivitaAteco() {
        return this.attivitaAteco;
    }

    public void setAttivitaAteco(List<AttivitaDtoV2> attivitaAteco) {
        this.attivitaAteco = attivitaAteco;
    }

    public IscrizioneRepertorioEconomicoDtoV2 getIscrizioneRegistroImprese() {
        return this.iscrizioneRegistroImprese;
    }

    public void setIscrizioneRegistroImprese(IscrizioneRepertorioEconomicoDtoV2 iscrizioneRegistroImprese) {
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
