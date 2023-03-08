package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

import java.time.LocalDateTime;

public class MovimentoValidazioneFascicoloAgsDto {
	private Long idFascicolo;
	private String cuaa;
	private String denominazione;
	private Integer validazioniPositive;
	private LocalDateTime dataUltimaValidazionePositiva;

	public MovimentoValidazioneFascicoloAgsDto(Long idFascicolo, String cuaa, String denominazione, Integer validazioniPositive, LocalDateTime dataUltimaValidazionePositiva) {
		super();
		this.idFascicolo = idFascicolo;
		this.cuaa = cuaa;
		this.denominazione = denominazione;
		this.validazioniPositive = validazioniPositive;
		this.dataUltimaValidazionePositiva = dataUltimaValidazionePositiva;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public Integer getValidazioniPositive() {
		return validazioniPositive;
	}
	public void setValidazioniPositive(Integer validazioniPositive) {
		this.validazioniPositive = validazioniPositive;
	}
	public LocalDateTime getDataUltimaValidazionePositiva() {
		return dataUltimaValidazionePositiva;
	}
	public void setDataUltimaValidazionePositiva(LocalDateTime dataUltimaValidazionePositiva) {
		this.dataUltimaValidazionePositiva = dataUltimaValidazionePositiva;
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

}
