package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import it.tndigitale.a4gutente.utility.ListSupport;

import java.util.List;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.service.builder.ProfiloBuilder.convert;
import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;
import static it.tndigitale.a4gutente.utility.ListSupport.isNotEmpty;

public class UtenteBuilder {

    public static Utente from(A4gtUtente utente, List<DomandaRegistrazioneUtente> domande) {
        DomandaRegistrazioneUtente domanda = domande.stream()
                                                    .filter(d -> utente.getIdentificativo().equals(d.getIdentificativoUtente()))
                                                    .findFirst()
                                                    .orElse(null);
        return new Utente().setCodiceFiscale(utente.getCodiceFiscale())
                           .setIdentificativo(utente.getIdentificativo())
                           .setId(utente.getId())
                           .setCognome((domanda != null)? domanda.getCognome():null)
                           .setNome((domanda != null)? domanda.getNome():null)
                           .setProfili(convert(ListSupport.convert(utente.getProfili())));
    }

    public static List<Utente> from(List<A4gtUtente> utenti, List<DomandaRegistrazioneUtente> domande) {
        return emptyIfNull(utenti).stream()
                                          .map(utente -> from(utente, domande))
                                          .collect(Collectors.toList());
    }

    public static A4gtUtente getUtenteDaPersistere(DomandaRegistrazioneUtente domanda) {
        return new A4gtUtente().setIdentificativo(domanda.getIdentificativoUtente())
                               .setCodiceFiscale(domanda.getCodiceFiscale())
                               .setEmail(domanda.getEmail())
                               .setTelefono(domanda.getTelefono());
    }
    
    
    public static Utente detail(A4gtUtente utente, PersonaEntita persona) {
        return from(utente, persona).setSedi(EnteBuilder.from(utente.getA4gtEntes()))
                                    .setAziende(AziendaBuilder.from(utente.getA4grUtenteAziendas()))
                                    .setDistributori(DistributoreBuilder.from(utente.getA4gtDistributori()));
    }
    
    public static Utente from(A4gtUtente utente, PersonaEntita persona) {
        Utente utenteDto =  convertSenzaProfiliESedi(utente, persona)
              .setProfili(convert(ListSupport.convert(utente.getProfili())));
        return utenteDto;
    }

    public static Utente convertUserWithDisabledProfiles(A4gtUtente utente, PersonaEntita persona) {
        Utente utenteDto =  convertSenzaProfiliESedi(utente, persona)
                .setProfili(convert(ListSupport.convert(utente.getProfili())));
        if (isNotEmpty(utente.getIstruttorie())) {
            utenteDto.disableProfiles(utente.getIstruttorie().get(0).getProfiliDisabilitati());
        }
        return utenteDto;
    }

    public static Utente convertSenzaProfiliESedi(A4gtUtente utente, PersonaEntita persona) {
        return new Utente().setCodiceFiscale(utente.getCodiceFiscale())
                .setIdentificativo(utente.getIdentificativo())
                .setId(utente.getId())
                .setCognome((persona != null)? persona.getCognome():null)
                .setNome((persona != null)? persona.getNome():null);
    }

    public static List<Utente> from(List<A4gtUtente> utenti) {
        return emptyIfNull(utenti).stream()
                                  .map(utente -> convertUserWithDisabledProfiles(utente, utente.getPersona()))
                                  .collect(Collectors.toList());

    }

}
