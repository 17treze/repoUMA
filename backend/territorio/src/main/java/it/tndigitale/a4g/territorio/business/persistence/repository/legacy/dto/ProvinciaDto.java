package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto;

public class ProvinciaDto {
	
	private String sigla;
	private String denominazione;
	private String codiceIstat;
	
	public String getSigla() {
		return sigla;
	}
	public ProvinciaDto setSigla(String sigla) {
		this.sigla = sigla;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public ProvinciaDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getCodiceIstat() {
		return codiceIstat;
	}
	public ProvinciaDto setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
		return this;
	}
}
