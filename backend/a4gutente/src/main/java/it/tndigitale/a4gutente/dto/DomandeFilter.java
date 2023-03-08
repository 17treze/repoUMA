package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiParam;

import java.util.Objects;

public class DomandeFilter {

    @ApiParam(value = "Filtro nome della ricerca domande")
    private String nome;
    @ApiParam(value = "Filtro cognome della ricerca domande")
    private String cognome;
    @ApiParam(value = "Filtro codiceFiscale della ricerca domande")
    private String codiceFiscale;

    public String getNome() {
        return nome;
    }

    public DomandeFilter setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getCognome() {
        return cognome;
    }

    public DomandeFilter setCognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public DomandeFilter setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomandeFilter that = (DomandeFilter) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(cognome, that.cognome) &&
                Objects.equals(codiceFiscale, that.codiceFiscale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, codiceFiscale);
    }

}
