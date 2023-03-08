package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto;

public class SezioneCatastaleDto {

	private String codice;
	private String denominazione;
	private ComuneAmministrativoDto comune;
	
	public String getCodice() {
		return codice;
	}
	public SezioneCatastaleDto setCodice(String codice) {
		this.codice = codice;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public SezioneCatastaleDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public ComuneAmministrativoDto getComune() {
		return comune;
	}
	public SezioneCatastaleDto setComune(ComuneAmministrativoDto comune) {
		this.comune = comune;
		return this;
	}

}
