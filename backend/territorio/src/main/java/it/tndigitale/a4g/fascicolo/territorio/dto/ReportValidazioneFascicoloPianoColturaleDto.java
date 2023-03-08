package it.tndigitale.a4g.fascicolo.territorio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloPianoColturaleDto {

	private String codiceAppezzamento;
	private String codiceDettaglioAppezzamento;
	private Integer superficieDettaglioAppezzamento;
	private String codiceColtura;
	private String descrizioneColtura;
	private String criterioMantenimento;
		
	public ReportValidazioneFascicoloPianoColturaleDto(String codiceAppezzamento,
			String codiceDettaglioAppezzamento, Integer superficieDettaglioAppezzamento, String codiceColtura,
			String descrizioneColtura, String criterioMantenimento) {
		super();
		this.codiceAppezzamento = codiceAppezzamento;
		this.codiceDettaglioAppezzamento = codiceDettaglioAppezzamento;
		this.superficieDettaglioAppezzamento = superficieDettaglioAppezzamento;
		this.codiceColtura = codiceColtura;
		this.descrizioneColtura = descrizioneColtura;
		this.criterioMantenimento = criterioMantenimento;
	}
	public String getCodiceAppezzamento() {
		return codiceAppezzamento;
	}
	public void setCodiceAppezzamento(String codiceAppezzamento) {
		this.codiceAppezzamento = codiceAppezzamento;
	}
	public String getCodiceDettaglioAppezzamento() {
		return codiceDettaglioAppezzamento;
	}
	public void setCodiceDettaglioAppezzamento(String codiceDettaglioAppezzamento) {
		this.codiceDettaglioAppezzamento = codiceDettaglioAppezzamento;
	}
	public Integer getSuperficieDettaglioAppezzamento() {
		return superficieDettaglioAppezzamento;
	}
	public void setSuperficieDettaglioAppezzamento(Integer superficieDettaglioAppezzamento) {
		this.superficieDettaglioAppezzamento = superficieDettaglioAppezzamento;
	}
	public String getCodiceColtura() {
		return codiceColtura;
	}
	public void setCodiceColtura(String codiceColtura) {
		this.codiceColtura = codiceColtura;
	}
	public String getDescrizioneColtura() {
		return descrizioneColtura;
	}
	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}
	public String getCriterioMantenimento() {
		return criterioMantenimento;
	}
	public void setCriterioMantenimento(String criterioMantenimento) {
		this.criterioMantenimento = criterioMantenimento;
	}

}
