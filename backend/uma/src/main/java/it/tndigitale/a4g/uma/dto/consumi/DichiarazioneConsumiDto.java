package it.tndigitale.a4g.uma.dto.consumi;

import java.time.LocalDateTime;

import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;

public class DichiarazioneConsumiDto {
	private Long id;
	private Long idRichiesta;
	private Long campagnaRichiesta;
	private String cuaa;
	private String cfRichiedente;
	private String denominazione;
	private LocalDateTime dataPresentazione;
	private LocalDateTime dataProtocollazione;
	private String protocollo;
	private StatoDichiarazioneConsumi stato;
	private CarburanteCompletoDto rimanenza;
	private LocalDateTime dataConduzione;
	private String motivazioneAccisa;
	private LocalDateTime dataLimitePrelievi;
	private boolean haPrelieviOltreLimite;

	public String getMotivazioneAccisa() {
		return motivazioneAccisa;
	}
	public DichiarazioneConsumiDto setMotivazioneAccisa(String motivazioneAccisa) {
		this.motivazioneAccisa = motivazioneAccisa;
		return this;
	}
	public LocalDateTime getDataConduzione() {
		return dataConduzione;
	}
	public DichiarazioneConsumiDto setDataConduzione(LocalDateTime dataConduzione) {
		this.dataConduzione = dataConduzione;
		return this;
	}

	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public DichiarazioneConsumiDto setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
		return this;
	}
	public String getCfRichiedente() {
		return cfRichiedente;
	}
	public DichiarazioneConsumiDto setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
		return this;
	}
	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}
	public DichiarazioneConsumiDto setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
		return this;
	}
	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}
	public DichiarazioneConsumiDto setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
		return this;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public DichiarazioneConsumiDto setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}
	public StatoDichiarazioneConsumi getStato() {
		return stato;
	}
	public DichiarazioneConsumiDto setStato(StatoDichiarazioneConsumi stato) {
		this.stato = stato;
		return this;
	}
	public Long getId() {
		return id;
	}
	public DichiarazioneConsumiDto setId(Long id) {
		this.id = id;
		return this;
	}
	public Long getCampagnaRichiesta() {
		return campagnaRichiesta;
	}
	public DichiarazioneConsumiDto setCampagnaRichiesta(Long campagnaRichiesta) {
		this.campagnaRichiesta = campagnaRichiesta;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public DichiarazioneConsumiDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public CarburanteCompletoDto getRimanenza() {
		return rimanenza;
	}
	public DichiarazioneConsumiDto setRimanenza(CarburanteCompletoDto rimanenza) {
		this.rimanenza = rimanenza;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public DichiarazioneConsumiDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public LocalDateTime getDataLimitePrelievi() {
		return dataLimitePrelievi;
	}
	public void setDataLimitePrelievi(LocalDateTime dataLimitePrelievi) {
		this.dataLimitePrelievi = dataLimitePrelievi;
	}
	public boolean isHaPrelieviOltreLimite() {
		return haPrelieviOltreLimite;
	}
	public void setHaPrelieviOltreLimite(boolean haPrelieviOltreLimite) {
		this.haPrelieviOltreLimite = haPrelieviOltreLimite;
	}
}
