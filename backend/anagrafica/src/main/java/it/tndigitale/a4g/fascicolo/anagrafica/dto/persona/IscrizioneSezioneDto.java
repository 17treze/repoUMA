package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.time.LocalDate;

public class IscrizioneSezioneDto {
	private SezioneEnum sezione;
	
	private LocalDate dataIscrizione;
	
	private String qualifica;
	
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
