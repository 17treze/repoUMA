package it.tndigitale.a4g.fascicolo.territorio.business.service;

public enum RisultatoControlloCompletezzaEnum {
	NO_DATA, // -2
	PROBLEMA_NON_BLOCCANTE, // -1
	OK, // 0
	PROBLEMA_BLOCCANTE; // -3
	
	public static RisultatoControlloCompletezzaEnum create(final short resultNumber) {
		RisultatoControlloCompletezzaEnum resEnum;
		switch(resultNumber) {
		case -2: resEnum = RisultatoControlloCompletezzaEnum.NO_DATA; break;
		case -1: resEnum = RisultatoControlloCompletezzaEnum.PROBLEMA_NON_BLOCCANTE; break;
		case 0: resEnum = RisultatoControlloCompletezzaEnum.OK; break;
		case -3: resEnum = RisultatoControlloCompletezzaEnum.PROBLEMA_BLOCCANTE; break;
		default: throw new IllegalArgumentException(
				String.format("Got unexpected value '%d' from stored procedure", resultNumber));
		}
		return resEnum;
	}
}
