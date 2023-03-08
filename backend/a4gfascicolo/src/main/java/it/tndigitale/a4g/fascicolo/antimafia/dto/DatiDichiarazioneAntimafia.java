
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "richiedente",
    "dettaglioImpresa",
"listaAllegatiFamConv"
})
public class DatiDichiarazioneAntimafia {

    @JsonProperty("richiedente")
    private Richiedente richiedente;
    @JsonProperty("dettaglioImpresa")
    private DettaglioImpresa dettaglioImpresa;

    @JsonProperty("richiedente")
    public Richiedente getRichiedente() {
        return richiedente;
    }

    @JsonProperty("richiedente")
    public void setRichiedente(Richiedente richiedente) {
        this.richiedente = richiedente;
    }

    @JsonProperty("dettaglioImpresa")
    public DettaglioImpresa getDettaglioImpresa() {
        return dettaglioImpresa;
    }

    @JsonProperty("dettaglioImpresa")
    public void setDettaglioImpresa(DettaglioImpresa dettaglioImpresa) {
        this.dettaglioImpresa = dettaglioImpresa;
    }

}
