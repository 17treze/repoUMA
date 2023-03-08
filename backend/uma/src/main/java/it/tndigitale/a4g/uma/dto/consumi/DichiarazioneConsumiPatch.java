package it.tndigitale.a4g.uma.dto.consumi;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public class DichiarazioneConsumiPatch {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(required = true, example = "2021-11-01T23:59" , description = "utilizzare il formato yyyy-MM-dd'T'HH:mm")
	private LocalDateTime dataConduzione;

	private String motivazioneAccisa;

	public String getMotivazioneAccisa() {
		return motivazioneAccisa;
	}
	public DichiarazioneConsumiPatch setMotivazioneAccisa(String motivazioneAccisa) {
		this.motivazioneAccisa = motivazioneAccisa;
		return this;
	}
	public LocalDateTime getDataConduzione() {
		return dataConduzione;
	}
	public DichiarazioneConsumiPatch setDataConduzione(LocalDateTime dataConduzione) {
		this.dataConduzione = dataConduzione;
		return this;
	}
}
