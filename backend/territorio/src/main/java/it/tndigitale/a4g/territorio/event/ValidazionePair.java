package it.tndigitale.a4g.territorio.event;

import java.io.Serializable;
import java.util.Objects;

public class ValidazionePair implements Serializable {
	private static final long serialVersionUID = -2144885926870031545L;
	
	private String cuaa;
	private Integer idValidazione;
	
	public ValidazionePair() {
		super();
	}

	public ValidazionePair(String cuaa, Integer idValidazione) {
		super();
		this.cuaa = cuaa;
		this.idValidazione = idValidazione;
	}
	
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public Integer getIdValidazione() {
		return idValidazione;
	}
	public void setIdValidazione(Integer idValidazione) {
		this.idValidazione = idValidazione;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cuaa, idValidazione);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidazionePair other = (ValidazionePair) obj;
		return Objects.equals(cuaa, other.cuaa) && Objects.equals(idValidazione, other.idValidazione);
	}
}
