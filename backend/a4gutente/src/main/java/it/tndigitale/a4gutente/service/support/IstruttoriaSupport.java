package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.dto.*;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloDistributore;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.utility.ObjectMapperUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE;
import static it.tndigitale.a4gutente.service.builder.AziendaBuilder.fromTitolariImpresa;
import static it.tndigitale.a4gutente.service.builder.IstruttoriaPerStoricoBuilder.from;
import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;

@Component
public class IstruttoriaSupport {

    @Autowired
    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    @Autowired
    private IIstruttoriaDao istruttoriaDao;
    @Autowired
    private UtenteSupport utenteSupport;

    public IstruttoriaSupport setComponents(IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao,
                                            IIstruttoriaDao istruttoriaDao,
                                            UtenteSupport utenteSupport) {
        this.domandaRegistrazioneUtenteDao = domandaRegistrazioneUtenteDao;
        this.istruttoriaDao = istruttoriaDao;
        this.utenteSupport = utenteSupport;
        return this;
    }

    public List<TitolareImpresa> extraiTitolatiImpresaDaDomandaIstruttoria(Long idDomanda) throws Exception {
        DomandaRegistrazioneUtente domanda = domandaRegistrazioneUtenteDao.getOne(idDomanda);
        ResponsabilitaRichieste responsabilitaRichieste = extraiResponsabilitaDaDomandaIstruttoria(domanda);
        return getTitolariImpresaFrom(responsabilitaRichieste);
    }

    public List<RuoloDistributore> estraiDistributoriDaDomandaIstruttoria(Long idDomanda) throws Exception {
        DomandaRegistrazioneUtente domanda = domandaRegistrazioneUtenteDao.getOne(idDomanda);
        ResponsabilitaRichieste responsabilitaRichieste = extraiResponsabilitaDaDomandaIstruttoria(domanda);
        if (responsabilitaRichieste == null) {
            return Collections.emptyList();
        }
        List<RuoloDistributore> ruoliDistributore = Optional.ofNullable(responsabilitaRichieste.getResponsabilitaDistributore())
                .orElse(new ArrayList<>());
        return ruoliDistributore;
    }

    public static Optional<A4gtProfilo> existProfiloWithResponsabilita(List<A4gtProfilo> profili, Responsabilita responsabilita) {
        return Optional.ofNullable(profili)
                       .orElse(Collections.emptyList())
                       .stream()
                       .filter(p -> responsabilita.equals(p.getResponsabilita()))
                       .findFirst();
    }

    public List<IstruttoriaPerStorico> caricaIstruttorieXStorico(Long idUtente) {
        List<IstruttoriaEntita> istruttorieUtente = istruttoriaDao.findIstruttorieByIdUtente(idUtente);
        return emptyIfNull(istruttorieUtente)
                .stream()
                .map(istruttoria -> from(istruttoria, caricaUtente(istruttoria), this))
                .collect(Collectors.toList());
    }






    public static void isUpdatableOrThrow(IstruttoriaEntita entita) {
        if (isNotUpdatable(entita)) {
            throw new ValidationException("Impossibile creare o aggiornare l'istruttoria. Domanda non è in stato di lavorazione");
        }
    }

    public static boolean isNotUpdatable(IstruttoriaEntita entita) {
        return entita.getDomanda()!=null &&
               !IN_LAVORAZIONE.equals(entita.getDomanda().getStato());
    }

    public static List<Azienda> estraiAziendeDaDomandaIstruttoria(DomandaRegistrazioneUtente domanda) {
        ResponsabilitaRichieste responsabilitaRichieste = extraiResponsabilitaDaDomandaIstruttoria(domanda);
        List<TitolareImpresa> titolariImpresa = getTitolariImpresaFrom(responsabilitaRichieste);
        return fromTitolariImpresa(titolariImpresa);
    }

    public static List<TitolareImpresa> getTitolariImpresaFrom(ResponsabilitaRichieste responsabilitaRichieste) {
        if (responsabilitaRichieste == null) {
            return Collections.emptyList();
        }
        List<TitolareImpresa> legaleRapp = Optional.ofNullable(responsabilitaRichieste.getResponsabilitaLegaleRappresentante())
                .orElse(new ArrayList<>());
        List<TitolareImpresa> titolareIm = Optional.ofNullable(responsabilitaRichieste.getResponsabilitaTitolare())
                .orElse(new ArrayList<>());
        List<TitolareImpresa> all = legaleRapp;
        all.addAll(titolareIm);
        return all;
    }





    private Utente caricaUtente(IstruttoriaEntita istruttoriaEntita) {
        if (istruttoriaEntita.getIstruttore() == null) {
            return null;
        }
        return utenteSupport.caricaUtenteConAnagrafica(istruttoriaEntita.getIstruttore().getId());
    }

    private static ResponsabilitaRichieste extraiResponsabilitaDaDomandaIstruttoria(DomandaRegistrazioneUtente domanda) {
        if (domanda == null || domanda.getResponsabilita() == null) {
            return null;
        }
        return ObjectMapperUtility.readValue(domanda.getResponsabilita(), ResponsabilitaRichieste.class);
    }


}

