package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

public class IndirizzoDto {

	private String indirizzo;
	private String comune;
	private String cap;

	public String getIndirizzo() {
		return indirizzo;
	}
	public IndirizzoDto setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public IndirizzoDto setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getCap() {
		return cap;
	}
	public IndirizzoDto setCap(String cap) {
		this.cap = cap;
		return this;
	}
}
