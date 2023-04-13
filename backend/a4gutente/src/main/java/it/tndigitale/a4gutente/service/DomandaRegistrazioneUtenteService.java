package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.PROTOCOLLATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.RIFIUTATA;
import static it.tndigitale.a4gutente.service.support.StatoDomandaSupport.riempiConStatiMancanti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import it.tndigitale.a4gutente.dto.Distributore;
import it.tndigitale.a4gutente.repository.dao.IDistributoreDao;
import it.tndigitale.a4gutente.repository.model.A4gtDistributore;
import it.tndigitale.a4gutente.service.builder.DistributoreBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gutente.codici.CodResponsabilita;
import it.tndigitale.a4gutente.codici.Dipartimento;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.component.StampaComponent;
import it.tndigitale.a4gutente.dto.CounterStato;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente.ServiziType;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneCreataResponse;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteDto;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.EnteCAA;
import it.tndigitale.a4gutente.dto.Firma;
import it.tndigitale.a4gutente.dto.Persona;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.CollaboratoreAltriEnti;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.Consulente;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloCAA;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloPAT;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloDistributore;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.event.DomandaRegistrazioneUtenteEvent;
import it.tndigitale.a4gutente.exception.UtenteException;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.dao.IEnteDao;
import it.tndigitale.a4gutente.repository.dao.IFirmaDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.model.A4gtAllegatoResponsabilita;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.FirmaDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.repository.specification.DomandaRegistrazioneUtenteSpecificationBuilder;
import it.tndigitale.a4gutente.service.builder.DatiDomandaRegistrazioneUtenteBuilder;
import it.tndigitale.a4gutente.service.builder.EnteBuilder;
import it.tndigitale.a4gutente.service.support.DomandaRegistrazioneUtenteSupport;
import it.tndigitale.a4gutente.utility.EmailSupport;

@Service
public class DomandaRegistrazioneUtenteService implements IDomandaRegistrazioneService {

	private static final Logger logger = LoggerFactory.getLogger(DomandaRegistrazioneUtenteService.class);

	@Autowired
	private IDomandaRegistrazioneUtenteDao domandaUtenteRep;
	@Autowired
	private IFirmaDomandaRegistrazioneUtenteDao firmaDomandaRep;
	@Autowired
	private StampaComponent stampaComponent;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private IEnteDao enteoDao;
	@Autowired
	private IDistributoreDao distributoreDao;
	@Autowired
	private FirmaService firmaService;
	@Autowired
	private EmailSupport emailSupport;
	@Autowired
	private DomandaRegistrazioneUtenteSupport domandaRegistrazioneUtenteSupport;
	@Value("${a4gutente.integrazioni.protocollo.tipologiaDocumento.codice}")
	private String tipologiaDocumentoProtocollazione;
	@Autowired
	private IstruttoriaService istruttoriaService;
	@PersistenceContext
	protected EntityManager entityManager;
	@Value("${a4gutente.integrazioni.uri}")
	private String a4gproxyUri;
	@Value("${a4gutente.integrazioni.protocollo.path}")
	private String protocolloUriPath;
	@Autowired
	private IPersonaService personaService;
	@Autowired
	private IDomandaRegistrazioneService domandaService;
	
	private EventBus eventBus;
	
//	utilizzo di EventBus (framework) per l'invio di eventi
	@Autowired
    public DomandaRegistrazioneUtenteService setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

	public DomandaRegistrazioneUtenteService setComponents(DomandaRegistrazioneUtenteSupport domandaRegistrazioneUtenteSupport,
														   EmailSupport emailSupport,
														   IstruttoriaService istruttoriaService,
														   EntityManager entityManager) {
		this.domandaRegistrazioneUtenteSupport = domandaRegistrazioneUtenteSupport;
		this.emailSupport = emailSupport;
		this.istruttoriaService = istruttoriaService;
		this.entityManager = entityManager;
		return this;
	}

	@Override
	@Transactional
	public Long registraDomanda(DatiDomandaRegistrazioneUtente datiDomanda) throws Exception {
		if (!isUtenteRegistrabile()) {
			throw new UtenteException("Per l'utente esiste gia' una domanda, non e' possibile registrarne una seconda");
		}
		cancellaDomandeInCompilazione();
		DomandaRegistrazioneUtente model = new DomandaRegistrazioneUtente();
		convertiInEntity(datiDomanda, model);
		initStatoDomanda(model);
		initUtenteDomanda(model);
		model = domandaUtenteRep.save(model);

		return model.getId();
	}

	@Transactional
	public Long aggiornaDomanda(DatiDomandaRegistrazioneUtente datiDomanda, Integer versione) throws Exception {
		if (esisteDomandaProtocollata()) {
			throw new UtenteException(
					"Per l'utente esiste gia' una domanda in stato protocollata, non e' possibile aggiornare");
		}
		Long id = datiDomanda.getId();
		FirmaDomandaRegistrazioneUtente firma;
		try {
			firma = firmaDomandaRep.findOneByIdDomanda(id);
		} catch (NoResultException | NonUniqueResultException pe) {
			firma = null;
		}
		if (firma != null) {
			firmaDomandaRep.delete(firma);
		}
		DomandaRegistrazioneUtente model = domandaUtenteRep.getOne(id);
		if (!isDomandaUtente(model)) {
			logger.error("aggiornaDomanda: l'utente {} sta cercando di modificare la domanda {} non sua", utenteComponent.utenza(), id);
			throw new UtenteException(
					"Non e' possibile aggiornare una domanda non di propria competenza");
		}
		convertiInEntity(datiDomanda, model);
		initStatoDomanda(model);
		initUtenteDomanda(model);
		model.setDocumento(null);
		model.setVersion(versione);
		model = domandaUtenteRep.cleanSave(model);

		return model.getId();
		
	}
	
	@Override
	@Transactional
	public Long aggiornaDomanda(DatiDomandaRegistrazioneUtente datiDomanda) throws Exception {
		if (esisteDomandaProtocollata()) {
			throw new UtenteException(
					"Per l'utente esiste gia' una domanda in stato protocollata, non e' possibile aggiornare");
		}
		Long id = datiDomanda.getId();
		FirmaDomandaRegistrazioneUtente firma;
		try {
			firma = firmaDomandaRep.findOneByIdDomanda(id);
		} catch (NoResultException | NonUniqueResultException pe) {
			firma = null;
		}
		if (firma != null) {
			firmaDomandaRep.delete(firma);
		}
		DomandaRegistrazioneUtente model = domandaUtenteRep.getOne(id);
		if (!isDomandaUtente(model)) {
			logger.error("aggiornaDomanda: l'utente {} sta cercando di modificare la domanda {} non sua", utenteComponent.utenza(), id);
			throw new UtenteException(
					"Non e' possibile aggiornare una domanda non di propria competenza");
		}
		convertiInEntity(datiDomanda, model);
		initStatoDomanda(model);
		initUtenteDomanda(model);
		model.setDocumento(null);
		model = domandaUtenteRep.save(model);

		return model.getId();
	}

	public void cancellaDomandeInCompilazione() throws Exception {
		List<DomandaRegistrazioneUtente> domandeUtenteInCompilazione = domandeUtente(StatoDomandaRegistrazioneUtente.IN_COMPILAZIONE);
		for (DomandaRegistrazioneUtente domanda : domandeUtenteInCompilazione) {
			FirmaDomandaRegistrazioneUtente firma = 
					firmaDomandaRep.findOneByIdDomanda(domanda.getId());
			if (firma != null) {
				firmaDomandaRep.delete(firma);
			}
			domandaUtenteRep.delete(domanda);
		}
	}
	
	@Override
	@Transactional
	public DatiDomandaRegistrazioneUtente getDomanda(Long id) throws Exception {
		DomandaRegistrazioneUtente domandaIn = domandaUtenteRep.findById(id).get();
		// non recupero allegati
		return convertToDatiDomanda(domandaIn);
	}

	protected DatiDomandaRegistrazioneUtente convertToDatiDomanda(DomandaRegistrazioneUtente domandaIn) throws Exception{
		DatiDomandaRegistrazioneUtente domandaOut = new DatiDomandaRegistrazioneUtente();
		convertiInDto(domandaIn, domandaOut);
		return domandaOut;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isUtenteRegistrabile() throws Exception {
		return !esisteDomandaProtocollata() && !esisteDomandaInLavorazione();
	}

	protected boolean esisteDomandaProtocollata() throws Exception {
		List<DomandaRegistrazioneUtente> domandeUtenteProcollate = domandeUtente(PROTOCOLLATA);
		return (domandeUtenteProcollate != null && !domandeUtenteProcollate.isEmpty());
	}

	protected boolean esisteDomandaInLavorazione() throws Exception {
		List<DomandaRegistrazioneUtente> domandeUtenteProcollate = domandeUtente(StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE);
		return (domandeUtenteProcollate != null && !domandeUtenteProcollate.isEmpty());
	}
	
	protected boolean isDomandaUtente(DomandaRegistrazioneUtente model) {
		return utenteComponent.utenza().equals(model.getIdentificativoUtente());
	}
	

	protected boolean esisteDomanda() throws Exception {
		List<DomandaRegistrazioneUtente> domandeUtente = domandeUtente(null);
		return (domandeUtente != null && !domandeUtente.isEmpty());
	}

	/**
	 * per l'utente corrente restituisce l'ultima domanda in base alla data di protocollazione (escludendo quindi date null)
	 */
	@Transactional(readOnly = true)
	public DatiDomandaRegistrazioneUtente ultimaDomandaUtenteCorrentePerDataProtocollazione(StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente, TipoDomandaRegistrazione tipoDomandaRegistrazione) throws Exception {
		List<DomandaRegistrazioneUtente> domandeRegistrazione = domandeUtente(utenteComponent.utenza(), statoDomandaRegistrazioneUtente, tipoDomandaRegistrazione);
		if(domandeRegistrazione != null && domandeRegistrazione.size() > 0) {
//			in teoria con APPROVATA ci deve sempre essere numero e data di protocollazione. Per sicurezza escludo comunque oggetti con dataProtocollazione == null
			domandeRegistrazione.removeIf(domanda -> domanda.getDtProtocollazione() == null);
//			ordino la lista scremata per data protocollazione decrescente
			domandeRegistrazione.sort(Comparator.comparing(DomandaRegistrazioneUtente::getDtProtocollazione).reversed());
			if(domandeRegistrazione.size() > 0) {
				return convertToDatiDomanda(domandeRegistrazione.get(0));	
			}else {
				return null;		
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<DomandaRegistrazioneUtente> domandeUtente(String identificativoUtente, StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente, TipoDomandaRegistrazione tipoDomandaRegistrazione) throws Exception {
		if(tipoDomandaRegistrazione == null) {
			if (statoDomandaRegistrazioneUtente == null) {
				return domandaUtenteRep.findByIdentificativoUtente(identificativoUtente);
			} else {
				return domandaUtenteRep.findByIdentificativoUtenteAndStato(identificativoUtente, statoDomandaRegistrazioneUtente);
			}
		} else {
			if (statoDomandaRegistrazioneUtente == null) {
				return domandaUtenteRep.findByIdentificativoUtenteAndTipoDomandaRegistrazione(identificativoUtente, tipoDomandaRegistrazione);
			} else {
				return domandaUtenteRep.findByIdentificativoUtenteAndStatoAndTipoDomandaRegistrazione(identificativoUtente, statoDomandaRegistrazioneUtente, tipoDomandaRegistrazione);
			}
		}
	}
	
	@Transactional(readOnly = true)
	public List<DomandaRegistrazioneUtente> domandeUtente(StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente) throws Exception {
		return domandeUtente(utenteComponent.utenza(), statoDomandaRegistrazioneUtente, null);
//		return domandeUtente(utenteComponent.utenza(), statoDomandaRegistrazioneUtente);
	}

//  TODO sostituito dal piu generico  domandeUtente(String identificativoUtente, StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente, TipoDomandaRegistrazione tipoDomandaRegistrazione)
//	@Transactional(readOnly = true)
//	public List<DomandaRegistrazioneUtente> domandeUtente(String identificativoUtente, StatoDomandaRegistrazioneUtente statoDomandaRegistrazioneUtente) throws Exception {
//		if (statoDomandaRegistrazioneUtente == null) {
//			return domandaUtenteRep.findByIdentificativoUtente(identificativoUtente);
//		} else {
//			return domandaUtenteRep.findByIdentificativoUtenteAndStato(identificativoUtente, statoDomandaRegistrazioneUtente);
//		}
//	}
	
//	TODO commentato perche' sembra inutilizzata
//	@Transactional(readOnly = true)
//	public DatiDomandaRegistrazioneUtente domandaUtenteProtocollata(String identificativoUtente) throws Exception {
//		List<DomandaRegistrazioneUtente> domande = domandeUtente(identificativoUtente, PROTOCOLLATA);
//		if (domande == null || domande.isEmpty()) {
//			return null;
//		}
//		return convertToDatiDomanda(domande.get(0));
//		
//	}
	
	public List<DatiDomandaRegistrazioneUtente> ricerca(DomandaRegistrazioneUtenteFilter criteri) throws Exception {
		List<DomandaRegistrazioneUtente> domande = domandaUtenteRep.findAll(DomandaRegistrazioneUtenteSpecificationBuilder.getFilter(criteri));
		if (domande == null) {
			return null;
		}
		List<DatiDomandaRegistrazioneUtente> result = new ArrayList<DatiDomandaRegistrazioneUtente>();
		for (DomandaRegistrazioneUtente domanda : domande) {
			result.add(convertToDatiDomanda(domanda));
		}
		
		return result;
	}
	
	
	protected void convertiInEntity(DatiDomandaRegistrazioneUtente datiDomanda, DomandaRegistrazioneUtente model) throws Exception {
		// BeanUtils.copyProperties(datiDomanda.getDatiAnagrafici(), model);
		BeanUtils.copyProperties(datiDomanda, model);

		Set<A4gtAllegatoResponsabilita> allegati = gestisciAllegati(datiDomanda, model);

		if (datiDomanda.getResponsabilitaRichieste() != null) {
			String responsabilitaJS = objectMapper.writeValueAsString(datiDomanda.getResponsabilitaRichieste());
			model.setResponsabilita(responsabilitaJS);
		}
		Set<ServiziType> servizi = datiDomanda.getServizi();
		if (servizi != null) {
			model.setA4g(datiDomanda.getServizi().contains(ServiziType.A4G));
			model.setAgs(datiDomanda.getServizi().contains(ServiziType.AGS));
			model.setSrt(datiDomanda.getServizi().contains(ServiziType.SRT));
		} else {
			model.setA4g(false);
			model.setAgs(false);
			model.setSrt(false);
		}

		model.setA4gtAllegatoResponsabilita(allegati);
	}


	protected void convertiInDto(DomandaRegistrazioneUtente model, DatiDomandaRegistrazioneUtente datiDomanda) throws Exception {
		
		// Explicitly ignoring dataProtocollazione property: manually taking care of that
		BeanUtils.copyProperties(model, datiDomanda, "dataProtocollazione");

		datiDomanda.setResponsabilitaRichieste(objectMapper.readValue(model.getResponsabilita(), ResponsabilitaRichieste.class));

		if (datiDomanda.getResponsabilitaRichieste() != null)
			domandaRegistrazioneUtenteSupport
					.caricaIdentificativiSediDeiRuoliCaa(datiDomanda.getResponsabilitaRichieste().getResponsabilitaCaa());

		Set<ServiziType> servizi = new HashSet<ServiziType>();
		if (!Boolean.FALSE.equals(model.getA4g())) {
			servizi.add(ServiziType.A4G);
		}
		if (!Boolean.FALSE.equals(model.getAgs())) {
			servizi.add(ServiziType.AGS);
		}
		if (!Boolean.FALSE.equals(model.getSrt())) {
			servizi.add(ServiziType.SRT);
		}

		datiDomanda.setServizi(servizi);
		datiDomanda.setDataProtocollazione(model.getDtProtocollazione());
	}

	private Set<A4gtAllegatoResponsabilita> gestisciAllegati(DatiDomandaRegistrazioneUtente datiDomanda, DomandaRegistrazioneUtente model) {

		Set<A4gtAllegatoResponsabilita> allegati = model.getA4gtAllegatoResponsabilita();
		if (allegati == null) {
			allegati = new HashSet<A4gtAllegatoResponsabilita>();
		}

		gestisciAllegatiPAT(allegati, datiDomanda);
		gestisciAllegatiCAA(allegati, datiDomanda);
		gestisciAllegatiAltriEnti(allegati, datiDomanda);
		gestisciAllegatiConsulente(allegati, datiDomanda);
		gestisciAllegatiDistributore(allegati, datiDomanda);

		for (A4gtAllegatoResponsabilita allegato : allegati) {
			allegato.setDomandaRegistrazione(model);
		}

		return allegati;
	}

	private Set<A4gtAllegatoResponsabilita> gestisciAllegatiPAT(Set<A4gtAllegatoResponsabilita> allegati, DatiDomandaRegistrazioneUtente datiDomanda) {
		List<RuoloPAT> responsabilita;

		Set<A4gtAllegatoResponsabilita> allegatoPAT = allegati.stream().filter(x -> CodResponsabilita.PAT.equals(x.getCodResponsabilita())).collect(Collectors.toSet());

		Set<A4gtAllegatoResponsabilita> daRimuovere;

		if (datiDomanda.getResponsabilitaRichieste() == null || datiDomanda.getResponsabilitaRichieste().getResponsabilitaPat() == null) {
			responsabilita = new ArrayList<RuoloPAT>();
			daRimuovere = allegatoPAT;
		} else {
			responsabilita = datiDomanda.getResponsabilitaRichieste().getResponsabilitaPat();

			daRimuovere = new HashSet<A4gtAllegatoResponsabilita>();

			for (A4gtAllegatoResponsabilita allegato : allegatoPAT) {
				Optional<RuoloPAT> ruolo = responsabilita.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny();

				if (!ruolo.isPresent() || ruolo.get().getAllegato() != null) {
					daRimuovere.add(allegato);
				} else if (!responsabilita.stream().filter(x -> allegato.getIdResponsabilita().equals(x.getIdResponsabilita())).findAny().isPresent()) {
					daRimuovere.add(allegato);
				}
			}
		}

		if (daRimuovere != null) {
			for (A4gtAllegatoResponsabilita allegato : daRimuovere) {
				allegati.remove(allegato);
				allegato.setDomandaRegistrazione(null);
			}
		}

		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date uploadDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

		for (RuoloPAT resp : responsabilita) {
			if (resp.getAllegato() != null) {
				A4gtAllegatoResponsabilita allegato = new A4gtAllegatoResponsabilita();
				BeanUtils.copyProperties(resp, allegato);
				allegato.setCodResponsabilita(CodResponsabilita.PAT);
				allegato.setDtInserimento(uploadDate);
				allegati.add(allegato);
				resp.setAllegato(null);
			}
		}

		return allegati;
	}

	private Set<A4gtAllegatoResponsabilita> gestisciAllegatiCAA(Set<A4gtAllegatoResponsabilita> allegati, DatiDomandaRegistrazioneUtente datiDomanda) {
		List<RuoloCAA> responsabilitaCaa;

		Set<A4gtAllegatoResponsabilita> daRimuovere;
		Set<A4gtAllegatoResponsabilita> allegatoCAA = allegati.stream().filter(x -> CodResponsabilita.CAA.equals(x.getCodResponsabilita())).collect(Collectors.toSet());
		if (datiDomanda.getResponsabilitaRichieste() == null || datiDomanda.getResponsabilitaRichieste().getResponsabilitaCaa() == null) {
			responsabilitaCaa = new ArrayList<RuoloCAA>();
			daRimuovere = allegatoCAA;
		} else {
			responsabilitaCaa = datiDomanda.getResponsabilitaRichieste().getResponsabilitaCaa();
			daRimuovere = new HashSet<A4gtAllegatoResponsabilita>();

			for (A4gtAllegatoResponsabilita allegato : allegatoCAA) {
				Optional<RuoloCAA> ruoloCaa = responsabilitaCaa.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny();

				if (!ruoloCaa.isPresent() || ruoloCaa.get().getAllegato() != null) {
					daRimuovere.add(allegato);
				} else if (!responsabilitaCaa.stream().filter(x -> allegato.getIdResponsabilita().equals(x.getIdResponsabilita())).findAny().isPresent()) {
					daRimuovere.add(allegato);
				}
			}
		}

		if (daRimuovere != null) {
			for (A4gtAllegatoResponsabilita allegato : daRimuovere) {
				allegati.remove(allegato);
				allegato.setDomandaRegistrazione(null);
			}
		}

		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date uploadDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

		for (RuoloCAA respCaa : responsabilitaCaa) {
			if (respCaa.getAllegato() != null) {
				A4gtAllegatoResponsabilita allegato = new A4gtAllegatoResponsabilita();
				BeanUtils.copyProperties(respCaa, allegato);
				allegato.setCodResponsabilita(CodResponsabilita.CAA);
				allegato.setDtInserimento(uploadDate);
				allegati.add(allegato);
				respCaa.setAllegato(null);
			}
		}

		return allegati;
	}


	private Set<A4gtAllegatoResponsabilita> gestisciAllegatiAltriEnti(Set<A4gtAllegatoResponsabilita> allegati, DatiDomandaRegistrazioneUtente datiDomanda) {
		List<CollaboratoreAltriEnti> responsabilitaAltriEnti;

		Set<A4gtAllegatoResponsabilita> daRimuovere;
		Set<A4gtAllegatoResponsabilita> allegatiAltriEnti = allegati.stream().filter(x -> CodResponsabilita.ALTRI.equals(x.getCodResponsabilita())).collect(Collectors.toSet());
		if (datiDomanda.getResponsabilitaRichieste() == null || datiDomanda.getResponsabilitaRichieste().getResponsabilitaAltriEnti() == null) {
			responsabilitaAltriEnti = new ArrayList<CollaboratoreAltriEnti>();
			daRimuovere = allegatiAltriEnti;
		} else {
			responsabilitaAltriEnti = datiDomanda.getResponsabilitaRichieste().getResponsabilitaAltriEnti();
			daRimuovere = new HashSet<A4gtAllegatoResponsabilita>();

			for (A4gtAllegatoResponsabilita allegato : allegatiAltriEnti) {
				Optional<CollaboratoreAltriEnti> responsabilitaAltroEnte = responsabilitaAltriEnti.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny();

				if (!responsabilitaAltroEnte.isPresent() || responsabilitaAltroEnte.get().getAllegato() != null) {
					daRimuovere.add(allegato);
				} else if (!responsabilitaAltriEnti.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny().isPresent()) {
					daRimuovere.add(allegato);
				}
			}
		}

		if (daRimuovere != null) {
			for (A4gtAllegatoResponsabilita allegato : daRimuovere) {
				allegati.remove(allegato);
				allegato.setDomandaRegistrazione(null);
			}
		}

		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date uploadDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

		for (CollaboratoreAltriEnti respAltroEnte : responsabilitaAltriEnti) {
			if (respAltroEnte.getAllegato() != null) {
				A4gtAllegatoResponsabilita allegato = new A4gtAllegatoResponsabilita();
				BeanUtils.copyProperties(respAltroEnte, allegato);
				allegato.setCodResponsabilita(CodResponsabilita.ALTRI);
				allegato.setDtInserimento(uploadDate);
				allegati.add(allegato);
				respAltroEnte.setAllegato(null);
			}
		}

		return allegati;
	}
	
	private Set<A4gtAllegatoResponsabilita> gestisciAllegatiConsulente(Set<A4gtAllegatoResponsabilita> allegati, DatiDomandaRegistrazioneUtente datiDomanda) {
		List<Consulente> responsabilitaConsulente;

		Set<A4gtAllegatoResponsabilita> daRimuovere;
		Set<A4gtAllegatoResponsabilita> allegatiConsulente = allegati.stream().filter(x -> CodResponsabilita.LPC.equals(x.getCodResponsabilita())).collect(Collectors.toSet());
		if (datiDomanda.getResponsabilitaRichieste() == null || datiDomanda.getResponsabilitaRichieste().getResponsabilitaConsulente() == null) {
			responsabilitaConsulente = new ArrayList<Consulente>();
			daRimuovere = allegatiConsulente;
		} else {
			responsabilitaConsulente = datiDomanda.getResponsabilitaRichieste().getResponsabilitaConsulente();
			daRimuovere = new HashSet<A4gtAllegatoResponsabilita>();

			for (A4gtAllegatoResponsabilita allegato : allegatiConsulente) {
				Optional<Consulente> responsabilitaLPConsulente = responsabilitaConsulente.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny();

				if (!responsabilitaLPConsulente.isPresent() || responsabilitaLPConsulente.get().getAllegato() != null) {
					daRimuovere.add(allegato);
				} else if (!responsabilitaConsulente.stream().filter(x -> x.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny().isPresent()) {
					daRimuovere.add(allegato);
				}
			}
		}

		if (daRimuovere != null) {
			for (A4gtAllegatoResponsabilita allegato : daRimuovere) {
				allegati.remove(allegato);
				allegato.setDomandaRegistrazione(null);
			}
		}

		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date uploadDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

		for (Consulente respLPConsulente : responsabilitaConsulente) {
			if (respLPConsulente.getAllegato() != null) {
				A4gtAllegatoResponsabilita allegato = new A4gtAllegatoResponsabilita();
				BeanUtils.copyProperties(respLPConsulente, allegato);
				allegato.setCodResponsabilita(CodResponsabilita.LPC);
				allegato.setDtInserimento(uploadDate);
				allegati.add(allegato);
				respLPConsulente.setAllegato(null);
			}
		}

		return allegati;
	}


	private Set<A4gtAllegatoResponsabilita> gestisciAllegatiDistributore(Set<A4gtAllegatoResponsabilita> allegati, DatiDomandaRegistrazioneUtente datiDomanda) {
		List<RuoloDistributore> responsabilitaDistributore;

		Set<A4gtAllegatoResponsabilita> daRimuovere;
		Set<A4gtAllegatoResponsabilita> allegatoDistributore = allegati.stream().filter(allegato -> CodResponsabilita.DIPENDENTE_DISTRIBUTORE.equals(allegato.getCodResponsabilita())).collect(Collectors.toSet());
		if (datiDomanda.getResponsabilitaRichieste() == null || datiDomanda.getResponsabilitaRichieste().getResponsabilitaDistributore() == null) {
			responsabilitaDistributore = new ArrayList<RuoloDistributore>();
			daRimuovere = allegatoDistributore;
		} else {
			responsabilitaDistributore = datiDomanda.getResponsabilitaRichieste().getResponsabilitaDistributore();
			daRimuovere = new HashSet<A4gtAllegatoResponsabilita>();

			for (A4gtAllegatoResponsabilita allegato : allegatoDistributore) {
				Optional<RuoloDistributore> ruoloDistributore = responsabilitaDistributore.stream().filter(resp -> resp.getIdResponsabilita().equals(allegato.getIdResponsabilita())).findAny();

				if (!ruoloDistributore.isPresent() || ruoloDistributore.get().getAllegato() != null) {
					daRimuovere.add(allegato);
				} else if (!responsabilitaDistributore.stream().filter(resp -> allegato.getIdResponsabilita().equals(resp.getIdResponsabilita())).findAny().isPresent()) {
					daRimuovere.add(allegato);
				}
			}
		}

		if (daRimuovere != null) {
			for (A4gtAllegatoResponsabilita allegato : daRimuovere) {
				allegati.remove(allegato);
				allegato.setDomandaRegistrazione(null);
			}
		}

		Date currentDate = new Date();
		LocalDateTime localCurrentDate = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
		Date uploadDate = Date.from(localCurrentDate.atZone(ZoneId.systemDefault()).toInstant());

		for (RuoloDistributore respDistributore : responsabilitaDistributore) {
			if (respDistributore.getAllegato() != null) {
				A4gtAllegatoResponsabilita allegato = new A4gtAllegatoResponsabilita();
				BeanUtils.copyProperties(respDistributore, allegato);
				allegato.setCodResponsabilita(CodResponsabilita.DIPENDENTE_DISTRIBUTORE);
				allegato.setDtInserimento(uploadDate);
				allegati.add(allegato);
				respDistributore.setAllegato(null);
			}
		}

		return allegati;
	}


	protected void initStatoDomanda(DomandaRegistrazioneUtente model) {
		model.setStato(StatoDomandaRegistrazioneUtente.IN_COMPILAZIONE);
	}

	protected void initUtenteDomanda(DomandaRegistrazioneUtente model) {
		model.setIdentificativoUtente(utenteComponent.utenza());
	}

	@Override
	public List<EnteCAA> getCAA(String params) throws Exception {
		List<A4gtEnte> a4gEnti = enteoDao.findAll();
		return EnteBuilder.from(a4gEnti);
	}
	@Override
	public List<Distributore> getDistributori(String params) throws Exception {
		Set<A4gtDistributore> a4gtDistributore = new HashSet<>(distributoreDao.findAll());
		return DistributoreBuilder.from(a4gtDistributore);
	}

	@Override
	public List<String> getDipartimenti() throws Exception {
		return Arrays.asList(Dipartimento.class.getEnumConstants()).stream().map(x -> x.toString()).sorted().collect(Collectors.toList());
	}

	@Override
	@Transactional
	public byte[] stampa(Long idDomanda) throws Exception {
		DomandaRegistrazioneUtente domandaRegistrata = domandaUtenteRep.getOne(idDomanda);
		byte[] stampa = domandaRegistrata.getDocumento();
		if (stampa == null) {
			if (!StatoDomandaRegistrazioneUtente.IN_COMPILAZIONE.equals(domandaRegistrata.getStato())) {
				throw new UtenteException("Non e' possibile stampare una domanda non compilazione");
			}
			DatiDomandaRegistrazioneUtente datiDomanda = new DatiDomandaRegistrazioneUtente();
			convertiInDto(domandaRegistrata, datiDomanda);
			datiDomanda.setLuogo("Trento");
			datiDomanda.setData(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			String jsonToken = objectMapper.writeValueAsString(datiDomanda);
//			stampa = stampaComponent.stampaPDF_A(jsonToken, "template/templateModuloDomandaRegistrazione.docx");
			stampa = "Any String you want".getBytes();
			domandaRegistrata.setDocumento(stampa);
			domandaUtenteRep.save(domandaRegistrata);
		}
		return stampa;
	}

	@Override
	@Transactional
	public Long firma(Long idDomanda, DatiAutenticazione datiAutenticazione) throws Exception {
		DomandaRegistrazioneUtente domandaRegistrata = domandaUtenteRep.getOne(idDomanda);
		if (!isDomandaUtente(domandaRegistrata)) {
			logger.error("firma: l'utente {} sta cercando di modificare la domanda {} non sua", utenteComponent.utenza(), idDomanda);
			throw new UtenteException(
					"Non e' possibile firmare una domanda non di propria competenza");
		}
//		byte[] stampa = domandaRegistrata.getDocumento();
//		if (stampa == null) {
//			throw new UtenteException("Non e' possibile firmare una domanda non stampata");
//		}
//		Firma firma = firmaService.firma("MODULO REGISTRAZIONE", datiAutenticazione, stampa);
		FirmaDomandaRegistrazioneUtente firmaModel = new FirmaDomandaRegistrazioneUtente();
		firmaModel.setIdDomanda(idDomanda);
//		firmaModel.setXml(firma.getXml());
//		firmaModel.setPdf(firma.getPdf());
		firmaModel = firmaDomandaRep.save(firmaModel);
		return firmaModel.getId();
	}
	
	/**
	 * salvataggio delle informazioni di protocollazione in un dto prima dell'invio
	 * dell'evento
	 */
	private DomandaRegistrazioneUtenteDto saveDomanda(Long idDomanda) throws Exception{
		DomandaRegistrazioneUtenteDto domandaRegistrazioneUtenteDto = new DomandaRegistrazioneUtenteDto().setIdDomanda(idDomanda);

		DomandaRegistrazioneUtente domandaRegistrata = domandaUtenteRep.getOne(idDomanda);
		if (!isDomandaUtente(domandaRegistrata)) {
			logger.error("protocolla: l'utente {} sta cercando di modificare la domanda {} non sua", utenteComponent.utenza(), idDomanda);
			throw new UtenteException(
					"Non e' possibile protocollare una domanda non di propria competenza");
		}
		if (!isProtocollabile(domandaRegistrata)) {
			throw new UtenteException("La domanda non risulta in uno stato che prevede protocollazione");
		}
		FirmaDomandaRegistrazioneUtente firma;
		try {
			firma = firmaDomandaRep.findOneByIdDomanda(idDomanda);
		} catch (NoResultException | NonUniqueResultException pe) {
			throw new UtenteException("La domanda non risulta protocollabile in quanto non firmata correttamente");
		}
		if (firma == null) {
			throw new UtenteException("La domanda non risulta protocollabile in quanto non firmata");
		}
		
		try {
			
			List<A4gtAllegatoResponsabilita> listOfAllegatiResponsabilita = new ArrayList<>(domandaRegistrata.getA4gtAllegatoResponsabilita());
			
			// costruzione della request
			ObjectNode jsonProtocollazione = objectMapper.createObjectNode();
			// costruzione tipologiaDocumentoPrincipale
			jsonProtocollazione.put("tipologiaDocumentoPrincipale", tipologiaDocumentoProtocollazione);
			// costruzione oggetto
			if (domandaRegistrata.getTipoDomandaRegistrazione().equals(TipoDomandaRegistrazione.RIDOTTA_AZIENDA)) {
				jsonProtocollazione.put("oggetto",
						String.format("%s - %s - %s %s", "MYAPPAG - AGGIUNTA PROFILO AZIENDA", domandaRegistrata.getCodiceFiscale(), domandaRegistrata.getNome(), domandaRegistrata.getCognome()));
			} else {
				jsonProtocollazione.put("oggetto",
						String.format("%s - %s - %s %s", "A4G - DOMANDA ACCESSO SISTEMA", domandaRegistrata.getCodiceFiscale(), domandaRegistrata.getNome(), domandaRegistrata.getCognome()));
			}
			// costruzione del mittente
			ObjectNode mittente = objectMapper.createObjectNode();
			mittente.putObject("mittente").put("name", domandaRegistrata.getNome()).put("surname", domandaRegistrata.getCognome()).put("email", domandaRegistrata.getEmail())
					.put("nationalIdentificationNumber", domandaRegistrata.getCodiceFiscale())
					.put("description", domandaRegistrata.getCodiceFiscale() + " - " + domandaRegistrata.getNome() + " " + domandaRegistrata.getCognome());
			jsonProtocollazione.setAll(mittente);
			
			domandaRegistrazioneUtenteDto.setJsonProtocollazione(jsonProtocollazione);
			firma = firmaDomandaRep.findOneByIdDomanda(domandaRegistrata.getId());

//			// costruzione allegati - responsabilità
			if (listOfAllegatiResponsabilita != null && !listOfAllegatiResponsabilita.isEmpty()) {
				AtomicInteger count = new AtomicInteger(0);
				listOfAllegatiResponsabilita.forEach(allegatoResponsabilita -> {
					domandaRegistrazioneUtenteDto.getAllegati().add(new ByteArrayResource(allegatoResponsabilita.getAllegato()) {
						@Override
						public String getFilename() {
							return String.format("%s%s%s%d%s", "AllegatoResponsabilita", domandaRegistrata.getCodiceFiscale(), "_", count.incrementAndGet(), ".pdf");
						}
					});
				});
			}
			
			// costruzione allegati - firma pdf
			domandaRegistrazioneUtenteDto.getAllegati().add(new ByteArrayResource(firma.getPdf()) {
				@Override
				public String getFilename() {
					return String.format("%s%s%s", "AllegatoFirmaPDF", domandaRegistrata.getCodiceFiscale(), ".pdf");
				}
			});

			// costruzione allegati - firma xml
			domandaRegistrazioneUtenteDto.getAllegati().add( new ByteArrayResource(firma.getXml().getBytes()) {
				@Override
				public String getFilename() {
					return String.format("%s%s%s", "AllegatoFirmaXML", domandaRegistrata.getCodiceFiscale(), ".xml");
				}
			});
			
//			costruzione documento
			ByteArrayResource documentoByteAsResource = new ByteArrayResource(domandaRegistrata.getDocumento()) {
				@Override
				public String getFilename() {
					return "DomandaAccessoSistema.pdf";
				}
			};
			
			domandaRegistrazioneUtenteDto.setDocumentoPrincipale(documentoByteAsResource);
			
//			TODO impostazione dello stato a PROTOCOLLATA? 
//			Se venisse fatto ciò lo scheduler di automazione approvazione scatterebbe subito.
//			E' corretto? Se sì si dovrebbe cambiare il messaggio della mail?
			domandaRegistrata.setStato(PROTOCOLLATA);
			domandaUtenteRep.save(domandaRegistrata);

		
			return domandaRegistrazioneUtenteDto;

		} catch (Exception ex) {
			logger.error("Errore nel salvataggio dei dati della Domanda Antimafia", ex);
			throw ex;
		}
	}

	/**
	 *invio di un evento tramite EventBus
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void protocollaDomanda(DomandaRegistrazioneUtenteDto domandaRegistrazioneUtenteDto) {
		DomandaRegistrazioneUtenteEvent event = new DomandaRegistrazioneUtenteEvent(domandaRegistrazioneUtenteDto);
		eventBus.publishEvent(event);
	}
	

	@Override
	@Transactional
	public void protocolla(Long idDomanda) throws Exception {
//		passo 1: salvataggio informazioni domanda in un dto
		DomandaRegistrazioneUtenteDto domandaRegitrazioneUtenteDto = saveDomanda(idDomanda);
//		passo 2: wrap del dto sopra in evento ed invio con EventBus
		domandaService.protocollaDomanda(domandaRegitrazioneUtenteDto);
	}

	protected boolean isProtocollabile(DomandaRegistrazioneUtente domandaRegistrata) {
		return StatoDomandaRegistrazioneUtente.IN_COMPILAZIONE.equals(domandaRegistrata.getStato());
	}

	public RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> ricercaDomande(DomandaRegistrazioneUtenteFilter criteri,
																				   Paginazione paginazione,
																				   Ordinamento ordinamento) throws Exception {
		Page<DomandaRegistrazioneUtente> page = domandaUtenteRep.findAll(DomandaRegistrazioneUtenteSpecificationBuilder.getFilter(criteri),
																		 PageableBuilder.build().from(paginazione, ordinamento));
		return DatiDomandaRegistrazioneUtenteBuilder.from(page);
	}
	
	public List<CounterStato> contaDomande(DomandaRegistrazioneUtenteFilter filter) throws Exception {
		List<StatoDomandaRegistrazioneUtente> listaStati = Arrays.asList(PROTOCOLLATA, IN_LAVORAZIONE, APPROVATA, RIFIUTATA);
        List<CounterStato> counters =  listaStati.stream()
                                                 .map(stato -> domandaRegistrazioneUtenteSupport.counterForStatoBy(stato, filter))
                                                 .collect(Collectors.toList());
        return riempiConStatiMancanti(counters);
	}

	@Override
	@Transactional
	public void presaInCarico(Long id) throws Exception {
		Optional<DomandaRegistrazioneUtente> domandaRegistrazioneUtenteResult = 
				domandaUtenteRep.findById(id);
		domandaRegistrazioneUtenteResult.orElseThrow(() -> new UtenteException(String.format("Nessuna domanda con id %d trovata", id)));
		domandaRegistrazioneUtenteResult
			.filter(d -> d.getStato().equals(PROTOCOLLATA))
			.map(d -> {d.setStato(StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE); return d;})
			.orElseThrow(() -> new UtenteException(String.format("La domanda %d non è in stato: %s", id, PROTOCOLLATA)));
		DomandaRegistrazioneUtente domandaDaAggiornare = domandaRegistrazioneUtenteResult.get();
		domandaUtenteRep.save(domandaDaAggiornare);
	}

	@Override
	@Transactional
	public Long approva(RichiestaDomandaApprovazione richiesta) throws Exception {
		richiesta.isValid();
		final DomandaRegistrazioneUtente domandaSalvata = domandaRegistrazioneUtenteSupport.cambiaStato(richiesta.getIdDomanda(), APPROVATA);
		IstruttoriaEntita istruttoriaEntita = new IstruttoriaEntita();
		if (domandaSalvata.getTipoDomandaRegistrazione().equals(TipoDomandaRegistrazione.RIDOTTA_AZIENDA)) {
			istruttoriaEntita = istruttoriaService.aggiornaIstruttoriaPerApprovazioneDomandaRidottaAzienda(richiesta);
		} else {
			istruttoriaEntita = istruttoriaService.aggiornaIstruttoriaPerApprovazioneDomanda(richiesta);
		}
		A4gtUtente utente = istruttoriaService.creaOAggiornaUtenzaPerApprovazioneDomanda(istruttoriaEntita);
		istruttoriaService.aggiungiUtenteAIstruttoria(utente, istruttoriaEntita);
		entityManager.flush();
		emailSupport.sendSimpleMessage(domandaSalvata.getEmail(),
				               "APPAG - Approvazione della domanda di richiesta accesso utente",
									   richiesta.getTestoMail() + EmailSupport.getFirmaAppagMail());
		return domandaSalvata.getId();
	}

	@Override
	@Transactional
	public Long rifiuta(RichiestaDomandaRifiuta richiesta) throws Exception {
		richiesta.isValid();
		final DomandaRegistrazioneUtente domandaSalvata = domandaRegistrazioneUtenteSupport.cambiaStato(richiesta.getIdDomanda(), RIFIUTATA);
		istruttoriaService.aggiornaIstruttoriaPerRifiutoDomanda(richiesta);
		entityManager.flush();
		emailSupport.sendSimpleMessage(domandaSalvata.getEmail(),
							   "APPAG - Rifiuto della domanda di richiesta accesso utente",
									   richiesta.getTestoMail() + EmailSupport.getFirmaAppagMail());
		return domandaSalvata.getId();
	}

	@Override
	@Transactional
	public DomandaRegistrazioneCreataResponse registrazioneFirmaDocumento(final DatiDomandaRegistrazioneUtente domandaRegistrazioneUtente,
			final DatiAutenticazione datiAutenticazione) {
		boolean isPrivacyProtocollata = false;
		try {
			// verifica privacy /a4gutente/persone
			List<Persona> persone = personaService.ricercaPersone(domandaRegistrazioneUtente.getCodiceFiscale());
			Persona persona = null;
			if (persone.size() == 1) {
				persona = persone.get(0);
				String datiDomandaAutorizzazioni = persona.getNrProtocolloPrivacyGenerale();
				if (datiDomandaAutorizzazioni != null && !datiDomandaAutorizzazioni.isEmpty()) {
					isPrivacyProtocollata = true;
				}
			}
			if (!isPrivacyProtocollata) {
				logger.error("DomandaRegistrazioneUtente: l'utente ha effettuato richiesta senza aver precedentemente firmato il modulo privacy");
				throw new UtenteException("PRIVACY_NOT_SIGNED_ERROR");
			}
			// registra
			Long domandaId = domandaRegistrazioneUtente.getId();
			if (domandaId == null ) {
				domandaId = domandaService.registraDomanda(domandaRegistrazioneUtente);
				domandaRegistrazioneUtente.setId(domandaId);
			} else {
				domandaService.aggiornaDomanda(domandaRegistrazioneUtente);
			}
			byte[] domandaPdf = domandaService.stampa(domandaId);
			// firma autenticazione
			domandaService.firma(domandaId, datiAutenticazione);
			// protocolla
			domandaService.protocolla(domandaId);
			DomandaRegistrazioneCreataResponse drc = new DomandaRegistrazioneCreataResponse();
			drc.setId(domandaId);
			drc.setBase64Content(Base64.getEncoder().encodeToString(domandaPdf));
			return drc;
		} catch (Exception e) {
			logger.error("Errore in registrazione utente", e);
		}
		return null;
	}
	
}
