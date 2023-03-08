package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

import java.time.LocalDateTime;

public class DetenzioneAgsDto {

	private String caa;
	private TipoDetenzioneAgs tipoDetenzione;
	private String sportello;
	private Long identificativoSportello;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;

	public String getCaa() {
		return caa;
	}
	public DetenzioneAgsDto setCaa(String caa) {
		this.caa = caa;
		return this;
	}
	public TipoDetenzioneAgs getTipoDetenzione() {
		return tipoDetenzione;
	}
	public DetenzioneAgsDto setTipoDetenzione(TipoDetenzioneAgs tipoDetenzione) {
		this.tipoDetenzione = tipoDetenzione;
		return this;
	}
	public String getSportello() {
		return sportello;
	}
	public DetenzioneAgsDto setSportello(String sportello) {
		this.sportello = sportello;
		return this;
	}
	public Long getIdentificativoSportello() {
		return identificativoSportello;
	}
	public DetenzioneAgsDto setIdentificativoSportello(Long identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
		return this;
	}
	public LocalDateTime getDataInizio() {
		return dataInizio;
	}
	public DetenzioneAgsDto setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}
	public LocalDateTime getDataFine() {
		return dataFine;
	}
	public DetenzioneAgsDto setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
		return this;
	}
}
