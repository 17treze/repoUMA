package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.util.Set;

public class AnomaliaPoligoniWithRootWorkspaceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idLavorazione;
	private String anomaliaDescrizione;
	private Set<AnomaliaValidazioneDto> anomaliaDto;
	// private Set<AnomaliaWorkspaceRelDto> relDto;

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

	public Set<AnomaliaValidazioneDto> getAnomaliaDto() {
		return anomaliaDto;
	}

	public void setAnomaliaDto(Set<AnomaliaValidazioneDto> anomaliaDto) {
		this.anomaliaDto = anomaliaDto;
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

	public String getAnomaliaDescrizione() {
		return anomaliaDescrizione;
	}

	public void setAnomaliaDescrizione(String anomaliaDescrizione) {
		this.anomaliaDescrizione = anomaliaDescrizione;
	}

}
