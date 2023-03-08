package it.tndigitale.a4gistruttoria.repository.model;

import java.util.stream.Stream;

public enum Intervento implements InterventoType {
    GREENING(InterventoDisaccoppiato.GREENING),
    GIOVANE(InterventoDisaccoppiato.GIOVANE),
    BPS(InterventoDisaccoppiato.BPS),

    SOIA(InterventoSuperficie.SOIA),
    PROTEAGINOSA(InterventoSuperficie.PROTEAGINOSA),
    FRUMENTO_DURO(InterventoSuperficie.FRUMENTO_DURO),
    LEGUMINOSA(InterventoSuperficie.LEGUMINOSA),
    RISO(InterventoSuperficie.RISO),
    POMODORO(InterventoSuperficie.POMODORO),
    OLIVETO(InterventoSuperficie.OLIVETO),
    OLIVETO_PENDENZA(InterventoSuperficie.OLIVETO_PENDENZA),
    OLIVETO_QUALITA(InterventoSuperficie.OLIVETO_QUALITA),

    VACCA_LATTE(InterventoZootecnico.VACCA_LATTE),
    VACCA_LATTE_MONTANA(InterventoZootecnico.VACCA_LATTE_MONTANA),
    BUFALA_30MESI(InterventoZootecnico.BUFALA_30MESI),
    VACCA_NUTRICE(InterventoZootecnico.VACCA_NUTRICE),
    VACCA_DUPLICE_ATTITUDINE(InterventoZootecnico.VACCA_DUPLICE_ATTITUDINE),
    BOVINO_MACELLATO(InterventoZootecnico.BOVINO_MACELLATO),
    BOVINO_MACELLATO_12MESI(InterventoZootecnico.BOVINO_MACELLATO_12MESI),
    BOVINO_MACELLATO_ETICHETTATO(InterventoZootecnico.BOVINO_MACELLATO_ETICHETTATO),
    AGNELLA(InterventoZootecnico.AGNELLA),
    OVICAPRINO_MACELLATO(InterventoZootecnico.OVICAPRINO_MACELLATO),
    VACCA_NUTRICE_NON_ISCRITTA(InterventoZootecnico.VACCA_NUTRICE_NON_ISCRITTA);

    private InterventoType intervento;

    Intervento(InterventoType intervento) {
        this.intervento = intervento;
    }

    public String getCodiceAgea() {
        return intervento.getCodiceAgea();
    }

    public String getMisura() {
        return intervento.getMisura();
    }

    public CodiceInterventoAgs getCodiceAgs() {
        return intervento.getCodiceAgs();
    }
    
    public InterventoType getInterventoType() {
        return intervento;
    }

    public static Stream<String> streamOfCodiciAgea() {
        return Stream
                .of(Intervento.values())
                .map(Intervento::getCodiceAgea);
    }
}
