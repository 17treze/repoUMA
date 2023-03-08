package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;

public class CreaBuchiLavorazioneInCorsoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5228115438663342197L;
	private String esito;


	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}
}
