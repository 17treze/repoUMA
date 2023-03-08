package it.tndigitale.a4g.territorio.dto;

public class SezioneCatastale {
	
	private String codice;
	private String denominazione;
	private ComuneAmministrativo comune;
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public ComuneAmministrativo getComune() {
		return comune;
	}
	public void setComune(ComuneAmministrativo comune) {
		this.comune = comune;
	}

}
