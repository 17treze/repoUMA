package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.List;

public class DichiarazioneFabbricatoDto {

	private Long idFabbricato;
	private List<DichiarazioneDto> dichiarazioni;

	public Long getIdFabbricato() {
		return idFabbricato;
	}
	public DichiarazioneFabbricatoDto setIdFabbricato(Long idFabbricato) {
		this.idFabbricato = idFabbricato;
		return this;
	}
	public List<DichiarazioneDto> getDichiarazioni() {
		return dichiarazioni;
	}
	public DichiarazioneFabbricatoDto setDichiarazioni(List<DichiarazioneDto> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
		return this;
	}
}
