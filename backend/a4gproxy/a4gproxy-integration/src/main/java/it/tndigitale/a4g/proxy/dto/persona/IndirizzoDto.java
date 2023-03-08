package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Indirizzo di un punto geografico")
public class IndirizzoDto implements Serializable {
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
        return denominazioneEstesa;
    }

    public IndirizzoDto setDenominazioneEstesa(String denominazioneEstesa) {
        this.denominazioneEstesa = denominazioneEstesa;
        return this;
    }

    public String getToponimo() {
        return toponimo;
    }

    public IndirizzoDto setToponimo(String toponimo) {
        this.toponimo = toponimo;
        return this;
    }

    public String getVia() {
        return via;
    }

    public IndirizzoDto setVia(String via) {
        this.via = via;
        return this;
    }

    public String getCivico() {
        return civico;
    }

    public IndirizzoDto setCivico(String civico) {
        this.civico = civico;
        return this;
    }

    public String getComune() {
        return comune;
    }

    public IndirizzoDto setComune(String comune) {
        this.comune = comune;
        return this;
    }

    public String getFrazione() {
        return frazione;
    }

    public IndirizzoDto setFrazione(String frazione) {
        this.frazione = frazione;
        return this;
    }

    public String getCap() {
        return cap;
    }

    public IndirizzoDto setCap(String cap) {
        this.cap = cap;
        return this;
    }

    public String getCodiceIstat() {
        return codiceIstat;
    }

    public IndirizzoDto setCodiceIstat(String codiceIstat) {
        this.codiceIstat = codiceIstat;
        return this;
    }

    public String getProvincia() {
        return provincia;
    }

    public IndirizzoDto setProvincia(String provincia) {
        this.provincia = provincia;
        return this;
    }
}
