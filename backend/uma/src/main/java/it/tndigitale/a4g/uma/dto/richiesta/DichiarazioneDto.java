package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.List;

public class DichiarazioneDto {

	private Long lavorazioneId;
	private List<FabbisognoDto> fabbisogni;

	public Long getLavorazioneId() {
		return lavorazioneId;
	}
	public DichiarazioneDto setLavorazioneId(Long lavorazioneId) {
		this.lavorazioneId = lavorazioneId;
		return this;
	}
	public List<FabbisognoDto> getFabbisogni() {
		return fabbisogni;
	}
	public DichiarazioneDto setFabbisogni(List<FabbisognoDto> fabbisogni) {
		this.fabbisogni = fabbisogni;
		return this;
	} 
}
