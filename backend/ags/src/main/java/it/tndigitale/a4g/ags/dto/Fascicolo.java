package it.tndigitale.a4g.ags.dto;


public class Fascicolo {

	private Long idFascicolo;
	private String denominazione;
	private String cuaa;
	private String stato; // TODO usare enum?
	private String caa;
	private String caacodice;
	// aggiunta card FAS-MIG-01
	private String tipoDetenzione;

	private Long idSoggetto;
	
	public Fascicolo() {
	}
	
	public Fascicolo(String cuaa, String denominazione) {
		super();
		this.denominazione = denominazione;
		this.cuaa = cuaa;
	}
	
	public Fascicolo(Long idFascicolo) {
		super();
		this.idFascicolo = idFascicolo;
	}

	public Long getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(Long idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

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

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getCaa() {
		return caa;
	}

	public void setCaa(String caa) {
		this.caa = caa;
	}

	public String getCaacodice() {
		return caacodice;
	}

	public void setCaacodice(String caacodice) {
		this.caacodice = caacodice;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}
	
	public String getTipoDetenzione() {
		return tipoDetenzione;
	}

	public void setTipoDetenzione(String tipoDetenzione) {
		this.tipoDetenzione = tipoDetenzione;
	}
}
