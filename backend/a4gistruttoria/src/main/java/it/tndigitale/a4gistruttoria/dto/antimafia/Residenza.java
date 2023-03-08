
package it.tndigitale.a4gistruttoria.dto.antimafia;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "indirizzo",
    "comune",
    "provincia",
    "CAP"
})
public class Residenza {

    @JsonProperty("indirizzo")
    private String indirizzo;
    @JsonProperty("comune")
    private String comune;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("CAP")
    private String cAP;
    
	@JsonProperty("indirizzo")
    public String getIndirizzo() {
        return indirizzo;
    }

    @JsonProperty("indirizzo")
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @JsonProperty("comune")
    public String getComune() {
        return comune;
    }

    @JsonProperty("comune")
    public void setComune(String comune) {
        this.comune = comune;
    }

    @JsonProperty("provincia")
    public String getProvincia() {
        return provincia;
    }

    @JsonProperty("provincia")
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @JsonProperty("CAP")
    public String getCAP() {
        return cAP;
    }

    @JsonProperty("CAP")
    public void setCAP(String cAP) {
        this.cAP = cAP;
    }

}
