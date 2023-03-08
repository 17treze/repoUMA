package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneDto {
	private String idValidazione = ""; // non popolare per ora
	private String data = "";
	private ReportValidazioneFascicoloAziendaDto azienda;
	private ReportValidazioneFascicoloDetenzioneDto detenzione;
	private ReportValidazioneFascicoloPersonaGiuridicaDto personaGiuridica;
	private ReportValidazioneFascicoloPersonaFisicaDto personaFisica;
	private ReportValidazioneFascicoloCciaaDto cciaa;
	private List<ReportValidazioneFascicoloModPagamentoDto> modPagamentoList;
	private String identificativoScheda;
	private ReportValidazioneFascicoloPrimoFirmatarioDto firmatario;
	
	public String getIdValidazione() {
		return idValidazione;
	}
	public void setIdValidazione(String idValidazione) {
		this.idValidazione = idValidazione;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public ReportValidazioneFascicoloAziendaDto getAzienda() {
		return azienda;
	}
	public void setAzienda(ReportValidazioneFascicoloAziendaDto azienda) {
		this.azienda = azienda;
	}
	public ReportValidazioneFascicoloDetenzioneDto getDetenzione() {
		return detenzione;
	}
	public void setDetenzione(ReportValidazioneFascicoloDetenzioneDto detenzione) {
		this.detenzione = detenzione;
	}
	public ReportValidazioneFascicoloPersonaGiuridicaDto getPersonaGiuridica() {
		return personaGiuridica;
	}
	public void setPersonaGiuridica(ReportValidazioneFascicoloPersonaGiuridicaDto personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}
	public ReportValidazioneFascicoloPersonaFisicaDto getPersonaFisica() {
		return personaFisica;
	}
	public void setPersonaFisica(ReportValidazioneFascicoloPersonaFisicaDto personaFisica) {
		this.personaFisica = personaFisica;
	}
	public ReportValidazioneFascicoloCciaaDto getCciaa() {
		return cciaa;
	}
	public void setCciaa(ReportValidazioneFascicoloCciaaDto cciaa) {
		this.cciaa = cciaa;
	}
	public List<ReportValidazioneFascicoloModPagamentoDto> getModPagamentoList() {
		return modPagamentoList;
	}
	public void setModPagamentoList(List<ReportValidazioneFascicoloModPagamentoDto> modPagamentoList) {
		this.modPagamentoList = modPagamentoList;
	}
	public String getIdentificativoScheda() {
		return identificativoScheda;
	}
	public void setIdentificativoScheda(String identificativoScheda) {
		this.identificativoScheda = identificativoScheda;
	}
	public ReportValidazioneFascicoloPrimoFirmatarioDto getFirmatario() {
		return firmatario;
	}
	public void setFirmatario(ReportValidazioneFascicoloPrimoFirmatarioDto firmatario) {
		this.firmatario = firmatario;
	}

}
