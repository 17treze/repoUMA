package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel("Rappresenta il modello relativo allo storico delle istruttorie di un utente")
public class StoricoIstruttorie {

    @ApiModelProperty(value = "Rappresenta l'utente a cui è riferito lo storico delle istruttorie")
    private Utente utente;
    @ApiModelProperty(value = "Rappresenta la lista delle istruttorie per lo storico")
    private List<IstruttoriaPerStorico> istruttorie;

    public Utente getUtente() {
        return utente;
    }

    public StoricoIstruttorie setUtente(Utente utente) {
        this.utente = utente;
        return this;
    }

    public List<IstruttoriaPerStorico> getIstruttorie() {
        return istruttorie;
    }

    public StoricoIstruttorie setIstruttorie(List<IstruttoriaPerStorico> istruttorie) {
        this.istruttorie = istruttorie;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoricoIstruttorie that = (StoricoIstruttorie) o;
        return Objects.equals(utente, that.utente) &&
                Objects.equals(istruttorie, that.istruttorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utente, istruttorie);
    }
}
