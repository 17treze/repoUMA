package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;

public class AziendaAgricolaDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cuaa;
	private String ragioneSociale;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	@Override
	public String toString() {
		return String.format("{cuaa=%s,ragioneSociale=%s}", cuaa, ragioneSociale);
	}
}
