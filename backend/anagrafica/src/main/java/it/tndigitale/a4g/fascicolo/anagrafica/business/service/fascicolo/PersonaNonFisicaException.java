package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class PersonaNonFisicaException extends Exception {

    public PersonaNonFisicaException(String cuaa) {
        super("Il cuaa [" + cuaa + "] non e' una persona fisica ");
    }
}
