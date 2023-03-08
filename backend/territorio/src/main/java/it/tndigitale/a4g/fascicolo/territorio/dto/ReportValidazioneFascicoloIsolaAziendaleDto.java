package it.tndigitale.a4g.fascicolo.territorio.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloIsolaAziendaleDto {
	
	private String codiceIsola;
	private Integer superficieIsola;
	private List<ReportValidazioneFascicoloParticellaCatastaleDto> particelleCondotteList;
	private List<ReportValidazioneFascicoloParcelleRiferimentoDto> parcelleRiferimentoList;
	private List<ReportValidazioneFascicoloPianoColturaleDto> appezzamentiList;

	public String getCodiceIsola() {
		return codiceIsola;
	}
	public void setCodiceIsola(String codiceIsola) {
		this.codiceIsola = codiceIsola;
	}
	public Integer getSuperficieIsola() {
		return superficieIsola;
	}
	public void setSuperficieIsola(Integer superficieIsola) {
		this.superficieIsola = superficieIsola;
	}
	public List<ReportValidazioneFascicoloParticellaCatastaleDto> getParticelleCondotteList() {
		return particelleCondotteList;
	}
	public void setParticelleCondotteList(List<ReportValidazioneFascicoloParticellaCatastaleDto> particelleCondotteList) {
		this.particelleCondotteList = particelleCondotteList;
	}
	public List<ReportValidazioneFascicoloParcelleRiferimentoDto> getParcelleRiferimentoList() {
		return parcelleRiferimentoList;
	}
	public void setParcelleRiferimentoList(List<ReportValidazioneFascicoloParcelleRiferimentoDto> parcelleRiferimentoList) {
		this.parcelleRiferimentoList = parcelleRiferimentoList;
	}
	public List<ReportValidazioneFascicoloPianoColturaleDto> getAppezzamentiList() {
		return appezzamentiList;
	}
	public void setAppezzamentiList(List<ReportValidazioneFascicoloPianoColturaleDto> appezzamentiList) {
		this.appezzamentiList = appezzamentiList;
	}
	public ReportValidazioneFascicoloIsolaAziendaleDto(String codiceIsola, Integer superficieIsola) {
		super();
		this.codiceIsola = codiceIsola;
		this.superficieIsola = superficieIsola;
	}
	
}
