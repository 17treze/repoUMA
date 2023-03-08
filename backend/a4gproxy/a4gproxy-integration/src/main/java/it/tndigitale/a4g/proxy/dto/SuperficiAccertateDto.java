package it.tndigitale.a4g.proxy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"cuaa",
"annoCampagna",
"identificativoDomanda",
"superficieAccertata",
"superficieDeterminata",
"motivazioneA1",
"motivazioneA2",
"motivazioneA3",
"motivazioneB0"
})
public class SuperficiAccertateDto {

	@JsonProperty("cuaa")
	public String cuaa;
	@JsonProperty("annoCampagna")
	public Long annoCampagna;
	@JsonProperty("identificativoDomanda")
	public String identificativoDomanda;
	@JsonProperty("superficieAccertata")
	public Double superficieAccertata;
	@JsonProperty("superficieDeterminata")
	public Double superficieDeterminata;
	@JsonProperty("motivazioneA1")
	public SiNoEnum motivazioneA1;
	@JsonProperty("motivazioneA2")
	public SiNoEnum motivazioneA2;
	@JsonProperty("motivazioneA3")
	public SiNoEnum motivazioneA3;
	@JsonProperty("motivazioneB0")
	public SiNoEnum motivazioneB0;
	@JsonProperty("motivazioneB1")
	public SiNoEnum motivazioneB1;
	
	public String getCuaa() {
		return cuaa;
	}
	
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	
	public Long getAnnoCampagna() {
		return annoCampagna;
	}
	
	public void setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
	}
	
	public String getIdentificativoDomanda() {
		return identificativoDomanda;
	}
	
	public void setIdentificativoDomanda(String identificativoDomanda) {
		this.identificativoDomanda = identificativoDomanda;
	}
	
	public Double getSuperficieAccertata() {
		return superficieAccertata;
	}
	
	public void setSuperficieAccertata(Double superficieAccertata) {
		this.superficieAccertata = superficieAccertata;
	}
	
	public Double getSuperficieDeterminata() {
		return superficieDeterminata;
	}
	
	public void setSuperficieDeterminata(Double superficieDeterminata) {
		this.superficieDeterminata = superficieDeterminata;
	}
	
	public SiNoEnum getMotivazioneA1() {
		return motivazioneA1;
	}
	
	public void setMotivazioneA1(SiNoEnum motivazioneA1) {
		this.motivazioneA1 = motivazioneA1;
	}
	
	public SiNoEnum getMotivazioneA2() {
		return motivazioneA2;
	}
	
	public void setMotivazioneA2(SiNoEnum motivazioneA2) {
		this.motivazioneA2 = motivazioneA2;
	}
	
	public SiNoEnum getMotivazioneA3() {
		return motivazioneA3;
	}
	
	public void setMotivazioneA3(SiNoEnum motivazioneA3) {
		this.motivazioneA3 = motivazioneA3;
	}
	
	public SiNoEnum getMotivazioneB0() {
		return motivazioneB0;
	}
	
	public void setMotivazioneB0(SiNoEnum motivazioneB0) {
		this.motivazioneB0 = motivazioneB0;
	}

	public SiNoEnum getMotivazioneB1() {
		return motivazioneB1;
	}

	public void setMotivazioneB1(SiNoEnum motivazioneB1) {
		this.motivazioneB1 = motivazioneB1;
	}
}