package it.tndigitale.a4gistruttoria.dto;

public class DatiCatastaliRegione {

	private Integer idRegione;
	private String denominazione;
	private String codiceIstat;

	public DatiCatastaliRegione() {
	}

	public Integer getIdRegione() {
		return idRegione;
	}

	public void setIdRegione(Integer idRegione) {
		this.idRegione = idRegione;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}
}