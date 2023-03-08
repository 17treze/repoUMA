package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;

import java.util.Objects;

public class CounterStato {

    private StatoDomandaRegistrazioneUtente stato;
    private Long count = 0L;

    public CounterStato() {}

    public CounterStato(StatoDomandaRegistrazioneUtente stato, Long count) {
        this.stato = stato;
        this.count = (count!=null)? count:0L;
    }

    public Long getCount() {
        return count;
    }

    public StatoDomandaRegistrazioneUtente getStato() {
        return stato;
    }

    public CounterStato setStato(StatoDomandaRegistrazioneUtente stato) {
        this.stato = stato;
        return this;
    }

    public CounterStato setCount(Long count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterStato that = (CounterStato) o;
        return stato == that.stato &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stato, count);
    }
}
