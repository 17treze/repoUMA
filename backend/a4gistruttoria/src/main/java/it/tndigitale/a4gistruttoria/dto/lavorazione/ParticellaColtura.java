package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.dto.Particella;

public class ParticellaColtura {
	private Particella particella;
	private String coltura;
	private String descColtura;
	private String livello;
	private Float valNum;
	private Boolean valBool;
	private String valString;
	private String descMantenimento;

	public String getColtura() {
		return coltura;
	}

	public void setColtura(String coltura) {
		this.coltura = coltura;
	}

	public Particella getParticella() {
		return particella;
	}

	public void setParticella(Particella particella) {
		this.particella = particella;
	}

	public String getLivello() {
		return livello;
	}

	public void setLivello(String livello) {
		this.livello = livello;
	}

	public Float getValNum() {
		return valNum;
	}

	public void setValNum(Float valNum) {
		this.valNum = valNum;
	}

	public Boolean getValBool() {
		return valBool;
	}

	public void setValBool(Boolean valBool) {
		this.valBool = valBool;
	}

	public String getValString() {
		return valString;
	}

	public void setValString(String valString) {
		this.valString = valString;
	}

	public String getDescMantenimento() {
		return descMantenimento;
	}

	public void setDescMantenimento(String descMantenimento) {
		this.descMantenimento = descMantenimento;
	}

	public String getDescColtura() {
		return descColtura;
	}

	public void setDescColtura(String descColtura) {
		this.descColtura = descColtura;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)

			return false;
		if (other == this)
			return true;
		if (!(other instanceof ParticellaColtura))
			return false;
		ParticellaColtura otherParticellaColtura = (ParticellaColtura) other;
		
		return otherParticellaColtura.getParticella().equals(this.getParticella()) 
				&& otherParticellaColtura.getColtura().equals(this.getColtura());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
