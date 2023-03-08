package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Azienda;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.repository.model.A4grUtenteAzienda;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.utility.ListSupport;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;

public class AziendaBuilder {

    public static Azienda from(TitolareImpresa titolareImpresa) {
        return new Azienda().setCuaa(titolareImpresa.getCuaa())
                            .setIdCarica(1L);
    }

    public static List<Azienda> fromTitolariImpresa(List<TitolareImpresa> titolari) {
        return emptyIfNull(titolari).stream()
                                    .map(titolare -> from(titolare))
                                    .collect(Collectors.toList());

    }

    public static List<Azienda> from(Set<A4grUtenteAzienda> aziende) {
        return Optional.ofNullable(aziende)
                       .orElse(new HashSet<>())
                       .stream()
                       .map(azienda -> from(azienda))
                       .collect(Collectors.toList());
    }

    public static Azienda from(A4grUtenteAzienda azienda) {
        return new Azienda().setCuaa(azienda.getCuaa())
                            .setDataAggiornamento(azienda.getDataAggiornamento())
                            .setId(azienda.getId())
                            .setIdCarica(azienda.getIdCarica());
    }

    public static A4grUtenteAzienda from(TitolareImpresa azienda, A4gtUtente utente) {
        A4grUtenteAzienda entita = new A4grUtenteAzienda();
        entita.setCuaa(azienda.getCuaa());
        entita.setIdCarica(1L);
        entita.setDataAggiornamento(LocalDateTime.now());
        entita.setA4gtUtente(utente);
        return entita;
    }

    public static Set<A4grUtenteAzienda> from(List<TitolareImpresa> aziende, A4gtUtente utente) {
        return Optional.ofNullable(aziende)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(azienda -> from(azienda, utente))
                       .collect(Collectors.toSet());
    }

}
