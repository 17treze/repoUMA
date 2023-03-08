package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

public class DatiPascolo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codPascolo;
	private String descPascolo;
	private Double uba;
	private List<Particella> particelle;

	public String getCodPascolo() {
		return codPascolo;
	}

	public void setCodPascolo(String codPascolo) {
		this.codPascolo = codPascolo;
	}

	public String getDescPascolo() {
		return descPascolo;
	}

	public void setDescPascolo(String descPascolo) {
		this.descPascolo = descPascolo;
	}

	public Double getUba() {
		return uba;
	}

	public void setUba(Double uba) {
		this.uba = uba;
	}

	public List<Particella> getParticelle() {
		return particelle;
	}

	public void setParticelle(List<Particella> particelle) {
		this.particelle = particelle;
	}

}
