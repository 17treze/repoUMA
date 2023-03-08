package it.tndigitale.a4gistruttoria.util;

public enum TipoFiltroDomanda {

	PASCOLO("PASCOLO"), GIOVANE("GIOVANE"), RISERVA("RISERVA_NAZIONALE");

	String descrizione;

	private TipoFiltroDomanda(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
