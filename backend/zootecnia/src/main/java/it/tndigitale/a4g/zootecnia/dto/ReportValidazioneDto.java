package it.tndigitale.a4g.zootecnia.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneDto {

	private List<ReportValidazioneAllevamentoDto> reportValidazioneAllevamenti;
	private List<ReportValidazioneConsistenzaZootecnicaDto> reportValidazioneConsistenzaZootecnica;
	private String dtAggiornamentoFontiEsterne;

	public List<ReportValidazioneAllevamentoDto> getReportValidazioneAllevamenti() {
		return reportValidazioneAllevamenti;
	}

	public ReportValidazioneDto setReportValidazioneAllevamenti(List<ReportValidazioneAllevamentoDto> reportValidazioneAllevamenti) {
		this.reportValidazioneAllevamenti = reportValidazioneAllevamenti;
		return this;
	}

	public List<ReportValidazioneConsistenzaZootecnicaDto> getReportValidazioneConsistenzaZootecnica() {
		return reportValidazioneConsistenzaZootecnica;
	}

	public void setReportValidazioneConsistenzaZootecnica(
			List<ReportValidazioneConsistenzaZootecnicaDto> reportValidazioneConsistenzaZootecnica) {
		this.reportValidazioneConsistenzaZootecnica = reportValidazioneConsistenzaZootecnica;
	}
	
	public String getDtAggiornamentoFontiEsterne() {
		return dtAggiornamentoFontiEsterne;
	}
	
	public void setDtAggiornamentoFontiEsterne(String dtAggiornamentoFontiEsterne) {
		this.dtAggiornamentoFontiEsterne = dtAggiornamentoFontiEsterne;
	}

}
