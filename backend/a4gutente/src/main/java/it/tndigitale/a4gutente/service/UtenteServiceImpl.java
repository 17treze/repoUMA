/**
 *
 */
package it.tndigitale.a4gutente.service;

import static it.tndigitale.a4gutente.service.builder.AziendaBuilder.from;
import static it.tndigitale.a4gutente.service.builder.IstruttoriaBuilder.getDisabledProfilesFrom;
import static it.tndigitale.a4gutente.service.builder.UtenteBuilder.getUtenteDaPersistere;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4gutente.dto.*;
import it.tndigitale.a4gutente.dto.UtenteProfiloA4gDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.security.configuration.SecurityContextWrapper;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.codici.CodiceProfilo;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.RuoloDistributore;
import it.tndigitale.a4gutente.dto.ResponsabilitaRichieste.TitolareImpresa;
import it.tndigitale.a4gutente.dto.csv.UtenteA4gCsv;
import it.tndigitale.a4gutente.dto.csv.UtenteA4gDTO;
import it.tndigitale.a4gutente.dto.csv.UtenteA4gSospesoDTO;
import it.tndigitale.a4gutente.event.InfoPrivacyEvent;
import it.tndigitale.a4gutente.repository.dao.IDistributoreDao;
import it.tndigitale.a4gutente.repository.dao.IPersonaDao;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.dao.RuoloDao;
import it.tndigitale.a4gutente.repository.dao.custom.UtenteCompletoCustomDao;
import it.tndigitale.a4gutente.repository.model.A4grUtenteAzienda;
import it.tndigitale.a4gutente.repository.model.A4gtDistributore;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import it.tndigitale.a4gutente.repository.model.Ruolo;
import it.tndigitale.a4gutente.service.builder.DistributoreBuilder;
import it.tndigitale.a4gutente.service.builder.UtenteBuilder;
import it.tndigitale.a4gutente.service.loader.IstruttoriaLoader;

/**
 * @author it417
 *
 */
@Service
public class UtenteServiceImpl implements IUtenteService {

	@Autowired
	private IUtenteCompletoDao utenteCompletoDao;

	@Autowired
	private RuoloDao ruoloDao;

	@Autowired
	private UtenteCompletoCustomDao utenteCompletoCustomDao;

	@Autowired
	private SecurityContextWrapper securityContextWrapper;

	@Autowired
	private IPersonaDao personaDao;

	@Autowired
	private IstruttoriaLoader istruttoriaLoader;

	@Autowired
	private Clock clock;

	//	@Autowired
	//	private DocumentiProxyClient documentiProxyClient;

	@Autowired private PersonaService personaService;

	@Autowired private IUtenteService utenteService;
	private EventBus eventBus;

	@Autowired
	public UtenteServiceImpl setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		return this;
	}

	@Autowired
	private IDistributoreDao distributoreDao;

	public UtenteServiceImpl setUtenteCompletoDao(IUtenteCompletoDao utenteCompletoDao,
			RuoloDao ruoloDao,
			UtenteCompletoCustomDao utenteCompletoCustomDao,
			SecurityContextWrapper securityContextWrapper,
			IPersonaDao personaDao,
			IstruttoriaLoader istruttoriaLoader,
			IDistributoreDao distributoreDao) {
		this.utenteCompletoDao = utenteCompletoDao;
		this.ruoloDao = ruoloDao;
		this.utenteCompletoCustomDao = utenteCompletoCustomDao;
		this.securityContextWrapper = securityContextWrapper;
		this.personaDao = personaDao;
		this.istruttoriaLoader = istruttoriaLoader;
		this.distributoreDao = distributoreDao;
		return this;
	}

	private static final Logger logger = LoggerFactory.getLogger(UtenteServiceImpl.class);


	/*
	 * (non-Javadoc)
	 *
	 * @see it.tndigitale.a4g.sso.service.IUtenteService#caricaProfiloUtente(java.lang.String)
	 */
	@Override
	public List<Profilo> caricaProfiliUtente() {
		String utenza = getUsername();
		return caricaProfiliUtente(utenza);
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see it.tndigitale.a4g.sso.service.IUtenteService#caricaProfiloUtente()
	 */
	@Override
	public List<Profilo> caricaProfiliUtente(String utenza) {
		List<Profilo> result = new ArrayList<>();
		A4gtUtente utente = getUtente(utenza);
		if (utente == null)
			return null;
		for (A4gtProfilo profilo : utenteCompletoDao.findByIdentificativo(utenza).getProfili()) {
			Profilo profiloOut = new Profilo();
			BeanUtils.copyProperties(profilo, profiloOut);
			result.add(profiloOut);
		}

		return result;
	}

	@Override
	public List<Ruolo> caricaRuoliUtente(String utenza) {
		if (!utenza.contains("@")) utenza = utenza.toUpperCase();
		return ruoloDao.findByUtente(utenza);
	}

	public A4gtUtente getUtente(String utenza) {
		A4gtUtente utente = new A4gtUtente();
		if (!utenza.contains("@")) utenza = utenza.toUpperCase();
		utente.setIdentificativo(utenza);
		logger.debug("getUtente : utenza = " + utenza);
		Optional<A4gtUtente> utenteOut = utenteCompletoDao.findOne(Example.of(utente));

		if (utenteOut.isPresent())
			return utenteOut.get();
		else
			return null;
	}

	@Override
	public List<String> caricaEntiUtente() {
		String utenza = getUsername();
		A4gtUtente utente = getUtente(utenza);
		logger.debug("caricaEntiUtente : utenza = " + utenza);

		if (utente != null) {
			Set<A4gtEnte> enti = utente.getA4gtEntes();
			List<String> list = enti.stream().map(x -> x.getIdentificativo().toString()).collect(Collectors.toList());
			return list;
		}
		logger.debug("caricaEntiUtente : utenza = " + utenza + " non abilitata");

		return new ArrayList<String>();
	}

	protected String getUsername() {
		String username = securityContextWrapper.getAuthentication().getName();
		return username;
	}

	@Override
	public List<String> caricaAziendeUtente() {
		String utenza = getUsername();
		logger.debug("caricaAziendeUtente : utenza = " + utenza);
		A4gtUtente utente = getUtente(utenza);
		if (utente != null) {
			Set<A4grUtenteAzienda> aziende = utente.getA4grUtenteAziendas();
			List<String> list = aziende.stream().map(x -> x.getCuaa()).collect(Collectors.toList());
			logger.debug("caricaAziendeUtente : trovate aziende = " + list);
			return list;
		}
		logger.debug("caricaAziendeUtente : utenza = " + utenza + " non abilitata");

		return new ArrayList<>();
	}

	@Override
	public List<Ruolo> caricaRuoliUtente() {
		String utenza = getUsername();
		return caricaRuoliUtente(utenza);
	}


	public A4gtUtente salvaUtente(A4gtUtente u) {
		return utenteCompletoDao.save(u);
	}

	public A4gtUtente createOrGetUtente(DomandaRegistrazioneUtente domanda) {
		A4gtUtente a4gUtente =  Optional.ofNullable(getUtente(domanda.getIdentificativoUtente()))
				.orElse(getUtenteDaPersistere(domanda));
		if (a4gUtente.getId() == null) {
			a4gUtente = utenteCompletoDao.save(a4gUtente);
		}
		return a4gUtente;
	}

	@Transactional
	@Override
	public void addEnti(A4gtUtente utente, List<A4gtEnte> enti) {
		Set<A4gtEnte> entiDaAssociare = Optional.ofNullable(enti)
				.orElse(new ArrayList<>())
				.stream().collect(Collectors.toSet());
		utente.setA4gtEntes(entiDaAssociare);
		salvaUtente(utente);
	}

	@Transactional
	@Override
	public void addProfili(A4gtUtente utente, List<A4gtProfilo> profili) {
		Set<A4gtProfilo> profiliDaAssociare = Optional.ofNullable(profili)
				.orElse(new ArrayList<>())
				.stream().collect(Collectors.toSet());
		utente.setProfili(profiliDaAssociare);
		salvaUtente(utente);
	}

	@Transactional
	@Override
	public void addAziende(A4gtUtente utente, List<TitolareImpresa> aziende) {
		if (utente.getA4grUtenteAziendas()==null) {
			utente.setA4grUtenteAziendas(new HashSet<>());
		}
		utente.getA4grUtenteAziendas().clear();
		if (!aziende.isEmpty()) {
			utente.getA4grUtenteAziendas().addAll(from(aziende, utente));
		}
		utenteCompletoDao.save(utente);
	}

	@Transactional
	@Override
	public void removeAziende(A4gtUtente utente) {
		if (utente.getA4grUtenteAziendas()!=null &&
				!utente.getA4grUtenteAziendas().isEmpty()) {
			utente.getA4grUtenteAziendas().clear();
			utenteCompletoDao.save(utente);
		}
	}

	@Transactional
	@Override
	public void addDistributori(A4gtUtente utente, List<RuoloDistributore> ruoliDistributore) {
		List<Distributore> distributori = new ArrayList<>();
		for (RuoloDistributore ruolo: ruoliDistributore) {
			distributori.addAll(ruolo.getDistributori());
		}
		if (utente.getA4gtDistributori()==null) {
			utente.setA4gtDistributori(new HashSet<>());
		}
		utente.getA4gtDistributori().clear();
		if (!distributori.isEmpty()) {
			List<A4gtDistributore> entitaDistributori = distributoreDao.findAll();
			for (Distributore dist: distributori) {
				A4gtDistributore distributoreAdd = entitaDistributori.stream()
						.filter(d -> d.getId().equals(dist.getId()))
						.findFirst()
						.get();
				utente.getA4gtDistributori().add(distributoreAdd);
			}
		}
		utenteCompletoDao.save(utente);
	}

	@Override
	public List<Utente> ricerca(UtentiFilter filter) throws Exception {
		UtentiFilter adjustFilter = UtentiFilter.getOrDefault(filter);
		List<A4gtUtente> utenti = utenteCompletoCustomDao.ricerca(adjustFilter);
		return UtenteBuilder.from(utenti);
	}

	@Override
	public Utente carica(String identificativo) throws Exception {
		A4gtUtente utente = Optional.ofNullable(utenteCompletoDao.findByIdentificativo(identificativo))
				.orElseThrow(() ->
				new EntityNotFoundException("Utente con identificativo " + identificativo + " non trovato"));
		return caricaUtenteConInfoPersonaEprofiliDisabilitati(utente);
	}

	@Override
	public Utente carica(Long id) {
		A4gtUtente utente = utenteCompletoDao.findById(id)
				.orElseThrow(() ->
				new EntityNotFoundException("Utente con ID " + id + " non trovato"));
		return caricaUtenteConInfoPersonaEprofiliDisabilitati(utente);
	}

	public Utente caricaUtenteConInfoPersonaEprofiliDisabilitati(A4gtUtente utente) {
		PersonaEntita persona = personaDao.findOneByCodiceFiscale(utente.getCodiceFiscale());
		IstruttoriaEntita istruttoriaEntita = istruttoriaLoader.loadLastIstruttoriaByIdentificativoUtente(utente.getIdentificativo());
		return UtenteBuilder.detail(utente, persona)
				.setMotivazioneDisattivazione((istruttoriaEntita != null)? istruttoriaEntita.getMotivazioneDisattivazione():null)
				.disableProfiles(getDisabledProfilesFrom(istruttoriaEntita));
	}

	@Override
	@Transactional
	public void protocollaPrivacy(
			String richiedenteNome, String richiedenteCognome, String richiedenteCodiceFiscale,
			String infoIn, MultipartFile documento, List<MultipartFile> allegati) throws Exception {
		List<Persona> ricercaPersone = personaService.ricercaPersone(richiedenteCodiceFiscale);
		Persona persona;
		if (ricercaPersone == null || ricercaPersone.isEmpty()) {
			persona = new Persona();
			persona.setNome(richiedenteNome);
			persona.setCognome(richiedenteCognome);
			persona.setCodiceFiscale(richiedenteCodiceFiscale);
		} else {
			persona = ricercaPersone.get(0);
		}
		personaService.inserisciAggiornaPersona(persona);
		InfoPrivacyDto infoPrivacyDto = saveInfoPrivacyInfo(richiedenteCodiceFiscale, infoIn, documento, allegati);
		utenteService.protocollaInformativaPrivacy(infoPrivacyDto);
	}	


	private InfoPrivacyDto saveInfoPrivacyInfo(String codiceFiscale, String infoIn, MultipartFile documento, List<MultipartFile> allegati)  throws Exception{

		InfoPrivacyDto result = new InfoPrivacyDto();
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(documento.getBytes()) {
			@Override
			public String getFilename() {
				return documento.getOriginalFilename();
			}
		};
		result.setDocumento(documentoByteAsResource);

		if (allegati != null && !allegati.isEmpty()) {
			allegati.forEach(allegato -> {
				try {
					result.getAllegati().add(new ByteArrayResource(allegato.getBytes()) {
						@Override
						public String getFilename() {
							return String.format(allegato.getOriginalFilename());
						}
					});
				} catch (IOException e) {
					throw new RuntimeException("", e);
				}
			});
		}

		result.setRichiedenteCodiceFiscale(codiceFiscale);
		result.setInfoIn(infoIn);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void protocollaInformativaPrivacy(InfoPrivacyDto infoPrivacyDto) {
		InfoPrivacyEvent event = new InfoPrivacyEvent(infoPrivacyDto);
		eventBus.publishEvent(event);
	}

	@Override
	public CsvFile getUtentiCsv() throws Exception {

		List<UtenteA4gDTO> utentiA4gList = utenteCompletoDao.findUtenteA4g();
		List<UtenteA4gSospesoDTO> utentiA4gSospesiList = utenteCompletoDao.findUtenteA4gSospesi();

		List<UtenteA4gCsv> utentiA4gListCsv = utentiA4gList
				.stream()
				.map(u -> {
					Optional<UtenteA4gSospesoDTO> data = Optional.ofNullable(utentiA4gSospesiList)
							.orElseGet(Collections::emptyList)
							.stream()
							.filter(s -> s.getUserName().equals(u.getUserName()))
							.findFirst();

					UtenteA4gCsv utenteCsv = data.isPresent() ? UtenteA4gCsv.buildFromDto(u,true,data.get().getMotivoSospensione()) : UtenteA4gCsv.buildFromDto(u,false,null);

					return utenteCsv;
				})
				.distinct()
				.sorted(Comparator.comparing(UtenteA4gCsv::getUsername))
				.collect(Collectors.toList());

		ObjectWriter objWriter = new CsvMapper()
				.writerFor(UtenteA4gCsv.class)
				.with(UtenteA4gCsv.getSchema());
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		objWriter.writeValues(csvByteArray).writeAll(utentiA4gListCsv);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		csvFile.setCsvFileName(String.format("utentiA4g.csv"));
		return csvFile;
	}

	@Override
	public List<Distributore> caricaDistributoriUtente() {
		String utenza = getUsername();
		A4gtUtente utente = getUtente(utenza);

		if (utente != null) {
			// l'utente istruttore uma può vedere tutti i distributori
			Optional<A4gtProfilo> profiloIstruttoreUma = utente.getProfili()
					.stream()
					.filter(p -> CodiceProfilo.ISTRUTTORE_UMA.getCodice().equalsIgnoreCase(p.getIdentificativo()))
					.collect(CustomCollectors.collectOne());
			if (profiloIstruttoreUma.isPresent()) {
				Set<A4gtDistributore> distributori = distributoreDao.findAll().stream()
						.filter(d -> clock.now().isAfter(d.getDataInizio()))
						.filter(d -> {
							var dtFine = Optional.ofNullable(d.getDataFine());
							return dtFine.isPresent() ? clock.now().isBefore(dtFine.get()) : Boolean.TRUE;
						})
						.collect(Collectors.toSet());

				return DistributoreBuilder.from(distributori);
			}
			return DistributoreBuilder.from(utente.getA4gtDistributori());
		}
		return new ArrayList<>();
	}

	@Override
	public Distributore caricaDistributoreUtente(Long id) {
		String utenza = getUsername();
		A4gtUtente utente = getUtente(utenza);

		if (utente != null) {
			// se è un istruttore uma può vedere il distributore 
			Optional<A4gtProfilo> profiloIstruttoreUma = utente.getProfili()
					.stream()
					.filter(p -> CodiceProfilo.ISTRUTTORE_UMA.getCodice().equalsIgnoreCase(p.getIdentificativo()))
					.collect(CustomCollectors.collectOne());
			if (profiloIstruttoreUma.isPresent()) {
				Optional<A4gtDistributore> distributoreOpt = distributoreDao.findById(id);
				if (!distributoreOpt.isPresent()) {return null;}
				Optional<Distributore> distributoreDtoOpt = DistributoreBuilder.from(Collections.singleton((distributoreOpt.get()))).stream().findFirst();
				return distributoreDtoOpt.isPresent() ? distributoreDtoOpt.get() : null;
			}
			Optional<Distributore> distributoreOpt = DistributoreBuilder.from(utente.getA4gtDistributori()).stream()
					.filter(d -> d.getId().equals(id))
					.collect(CustomCollectors.collectOne());
			return distributoreOpt.isPresent() ? distributoreOpt.get() : null;
		}
		return null;
	}
	
	public ReportValidazioneDto getReportValidazione(String identificativo) throws Exception {

		ReportValidazioneDto reportValidazioneDto = new ReportValidazioneDto();
		Utente utente = utenteService.carica(identificativo);
		if (utente != null) {
			reportValidazioneDto.setCodiceFiscale(utente.getCodiceFiscale());
			reportValidazioneDto.setCognome(utente.getCognome());
			reportValidazioneDto.setNome(utente.getNome());
		}
		
		return reportValidazioneDto;
	}

	public List<UtenteProfiloA4gDto> getAllUtentiProfilo (List<ProfiloUtente> profili) throws Exception {

		List<String> profs = new ArrayList<>();
		profili.forEach(p->profs.add(p.name()));

		return utenteCompletoDao.findAllUtentiA4gProfilo(profs);
	}
}
