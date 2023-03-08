package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;

public class IndirizzoDto implements Serializable {
	private static final long serialVersionUID = -8184117308835771946L;
	
	private String via;
	private String denominazioneComune;
	private String denominazioneProvincia;
	private String siglaProvincia;
	private String cap;
	
	public String getVia() {
		return via;
	}
	public String getDenominazioneComune() {
		return denominazioneComune;
	}
	public String getDenominazioneProvincia() {
		return denominazioneProvincia;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public String getCap() {
		return cap;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public void setDenominazioneComune(String denominazioneComune) {
		this.denominazioneComune = denominazioneComune;
	}
	public void setDenominazioneProvincia(String denominazioneProvincia) {
		this.denominazioneProvincia = denominazioneProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
}
