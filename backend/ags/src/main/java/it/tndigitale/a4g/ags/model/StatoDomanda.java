package it.tndigitale.a4g.ags.model;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Arrays;

public enum StatoDomanda {

    RICEVIBILE("RICEVIBILE","000050"),
    NON_RICEVIBILE("NON RICEVIBILE","000055"),
    IN_ISTRUTTORIA("IN ISTRUTTORIA","000066"),
    PROTOCOLLATA("PROTOCOLLATA","000015");

    private String dbAgsValue;
    private final String codStato;

    StatoDomanda(String dbAgsValue, String codStato) {
        this.dbAgsValue = dbAgsValue;
        this.codStato = codStato;
    }

    public String getDbAgsValue() {
        return dbAgsValue;
    }
    
	public String getCodStato() {
		return codStato;
	}  

    public static StatoDomanda from(String dbAgsValue) {
        if (isEmpty(dbAgsValue)) {
            return null;
        }
        return Arrays.asList(StatoDomanda.values())
                     .stream()
                     .filter(stato -> dbAgsValue.equals(stato.getDbAgsValue()))
                     .findFirst()
                     .orElse(null);
    }
    
    public static StatoDomanda fromCodStato(String codStato) {
        for (StatoDomanda e2 : values()) {
            if (e2.codStato.equals(codStato)) {
                return e2;
            }
        }
        return null;
    }
}





//TODO
//000002 CANCELLATA
//000110 SOLO DENUNCIA
//000303 PROPOSTA LIQUIDAZIONE PAT
//REVISI REVISIONE
//000066 IN_ISTRUTTORIA
//000050 RICEVIBILE
//000095 IN SOSPENSIONE
//000055 NON RICEVIBILE
//000400 LIQUIDABILE ACCONTO
//000015 PROTOCOLLATA
//000067 ISTRUTTORIA PARZIALE
//000064 NON AMMISSIBILE
//000120 RINUNCIATA
//000060 ASSEGNABILE
//000140 CONTROLLO DI VERIFICA
//000062 NON ASSEGNATA
//000100 LIBRETTO EMESSO
//000090 IN LIQUIDAZIONE
//000000 CREATA
//000065 IN CORREZIONE
//000068 LIQUIDABILE
//000063 AMMISSIBILE
//000306 PROPOSTA LIQUIDAZIONE UE (Misura 10)
//000C66 INIZIO ISTRUTTORIA DOMANDE A CAMPIONE
//000075 CORRETTA
//000160 ISTRUTTORIA NEGATIVA
//000071 IN CORREZIONE CAA
//000011 RETTIFICATA
//000087 PROPOSTA LIQUIDAZIONE
//000170 INTEGRAZIONE
//000069 NON LIQUIDABILE
//000200 ANNULLATA
//000150 CHIUSURA CONTROLLI
//000304 PROPOSTA LIQUIDAZIONE UE (Economie)
//000305 PROPOSTA LIQUIDAZIONE UE (Misura 11)
//000302 PROPOSTA LIQUIDAZIONE UE
//000001 IN COMPILAZIONE
//000010 PRESENTATA
//000079 ISTRUTTORIA CONCLUSA
//000145 CONTROLLATA
//000061 ASSEGNATA
//INIIST INIZIO ISTRUTTORIA
//000003 CONTROLLATA
//000072 CORRETTA CAA
//000C67 ISTRUTTORIA PARZIALE DOMANDE A CAMPIONE

