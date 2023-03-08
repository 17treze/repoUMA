package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneDto {

	private List<ReportValidazioneMacchinarioDto> macchine;
	// aggiungere qui report validazione fabbricati

	private List<ReportValidazioneFabbricatoDto> fabbricati;

	public List<ReportValidazioneMacchinarioDto> getMacchine() {
		return macchine;
	}
	public ReportValidazioneDto setMacchine(List<ReportValidazioneMacchinarioDto> macchine) {
		this.macchine = macchine;
		return this;
	}
	public ReportValidazioneDto addMacchina(ReportValidazioneMacchinarioDto macchina) {
		macchine.add(macchina);
		return this;
	}

	public List<ReportValidazioneFabbricatoDto> getFabbricati() {
		return fabbricati;
	}

	public ReportValidazioneDto setFabbricati(List<ReportValidazioneFabbricatoDto> fabbricati) {
		this.fabbricati = fabbricati;
		return this;
	}

	public ReportValidazioneDto addFabbricato(ReportValidazioneFabbricatoDto fabbricato) {
		fabbricati.add(fabbricato);
		return this;
	}
}
