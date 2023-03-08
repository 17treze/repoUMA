package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloCreationAnomalyEnum;

public class FascicoloMigrationResultDto {
	List<FascicoloCreationAnomalyEnum> anomalies;
	Long id;
	
	public FascicoloMigrationResultDto() {
		super();
	}
	public FascicoloMigrationResultDto(List<FascicoloCreationAnomalyEnum> anomalies, Long id) {
		this.id = id;
		this.anomalies = anomalies;
	}
	public List<FascicoloCreationAnomalyEnum> getAnomalies() {
		return anomalies;
	}
	public void setAnomalies(List<FascicoloCreationAnomalyEnum> anomalyList) {
		this.anomalies = anomalyList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
