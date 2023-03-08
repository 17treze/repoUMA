package it.tndigitale.a4g.proxy.dto.catasto;

public enum TipologiaQualitaColtura {


    /**
     * Valore per "Arativo". Valore per una particella fondiaria
     * 
     */
    ARATIVO,

    /**
     * Valore per "Prato". Valore per una particella fondiaria
     * 
     */
    PRATO,

    /**
     * Valore per "Orto". Valore per una particella fondiaria
     * 
     */
    ORTO,

    /**
     * Valore per "Frutteto". Valore per una particella fondiaria
     * 
     */
    FRUTTETO,

    /**
     * Valore per "Vigna". Valore per una particella fondiaria
     * 
     */
    VIGNA,

    /**
     * Valore per "Pascolo". Valore per una particella fondiaria
     * 
     */
    PASCOLO,

    /**
     * Valore per "Alpe". Valore per una particella fondiaria
     * 
     */
    ALPE,

    /**
     * Valore per "Bosco". Valore per una particella fondiaria
     * 
     */
    BOSCO,

    /**
     * Valore per "Palude Stagno". Valore per una particella fondiaria
     * 
     */
    PALUDE_STAGNO,

    /**
     * Valore per "Lago". Valore per una particella fondiaria
     * 
     */
    LAGO,

    /**
     * Valore per "Lago esente estimo". Valore per una particella fondiaria
     * 
     */
    LAGO_ESENTE_ESTIMO,

    /**
     * Valore per "Esente imposta". Valore per una particella fondiaria
     * 
     */
    ESENTE_IMPOSTA,

    /**
     * Valore per "Fiume Torrente". Valore per una particella fondiaria
     * 
     */
    FIUME_TORRENTE,

    /**
     * Valore per "Strada". Valore per una particella fondiaria
     * 
     */
    STRADA,

    /**
     * Valore per "Improduttivo". Valore per una particella fondiaria
     * 
     */
    IMPRODUTTIVO,

    /**
     * Valore per "Area edificiale". Valore per una particella edificiale
     * 
     */
    AREA_EDIFICIALE,

    /**
     * Valore per "Edificio". Valore per una particella edificiale
     * 
     */
    EDIFICIO,

    /**
     * Valore per "Proprieta' superficiaria". Valore per una particella edificiale
     * 
     */
    FABBRICATO_SOTTERRANEO,

    /**
     * Valore per "Fabbricato sotterraneo". Valore per una particella edificiale
     * 
     */
    PROPRIETA_SUPERFICIARIA;

    public String value() {
        return name();
    }

    public static TipologiaQualitaColtura fromValue(String v) {
        return valueOf(v);
    }

}
