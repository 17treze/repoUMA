package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

@Deprecated
public class Ordinamento implements Serializable {
	
	private static final long serialVersionUID = 4711586120128894700L;
	
	private String proprieta;
	private Ordine ordine;
	
	
	public String getProprieta() {
		return proprieta;
	}


	public void setProprieta(String proprieta) {
		this.proprieta = proprieta;
	}


	public Ordine getOrdine() {
		return ordine;
	}


	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}


	public static enum Ordine {
		ASC, DESC;
	}
}
