package it.tndigitale.a4g.proxy.dto;

public class Indirizzo {
	
	private String indirizzo;
	private String numeroCivico;
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getNumeroCivico() {
		return numeroCivico;
	}
	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}
	@Override
	public String toString() {
		return "Indirizzo [indirizzo=" + indirizzo + ", numeroCivico=" + numeroCivico + "]";
	}

}
