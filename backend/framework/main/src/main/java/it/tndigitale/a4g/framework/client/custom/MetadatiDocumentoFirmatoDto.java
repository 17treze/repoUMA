package it.tndigitale.a4g.framework.client.custom;

import java.time.LocalDate;

public class MetadatiDocumentoFirmatoDto {

	private LocalDate dataFirma;
	private String cfFirmatario;

	public LocalDate getDataFirma() {
		return dataFirma;
	}

	public void setDataFirma(LocalDate dataFirma) {
		this.dataFirma = dataFirma;
	}

	public String getCfFirmatario() {
		return cfFirmatario;
	}

	public void setCfFirmatario(String cfFirmatario) {
		this.cfFirmatario = cfFirmatario;
	}
}
