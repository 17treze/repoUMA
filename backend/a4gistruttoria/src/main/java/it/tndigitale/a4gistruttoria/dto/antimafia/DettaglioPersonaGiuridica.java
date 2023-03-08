
package it.tndigitale.a4gistruttoria.dto.antimafia;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "formaGiuridica",
    "estremiCostituzione",
    "capitaleSociale",
    "durata"
})
public class DettaglioPersonaGiuridica {

    @JsonProperty("formaGiuridica")
    private String formaGiuridica;
    @JsonProperty("estremiCostituzione")
    private String estremiCostituzione;
    @JsonProperty("capitaleSociale")
    private String capitaleSociale;
    @JsonProperty("durata")
	private String durata;

    @JsonProperty("formaGiuridica")
    public String getFormaGiuridica() {
        return formaGiuridica;
    }

    @JsonProperty("formaGiuridica")
    public void setFormaGiuridica(String formaGiuridica) {
        this.formaGiuridica = formaGiuridica;
    }

    @JsonProperty("estremiCostituzione")
    public String getEstremiCostituzione() {
        return estremiCostituzione;
    }

    @JsonProperty("estremiCostituzione")
    public void setEstremiCostituzione(String estremiCostituzione) {
        this.estremiCostituzione = estremiCostituzione;
    }

    @JsonProperty("capitaleSociale")
    public String getCapitaleSociale() {
        return capitaleSociale;
    }

    @JsonProperty("capitaleSociale")
    public void setCapitaleSociale(String capitaleSociale) {
        this.capitaleSociale = capitaleSociale;
    }

    @JsonProperty("durata")
    public String getDurata() {
        return durata;
    }

    @JsonProperty("durata")
    public void setDurata(String durata) {
        this.durata = durata;
    }

}
