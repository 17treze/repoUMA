package it.tndigitale.a4g.fascicolo.antimafia.dto;


/**
 * Oggetto contenente le informazioni a seguito di un aggiornamento della Dichiarazione Antimafia
 */
public class AggiornaDichiarazioneEsito {
	
	private Dichiarazione dichiarazione;
	private Boolean creataNuovaDichiarazione;
	private String esito;
	
	public Dichiarazione getDichiarazione() {
		return dichiarazione;
	}
	
	public String getEsito() {
		return esito;
	}
	
	public void setDichiarazione(Dichiarazione dichiarazione) {
		this.dichiarazione = dichiarazione;
	}
	
	public void setEsito(String esito) {
		this.esito = esito;
	}

	public Boolean getCreataNuovaDichiarazione() {
		return creataNuovaDichiarazione;
	}

	public void setCreataNuovaDichiarazione(Boolean creataNuovaDichiarazione) {
		this.creataNuovaDichiarazione = creataNuovaDichiarazione;
	}
	
	
	
	
	
}
