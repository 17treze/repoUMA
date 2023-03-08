package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum CodiciDiRitornoEnum {
    OPERAZIONE_OK("012", "	operazione correttamente eseguita"),
    ERRORE_GENERICO("013", "Errore generico"),
    DATI_NON_TROVATI("016", "dati non trovati"),
    IDENTIFICATIVO_NON_VALIDO("017", "Identificativo non valido"),
    CAMPO_OBBLIGATORIO("020", "campo obbligatorio"),
    PARAMETRI_NON_VALIDI("021", "parametri non validi/non coerenti"),
    SOGGETTO_NON_TROVATO("030", "Soggetto non trovato"),
    FASCICOLO_NON_TROVATO("031", "fascicolo non trovato"),
    FASCICOLO_NON_DI_COMPETENZA("032", "Fascicolo non di competenza"),
    FASCICOLO_NON_VALIDATO("076", "fascicolo non validato"),
    REGIONE_NON_ABILITATA("079", "regione non abilitata perche’ diversa da quella che ha gia’ inviato il fascicolo"),
    FASCICOLO_IN_LAVORAZIONE("097", "Fascicolo in lavorazione");


    private final String codice;
    private final String descrizione;

    CodiciDiRitornoEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static CodiciDiRitornoEnum fromCodice(String codice) {
        return Arrays.stream(CodiciDiRitornoEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
