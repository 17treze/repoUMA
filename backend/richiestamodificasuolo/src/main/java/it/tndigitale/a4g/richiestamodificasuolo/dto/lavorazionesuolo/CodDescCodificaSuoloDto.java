package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;

public class CodDescCodificaSuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String descrizione;
	private String codice;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

}
