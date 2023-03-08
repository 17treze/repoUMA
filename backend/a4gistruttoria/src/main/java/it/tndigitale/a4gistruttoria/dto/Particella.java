package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.Objects;

public class Particella implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idParticella;
	private String comune;
	private String codNazionale;
	private Long foglio;
	private String particella;
	private String sub;

	public Particella() {
	}

	public Particella(String pidParticella, String pcomune, String pcodNazionale, String pfoglio, String pparticella, String psub) {
		// idParticella = pidParticella;
		comune = pcomune;
		codNazionale = pcodNazionale;
		// foglio = pfoglio;
		particella = pparticella;
		sub = psub;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(codNazionale, comune, foglio, idParticella, particella, sub);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Particella other = (Particella) obj;
		return Objects.equals(codNazionale, other.codNazionale) 
				&& Objects.equals(comune, other.comune)
				&& Objects.equals(foglio, other.foglio) 
				&& Objects.equals(idParticella, other.idParticella)
				&& Objects.equals(particella, other.particella) 
				&& Objects.equals(sub, other.sub);
	}
}
