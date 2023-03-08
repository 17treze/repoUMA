package it.tndigitale.a4gistruttoria.dto;

import java.util.Objects;

public class CuaaDenominazione {
	private String cuaa;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaaIntestatario) {
		this.cuaa = cuaaIntestatario;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String ragioneSociale) {
		this.denominazione = ragioneSociale;
	}

	private String denominazione;

	public CuaaDenominazione(String pcuaa, String pdenominazione) {
		this.cuaa = pcuaa;
		this.denominazione = pdenominazione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cuaa, denominazione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CuaaDenominazione other = (CuaaDenominazione) obj;
		return Objects.equals(cuaa, other.cuaa) && Objects.equals(denominazione, other.denominazione);
	}
}
