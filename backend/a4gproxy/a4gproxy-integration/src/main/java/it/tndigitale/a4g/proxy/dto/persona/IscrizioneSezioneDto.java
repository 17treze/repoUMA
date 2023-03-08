package it.tndigitale.a4g.proxy.dto.persona;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;

public class IscrizioneSezioneDto {
	
	@ApiModelProperty(value = "Tipo sezione", required = true)
	private SezioneEnum sezione;
	
	@ApiModelProperty(value = "Data iscrizione sezione", required = true)
	private LocalDate dataIscrizione;
	
	@ApiModelProperty(value = "Qualifica iscrizione")
	private String qualifica;
	
	@ApiModelProperty(value = "Contenuto campo coltivatore diretto come ottenuto da Parix")
	private String coltivatoreDiretto;

	public SezioneEnum getSezione() {
		return sezione;
	}

	public void setSezione(SezioneEnum sezione) {
		this.sezione = sezione;
	}

	public LocalDate getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(LocalDate dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public String getQualifica() {
		return qualifica;
	}

	public void setQualifica(String qualifica) {
		this.qualifica = qualifica;
	}

	public String getColtivatoreDiretto() {
		return coltivatoreDiretto;
	}

	public void setColtivatoreDiretto(String coltivatoreDiretto) {
		this.coltivatoreDiretto = coltivatoreDiretto;
	}
}
