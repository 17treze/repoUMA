package it.tndigitale.a4gutente.service.support;

import static it.tndigitale.a4gutente.service.support.WorkflowDomandaRegistrazioneUtente.checkCambioStato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.CounterStato;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloCAA;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IEnteDao;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.specification.DomandaRegistrazioneUtenteSpecificationBuilder;
import it.tndigitale.a4gutente.service.loader.DomandaLoader;

@Component
public class DomandaRegistrazioneUtenteSupport {

    private DomandaLoader domandaLoader;
    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    private IEnteDao enteDao;
    private Clock clock;

    @Autowired
    public DomandaRegistrazioneUtenteSupport setComponents(
    		DomandaLoader domandaLoader,
    		IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao,
    		IEnteDao enteDao,
    		Clock clock) {
        this.domandaLoader = domandaLoader;
        this.domandaRegistrazioneUtenteDao = domandaRegistrazioneUtenteDao;
        this.enteDao = enteDao;
        this.clock = clock;
        return this;
    }

    @Transactional
    public DomandaRegistrazioneUtente cambiaStato(Long idDomanda, StatoDomandaRegistrazioneUtente newState) throws Exception {
        DomandaRegistrazioneUtente domandaRegistrazioneUtente = domandaLoader.load(idDomanda);
        checkCambioStato(domandaRegistrazioneUtente, newState);
        domandaRegistrazioneUtente.preparaPerCambioStato(newState);
        final DomandaRegistrazioneUtente domandaSalvata = domandaRegistrazioneUtenteDao.save(domandaRegistrazioneUtente);
        return domandaSalvata;
    }

    @Transactional
    public void caricaIdentificativiSediDeiRuoliCaa(List<RuoloCAA> ruoli) {
        Optional.ofNullable(ruoli)
                .orElse(new ArrayList<>())
                .forEach(ruolo -> caricaIdentificativiDelleSediDelRuoloCaa(ruolo));

    }

    @Transactional
    public void caricaIdentificativiDelleSediDelRuoloCaa(RuoloCAA ruolo) {
        Optional.ofNullable(ruolo.getSedi())
                .orElse(new ArrayList<>())
                .forEach(sede -> {
                    enteDao.findByIdentificativo(sede.getIdentificativo())
                            .ifPresent(ente -> {
                                sede.setId(ente.getId());
                            });
                });
    }

    @Transactional
    public CounterStato counterForStatoBy(StatoDomandaRegistrazioneUtente stato,
                                          DomandaRegistrazioneUtenteFilter filtri) {
        filtri.setStato(stato);
        Specification<DomandaRegistrazioneUtente> specification =
                DomandaRegistrazioneUtenteSpecificationBuilder.getFilterForCount(filtri);
        Long count = domandaRegistrazioneUtenteDao.count(specification);
        return new CounterStato().setCount(count)
                                 .setStato(stato);
    }
}
