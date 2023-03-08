package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloMandatoDto {
	
	private String denominazioneCaa;
	private String denominazioneSportello;
	private String dataSottoscrizione;
	
	public String getDenominazioneCaa() {
		return denominazioneCaa;
	}
	public void setDenominazioneCaa(String denominazioneCaa) {
		this.denominazioneCaa = denominazioneCaa;
	}
	public String getDenominazioneSportello() {
		return denominazioneSportello;
	}
	public void setDenominazioneSportello(String denominazioneSportello) {
		this.denominazioneSportello = denominazioneSportello;
	}
	public String getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(String dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
}