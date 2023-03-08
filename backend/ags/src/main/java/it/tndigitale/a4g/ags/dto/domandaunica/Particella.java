package it.tndigitale.a4g.ags.dto.domandaunica;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Particella {

	private Long idParticella;
	private String comune;
	private String codNazionale;
	private Long foglio;
	private String particella;
	private String sub;

	public Long getIdParticella() {
		return idParticella;
	}

	public void setIdParticella(Long idParticella) {
		this.idParticella = idParticella;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getCodNazionale() {
		return codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
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
