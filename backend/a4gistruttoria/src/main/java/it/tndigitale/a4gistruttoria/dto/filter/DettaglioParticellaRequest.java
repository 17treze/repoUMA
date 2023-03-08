package it.tndigitale.a4gistruttoria.dto.filter;



import java.util.Objects;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;

public class DettaglioParticellaRequest {

    private Long idIstruttoria;
    private Boolean pascolo;
    private Paginazione paginazione;
    private Ordinamento ordinamento;

    public Long getIdIstruttoria() {
        return idIstruttoria;
    }

    public DettaglioParticellaRequest setIdIstruttoria(Long idIstruttoria) {
        this.idIstruttoria = idIstruttoria;
        return this;
    }

    public Paginazione getPaginazione() {
        return paginazione;
    }

    public DettaglioParticellaRequest setPaginazione(Paginazione paginazione) {
        this.paginazione = paginazione;
        return this;
    }

    public Ordinamento getOrdinamento() {
        return ordinamento;
    }

    public DettaglioParticellaRequest setOrdinamento(Ordinamento ordinamento) {
        this.ordinamento = ordinamento;
        return this;
    }

    public Boolean getPascolo() {
        return pascolo;
    }

    public DettaglioParticellaRequest setPascolo(Boolean pascolo) {
        this.pascolo = pascolo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DettaglioParticellaRequest that = (DettaglioParticellaRequest) o;
        return Objects.equals(idIstruttoria, that.idIstruttoria) &&
                Objects.equals(pascolo, that.pascolo) &&
                Objects.equals(paginazione, that.paginazione) &&
                Objects.equals(ordinamento, that.ordinamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIstruttoria, pascolo, paginazione, ordinamento);
    }
}
