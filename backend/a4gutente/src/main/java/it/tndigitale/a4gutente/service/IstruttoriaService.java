package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.dto.Responsabilita.TITOLARE_AZIENDA_AGRICOLA;
import static it.tndigitale.a4gutente.dto.Responsabilita.DIPENDENTE_DISTRIBUTORE;
import static it.tndigitale.a4gutente.service.builder.IstruttoriaBuilder.from;
import static it.tndigitale.a4gutente.service.builder.ProfiloBuilder.isPresent;
import static it.tndigitale.a4gutente.service.support.IstruttoriaSupport.existProfiloWithResponsabilita;
import static it.tndigitale.a4gutente.service.support.IstruttoriaSupport.isUpdatableOrThrow;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4gutente.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IEnteDao;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.dao.IProfiloDao;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.service.loader.EnteLoader;
import it.tndigitale.a4gutente.service.loader.IstruttoriaLoader;
import it.tndigitale.a4gutente.service.loader.ProfiloLoader;
import it.tndigitale.a4gutente.service.loader.UtenteLoader;
import it.tndigitale.a4gutente.service.support.IstruttoriaSupport;
import it.tndigitale.a4gutente.service.support.UtenteSupport;

@Service
public class IstruttoriaService {
	
	private static final Logger logger = LoggerFactory.getLogger(IstruttoriaService.class);

    @Autowired
    private IIstruttoriaDao istruttoriaDao;
    @Autowired
    private IstruttoriaLoader istruttoriaLoader;
    @Autowired
    private IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao;
    @Autowired
    private IUtenteService utenteService;
    @Autowired
    private IstruttoriaSupport istruttoriaSupport;
    @Autowired
    private UtenteLoader utenteLoader;
    @Autowired
    private EnteLoader enteLoader;
    @Autowired
    private ProfiloLoader profiloLoader;
    @Autowired
    private UtenteSupport utenteSupport;
    @Autowired
    private Clock clock;
    @Autowired
	private IProfiloDao profiloRepository;
    @Autowired
    private IEnteDao enteRepository;

    public IstruttoriaService setComponents(IIstruttoriaDao istruttoriaDao,
                                            IstruttoriaLoader istruttoriaLoader,
                                            IDomandaRegistrazioneUtenteDao domandaRegistrazioneUtenteDao,
                                            IUtenteService utenteService,
                                            IstruttoriaSupport istruttoriaSupport,
                                            UtenteLoader utenteLoader,
                                            EnteLoader enteLoader,
                                            ProfiloLoader profiloLoader,
                                            UtenteSupport utenteSupport,
                                            Clock clock) {
        this.istruttoriaDao = istruttoriaDao;
        this.istruttoriaLoader = istruttoriaLoader;
        this.domandaRegistrazioneUtenteDao = domandaRegistrazioneUtenteDao;
        this.utenteService = utenteService;
        this.istruttoriaSupport = istruttoriaSupport;
        this.utenteLoader = utenteLoader;
        this.enteLoader = enteLoader;
        this.profiloLoader = profiloLoader;
        this.utenteSupport = utenteSupport;
        this.clock = clock;
        return this;
    }

    public Istruttoria findByIdDomanda(Long idDomanda) throws Exception {
        IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadByIdDomanda(idDomanda);
        return from(istruttoriaEntita);
    }

    @Transactional
    public Long crea(Istruttoria istruttoria) {
        IstruttoriaEntita entita = istruttoriaLoader.loadForCreate(istruttoria);
        isUpdatableOrThrow(entita);
        IstruttoriaEntita result = istruttoriaDao.save(entita);
        domandaRegistrazioneUtenteDao.save(result.getDomanda().setConfigurato(Boolean.TRUE));
        return result.getId();
    }

    @Transactional
    public Long aggiorna(Istruttoria istruttoria) {
        IstruttoriaEntita entita = istruttoriaLoader.loadForUpdate(istruttoria);
        isUpdatableOrThrow(entita);
        final IstruttoriaEntita result = istruttoriaDao.save(entita);
        return result.getId();
    }

    @Transactional
    public IstruttoriaEntita aggiornaIstruttoriaPerApprovazioneDomanda(RichiestaDomandaApprovazione richiesta) {
        IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadByIdDomanda(richiesta.getIdDomanda());
        setIstruttoriaEntita(richiesta, istruttoriaEntita);
        IstruttoriaEntita istruttoriaSalvata = istruttoriaDao.save(istruttoriaEntita);
        return istruttoriaSalvata;
    }
    
    @Transactional
    public IstruttoriaEntita aggiornaIstruttoriaPerApprovazioneDomandaRidottaAzienda(RichiestaDomandaApprovazione richiesta) throws Exception {
    	IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadByIdDomanda(richiesta.getIdDomanda());
    	setIstruttoriaEntita(richiesta, istruttoriaEntita);
    	istruttoriaEntita.setProfili(aggiungiProfiliEsistenti(istruttoriaEntita));
    	istruttoriaEntita.setEnti(aggiungiEntiEsistenti(istruttoriaEntita));
        IstruttoriaEntita istruttoriaSalvata = istruttoriaDao.save(istruttoriaEntita);
        return istruttoriaSalvata;
    }

    @Transactional
    public IstruttoriaEntita aggiornaIstruttoriaPerRifiutoDomanda(RichiestaDomandaRifiuta richiesta) {
        IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadByIdOrCreate(richiesta.getIdDomanda());
        istruttoriaEntita.setMotivazioneRifiuto(richiesta.getMotivazioneRifiuto());
        istruttoriaEntita.setNote(richiesta.getNote());
        istruttoriaEntita.setTestoMailInviata(richiesta.getTestoMail());
        istruttoriaEntita.preparaPerCambioStato(clock.now(), utenteLoader.loadUtenteConnesso());
        IstruttoriaEntita istruttoriaSalvata = istruttoriaDao.save(istruttoriaEntita);
        return istruttoriaSalvata;
    }

    @Transactional
    public A4gtUtente creaOAggiornaUtenzaPerApprovazioneDomanda(IstruttoriaEntita istruttoriaEntita) throws Exception {
        A4gtUtente a4gtUtente = utenteService.createOrGetUtente(istruttoriaEntita.getDomanda());
        utenteService.addEnti(a4gtUtente, istruttoriaEntita.getEnti());
        utenteService.addProfili(a4gtUtente, istruttoriaEntita.getProfili());
        if (existProfiloWithResponsabilita(istruttoriaEntita.getProfili(), TITOLARE_AZIENDA_AGRICOLA).isPresent()) {
            List<TitolareImpresa> aziende =  istruttoriaSupport.extraiTitolatiImpresaDaDomandaIstruttoria(istruttoriaEntita.getDomanda().getId());
            utenteService.addAziende(a4gtUtente, aziende);
        }
        if (existProfiloWithResponsabilita(istruttoriaEntita.getProfili(), DIPENDENTE_DISTRIBUTORE).isPresent()) {
            List<ResponsabilitaRichieste.RuoloDistributore> ruoliDistributore =  istruttoriaSupport.estraiDistributoriDaDomandaIstruttoria(istruttoriaEntita.getDomanda().getId());
            utenteService.addDistributori(a4gtUtente, ruoliDistributore);
        }
        return a4gtUtente;
    }

    @Transactional
    public IstruttoriaEntita aggiungiUtenteAIstruttoria(A4gtUtente utente, IstruttoriaEntita istruttoria) {
        istruttoria.setUtente(utente);
        final IstruttoriaEntita result = istruttoriaDao.save(istruttoria);
        return result;
    }

    @Transactional
    public Long creaSenzaDomanda(IstruttoriaSenzaDomanda istruttoriaSenzaDomanda) {
        istruttoriaSenzaDomanda.valida();
        A4gtUtente utente = utenteLoader.load(istruttoriaSenzaDomanda.getIdUtente());
        IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadForCreateSenzaDomanda(istruttoriaSenzaDomanda,
                                                                                          utente);
        final IstruttoriaEntita istruttoriaSalvata = istruttoriaDao.save(istruttoriaEntita);

        utenteService.addEnti(utente, enteLoader.load(istruttoriaSenzaDomanda.getSedi()));
        List<A4gtProfilo> profiliEntita = profiloLoader.load(istruttoriaSenzaDomanda.getProfili());
        utenteService.addProfili(utente, profiliEntita);
        if (!isPresent(profiliEntita, TITOLARE_AZIENDA_AGRICOLA)) {
            utenteService.removeAziende(utente);
        }

        return istruttoriaSalvata.getId();
    }

	public StoricoIstruttorie listaStorico(Long idUtente) {
        Utente utente = utenteSupport.caricaUtenteConAnagrafica(idUtente);
        List<IstruttoriaPerStorico> istruttorie = istruttoriaSupport.caricaIstruttorieXStorico(idUtente);
        return new StoricoIstruttorie().setUtente(utente)
                                       .setIstruttorie(istruttorie);
	}
	
	private IstruttoriaEntita setIstruttoriaEntita(RichiestaDomandaApprovazione richiesta, IstruttoriaEntita istruttoriaEntita) {
        istruttoriaEntita.setNote(richiesta.getNote());
        istruttoriaEntita.setTestoMailInviata(richiesta.getTestoMail());
        istruttoriaEntita.preparaPerCambioStato(clock.now(), utenteLoader.loadUtenteConnesso());
        return istruttoriaEntita;
	}
	
	private List<A4gtProfilo> aggiungiProfiliEsistenti(IstruttoriaEntita istruttoriaEntita) throws Exception {
		List<A4gtProfilo> profili = istruttoriaEntita.getProfili();
		List<A4gtProfilo> profiliEntita = profiloRepository.findAll();

		Utente utente = new Utente();
		try {
			utente = utenteService.carica(istruttoriaEntita.getDomanda().getIdentificativoUtente());
		} catch (EntityNotFoundException enf) {
			logger.error("aggiungiProfiliEsistenti:EntityNotFoundException Utente per istruttoria " + istruttoriaEntita.getId());
			return profili;
		}

		if (!utente.getProfili().isEmpty()) {
			for (Profilo profiloUtente: utente.getProfili()) {
				if (profiloUtente.getResponsabilita() != Responsabilita.TITOLARE_AZIENDA_AGRICOLA) {
					A4gtProfilo profiloAdd = profiliEntita.stream()
							.filter(l -> l.getIdentificativo().equals(profiloUtente.getIdentificativo()))
							.findFirst()
							.get();
					profili.add(profiloAdd);
				}
			}
		}
		return profili;
	}
	
	private List<A4gtEnte> aggiungiEntiEsistenti(IstruttoriaEntita istruttoriaEntita) throws Exception {
		List<A4gtEnte> enti = istruttoriaEntita.getEnti();
		List<A4gtEnte> entiEntita = enteRepository.findAll();
		
		Utente utente = new Utente();
		try {
			utente = utenteService.carica(istruttoriaEntita.getDomanda().getIdentificativoUtente());
		} catch (EntityNotFoundException enf) {
			logger.error("aggiungiEntiEsistenti:EntityNotFoundException Utente per istruttoria " + istruttoriaEntita.getId());
			return enti;
		}
		
    	if (!utente.getSedi().isEmpty()) {
	        for (EnteSede sedeUtente: utente.getSedi()) {
	        		A4gtEnte enteAdd = entiEntita.stream()
	    	        	    .filter(l -> l.getIdentificativo().equals(sedeUtente.getIdentificativo()))
	    	        	    .findFirst()
	    	        	    .get();
	        		enti.add(enteAdd); 	
			}
    	}
    	return enti;
	}

}
