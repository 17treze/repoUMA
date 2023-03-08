package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloModPagamentoDto {
	private String iban;
	private String denominazioneBanca;
	private String filiale;
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getDenominazioneBanca() {
		return denominazioneBanca;
	}
	public void setDenominazioneBanca(String denominazioneBanca) {
		this.denominazioneBanca = denominazioneBanca;
	}
	public String getFiliale() {
		return filiale;
	}
	public void setFiliale(String filiale) {
		this.filiale = filiale;
	}
	
}