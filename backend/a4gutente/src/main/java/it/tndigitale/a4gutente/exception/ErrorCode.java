package it.tndigitale.a4gutente.exception;

public enum ErrorCode {

    FILTRO_RICERCA_UTENTI_NON_VALIDO(1);

    private Integer codice;

    ErrorCode(Integer codice) {
        this.codice = codice;
    }

    public Integer getCodice() {
        return codice;
    }

}
