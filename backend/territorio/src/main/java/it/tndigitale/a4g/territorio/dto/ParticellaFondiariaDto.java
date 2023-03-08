package it.tndigitale.a4g.territorio.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ParticellaFondiariaDto  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String particella;
    private Integer foglio;
    private String sub;
    private String sezione;
    private String comune;
    private Long superficieCondotta;
    
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public Integer getFoglio() {
		return foglio;
	}
	public void setFoglio(Integer foglio) {
		this.foglio = foglio;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public Long getSuperficieCondotta() {
		return superficieCondotta;
	}
	public void setSuperficieCondotta(Long superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}

}
