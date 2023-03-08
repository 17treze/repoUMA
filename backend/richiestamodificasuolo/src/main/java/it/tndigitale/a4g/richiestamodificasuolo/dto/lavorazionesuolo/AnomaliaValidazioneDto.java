package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;

public class AnomaliaValidazioneDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idLavorazione;
	private String tipoAnomalia;
	// private Set<WorkspaceLavSuoloDto> workspaceDto;
	private String dettaglioAnomalia;
	private Double area;
	private Double[] extent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}

	public String getTipoAnomalia() {
		return tipoAnomalia;
	}

	public void setTipoAnomalia(String tipoAnomalia) {
		this.tipoAnomalia = tipoAnomalia;
	}

	// public Set<WorkspaceLavSuoloDto> getWorkspaceDto() {
	// return workspaceDto;
	// }
	//
	// public void setWorkspaceDto(Set<WorkspaceLavSuoloDto> workspaceDto) {
	// this.workspaceDto = workspaceDto;
	// }

	public String getDettaglioAnomalia() {
		return dettaglioAnomalia;
	}

	public void setDettaglioAnomalia(String dettaglioAnomalia) {
		this.dettaglioAnomalia = dettaglioAnomalia;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Double[] getExtent() {
		return extent;
	}

	public void setExtent(Double[] extent) {
		this.extent = extent;
	}

}
