package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;

public class DatiIstruttoria {
	private Long id;
	private BigDecimal bPSSuperficie;
	private BigDecimal greeningSuperficie;
	private BigDecimal importoSalari;
	private Boolean controlloAntimafia;
	private String noteIstruttore;
	private Boolean domAnnoPrecNonLiquidabile;
	private Boolean bpsSanzioniAnnoPrecedente;
	private BigDecimal bpsImportoSanzioniAnnoPrecedente;
	private Boolean gioSanzioniAnnoPrecedente;
	private BigDecimal gioImportoSanzioniAnnoPrecedente;
	private Boolean annulloRiduzione;

	public Boolean getControlloAntimafia() {
		return controlloAntimafia;
	}

	public void setControlloAntimafia(Boolean certificazioneAntimafia) {
		this.controlloAntimafia = certificazioneAntimafia;
	}

	public BigDecimal getbPSSuperficie() {
		return bPSSuperficie;
	}

	public void setbPSSuperficie(BigDecimal bPSSuperficie) {
		this.bPSSuperficie = bPSSuperficie;
	}

	public BigDecimal getGreeningSuperficie() {
		return greeningSuperficie;
	}

	public void setGreeningSuperficie(BigDecimal greeningSuperficie) {
		this.greeningSuperficie = greeningSuperficie;
	}

	public BigDecimal getImportoSalari() {
		return importoSalari;
	}

	public void setImportoSalari(BigDecimal importoSalari) {
		this.importoSalari = importoSalari;
	}

	public String getNoteIstruttore() {
		return noteIstruttore;
	}

	public void setNoteIstruttore(String noteIstruttore) {
		this.noteIstruttore = noteIstruttore;
	}

	public Boolean getDomAnnoPrecNonLiquidabile() {
		return domAnnoPrecNonLiquidabile;
	}

	public void setDomAnnoPrecNonLiquidabile(Boolean domAnnoPrecNonLiquidabile) {
		this.domAnnoPrecNonLiquidabile = domAnnoPrecNonLiquidabile;
	}
	
	public Boolean getBpsSanzioniAnnoPrecedente() {
		return bpsSanzioniAnnoPrecedente;
	}

	public void setBpsSanzioniAnnoPrecedente(Boolean bpsSanzioniAnnoPrecedente) {
		this.bpsSanzioniAnnoPrecedente = bpsSanzioniAnnoPrecedente;
	}

	public BigDecimal getBpsImportoSanzioniAnnoPrecedente() {
		return bpsImportoSanzioniAnnoPrecedente;
	}

	public void setBpsImportoSanzioniAnnoPrecedente(BigDecimal bpsImportoSanzioniAnnoPrecedente) {
		this.bpsImportoSanzioniAnnoPrecedente = bpsImportoSanzioniAnnoPrecedente;
	}

	public Boolean getGioSanzioniAnnoPrecedente() {
		return gioSanzioniAnnoPrecedente;
	}

	public void setGioSanzioniAnnoPrecedente(Boolean gioSanzioniAnnoPrecedente) {
		this.gioSanzioniAnnoPrecedente = gioSanzioniAnnoPrecedente;
	}

	public BigDecimal getGioImportoSanzioniAnnoPrecedente() {
		return gioImportoSanzioniAnnoPrecedente;
	}

	public void setGioImportoSanzioniAnnoPrecedente(BigDecimal gioImportoSanzioniAnnoPrecedente) {
		this.gioImportoSanzioniAnnoPrecedente = gioImportoSanzioniAnnoPrecedente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Boolean getAnnulloRiduzione() {
		return annulloRiduzione;
	}

	public void setAnnulloRiduzione(Boolean annulloRiduzione) {
		this.annulloRiduzione = annulloRiduzione;
	}

}
