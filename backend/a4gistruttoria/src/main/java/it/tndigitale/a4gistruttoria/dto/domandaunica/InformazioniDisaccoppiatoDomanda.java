package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.io.Serializable;

public class InformazioniDisaccoppiatoDomanda implements Serializable {
	
	private static final long serialVersionUID = -172170480876491834L;

	private boolean campione;
	private boolean pascolo;
	private boolean giovane;
	private String riserva;
	
	public boolean isCampione() {
		return campione;
	}
	public boolean isPascolo() {
		return pascolo;
	}
	public boolean isGiovane() {
		return giovane;
	}
	public String getRiserva() {
		return riserva;
	}
	public void setCampione(boolean campione) {
		this.campione = campione;
	}
	public void setPascolo(boolean pascolo) {
		this.pascolo = pascolo;
	}
	public void setGiovane(boolean giovane) {
		this.giovane = giovane;
	}
	public void setRiserva(String riserva) {
		this.riserva = riserva;
	}
}
