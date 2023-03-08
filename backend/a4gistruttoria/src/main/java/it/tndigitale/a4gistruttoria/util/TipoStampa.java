package it.tndigitale.a4gistruttoria.util;

public enum TipoStampa {

	VERBALE_ISTRUTTORIA("VERBALE_ISTRUTTORIA"), VERBALE_LIQUIDAZIONE("VERBALE_LIQUIDAZIONE");

	private String tipoStampa;

	TipoStampa(String tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	public String getTipoStampa() {
		return tipoStampa;
	}

}
