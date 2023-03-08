package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;


public class CapoOvicaprinoDto extends CapoDto {

	private LocalDate dataApplicazioneMarchio;
	private LocalDate dataInserimentoBdnNascita;

	public CapoOvicaprinoDto() {}

	public CapoOvicaprinoDto(JsonNode capoNode) {
		super(capoNode);
	}

	public LocalDate getDataApplicazioneMarchio() {
		return dataApplicazioneMarchio;
	}
	public CapoOvicaprinoDto setDataApplicazioneMarchio(LocalDate dataApplicazioneMarchio) {
		this.dataApplicazioneMarchio = dataApplicazioneMarchio;
		return this;
	}
	public LocalDate getDataInserimentoBdnNascita() {
		return dataInserimentoBdnNascita;
	}
	public CapoOvicaprinoDto setDataInserimentoBdnNascita(LocalDate dataInserimentoBdnNascita) {
		this.dataInserimentoBdnNascita = dataInserimentoBdnNascita;
		return this;
	}

}
