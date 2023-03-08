package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Indirizzo di un punto geografico")
public class IndirizzoDtoV2 implements Serializable {
    private static final long serialVersionUID = 8341465607814120627L;

    @ApiModelProperty(value = "Indica se via, piazza, corso ecc.")
    private String toponimo;

    @ApiModelProperty(value = "Denominazione dell'indirizzo")
    private String via;

    @ApiModelProperty(value = "Numero civico dell'indirizzo")
    private String civico;

    @ApiModelProperty(value = "Comune dove e' ubicato l'indirizzo")
    private String comune;

    @ApiModelProperty(value = "Frazione dove e' ubicato l'indirizzo")
    private String frazione;

    @ApiModelProperty(value = "Codice avviamento postale dove e' ubicato l'indirizzo")
    private String cap;

    @ApiModelProperty(value = "Codice istat del comune dove e' ubicato l'indirizzo")
    private String codiceIstat;

    @ApiModelProperty(value = "Provincia dove e' ubicato l'indirizzo")
    private String provincia;
    
    @ApiModelProperty(value = "Aggregazione dei dati toponimo, via e civico")
    private String denominazioneEstesa;

    public String getDenominazioneEstesa() {
    	if (denominazioneEstesa != null) {
    		return denominazioneEstesa;
    	}
    	return toponimo + via + civico;
    }
    
    public void setDenominazioneEstesa (String denominazioneEstesa) {
    	this.denominazioneEstesa = denominazioneEstesa;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getFrazione() {
        return frazione;
    }

    public void setFrazione(String frazione) {
        this.frazione = frazione;
    }

    public String getCodiceIstat() {
        return codiceIstat;
    }

    public void setCodiceIstat(String codiceIstat) {
        this.codiceIstat = codiceIstat;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getToponimo() {
        return toponimo;
    }

    public void setToponimo(String toponimo) {
        this.toponimo = toponimo;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }
}
