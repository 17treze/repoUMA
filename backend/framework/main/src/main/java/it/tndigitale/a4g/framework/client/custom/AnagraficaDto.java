package it.tndigitale.a4g.framework.client.custom;

import java.util.List;

public class AnagraficaDto {
	List<AnagraficaEnum> anomalies;
	Long id;
	
	public AnagraficaDto() {
		super();
	}
	public AnagraficaDto(List<AnagraficaEnum> anomalies, Long id) {
		this.id = id;
		this.anomalies = anomalies;
	}
	public List<AnagraficaEnum> getAnomalies() {
		return anomalies;
	}
	public void setAnomalies(List<AnagraficaEnum> anomalyList) {
		this.anomalies = anomalyList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
