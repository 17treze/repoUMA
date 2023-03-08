package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public class CapoLatteDto extends CapoDto {

	private List<VitelloDto> vitelli;

	public CapoLatteDto() {}

	public CapoLatteDto(JsonNode capoNode) {
		super(capoNode);
	}

	public List<VitelloDto> getVitelli() {
		return vitelli;
	}

	public CapoLatteDto setVitelli(List<VitelloDto> vitelli) {
		this.vitelli = vitelli;
		return this;
	}

	@Override
	public CapoLatteDto setDetenzioni(List<DetenzioneDto> detenzioni) {
		super.setDetenzioni(detenzioni);
		return this;
	}

	@Override
	public CapoLatteDto setDataNascita(LocalDate dataNascita) {
		super.setDataNascita(dataNascita);
		return this;
	}

}
