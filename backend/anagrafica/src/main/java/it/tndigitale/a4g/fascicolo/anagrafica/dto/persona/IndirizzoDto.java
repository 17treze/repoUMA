package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;

public class IndirizzoDto implements Serializable {
	private static final long serialVersionUID = 3419455083473633478L;

	private String provincia;

	private String comune;

	private String localita;

	private String toponimo; // toponimo

	private String cap;
	
	private String civico;
	
	private String frazione;
	
	private String via;
	
	private String codiceIstat;

	public String getProvincia() {
		return provincia;
	}

	public IndirizzoDto setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public IndirizzoDto setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public String getLocalita() {
		return localita;
	}

	public IndirizzoDto setLocalita(String localita) {
		this.localita = localita;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public IndirizzoDto setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getFrazione() {
		return frazione;
	}

	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getToponimo() {
		return toponimo;
	}

	public IndirizzoDto setToponimo(String toponimo) {
		this.toponimo = toponimo;
		return this;
	}

}