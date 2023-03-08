package it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione;

import java.util.List;

public class ParamsRicercaFascicolo {
	private String denominazione;
	private String cuaa;
	private List<String> caacodici;

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public List<String> getCaacodici() {
		return caacodici;
	}

	public void setCaacodici(List<String> caacodici) {
		this.caacodici = caacodici;
	}

	@Override
	public String toString() {
		return String.format("ParamsRicercaFascicolo [denominazione=%s, cuaa=%s, caacodici=%s]", denominazione, cuaa, caacodici);
	}
}
