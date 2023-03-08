package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloAziendaDto {
	private String cuaa;
	private String denominazioneAzienda;
	private String dataCostituzione;
	private String organismoPagatore;
//	private String nomeFirmatario;
	private String dtAggiornamentoFontiEsterne;
	private String dtEsecuzioneControlliCompletezza;

	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getDenominazioneAzienda() {
		return denominazioneAzienda;
	}
	public void setDenominazioneAzienda(String denominazioneAzienda) {
		this.denominazioneAzienda = denominazioneAzienda;
	}
	public String getDataCostituzione() {
		return dataCostituzione;
	}
	public void setDataCostituzione(String dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
	}
	public String getOrganismoPagatore() {
		return organismoPagatore;
	}
	public void setOrganismoPagatore(String organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
	}
//	public String getNomeFirmatario() {
//		return nomeFirmatario;
//	}
//	public void setNomeFirmatario(String nomeFirmatario) {
//		this.nomeFirmatario = nomeFirmatario;
//	}
	public String getDtAggiornamentoFontiEsterne() {
		return dtAggiornamentoFontiEsterne;
	}
	public void setDtAggiornamentoFontiEsterne(String dtAggiornamentoFontiEsterne) {
		this.dtAggiornamentoFontiEsterne = dtAggiornamentoFontiEsterne;
	}
	public String getDtEsecuzioneControlliCompletezza() {
		return dtEsecuzioneControlliCompletezza;
	}
	public void setDtEsecuzioneControlliCompletezza(String dtEsecuzioneControlliCompletezza) {
		this.dtEsecuzioneControlliCompletezza = dtEsecuzioneControlliCompletezza;
	}
}