package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "DTO della dei dati della particella del terreno recuperato dal SIAN")
public class ParticellaDto implements Serializable{


    @ApiModelProperty(value = "Provincia")
	private String provincia;
    @ApiModelProperty(value = "Comune")
	private String comune;
    @ApiModelProperty(value = "Sezione")
    private String sezione;
    @ApiModelProperty(value = "Foglio")
    private String foglio;
    @ApiModelProperty(value = "particella")
    private String particella;
    @ApiModelProperty(value = "subalterno")
    private String subalterno;

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

    public String getSezione() {
        return sezione;
    }

    public void setSezione(String sezione) {
        this.sezione = sezione;
    }

    public String getFoglio() {
        return foglio;
    }

    public void setFoglio(String foglio) {
        this.foglio = foglio;
    }

    public String getParticella() {
        return particella;
    }

    public void setParticella(String particella) {
        this.particella = particella;
    }

    public String getSubalterno() {
        return subalterno;
    }

    public void setSubalterno(String subalterno) {
        this.subalterno = subalterno;
    }
}
