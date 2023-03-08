package it.tndigitale.a4gistruttoria.repository.model;

import java.util.stream.Stream;

public enum InterventoZootecnico implements InterventoType {
	VACCA_LATTE("310", "M1", CodiceInterventoAgs.LATTE),
	VACCA_LATTE_MONTANA("311", "M2", CodiceInterventoAgs.LATTE_BMONT),

	// ?
	BUFALA_30MESI("312", "M3", CodiceInterventoAgs.LATTE_BUFAL),

	VACCA_NUTRICE("313", "M4", CodiceInterventoAgs.BOVINI_VAC),

	// ?
	VACCA_DUPLICE_ATTITUDINE("314", "M18", CodiceInterventoAgs.BOVINI_VAC_SEL),

	BOVINO_MACELLATO("315", "M5", CodiceInterventoAgs.BOVINI_MAC),
	BOVINO_MACELLATO_12MESI("316", "M19", CodiceInterventoAgs.BOVINI_MAC_12M),
	BOVINO_MACELLATO_ETICHETTATO("318", "M19", CodiceInterventoAgs.BOVINI_MAC_ETIC),
	AGNELLA("320", "M6", CodiceInterventoAgs.OVICAP_AGN),
	OVICAPRINO_MACELLATO("321", "M7", CodiceInterventoAgs.OVICAP_MAC),
	VACCA_NUTRICE_NON_ISCRITTA("322", "M20", CodiceInterventoAgs.BOVINI_VAC_NO_ISCR);

	private String codiceAgea;

	private String misura;

	private CodiceInterventoAgs codiceAgs;

	InterventoZootecnico(String codiceAgea, String misura, CodiceInterventoAgs codiceAgs) {
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
        		.of(InterventoZootecnico.values())
        		.map(InterventoZootecnico::getCodiceAgea);
    }
}
