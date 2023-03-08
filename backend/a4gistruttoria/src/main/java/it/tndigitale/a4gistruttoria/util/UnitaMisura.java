package it.tndigitale.a4gistruttoria.util;

public enum UnitaMisura {
	NULL(""), PERCENTUALE("%"), ETTARI("ha"), EURO("euro"), METRIQUADRI("mq"), UBA("UBA"), UBA_HA("UBA/ha");

	private String value;

	private UnitaMisura(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
