package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloIscrizioneSezioneDto {
	private String dataIscrizione;
	private String descrizione;
	
	public String getDataIscrizione() {
		return dataIscrizione;
	}
	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}