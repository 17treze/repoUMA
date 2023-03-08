package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.exception.ValidationException;

import java.util.Objects;

import static it.tndigitale.a4gutente.exception.ErrorCode.FILTRO_RICERCA_UTENTI_NON_VALIDO;

@ApiModel("Rappresenta il filtro di una ricerca utenti ")
public class UtentiFilter {

    @ApiParam(value = "Filtro nome della ricerca utenti")
    private String nome;
    @ApiParam(value = "Filtro cognome della ricerca utenti")
    private String cognome;
    @ApiParam(value = "Filtro codiceFiscale della ricerca utenti")
    private String codiceFiscale;

    public String getNome() {
        return nome;
    }

    public UtentiFilter setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getCognome() {
        return cognome;
    }

    public UtentiFilter setCognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public UtentiFilter setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtentiFilter that = (UtentiFilter) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(cognome, that.cognome) &&
                Objects.equals(codiceFiscale, that.codiceFiscale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, codiceFiscale);
    }

    public static void isValid(UtentiFilter filter) {
        if (filter == null || (StringSupport.isEmptyOrNullTrim(filter.getNome()) 
        		   && StringSupport.isEmptyOrNullTrim(filter.getCognome()) 
        		   && StringSupport.isEmptyOrNullTrim(filter.getCodiceFiscale()))) {
            throw new ValidationException("Filtro utenti non valido: " +
                    "specificare una delle seguenti proprietà: nome, cognome, codice fiscale")
                    .setCode(FILTRO_RICERCA_UTENTI_NON_VALIDO.getCodice());
        }
    }

    public static UtentiFilter getOrDefault(UtentiFilter filter) {
        return new UtentiFilter().setCodiceFiscale(StringSupport.getOrDefault(filter.getCodiceFiscale()))
                                 .setNome(StringSupport.getOrDefault(filter.getNome()))
                                 .setCognome(StringSupport.getOrDefault(filter.getCognome()));
    }
}
