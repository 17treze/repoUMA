package it.tndigitale.a4g.fascicolo.territorio.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneTerreniAgsDto {

	private List<ReportValidazioneFascicoloIsolaAziendaleDto> isoleList;
	private List<ReportValidazioneFascicoloTitoloConduzioneDto> titoliConduzioneList;
	private List<ReportValidazioneFascicoloSchedarioFrutticoloDto> schedarioFrutticoloList;
	
	public List<ReportValidazioneFascicoloSchedarioFrutticoloDto> getSchedarioFrutticoloList() {
		return schedarioFrutticoloList;
	}
	public void setSchedarioFrutticoloList(List<ReportValidazioneFascicoloSchedarioFrutticoloDto> schedarioFrutticoloList) {
		this.schedarioFrutticoloList = schedarioFrutticoloList;
	}
	public List<ReportValidazioneFascicoloIsolaAziendaleDto> getIsoleList() {
		return isoleList;
	}
	public void setIsoleList(List<ReportValidazioneFascicoloIsolaAziendaleDto> isoleList) {
		this.isoleList = isoleList;
	}
	public List<ReportValidazioneFascicoloTitoloConduzioneDto> getTitoliConduzioneList() {
		return titoliConduzioneList;
	}
	public void setTitoliConduzioneList(List<ReportValidazioneFascicoloTitoloConduzioneDto> titoliConduzioneList) {
		this.titoliConduzioneList = titoliConduzioneList;
	}
}
