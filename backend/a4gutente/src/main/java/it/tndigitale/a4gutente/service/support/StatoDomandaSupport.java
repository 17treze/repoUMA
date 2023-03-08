package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.CounterStato;

import java.util.List;
import java.util.stream.Collectors;

public class StatoDomandaSupport {

    public static List<CounterStato> riempiConStatiMancanti(List<CounterStato> counters) {
        List<StatoDomandaRegistrazioneUtente> stati = counters.stream().map(item -> item.getStato()).collect(Collectors.toList());
        StatoDomandaRegistrazioneUtente.ritornaComplementari(stati)
                .forEach(stato -> {
                    counters.add(new CounterStato().setStato(stato).setCount(0L));
                });
        return counters;
    }

}
