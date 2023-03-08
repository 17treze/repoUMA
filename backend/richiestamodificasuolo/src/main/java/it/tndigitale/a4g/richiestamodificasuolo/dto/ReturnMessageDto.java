package it.tndigitale.a4g.richiestamodificasuolo.dto;

import java.io.Serializable;

public class ReturnMessageDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String RETURN;
	private String TIPO_ERRORE;

	public ReturnMessageDto(String returnMessage, String tipoErrore) {
		RETURN = returnMessage;
		TIPO_ERRORE = tipoErrore;
	}

	public String getRETURN() {
		return RETURN;
	}

	public void setRETURN(String rETURN) {
		RETURN = rETURN;
	}

	public String getTIPO_ERRORE() {
		return TIPO_ERRORE;
	}

	public void setTIPO_ERRORE(String tIPO_ERRORE) {
		TIPO_ERRORE = tIPO_ERRORE;
	}

}
