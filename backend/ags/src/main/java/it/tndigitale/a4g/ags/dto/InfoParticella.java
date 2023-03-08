package it.tndigitale.a4g.ags.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InfoParticella {

	@JsonProperty(required = true)
	private Integer codiceComuneCatastale;
	@JsonProperty(required = true)
	private String particella;
	private String sub;
	
	public Integer getCodiceComuneCatastale() {
		return codiceComuneCatastale;
	}
	public void setCodiceComuneCatastale(Integer codiceComuneCatastale) {
		this.codiceComuneCatastale = codiceComuneCatastale;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
}
