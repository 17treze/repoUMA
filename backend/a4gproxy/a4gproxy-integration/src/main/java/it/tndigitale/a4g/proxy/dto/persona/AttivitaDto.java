package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Attivita ateco di un'azienda")
public class AttivitaDto implements Serializable {
    private static final long serialVersionUID = 1108850781188998524L;

    @ApiModelProperty(value = "Codifica ateco", required = true)
    private String codice;

    @ApiModelProperty(value = "Descrizione dell'attivita", required = true)
    private String descrizione;

    @ApiModelProperty(value = "Indica il livello di importanza dell'attivita", required = true)
    private ImportanzaAttivita importanza;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public ImportanzaAttivita getImportanza() {
        return importanza;
    }

    public void setImportanza(ImportanzaAttivita importanza) {
        this.importanza = importanza;
    }
}
