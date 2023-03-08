package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class FascicoloAgsFilter {

	private String denominazione;
	private String cuaa;
	private List<String> caacodici;
	
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public List<String> getCaacodici() {
		return caacodici;
	}
	public void setCaacodici(List<String> caacodici) {
		this.caacodici = caacodici;
	}
}
