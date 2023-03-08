package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FonteDatoAnagrafico;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImportanzaAttivita;

public class AttivitaDto {
	@ApiModelProperty(value = "Codifica ateco", required = true)
    private String codice;

    @ApiModelProperty(value = "Descrizione dell'attivita", required = true)
    private String descrizione;

    @ApiModelProperty(value = "Indica il livello di importanza dell'attivita", required = true)
    private ImportanzaAttivita importanza;
    
    @ApiModelProperty(value = "Indica il livello di importanza dell'attivita", required = true)
    private FonteDatoAnagrafico fonteDato;

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

	public FonteDatoAnagrafico getFonteDato() {
		return fonteDato;
	}

	public void setFonteDato(FonteDatoAnagrafico fonteDato) {
		this.fonteDato = fonteDato;
	}

	public void setImportanza(ImportanzaAttivita importanza) {
		this.importanza = importanza;
	}
}

