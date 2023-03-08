package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class PersonaFisicaNonDecedutaException extends Exception {

    public PersonaFisicaNonDecedutaException(String cuaa) {
        super("Il cuaa [" + cuaa + "] corrisponde persona fisica non deceduta");
    }
}
