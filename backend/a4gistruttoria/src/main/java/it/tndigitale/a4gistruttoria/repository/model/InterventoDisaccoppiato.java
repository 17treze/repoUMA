package it.tndigitale.a4gistruttoria.repository.model;

import java.util.stream.Stream;

public enum InterventoDisaccoppiato implements InterventoType {
    GREENING("008", CodiceInterventoAgs.GREE),
    GIOVANE("009", CodiceInterventoAgs.GIOV),
    BPS("026", CodiceInterventoAgs.BPS);

    private String codiceAgea;

    private CodiceInterventoAgs codiceAgs;

    InterventoDisaccoppiato(String codiceAgea, CodiceInterventoAgs codiceAgs) {
        this.codiceAgea = codiceAgea;
        this.codiceAgs = codiceAgs;
    }
    @Override
    public String getCodiceAgea() {
        return codiceAgea;
    }

    @Override
    public String getMisura() {
        return null;
    }

    @Override
    public CodiceInterventoAgs getCodiceAgs() {
        return codiceAgs;
    }

    public static Stream<String> streamOfCodiciAgea() {
        return Stream
                .of(InterventoDisaccoppiato.values())
                .map(InterventoDisaccoppiato::getCodiceAgea);
    }
}
