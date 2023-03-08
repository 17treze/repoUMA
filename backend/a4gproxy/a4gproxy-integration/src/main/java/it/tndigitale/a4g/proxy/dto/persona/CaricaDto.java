package it.tndigitale.a4g.proxy.dto.persona;

import java.time.LocalDate;

public class CaricaDto {
	private LocalDate dataInizio;

	private String identificativo;

	private String descrizione;

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public CaricaDto setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
		return this;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public CaricaDto setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public CaricaDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
}
