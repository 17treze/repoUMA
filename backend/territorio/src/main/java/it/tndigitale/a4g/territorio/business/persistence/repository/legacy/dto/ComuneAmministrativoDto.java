package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto;

public class ComuneAmministrativoDto {

	private String codiceFiscale;
	private String codiceIstat;
	private String denominazione;
	private ProvinciaDto provincia;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public ComuneAmministrativoDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}
	public String getCodiceIstat() {
		return codiceIstat;
	}
	public ComuneAmministrativoDto setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public ComuneAmministrativoDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public ProvinciaDto getProvincia() {
		return provincia;
	}
	public ComuneAmministrativoDto setProvincia(ProvinciaDto provincia) {
		this.provincia = provincia;
		return this;
	}
	
}
