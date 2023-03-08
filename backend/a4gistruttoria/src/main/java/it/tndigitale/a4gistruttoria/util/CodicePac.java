package it.tndigitale.a4gistruttoria.util;

public enum CodicePac {
	
	PAC_2014_2020("PAC1420");
	
	private String codice;
	
	private CodicePac(String codicePac) {
		this.codice=codicePac;
	}

	public String getCodice() {
		return codice;
	}

}
