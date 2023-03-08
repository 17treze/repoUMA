package it.tndigitale.a4g.ags.dto.domandaunica;

import java.util.List;

public class DatiPascolo {

	private TipoPascolo tipoPascolo;
	private String codPascolo;
	private String descPascolo;
	private Double uba;
	private List<Particella> particelle;
	
	public static enum TipoPascolo {
		MALGA, AZIENDALE;
	}

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

	public TipoPascolo getTipoPascolo() {
		return tipoPascolo;
	}

	public void setTipoPascolo(TipoPascolo tipoPascolo) {
		this.tipoPascolo = tipoPascolo;
	}

}
