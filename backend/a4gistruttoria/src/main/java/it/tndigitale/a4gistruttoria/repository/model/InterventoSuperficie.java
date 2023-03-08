package it.tndigitale.a4gistruttoria.repository.model;

import java.util.stream.Stream;

public enum InterventoSuperficie implements InterventoType {
	SOIA("122", "M8", CodiceInterventoAgs.SOIA),
	PROTEAGINOSA("123", "M10", CodiceInterventoAgs.CPROT),
	FRUMENTO_DURO("124", "M9", CodiceInterventoAgs.GDURO),
	LEGUMINOSA("125", "M11", CodiceInterventoAgs.LEGUMIN),
	// ?
	RISO("126", "M12", CodiceInterventoAgs.RISONE),
	POMODORO("128", "M14", CodiceInterventoAgs.POMOD),
	OLIVETO("129", "M15", CodiceInterventoAgs.OLIO),
	OLIVETO_PENDENZA("132", "M16", CodiceInterventoAgs.OLIVE_PEND75),
	OLIVETO_QUALITA("138", "M17", CodiceInterventoAgs.OLIVE_DISC);

	private String codiceAgea;

	private String misura;

	private CodiceInterventoAgs codiceAgs;

	InterventoSuperficie(String codiceAgea, String misura, CodiceInterventoAgs codiceAgs) {
		this.codiceAgea = codiceAgea;
		this.misura = misura;
		this.codiceAgs = codiceAgs;
	}

	@Override
	public String getCodiceAgea() {
		return codiceAgea;
	}

	@Override
	public String getMisura() {
		return misura;
	}

	@Override
	public CodiceInterventoAgs getCodiceAgs() {
		return codiceAgs;
	}

	public static Stream<String> streamOfCodiciAgea() {
        return Stream
        		.of(InterventoSuperficie.values())
        		.map(InterventoSuperficie::getCodiceAgea);
    }
}
