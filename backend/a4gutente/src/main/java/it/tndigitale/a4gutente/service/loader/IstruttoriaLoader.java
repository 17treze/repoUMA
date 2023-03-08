package it.tndigitale.a4gutente.service.loader;

import static it.tndigitale.a4gutente.service.builder.IstruttoriaBuilder.convert;
import static it.tndigitale.a4gutente.utility.ListSupport.getFirstElementOf;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.dto.IstruttoriaSenzaDomanda;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;

@Component
public class IstruttoriaLoader extends AbstractLoader {

    @Autowired
    private DomandaLoader domandaLoader;
    @Autowired
    private EnteLoader enteLoader;
    @Autowired
    private ProfiloLoader profiloLoader;
    @Autowired
    private IIstruttoriaDao istruttoriaDao;
    @Autowired
    private UtenteLoader utenteLoader;
    @Autowired
    private Clock clock;

    public IstruttoriaLoader setComponents(DomandaLoader domandaLoader,
                                           EnteLoader enteLoader,
                                           ProfiloLoader profiloLoader,
                                           IIstruttoriaDao istruttoriaDao,
                                           UtenteLoader utenteLoader,
                                           Clock clock) {
        this.domandaLoader = domandaLoader;
        this.enteLoader = enteLoader;
        this.profiloLoader = profiloLoader;
        this.istruttoriaDao = istruttoriaDao;
        this.utenteLoader = utenteLoader;
        this.clock = clock;
        return this;
    }

    public IstruttoriaEntita load(Long idIstruttoria) {
        checkId(idIstruttoria);
        return istruttoriaDao.findById(idIstruttoria)
                .orElseThrow(() -> new ValidationException("Istruttoria con identificativo " + idIstruttoria + " non trovato"));
    }

    public IstruttoriaEntita loadLastIstruttoriaByIdentificativoUtente(String identificativoUtente) {
        List<IstruttoriaEntita> istruttorie = istruttoriaDao.findSenzaDomandaXIdentificativoUtente(identificativoUtente);
        return getFirstElementOf(istruttorie);
    }

    public IstruttoriaEntita loadForCreate(Istruttoria istruttoria) {
        verificaNonEsistenzaIstruttoriaPerDomanda(istruttoria.getIdDomanda());
        IstruttoriaEntita entita = new IstruttoriaEntita();
        entita.setDomanda(domandaLoader.load(istruttoria.getIdDomanda()));
        entita.setEnti(enteLoader.load(istruttoria.getSedi()));
        entita.setProfili(profiloLoader.load(istruttoria.getProfili()));
        convert(istruttoria, entita);
        return entita;
    }

    public IstruttoriaEntita loadForUpdate(Istruttoria istruttoria) {
        IstruttoriaEntita entita = this.load(istruttoria.getId());
        checkDomandaForUpdate(istruttoria.getIdDomanda(), entita.getDomanda().getId());
        entita.setEnti(enteLoader.load(istruttoria.getSedi()));
        entita.setProfili(profiloLoader.load(istruttoria.getProfili()));
        convert(istruttoria, entita);
        return entita;
    }

    public IstruttoriaEntita loadForCreateSenzaDomanda(IstruttoriaSenzaDomanda istruttoriaSenzaDomanda,
                                                       A4gtUtente utente) {
        IstruttoriaEntita entita = new IstruttoriaEntita();
        entita.setEnti(enteLoader.load(istruttoriaSenzaDomanda.getSedi()));
        entita.setProfili(profiloLoader.load(istruttoriaSenzaDomanda.getProfili()));
        entita.setProfiliDisabilitati(profiloLoader.load(istruttoriaSenzaDomanda.getProfiliDaDisattivare()));
        entita.setUtente(utente);
        entita.setIstruttore(utenteLoader.loadUtenteConnesso());
        entita.setDataTermineIstruttoria(clock.now());
        entita.setVariazioneRichiesta(istruttoriaSenzaDomanda.getVariazioneRichiesta());
        entita.setMotivazioneDisattivazione(istruttoriaSenzaDomanda.getMotivazioneDisattivazione());
        return entita;
    }

    public IstruttoriaEntita loadByIdDomanda(Long idDomanda) {
        return istruttoriaDao.findByIdDomanda(idDomanda)
                             .orElseThrow(() -> new EntityNotFoundException("Istruttoria con identificativo " + idDomanda + " non presente"));

    }

    public IstruttoriaEntita loadByIdOrCreate(Long idDomanda) {
        return istruttoriaDao.findByIdDomanda(idDomanda)
                             .orElse(new IstruttoriaEntita().setDomanda(domandaLoader.load(idDomanda)));
    }






    private void verificaNonEsistenzaIstruttoriaPerDomanda(Long idDomanda) {
        IstruttoriaEntita istruttoriaEntita = istruttoriaDao.findByIdDomanda(idDomanda)
                                                            .orElse(null);
        if (istruttoriaEntita != null) {
            throw new ValidationException("Esiste già una istruttoria correlata alla domanda con identificativo " + idDomanda);
        }
    }

    private void checkDomandaForUpdate(Long idDomandaDto, Long idDomandaEntita) {
        if (!idDomandaEntita.equals(idDomandaDto)) {
            throw new ValidationException("L'identificativo della domanda associata all'istruttoria è differente rispetto" +
                    "a quello presente nel database");
        }
    }

}
