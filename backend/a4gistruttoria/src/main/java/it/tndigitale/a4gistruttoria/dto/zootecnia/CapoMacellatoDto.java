package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public class CapoMacellatoDto extends CapoDto {

	private LocalDate dataMacellazione;
	private LocalDate dataApplicazioneMarchio;
	
	public CapoMacellatoDto() {}

	public CapoMacellatoDto(JsonNode capoNode) {
		super(capoNode);
	}

	public LocalDate getDataMacellazione() {
		return dataMacellazione;
	}
	
	public CapoMacellatoDto setDataMacellazione(LocalDate dataMacellazione) {
		this.dataMacellazione = dataMacellazione;
		return this;
	}
	
	public LocalDate getDataApplicazioneMarchio() {
		return dataApplicazioneMarchio;
	}
	
	public CapoMacellatoDto setDataApplicazioneMarchio(LocalDate dataApplicazioneMarchio) {
		this.dataApplicazioneMarchio = dataApplicazioneMarchio;
		return this;
	}
	
	@Override
	public CapoMacellatoDto setDetenzioni(List<DetenzioneDto> detenzioni) {
		super.setDetenzioni(detenzioni);
		return this;
	}
	
}
