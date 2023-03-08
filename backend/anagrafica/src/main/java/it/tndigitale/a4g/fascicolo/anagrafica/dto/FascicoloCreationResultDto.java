package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloCreationAnomalyEnum;

public class FascicoloCreationResultDto {
	List<FascicoloCreationAnomalyEnum> anomalies;
	FascicoloDto fascicoloDto;
	
	public FascicoloCreationResultDto() {
		super();
	}
	public FascicoloCreationResultDto(List<FascicoloCreationAnomalyEnum> anomalies, FascicoloDto fascicoloDto) {
		this.fascicoloDto = fascicoloDto;
		this.anomalies = anomalies;
	}
	public List<FascicoloCreationAnomalyEnum> getAnomalies() {
		return anomalies;
	}
	public void setAnomalies(List<FascicoloCreationAnomalyEnum> anomalyList) {
		this.anomalies = anomalyList;
	}
	public FascicoloDto getFascicoloDto() {
		return fascicoloDto;
	}
	public void setFascicoloDto(FascicoloDto fascicoloDto) {
		this.fascicoloDto = fascicoloDto;
	}
}
