package it.tndigitale.a4g.fascicolo.antimafia.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.fascicolo.antimafia.A4gfascicoloConstants;
import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.action.AziendeCollegateConsumer;
import it.tndigitale.a4g.fascicolo.antimafia.action.FunctionDichiarazioneAntimafiaDecorator;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AggiornaDichiarazioneEsito;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliareConviventeFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventi;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventiResult;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Azienda;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AziendaCollegata;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Carica;
import it.tndigitale.a4g.fascicolo.antimafia.dto.CustomThreadLocal;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DatiDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DettaglioImpresa;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DettaglioImpresaBuilder;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioniAntimafiaCsv;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Nota;
import it.tndigitale.a4g.fascicolo.antimafia.dto.NotaFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.PageResultWrapper;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Pagination;
import it.tndigitale.a4g.fascicolo.antimafia.dto.ProcedimentiEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.ProtocollaCertificazioneAntimafiaDto;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Richiedente;
import it.tndigitale.a4g.fascicolo.antimafia.dto.RichiedenteBuilder;
import it.tndigitale.a4g.fascicolo.antimafia.dto.SoggettoImpresa;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Sort;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneCounter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.TipoNotaEnum;
import it.tndigitale.a4g.fascicolo.antimafia.event.ProtocollaCertificazioneAntimafiaEvent;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.AllegatoDicAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.AllegatoDicFamConvDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.AziendaAgricolaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.DichiarazioneAntimafiaSpecificationsFilter;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.NotaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.ProcedimentoDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.StatoDichiarazioneAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.TipoAllegatoDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gdStatoDicAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gdTipoAllegato;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtAllegatoDicAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtAllegatoDicFamConv;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtAziendaAgricola;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtNota;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtProcedimentoAmf;
import it.tndigitale.a4g.fascicolo.antimafia.rest.ProxyClient;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.time.Clock;

/**
 * The Class AntimafiaServiceImpl.
 */
@Service
public class AntimafiaServiceImpl implements AntimafiaService {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	private static final String ESITO = "esito";
	private static final Logger logger = LoggerFactory.getLogger(AntimafiaServiceImpl.class);
	@Autowired
	private DichiarazioneAntimafiaDao daoDichiarazioneAntimafia;
	@Autowired
	private AziendaAgricolaDao daoAziendaAgricola;
	@Autowired
	private StatoDichiarazioneAntimafiaDao daoStatoDichiarazioneAntimafia;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AllegatoDicFamConvDao allegatoDicFamConvDao;
	@Autowired
	private RichiedenteBuilder richiedenteBuilder;
	@Autowired
	private AziendeCollegateConsumer aziendeCollegateConsumer;
	@Autowired
	private NotaDao notaDao;
	@Autowired
	private DettaglioImpresaBuilder dettaglioImpresaBuilder;
	@Autowired
	private TipoAllegatoDao tipoAllegatoDao;
	@Autowired
	private AllegatoDicAntimafiaDao allegatoDicAntimafiaDao;
	@Autowired
	private FunctionDichiarazioneAntimafiaDecorator functionDichiarazioneAntimafiaDecorator;
	@Value("${a4gfascicolo.integrazioni.anagrafetributaria.uri}")
	private String urlIntegrazioniAnagrafeTributaria;
	@Value("${a4gfascicolo.integrazioni.anagraficaimpresa.uri}")
	private String urlIntegrazioniAnagraficaImpresa;
	@Value("${a4gfascicolo.integrazioni.verificafirma.uri}")
	private String urlIntegrazioniVerificaFirma;
	@Value("${a4gfascicolo.integrazioni.protocollo.documenti.uri}")
	private String urlIntegrazioniProtocolloDomanda;
	@Value("${domanda.certificazione.antimafia.oggetto}")
	private String oggettoDomandaCertificazioneAntimafia;
	@Value("${domanda.certificazione.antimafia.tipologiaDocumentoPrincipale}")
	private String tipologiaDocumentoProtocollazioneDomandaCertificazioneAntimafia;
	@Value("${a4gfascicolo.integrazioni.sincronizzazione.antimafia.uri}")
	private String urlIntegrazioniSincronizzazioneAntimafia;
	@Autowired
	private ProcedimentoDao procedimentoDao;
	@Autowired
	private ProxyClient proxyClient;
	@Autowired
	private AntimafiaService antimafiaService;
	@Autowired
	private Clock clock;
	
	private EventBus eventBus;

	@Autowired
	public AntimafiaServiceImpl setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AntimafiaService#getDichiarazione(java .lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Dichiarazione getDichiarazione(Long id) throws Exception {
		Optional<A4gtDichiarazioneAntimafia> dichiarazioneInOpt = daoDichiarazioneAntimafia.findById(id);

		if (!dichiarazioneInOpt.isPresent()) {
			throw new EntityNotFoundException("Impossibile trovare dichiarazione antimafia con id ".concat(String.valueOf(id)));
		}
		return fillDichiarazioneAntimafia(dichiarazioneInOpt.get());
	}


	@Override
	@Transactional
	public Dichiarazione getDichiarazioneByCuaa(String cuaa) throws Exception {

		A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafiaConcorrenza = new A4gtDichiarazioneAntimafia();
		a4gtDichiarazioneAntimafiaConcorrenza.setA4gtAziendaAgricola(new A4gtAziendaAgricola());
		a4gtDichiarazioneAntimafiaConcorrenza.getA4gtAziendaAgricola().setCuaa(cuaa);
		List<A4gtDichiarazioneAntimafia> dichiarazioni = daoDichiarazioneAntimafia.findAll(Example.of(a4gtDichiarazioneAntimafiaConcorrenza));

		Optional<A4gtDichiarazioneAntimafia> dichiarazioneInOpt = dichiarazioni.stream().findFirst();
		if(dichiarazioni.size() > 1) {
			dichiarazioneInOpt = dichiarazioni.stream().filter(dic -> dic.getDtFine() == null).findFirst();
		}
		if (!dichiarazioneInOpt.isPresent()) {
			throw new EntityNotFoundException("Impossibile trovare dichiarazione antimafia con cuaa ".concat(cuaa));
		}
		final A4gtDichiarazioneAntimafia dichiarazioneIn = dichiarazioneInOpt.get();

		A4gdStatoDicAntimafia statoIn = dichiarazioneIn.getA4gdStatoDicAntimafia();
		if (checkStatoDichiarazione(statoIn.getIdentificativo())) {
			return fillDichiarazioneAntimafia(dichiarazioneIn);
		} else {
			return null;
		}
	}

	private boolean checkStatoDichiarazione(String identificativo) {
		switch (StatoDichiarazioneEnum.valueOf(identificativo)) {
		case PROTOCOLLATA:
		case CONTROLLO_MANUALE:
		case RIFIUTATA:
		case CONTROLLATA:
		case POSITIVO:
		case VERIFICA_PERIODICA:
			return true;
		case BOZZA:
		case CHIUSA:
		case RETTIFICATA:
		case FIRMATA:
		default:
			return false;
		}
	}

	private Dichiarazione fillDichiarazioneAntimafia(A4gtDichiarazioneAntimafia dichiarazioneIn) throws JsonProcessingException {

		Dichiarazione dichiarazioneOut = new Dichiarazione();

		@SuppressWarnings("unchecked")
		List<String> expandParams = CustomThreadLocal.getVariable("expand") != null ? (List<String>) CustomThreadLocal.getVariable("expand") : null;

		if (!CollectionUtils.isEmpty(expandParams) && expandParams.contains("pdfFirmato")) {
			Optional<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafiaCheck = dichiarazioneIn.getA4gtAllegatoDicAntimafias().stream()
					.sorted(Comparator.comparing(A4gtAllegatoDicAntimafia::getDtUploadPdfAllegato).reversed())
					.findFirst();
			BeanUtils.copyProperties(dichiarazioneIn, dichiarazioneOut);
			if (a4gtAllegatoDicAntimafiaCheck.isPresent()) {
				dichiarazioneOut.setTipoPdfFirmato(a4gtAllegatoDicAntimafiaCheck.get().getA4gdTipoAllegato().getIdentificativo());
			}
		} else {
			BeanUtils.copyProperties(dichiarazioneIn, dichiarazioneOut, "pdfFirmato");
		}

		dichiarazioneOut.setAzienda(new Azienda());
		BeanUtils.copyProperties(dichiarazioneIn.getA4gtAziendaAgricola(), dichiarazioneOut.getAzienda());
		A4gdStatoDicAntimafia statoIn = dichiarazioneIn.getA4gdStatoDicAntimafia();
		if (statoIn != null) {
			dichiarazioneOut.setStato(new StatoDic());
			BeanUtils.copyProperties(dichiarazioneIn.getA4gdStatoDicAntimafia(), dichiarazioneOut.getStato());
		}
		if (dichiarazioneIn.getDatiDichiarazione() != null) {
			dichiarazioneOut.setDatiDichiarazione(objectMapper.readValue(dichiarazioneIn.getDatiDichiarazione(), DatiDichiarazioneAntimafia.class));
			if (dichiarazioneOut.getDatiDichiarazione().getDettaglioImpresa() != null) {
				List<SoggettoImpresa> soggettiImpresa = dichiarazioneOut.getDatiDichiarazione().getDettaglioImpresa().getSoggettiImpresa();
				if (!CollectionUtils.isEmpty(soggettiImpresa)) {
					soggettiImpresa.forEach(soggettoImpresa -> {
						List<Carica> cariche = soggettoImpresa.getCarica();
						if (!CollectionUtils.isEmpty(cariche)) {
							cariche.forEach(carica -> {
								if (carica.getHref() != null && !carica.getHref().isEmpty()) {
									String idFileAllegato = carica.getHref().split("/")[4];
									Optional<A4gtAllegatoDicFamConv> a4gtAllegatoDicFamConvOpt = allegatoDicFamConvDao.findById(Long.valueOf(idFileAllegato));
									String tipoAllegato = "pdf";
									if (a4gtAllegatoDicFamConvOpt.isPresent()) {
										A4gtAllegatoDicFamConv a4gtAllegatoDicFamConv = a4gtAllegatoDicFamConvOpt.get();
										tipoAllegato = a4gtAllegatoDicFamConv.getA4gdTipoAllegato().getIdentificativo();
									}
									carica.setTipoAllegato(tipoAllegato);
								}
							});
						}
					});
				}
			}
		}

		StatoDichiarazioneEnum stato = StatoDichiarazioneEnum.valueOf(dichiarazioneOut.getStato().getIdentificativo());
		if(StatoDichiarazioneEnum.VERIFICA_PERIODICA != stato && StatoDichiarazioneEnum.RIFIUTATA != stato && dichiarazioneOut.getDtProtocollazione() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dichiarazioneOut.getDtProtocollazione());
			calendar.add(Calendar.DAY_OF_YEAR, 180);
			dichiarazioneOut.setDataScadenza(calendar.getTime());
		}

		return dichiarazioneOut;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AntimafiaService#creaDichiarazione(it. tndigitale.a4gfascicolo.dto.Dichiarazione)
	 */
	@Override
	@Transactional
	public Long creaDichiarazione(Dichiarazione dichiarazione) throws Exception {
		JsonNode datiIntegrazioniAnagraficaImpresaDettaglioCompleto = getAnagraficaImpresa(dichiarazione.getAzienda().getCuaa());

		// TODO -
		// dichiarazione.getDatiDichiarazione().getRichiedente().getCodiceFiscale()
		// obbligatorio, validare?
		String cfRichiedente = dichiarazione.getDatiDichiarazione().getRichiedente().getCodiceFiscale();
		JsonNode personeSede = datiIntegrazioniAnagraficaImpresaDettaglioCompleto.at("/datiimpresa/personesede/persona");
		JsonNode caricaTitolare = null;
		if (personeSede != null && personeSede.elements().hasNext()) {
			Optional<JsonNode> personaRichiedente = StreamSupport.stream(personeSede.spliterator(), false)
					.filter(personaSede -> cfRichiedente.equals(personaSede.path("personafisica").path("codicefiscale").textValue())).findFirst();
			Optional<JsonNode> personaRichiedentePersonaGiuridica = Optional.empty();
			// se la persona richiedente è stata già trovata evito di effettuare la ricerca anche tra le persone giuridiche
			if (!personaRichiedente.isPresent()) {
				personaRichiedentePersonaGiuridica = StreamSupport.stream(personeSede.spliterator(), false).filter(personaSede -> !personaSede.path("personagiuridica").isNull())
						.filter(personagiuridica -> {
							try {
								JsonNode datiIntegrazioniAnagraficaImpresaDettaglioCompletoPg = getAnagraficaImpresa(personagiuridica.path("personagiuridica").path("codicefiscale").textValue());
								JsonNode personeSedePersonaGiuridica = datiIntegrazioniAnagraficaImpresaDettaglioCompletoPg.at("/datiimpresa/personesede/persona");
								if (personeSedePersonaGiuridica != null && personeSedePersonaGiuridica.elements().hasNext()) {
									Optional<JsonNode> personaRichiedenteTrovata = StreamSupport.stream(personeSedePersonaGiuridica.spliterator(), false)
											.filter(ps -> cfRichiedente.equals(ps.path("personafisica").path("codicefiscale").textValue())).findFirst();
									return personaRichiedenteTrovata.isPresent();
								}
							} catch (Exception e) {
								throw new RuntimeException("Errore chiamata servizio Anagrafica Impresa", e);
							}
							return false;
						}).findFirst();
			}
			if (personaRichiedente.isPresent() || personaRichiedentePersonaGiuridica.isPresent()) {
				JsonNode cariche = personaRichiedente.isPresent() ? personaRichiedente.get().path("cariche").path("carica") : personaRichiedentePersonaGiuridica.get().path("cariche").path("carica");
				// TODO - valutare una tabella per la codifica delle cariche o una enum
				Optional<JsonNode> isTitolare = StreamSupport.stream(cariche.spliterator(), false).filter(caricaIn -> A4gfascicoloConstants.IS_RESPONSABILE.equals(caricaIn.path("ccarica").textValue())
						|| A4gfascicoloConstants.CARICA_RESPONSABILE.contains("-" + caricaIn.path("ccarica").textValue() + "-")).findFirst();
				// BR-CUAA-PARIX-01
				if (!isTitolare.isPresent()) {
					throw new Exception(String.format("Il Soggetto %s non ricopre il ruolo indicato per l'impresa", personaRichiedente.get().path("personafisica").path("codicefiscale").textValue()));
				}
				caricaTitolare = isTitolare.get();
			} else {
				throw new Exception("Codice Fiscale Richiedente non presente tra le persone registrate");
			}
		}
		// Recupero informazioni anagrafiche del soggetto richiedente invocando
		// il servizio di Anagrafe Tributaria
		String responseIntegrazioniAnagrafeTributaria = restTemplate.getForObject(new URI(urlIntegrazioniAnagrafeTributaria.concat(cfRichiedente)), String.class);
		JsonNode resultIntegrazioniAnagrafeTributaria = objectMapper.readTree(responseIntegrazioniAnagrafeTributaria);
		JsonNode codiceEsito = resultIntegrazioniAnagrafeTributaria.path(ESITO).path("codice");
		// BR-CF-ANAGTRIB-01 : soggetti censiti in anagrafe tributaria
		if (!codiceEsito.textValue().equals("012")) {
			logger.error("il soggetto ".concat(cfRichiedente).concat(" non è censito in Anagrafe Tributaria"));
			throw new Exception("il soggetto ".concat(cfRichiedente).concat(" non è censito in Anagrafe Tributaria"));
		}
		JsonNode risposta = resultIntegrazioniAnagrafeTributaria.path("risposta");
		JsonNode persona = risposta.path("persona");
		JsonNode personaFisicaElement = persona.path("personaFisica");
		JsonNode personaFisica = personaFisicaElement;
		Richiedente richiedente = richiedenteBuilder.build(personaFisica, caricaTitolare);
		JsonNode datiImpresa = datiIntegrazioniAnagraficaImpresaDettaglioCompleto.path("datiimpresa");
		DettaglioImpresa dettaglioImpresa = dettaglioImpresaBuilder.build(datiImpresa);
		// inizio - ricerca dettaglio aziende collegate
		logger.debug("Recupero dati aziende collegate");
		dettaglioImpresa.getAziendeCollegate().forEach(aziendeCollegateConsumer);
		// fine

		DatiDichiarazioneAntimafia datiDichiarazioneAntimafia = dichiarazione.getDatiDichiarazione();
		datiDichiarazioneAntimafia.setRichiedente(richiedente);
		datiDichiarazioneAntimafia.setDettaglioImpresa(dettaglioImpresa);

		dichiarazione.setDatiDichiarazione(datiDichiarazioneAntimafia);
		A4gtDichiarazioneAntimafia dichiarazioneToInsert = new A4gtDichiarazioneAntimafia();
		BeanUtils.copyProperties(dichiarazioneToInsert, dichiarazione);
		if (dichiarazione.getAzienda() != null) {
			dichiarazioneToInsert.setA4gtAziendaAgricola(new A4gtAziendaAgricola());
			BeanUtils.copyProperties(dichiarazione.getAzienda(), dichiarazioneToInsert.getA4gtAziendaAgricola());
			Optional<A4gtAziendaAgricola> optAzienda = daoAziendaAgricola.findOne(Example.of(dichiarazioneToInsert.getA4gtAziendaAgricola()));
			if (optAzienda.isPresent()) {
				dichiarazioneToInsert.setA4gtAziendaAgricola(optAzienda.get());
			} else {
				A4gtAziendaAgricola aziendaSalvata = daoAziendaAgricola.save(dichiarazioneToInsert.getA4gtAziendaAgricola());
				dichiarazioneToInsert.setA4gtAziendaAgricola(aziendaSalvata);
			}
		}
		A4gdStatoDicAntimafia statoBozza = new A4gdStatoDicAntimafia();
		statoBozza.setIdentificativo(A4gfascicoloConstants.STATO_BOZZA);
		if (dichiarazione.getStato() != null) {
			dichiarazioneToInsert.setA4gdStatoDicAntimafia(new A4gdStatoDicAntimafia());
			BeanUtils.copyProperties(dichiarazione.getStato(), dichiarazioneToInsert.getA4gdStatoDicAntimafia());
			Optional<A4gdStatoDicAntimafia> statoD = daoStatoDichiarazioneAntimafia.findOne(Example.of(statoBozza));
			if (statoD.isPresent()) {
				dichiarazioneToInsert.setA4gdStatoDicAntimafia(statoD.get());
			}
		}
		if (dichiarazione.getDatiDichiarazione() != null) {
			dichiarazioneToInsert.setDatiDichiarazione(objectMapper.writeValueAsString(dichiarazione.getDatiDichiarazione()));
		}
		// non deve essere possibile creare due domande amf in stato bozza aggiungere controllo su pulsante crea domanda
		A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafiaConcorrenza = new A4gtDichiarazioneAntimafia();
		a4gtDichiarazioneAntimafiaConcorrenza.setA4gtAziendaAgricola(new A4gtAziendaAgricola());
		a4gtDichiarazioneAntimafiaConcorrenza.getA4gtAziendaAgricola().setCuaa(dichiarazioneToInsert.getA4gtAziendaAgricola().getCuaa());
		a4gtDichiarazioneAntimafiaConcorrenza.setA4gdStatoDicAntimafia(statoBozza);
		List<A4gtDichiarazioneAntimafia> listaDichiarazioniInBozza = daoDichiarazioneAntimafia.findAll(Example.of(a4gtDichiarazioneAntimafiaConcorrenza));
		if (CollectionUtils.isEmpty(listaDichiarazioniInBozza)) {
			A4gtDichiarazioneAntimafia dichiarazioneNew = daoDichiarazioneAntimafia.save(dichiarazioneToInsert);
			return dichiarazioneNew.getId();
		} else {
			return listaDichiarazioniInBozza.get(0).getId();
		}
	}

	@Override
	public JsonNode getAnagraficaImpresa(String cuaa) throws URISyntaxException, IOException, Exception, UnsupportedEncodingException, JsonProcessingException {
		// TODO - dichiarazione.getAzienda().getCuaa() obbligatorio, validare?
		String responseIntegrazioniAnagraficaImpresa = restTemplate.getForObject(new URI(urlIntegrazioniAnagraficaImpresa.concat(cuaa)), String.class);

		// mapping dei dati di Anagrafe Tributaria su Richiedente
		JsonNode jsonIntegrazioniAnagraficaImpresa = objectMapper.readTree(responseIntegrazioniAnagraficaImpresa);
		JsonNode header = jsonIntegrazioniAnagraficaImpresa.path("header");
		String esitoIntegrazioniAnagraficaImpresa = header.path(ESITO).textValue();
		JsonNode datiIntegrazioniAnagraficaImpresa = jsonIntegrazioniAnagraficaImpresa.path("dati");
		// TODO - gestire codici servizio esterno, costanti? enum?
		if (!esitoIntegrazioniAnagraficaImpresa.equals("OK")) {
			JsonNode errore = datiIntegrazioniAnagraficaImpresa.path("errore");
			String messaggio = errore.path("msgerr").textValue();
			logger.error("[EXT] - ".concat(messaggio));
			throw new Exception("L'impresa ".concat(cuaa).concat(" non risulta attiva presso la Camera di Commercio"));
		}

		JsonNode datiIscrizioneREA;
		try {
			JsonNode estremiImpresa = datiIntegrazioniAnagraficaImpresa.path("listaimprese").path("estremiimpresa");
			if (estremiImpresa.elements().hasNext()) {
				JsonNode estremiImpresaNode = estremiImpresa.elements().next();
				JsonNode datiIscrizioneREAArray = estremiImpresaNode.path("datiiscrizionerea");
				if (!datiIscrizioneREAArray.elements().hasNext()) {
					throw new Exception("Dati iscrizione azienda non trovati");
				}
				datiIscrizioneREA = StreamSupport.stream(datiIscrizioneREAArray.spliterator(), false)
						.filter(datiIscrizione -> "TN".equals(datiIscrizione.path("cciaa").textValue()))
						.findFirst()
						.orElseThrow(() -> new Exception("Nessuna iscrizione alla Camera di Commercio trovata"));
			} else {
				throw new Exception("Impresa non trovata");
			}
		} catch (NullPointerException e) {
			throw new Exception("Si è verificato un errore durante il recupero dei dati dell'azienda");
		}

		String numeroREASede = datiIscrizioneREA.path("nrea").textValue();
		String provinciaSede = datiIscrizioneREA.path("cciaa").textValue();
		ObjectNode inputRicerca = objectMapper.createObjectNode();
		inputRicerca.put("provinciaSede", provinciaSede);
		inputRicerca.put("numeroREASede", numeroREASede);
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(inputRicerca), StandardCharsets.UTF_8.name()));
		String responseIntegrazioniAnagraficaImpresaDettaglioCompleto = restTemplate.getForObject(new URI(urlIntegrazioniAnagraficaImpresa.concat("dettagliocompleto").concat("/").concat(params)),
				String.class);
		JsonNode jsonIntegrazioniAnagraficaImpresaDettaglioCompleto = objectMapper.readTree(responseIntegrazioniAnagraficaImpresaDettaglioCompleto);
		JsonNode headerIntegrazioniAnagraficaImpresaDettaglioCompleto = jsonIntegrazioniAnagraficaImpresaDettaglioCompleto.path("header");
		String esitoIntegrazioniAnagraficaImpresaDettaglioCompleto = headerIntegrazioniAnagraficaImpresaDettaglioCompleto.path(ESITO).textValue();
		JsonNode datiIntegrazioniAnagraficaImpresaDettaglioCompleto = jsonIntegrazioniAnagraficaImpresaDettaglioCompleto.path("dati");
		// TODO - gestire codici servizio esterno, costanti? enum?
		if (!esitoIntegrazioniAnagraficaImpresaDettaglioCompleto.equals("OK")) {
			JsonNode errore = datiIntegrazioniAnagraficaImpresaDettaglioCompleto.path("errore");
			String messaggio = errore.path("msgerr").textValue();
			logger.error("[EXT] - ".concat(messaggio));
			throw new Exception("L'impresa ".concat(cuaa).concat(" non risulta attiva presso la Camera di Commercio"));
		}
		return datiIntegrazioniAnagraficaImpresaDettaglioCompleto;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AntimafiaService#aggiornaDichiarazione (java.lang.Long, Dichiarazione)
	 */
	@Override
	@Transactional
	public AggiornaDichiarazioneEsito aggiornaDichiarazione(Dichiarazione dichiarazione) throws Exception {
		AggiornaDichiarazioneEsito aggiornaDichiarazioneEsito = new AggiornaDichiarazioneEsito();
		A4gtDichiarazioneAntimafia dichiarazioneToUpdate = daoDichiarazioneAntimafia.getOne(dichiarazione.getId());
		// Agiornamento stato.
		// Se in input esiste l'identificativo dello stato allora viene eseguito un controllo di esistenza sul DB.
		// In caso affermativo viene comparato con lo stato corrente della dichiarazione per determinare se effettuare o meno un aggiornamento.
		if (dichiarazione.getStato() != null) {
			String stateMaybeToUpdate = dichiarazione.getStato().getIdentificativo();
			if (Objects.nonNull(stateMaybeToUpdate)) {
				A4gdStatoDicAntimafia input = new A4gdStatoDicAntimafia();
				input.setIdentificativo(stateMaybeToUpdate);
				Optional<A4gdStatoDicAntimafia> checkStateToUpdate = daoStatoDichiarazioneAntimafia.findOne(Example.of(input));
				if (checkStateToUpdate.isPresent()) {
					A4gdStatoDicAntimafia stateToUpdate = checkStateToUpdate.get();
					if (dichiarazioneToUpdate.getA4gdStatoDicAntimafia() != null) {
						Long currentSateId = dichiarazioneToUpdate.getA4gdStatoDicAntimafia().getId();
						if (!stateToUpdate.getId().equals(currentSateId)) {
							dichiarazioneToUpdate.setA4gdStatoDicAntimafia(stateToUpdate);
							dichiarazione.setStato(new StatoDic());
							BeanUtils.copyProperties(dichiarazioneToUpdate.getA4gdStatoDicAntimafia(), dichiarazione.getStato());
						}
					}
				}
			}
		}
		BeanUtils.copyProperties(dichiarazione, dichiarazioneToUpdate, "pdfFirmato", "dtUploadPdfFirmato", "version");
		dichiarazioneToUpdate.setDatiDichiarazione(dichiarazione.getDatiDichiarazione() != null ? objectMapper.writeValueAsString(dichiarazione.getDatiDichiarazione()) : null);
		byte[] filePdf = dichiarazione.getPdfFirmato();
		if (filePdf != null && dichiarazione.getPdfFirmatoName() != null) {
			Date in = new Date();
			LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
			Date uploadDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			dichiarazioneToUpdate.setDtUploadPdfFirmato(uploadDate);
			dichiarazioneToUpdate.setPdfFirmato(dichiarazione.getPdfFirmato());
			ByteArrayResource byteAsResource = new ByteArrayResource(filePdf) {
				@Override
				public String getFilename() {
					return dichiarazione.getPdfFirmatoName();
				}
			};

			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
			bodyMap.add("documentoFirmato", byteAsResource);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
			ResponseEntity<String> response = restTemplate.exchange(urlIntegrazioniVerificaFirma, HttpMethod.POST, requestEntity, String.class);
			JsonNode jsonVerificaFirma = objectMapper.readTree(response.getBody());
			JsonNode warningFaults = jsonVerificaFirma.path("warningFault");
			if (warningFaults.size() > 0) {
				JsonNode errorCode = warningFaults.get(0).path("errorCode");
				JsonNode errorMsg = warningFaults.get(0).path("errorMsg");
				aggiornaDichiarazioneEsito.setEsito(String.format("%s:%s", errorCode.textValue(), errorMsg.textValue()));
				return aggiornaDichiarazioneEsito;
			}
		}
		A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia = daoDichiarazioneAntimafia.save(dichiarazioneToUpdate);

		// gestione del salvataggio tipologia file
		if (dichiarazione.getTipoPdfFirmato() != null) {

			A4gdTipoAllegato a4gdTipoAllegatoFilter = new A4gdTipoAllegato();
			a4gdTipoAllegatoFilter.setIdentificativo(dichiarazione.getTipoPdfFirmato());
			A4gdTipoAllegato a4gdTipoAllegatoCheck = tipoAllegatoDao.findOne(Example.of(a4gdTipoAllegatoFilter))
					.orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna tipologia di file traovata per l'estensione %s ", dichiarazione.getTipoPdfFirmato())));

			A4gtAllegatoDicAntimafia a4gtAllegatoDicAntimafia = new A4gtAllegatoDicAntimafia();
			a4gtAllegatoDicAntimafia.setA4gtDichiarazioneAntimafia(a4gtDichiarazioneAntimafia);
			a4gtAllegatoDicAntimafia.setDtUploadPdfAllegato(dichiarazioneToUpdate.getDtUploadPdfFirmato());
			a4gtAllegatoDicAntimafia.setA4gdTipoAllegato(a4gdTipoAllegatoCheck);

			allegatoDicAntimafiaDao.save(a4gtAllegatoDicAntimafia);
		}
		BeanUtils.copyProperties(a4gtDichiarazioneAntimafia, dichiarazione);
		aggiornaDichiarazioneEsito.setDichiarazione(dichiarazione);

		return aggiornaDichiarazioneEsito;
	}

	@Override
	@Transactional
	public AggiornaDichiarazioneEsito chiudiRecreaDichiarazione(Dichiarazione daChiudere,Dichiarazione exNovo) throws Exception {

		daChiudere.getStato().setIdentificativo(StatoDichiarazioneEnum.CHIUSA.name());
		daChiudere.setDtFine(clock.nowDate());
		AggiornaDichiarazioneEsito esitoAggiorna = this.aggiornaDichiarazione(daChiudere);
		esitoAggiorna.setCreataNuovaDichiarazione(false);

		// sincronizza dati sian. get sincronizzazione , aggiorna campi e poi put nuovamente 
		it.tndigitale.a4g.proxy.client.model.Dichiarazione sinc = proxyClient.getSincronizzazioneAntimafia(daChiudere.getId());
		sinc.setDataFineVali(DATE_FORMAT.format(daChiudere.getDtFine()));
		proxyClient.putSincronizzazioneAntimafia(daChiudere.getId(), sinc);

		if(exNovo != null) {

			Azienda azienda = exNovo.getAzienda();
			DatiDichiarazioneAntimafia datiDichiarazione = exNovo.getDatiDichiarazione();
			BeanUtils.copyProperties(exNovo, daChiudere);

			exNovo.setId(null);
			exNovo.setAzienda(azienda);
			exNovo.setDatiDichiarazione(datiDichiarazione);

			Long idNewDichiarazione = this.creaDichiarazione(exNovo);
			exNovo.setId(idNewDichiarazione);
			esitoAggiorna.setDichiarazione(exNovo);
			esitoAggiorna.setEsito(null);
			esitoAggiorna.setCreataNuovaDichiarazione(true);
		}

		return esitoAggiorna;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AntimafiaService# aggiornaPdfDichiarazione(java.lang.Long)
	 */
	@Override
	@Transactional
	public Long aggiornaPdfDichiarazione(Long id) throws Exception {
		A4gtDichiarazioneAntimafia dichiarazioneToUpdate = daoDichiarazioneAntimafia.getOne(id);
		Dichiarazione dichiarazione = new Dichiarazione();
		BeanUtils.copyProperties(dichiarazioneToUpdate, dichiarazione);
		dichiarazioneToUpdate.setPdfGenerato(dichiarazione.getPdfGenerato());
		dichiarazioneToUpdate.setDtGenerazionePdf(dichiarazione.getDtGenerazionePdf());
		daoDichiarazioneAntimafia.save(dichiarazioneToUpdate);
		return dichiarazioneToUpdate.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AntimafiaService#getDichiarazioni(it. tndigitale.a4gfascicolo.dto.Dichiarazione)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Dichiarazione> getDichiarazioni(DichiarazioneFilter dichiarazioneFilter) throws Exception {
		A4gtDichiarazioneAntimafia dichiarazioneInput = new A4gtDichiarazioneAntimafia();
		BeanUtils.copyProperties(dichiarazioneFilter, dichiarazioneInput);
		dichiarazioneInput.setA4gtAziendaAgricola(new A4gtAziendaAgricola());
		BeanUtils.copyProperties(dichiarazioneFilter.getAzienda(), dichiarazioneInput.getA4gtAziendaAgricola());
		if (dichiarazioneFilter.getAzienda().getCuaa() != null || dichiarazioneFilter.getAzienda().getId() != null) {
			Optional<A4gtAziendaAgricola> optAzienda = daoAziendaAgricola.findOne(Example.of(dichiarazioneInput.getA4gtAziendaAgricola()));
			if (optAzienda.isPresent()) {
				dichiarazioneInput.setA4gtAziendaAgricola(optAzienda.get());
			} else {
				A4gtAziendaAgricola aziendaSalvata = daoAziendaAgricola.save(dichiarazioneInput.getA4gtAziendaAgricola());
				dichiarazioneInput.setA4gtAziendaAgricola(aziendaSalvata);
			}
		}
		if (dichiarazioneFilter.getDatiDichiarazione() != null) {
			dichiarazioneInput.setDatiDichiarazione(objectMapper.writeValueAsString(dichiarazioneFilter.getDatiDichiarazione()));
		}
		List<A4gtDichiarazioneAntimafia> dichiarazioniA = daoDichiarazioneAntimafia.findAll(DichiarazioneAntimafiaDao.DichiarazioneAntimafiaSpecifications.build(dichiarazioneFilter));
		// TODO da capire se la Lista deve essere filtrata per stato non CHIUSA
		// .stream()
		// .filter(a4gtDichiarazioneAntimafia -> a4gtDichiarazioneAntimafia.getA4gdStatoDicAntimafia() != null
		// && !StatoDichiarazioneEnum.CHIUSA.equals(a4gtDichiarazioneAntimafia.getA4gdStatoDicAntimafia().getIdentificativo()))
		// .collect(Collectors.toList());

		List<Dichiarazione> dichiarazioniOut = new ArrayList<>(dichiarazioniA != null ? dichiarazioniA.size() : 0);
		for (A4gtDichiarazioneAntimafia dichiarazioneIn : dichiarazioniA) {
			Dichiarazione dichiarazioneOut = new Dichiarazione();
			List<String> expandParams = CustomThreadLocal.getVariable("expand") != null ? (List<String>) CustomThreadLocal.getVariable("expand") : null;

			if (!CollectionUtils.isEmpty(expandParams) && expandParams.contains("pdfFirmato")) {
				Optional<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafiaCheck = dichiarazioneIn.getA4gtAllegatoDicAntimafias().stream()
						.sorted(Comparator.comparing(A4gtAllegatoDicAntimafia::getDtUploadPdfAllegato).reversed())
						.findFirst();
				BeanUtils.copyProperties(dichiarazioneIn, dichiarazioneOut);
				if (a4gtAllegatoDicAntimafiaCheck.isPresent()) {
					dichiarazioneOut.setTipoPdfFirmato(a4gtAllegatoDicAntimafiaCheck.get().getA4gdTipoAllegato().getIdentificativo());
				}
			} else {
				BeanUtils.copyProperties(dichiarazioneIn, dichiarazioneOut, "pdfFirmato");
			}

			A4gtAziendaAgricola a4gtAziendaAgricola = dichiarazioneIn.getA4gtAziendaAgricola();
			if (a4gtAziendaAgricola != null) {
				dichiarazioneOut.setAzienda(new Azienda());
				BeanUtils.copyProperties(dichiarazioneIn.getA4gtAziendaAgricola(), dichiarazioneOut.getAzienda());
			}
			A4gdStatoDicAntimafia statoIn = dichiarazioneIn.getA4gdStatoDicAntimafia();
			if (statoIn != null) {
				dichiarazioneOut.setStato(new StatoDic());
				BeanUtils.copyProperties(dichiarazioneIn.getA4gdStatoDicAntimafia(), dichiarazioneOut.getStato());
			}
			if (dichiarazioneIn.getDatiDichiarazione() != null) {
				dichiarazioneOut.setDatiDichiarazione(objectMapper.readValue(dichiarazioneIn.getDatiDichiarazione(), DatiDichiarazioneAntimafia.class));
			}
			dichiarazioniOut.add(dichiarazioneOut);
		}

		if (dichiarazioneFilter.isFiltroUtenteEnte()) {
			return functionDichiarazioneAntimafiaDecorator.apply(dichiarazioniOut);
		}

		return dichiarazioniOut;
	}

	@Override
	public PageResultWrapper<Dichiarazione> getDichiarazioniPaginata(DichiarazionePaginataFilter dichiarazioneInput, Pagination pagination, Sort sort) throws Exception {
		List<Dichiarazione> dichiarazioniOut = new ArrayList<>();
		Pageable pageable = null;
		List<A4gtDichiarazioneAntimafia> a4gtDichiarazioni;
		Page<A4gtDichiarazioneAntimafia> a4gtDichiarazioniPage = null;
		boolean isPaginationEmpty = pagination == null || pagination.getPagSize() == null || pagination.getPagStart() == null;
		if (isPaginationEmpty) {
			a4gtDichiarazioni = daoDichiarazioneAntimafia.findAll(DichiarazioneAntimafiaSpecificationsFilter.findByCriteria(dichiarazioneInput, sort));
		} else {
			int pageNum = Math.floorDiv(Integer.valueOf(pagination.getPagStart()), Integer.valueOf(pagination.getPagSize()));
			pageable = PageRequest.of(pageNum, Integer.valueOf(pagination.getPagSize()));
			a4gtDichiarazioniPage = daoDichiarazioneAntimafia.findAll(DichiarazioneAntimafiaSpecificationsFilter.findByCriteria(dichiarazioneInput, sort), pageable);
			a4gtDichiarazioni = a4gtDichiarazioniPage.getContent();
		}
		for (A4gtDichiarazioneAntimafia a4gtDichiarazione : a4gtDichiarazioni) {
			Dichiarazione dichiarazioneOut = new Dichiarazione();
			BeanUtils.copyProperties(a4gtDichiarazione, dichiarazioneOut, "pdfFirmato");
			dichiarazioneOut.setAzienda(new Azienda());
			BeanUtils.copyProperties(a4gtDichiarazione.getA4gtAziendaAgricola(), dichiarazioneOut.getAzienda());
			dichiarazioneOut.setStato(new StatoDic());
			BeanUtils.copyProperties(a4gtDichiarazione.getA4gdStatoDicAntimafia(), dichiarazioneOut.getStato());
			if (a4gtDichiarazione.getDatiDichiarazione() != null) {
				dichiarazioneOut.setDatiDichiarazione(objectMapper.readValue(a4gtDichiarazione.getDatiDichiarazione(), DatiDichiarazioneAntimafia.class));
			}
			dichiarazioniOut.add(dichiarazioneOut);
		}
		PageResultWrapper<Dichiarazione> pageResultWrapper = new PageResultWrapper<>();
		pageResultWrapper.setResults(dichiarazioniOut);
		pageResultWrapper.setTotal(isPaginationEmpty ? dichiarazioniOut.size() : a4gtDichiarazioniPage.getTotalElements());
		pageResultWrapper.setPagSize(isPaginationEmpty ? dichiarazioniOut.size() : pageable.getPageSize());
		pageResultWrapper.setPagStart(isPaginationEmpty ? 0 : pageable.getOffset());
		return pageResultWrapper;
	}

	@Override
	@Transactional
	public AllegatoFamiliariConviventiResult allegaFamiliariConviventi(AllegatoFamiliariConviventi allegatoFamiliariConviventi, MultipartFile documento, Long idDichiarazioneAntimafia)
			throws Exception {

		logger.debug("[SERVIZIO MEMORIZZAZIONE ALLEGATO DIC CONV FAM] - Avvio memorizzazione");

		AllegatoFamiliariConviventiResult allegatoFamiliariConviventiResult = new AllegatoFamiliariConviventiResult();

		/************ Verifica Firma Digitale ********************/

		if (allegatoFamiliariConviventi.isFirmaDigitale()) {
			ByteArrayResource byteAsResource = new ByteArrayResource(documento.getBytes()) {
				@Override
				public String getFilename() {
					return documento.getOriginalFilename();
				}
			};

			MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
			bodyMap.add("documentoFirmato", byteAsResource);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
			ResponseEntity<String> response = restTemplate.exchange(urlIntegrazioniVerificaFirma, HttpMethod.POST, requestEntity, String.class);
			JsonNode jsonVerificaFirma = objectMapper.readTree(response.getBody());
			JsonNode warningFaults = jsonVerificaFirma.path("warningFault");

			if (warningFaults.size() > 0) {
				JsonNode errorCode = warningFaults.get(0).path("errorCode");
				JsonNode errorMsg = warningFaults.get(0).path("errorMsg");
				allegatoFamiliariConviventiResult.setEsito(String.format("%s:%s", errorCode.textValue(), errorMsg.textValue()));
				return allegatoFamiliariConviventiResult;
			}
			// } catch (IOException ex) {
			// logger.info("Errore nella verifica della firma digitale,impossibile salvare l' allegato dichiarazione familiari conviventi");
			// throw new Exception("Errore nella verifica della firma digitale,impossibile salvare l' allegato dichiarazione familiari conviventi", ex);
			// }
		}

		try {

			LocalDateTime ldt = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
			allegatoFamiliariConviventi.setDtPdfDichFamConv(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
			A4gtAllegatoDicFamConv a4gtAllegatoDicFamConv = new A4gtAllegatoDicFamConv();
			// check esistenza id, in caso affermativo carico l'entity dal DB
			if (allegatoFamiliariConviventi.getId() != null) {
				a4gtAllegatoDicFamConv = allegatoDicFamConvDao.getOne(allegatoFamiliariConviventi.getId());
			}
			BeanUtils.copyProperties(allegatoFamiliariConviventi, a4gtAllegatoDicFamConv, "allegatoFamiliariConviventiPdf");
			a4gtAllegatoDicFamConv.setPdfDicFamConv(documento.getBytes());
			Optional<A4gtDichiarazioneAntimafia> a4gtDichiarazioneAntimafia = daoDichiarazioneAntimafia.findById(idDichiarazioneAntimafia);
			if (!a4gtDichiarazioneAntimafia.isPresent()) {
				throw new RuntimeException("Dichiarazione antimafia non trovata, con ID ".concat(idDichiarazioneAntimafia.toString()));
			}

			// Salvataggio tipo file se .pdf o pdf.p7m
			A4gdTipoAllegato a4gdTipoAllegatoFilter = new A4gdTipoAllegato();
			a4gdTipoAllegatoFilter.setIdentificativo(allegatoFamiliariConviventi.getTipoFile());
			A4gdTipoAllegato a4gdTipoAllegatoCheck = tipoAllegatoDao.findOne(Example.of(a4gdTipoAllegatoFilter))
					.orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna tipologia di file traovata per l'estensione %s ", allegatoFamiliariConviventi.getTipoFile())));
			a4gtAllegatoDicFamConv.setA4gdTipoAllegato(a4gdTipoAllegatoCheck);

			a4gtAllegatoDicFamConv.setA4gtDichiarazioneAntimafia(a4gtDichiarazioneAntimafia.get());
			a4gtAllegatoDicFamConv = allegatoDicFamConvDao.save(a4gtAllegatoDicFamConv);
			Long idAlleagtoDicFamConv = a4gtAllegatoDicFamConv.getId();

			// nella copia delle properties dall'entity al DTO, viene ignorato il byte array dell'entity
			BeanUtils.copyProperties(a4gtAllegatoDicFamConv, allegatoFamiliariConviventi, "pdfDicFamConv");
			allegatoFamiliariConviventi.setTipoFile(a4gdTipoAllegatoCheck.getIdentificativo());
			allegatoFamiliariConviventiResult.setAllegatoFamiliariConviventi(allegatoFamiliariConviventi);

			/** Inizio aggiornamento della tabella AllegatoDicConvFam */

			List<SoggettoImpresa> listSoggettiImporesaAggiornata = null;
			Dichiarazione dichiarazioneAntimafia = new Dichiarazione();
			BeanUtils.copyProperties(a4gtDichiarazioneAntimafia.get(), dichiarazioneAntimafia);
			if (a4gtDichiarazioneAntimafia.get().getDatiDichiarazione() != null) {
				dichiarazioneAntimafia.setDatiDichiarazione(objectMapper.readValue(a4gtDichiarazioneAntimafia.get().getDatiDichiarazione(), DatiDichiarazioneAntimafia.class));
			}

			if (dichiarazioneAntimafia.getDatiDichiarazione() != null && dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa() != null) {
				List<SoggettoImpresa> listSoggettiImpresa = dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa().getSoggettiImpresa();
				if (listSoggettiImpresa != null && !listSoggettiImpresa.isEmpty()) {
					listSoggettiImporesaAggiornata = listSoggettiImpresa.stream().map(soggettoImpresa -> {
						// Filtro i soggetti per CF uguale al CF di iput, il
						// primo
						// che trova lo restituisce
						if (soggettoImpresa.getCodiceFiscale().equals(allegatoFamiliariConviventi.getCfSoggettoImpresa())) {
							// clone del soggetto per modificare la carica
							// associata
							SoggettoImpresa soggettoImpresaClone = new SoggettoImpresa();
							BeanUtils.copyProperties(soggettoImpresa, soggettoImpresaClone);
							if (soggettoImpresaClone.getCarica() != null && !soggettoImpresaClone.getCarica().isEmpty()) {

								/**
								 * Inizio aggiornamento della carica associata al soggetto
								 */

								String href = String.format("/antimafia/%d/allegatoFamiliariConviventi/%d", dichiarazioneAntimafia.getId(), idAlleagtoDicFamConv);
								// come sopra ma stavolta ciclo sull'oggetto
								// clonato per modificare il valore della
								// carica associata
								List<Carica> listCariche = soggettoImpresaClone.getCarica().stream().map(carica -> {
									if (carica.getCodice().equals(allegatoFamiliariConviventi.getCodCarica())) {
										Carica caricaClone = new Carica();
										BeanUtils.copyProperties(carica, caricaClone);
										caricaClone.setTipoAllegato(a4gdTipoAllegatoCheck.getIdentificativo());
										caricaClone.setHref(href);
										allegatoFamiliariConviventiResult.setCarica(caricaClone);
										return caricaClone;
									}
									return carica;
								}).collect(Collectors.toList());

								soggettoImpresaClone.setCarica(listCariche);

								/**
								 * Fine aggiornamento della carica associata al soggetto
								 */
							}
							return soggettoImpresaClone;
						}
						return soggettoImpresa;
					}).collect(Collectors.toList());

				}
			}

			/** Fine aggiornamento della tabella AllegatoDicConvFam */

			/** Inizio aggiornamento tabella DichiarazioneAntimafia */

			if (listSoggettiImporesaAggiornata != null && !listSoggettiImporesaAggiornata.isEmpty()) {
				dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa().setSoggettiImpresa(listSoggettiImporesaAggiornata);
				ObjectMapper mapper = new ObjectMapper();
				Optional<A4gtDichiarazioneAntimafia> dichiarazioneToUpdateOptional = daoDichiarazioneAntimafia.findById(dichiarazioneAntimafia.getId());
				if (dichiarazioneToUpdateOptional.isPresent()) {
					A4gtDichiarazioneAntimafia dichiarazioneToUpdate = dichiarazioneToUpdateOptional.get();
					dichiarazioneToUpdate.setDatiDichiarazione(mapper.writeValueAsString(dichiarazioneAntimafia.getDatiDichiarazione()));
					daoDichiarazioneAntimafia.save(dichiarazioneToUpdate);
				} else {
					throw new Exception("Non è stato possibile aggiornare la Dichiarazione Antimafia dopo il caricamento del file Allegato Familiari Conviventi");
				}
			}

			/** Fine aggiornamento tabella DichiarazioneAntimafia */

		} catch (Exception ex) {

			logger.error("Impossibile salvare l' allegato dichiarazione familiari conviventi");
			throw new Exception("Impossibile salvare l' allegato dichiarazione familiari conviventi", ex);
		}

		return allegatoFamiliariConviventiResult;
	}

	@Override
	@Transactional
	public Long eliminaDichiarazione(Long id) {
		// eliminazione a cascata degli elementi collegati alla dichiarazione
		// antimafia
		logger.info("Eliminazione dichiarazione con id: {}", id);
		A4gtDichiarazioneAntimafia dichiarazioneAntimafia = daoDichiarazioneAntimafia.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna dichiarazione trovata per id: %d", id)));
		if (!A4gfascicoloConstants.STATO_BOZZA.equals(dichiarazioneAntimafia.getA4gdStatoDicAntimafia().getIdentificativo())) {
			throw new IllegalArgumentException(
					String.format("E' possibile eliminare solo dichiarazioni in stato BOZZA. Stato dichirazione attuale: %s", dichiarazioneAntimafia.getA4gdStatoDicAntimafia().getIdentificativo()));
		}
		Set<A4gtAllegatoDicAntimafia> allegatiDicAnt = dichiarazioneAntimafia.getA4gtAllegatoDicAntimafias();
		if (!CollectionUtils.isEmpty(allegatiDicAnt)) {
			logger.debug("Eliminazione di {} allegati dichiarazione ", allegatiDicAnt.size());
			allegatoDicAntimafiaDao.deleteAll(allegatiDicAnt);
		}
		Set<A4gtAllegatoDicFamConv> allegatiDicFamConv = dichiarazioneAntimafia.getA4gtAllegatoDicFamConv();
		if (!CollectionUtils.isEmpty(allegatiDicFamConv)) {
			logger.debug("Eliminazione di {} allegati dichiarazione ", allegatiDicFamConv.size());
			allegatoDicFamConvDao.deleteAll(allegatiDicFamConv);
		}
		Set<A4gtProcedimentoAmf> procedimenti = dichiarazioneAntimafia.getA4gtProcedimentoAmfs();
		if (!CollectionUtils.isEmpty(procedimenti)) {
			procedimentoDao.deleteAll(procedimenti);
		}
		daoDichiarazioneAntimafia.deleteById(id);
		logger.debug("Eliminazione dichiarazione con id {} andata a buon fine", id);
		return id;
	}

	@Override
	@Transactional
	public Dichiarazione processoProtocollazioneDomanda(Long id) throws Exception {
		ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto = saveDomanda(id);

		antimafiaService.protocollaDomanda(protocollaCertificazioneAntimafiaDto);

		Dichiarazione dichiarazioneAntimafia = protocollaCertificazioneAntimafiaDto.getDichiarazioneAntimafia();
		antimafiaService.sincronizzazioneProtocollaDomanda(dichiarazioneAntimafia);
		return dichiarazioneAntimafia;

		//        return protocollaCertificazioneAntimafiaDto.getDichiarazioneAntimafia();
	}

	@Override
	public ProtocollaCertificazioneAntimafiaDto saveDomanda(Long id) throws Exception {

		ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto = new ProtocollaCertificazioneAntimafiaDto().setIdDomandaAntimafia(id);
		logger.debug("Avvio del salvataggio dei dati della domanda Antimafia con id: {}", id);

		Optional<A4gtDichiarazioneAntimafia> dichiarazioneAntimafiaEntityOptional = daoDichiarazioneAntimafia.findById(id);
		A4gtDichiarazioneAntimafia dichiarazioneAntimafiaEntity = dichiarazioneAntimafiaEntityOptional.get();
		Assert.notNull(dichiarazioneAntimafiaEntity, String.format("Nessuna dichiarazione trovata, impossibile effettuare la protocollazione per l'id %d", id));

		Dichiarazione dichiarazioneAntimafia = new Dichiarazione();
		List<A4gtAllegatoDicFamConv> allegatiFamiliariConviventi = new ArrayList<>();

		try {
			if (dichiarazioneAntimafiaEntity.getDatiDichiarazione() != null) {
				BeanUtils.copyProperties(dichiarazioneAntimafiaEntity, dichiarazioneAntimafia);
				dichiarazioneAntimafia.setDatiDichiarazione(objectMapper.readValue(dichiarazioneAntimafiaEntity.getDatiDichiarazione(), DatiDichiarazioneAntimafia.class));
				dichiarazioneAntimafia.setAzienda(new Azienda());
				BeanUtils.copyProperties(dichiarazioneAntimafiaEntity.getA4gtAziendaAgricola(), dichiarazioneAntimafia.getAzienda());
				if (dichiarazioneAntimafiaEntity.getA4gtAllegatoDicFamConv() != null && !dichiarazioneAntimafiaEntity.getA4gtAllegatoDicFamConv().isEmpty()) {
					allegatiFamiliariConviventi = new ArrayList<>(dichiarazioneAntimafiaEntity.getA4gtAllegatoDicFamConv());
				}
			}


			// costruzione della request
			ObjectNode jsonProtocollazione = objectMapper.createObjectNode();
			if (dichiarazioneAntimafia.getDatiDichiarazione() != null) {
				// costruzione del mittente
				ObjectNode mittente = objectMapper.createObjectNode();
				Richiedente richiedente = dichiarazioneAntimafia.getDatiDichiarazione().getRichiedente();
				String cuaa = dichiarazioneAntimafia.getAzienda() != null ? dichiarazioneAntimafia.getAzienda().getCuaa() : null;
				if (richiedente != null) {
					String descrizioneImpresa = dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa() != null
							? String.format("%s - %s", cuaa, dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa().getDenominazione()) : null;
					mittente.putObject("mittente").put("name", richiedente.getNome()).put("surname", richiedente.getCognome()).put("email", richiedente.getIndirizzoPEC())
					.put("nationalIdentificationNumber", richiedente.getCodiceFiscale()).put("description", descrizioneImpresa);
					jsonProtocollazione.setAll(mittente);
				}

				List<ByteArrayResource> byteArrayResourceAllegatiList = new ArrayList<>();
				// costruzione allegati
				allegatiFamiliariConviventi.forEach(allegatoFamiliariConviventi -> {
					byteArrayResourceAllegatiList.add(new ByteArrayResource(allegatoFamiliariConviventi.getPdfDicFamConv()) {
						@Override
						public String getFilename() {
							Optional<A4gdTipoAllegato> a4gdTipoAllegatoCheckOptional = tipoAllegatoDao.findById(allegatoFamiliariConviventi.getA4gdTipoAllegato().getId());
							if (a4gdTipoAllegatoCheckOptional.isPresent()) {
								A4gdTipoAllegato a4gdTipoAllegatoCheck = a4gdTipoAllegatoCheckOptional.get();
								return String.format("%s%s.%s", "AllegatoFamiliariConviventi", allegatoFamiliariConviventi.getCfSoggettoImpresa(), a4gdTipoAllegatoCheck.getIdentificativo());
							}
							return null;
						}
					});
				});
				protocollaCertificazioneAntimafiaDto.setAllegati(byteArrayResourceAllegatiList);

				// costruzione fascicolo
				// jsonProtocollazione.put("codiceFascicolo", codiceFascicoloDomandaCertificazioneAntimafia);
				// costruzione tipologiaDocumentoProtocollazioneDomandaCertificazioneAntimafia
				jsonProtocollazione.put("tipologiaDocumentoPrincipale", tipologiaDocumentoProtocollazioneDomandaCertificazioneAntimafia);
				// costruzione oggetto
				jsonProtocollazione.put("oggetto",
						String.format("%s - %s - %s", oggettoDomandaCertificazioneAntimafia, cuaa, dichiarazioneAntimafia.getDatiDichiarazione().getDettaglioImpresa().getDenominazione()));
			}

			if (jsonProtocollazione.size() > 0) {
				String str = null;

				for (A4gtAllegatoDicAntimafia en : dichiarazioneAntimafiaEntity.getA4gtAllegatoDicAntimafias()) {
					Optional<A4gdTipoAllegato> tipoAllegatoOptional = tipoAllegatoDao.findById(en.getA4gdTipoAllegato().getId());
					if (tipoAllegatoOptional.isPresent()) {
						A4gdTipoAllegato a4gdTipoAllegato = tipoAllegatoOptional.get();
						//                        A4gdTipoAllegato a4gdTipoAllegato = tipoAllegatoDao.getOne(en.getA4gdTipoAllegato().getId());
						str = String.format("%s.%s", "DomandaCertificazioneAntimafia", a4gdTipoAllegato.getIdentificativo());
						if (str != null) {
							break;
						}
					}
				}
				final String computedStr = (str != null ? new String(str) : null);
				ByteArrayResource documentoByteAsResource = new ByteArrayResource(dichiarazioneAntimafia.getPdfFirmato()) {
					@Override
					public String getFilename() {
						return computedStr;
					}
				};

				protocollaCertificazioneAntimafiaDto.setJsonProtocollazione(jsonProtocollazione);
				protocollaCertificazioneAntimafiaDto.setDocumento(documentoByteAsResource);
			}

			// AGGIORNAMENTO DELLO STATO
			A4gdStatoDicAntimafia input = new A4gdStatoDicAntimafia();
			input.setIdentificativo(A4gfascicoloConstants.STATO_PROTOCOLLATA);
			Optional<A4gdStatoDicAntimafia> statoD = daoStatoDichiarazioneAntimafia.findOne(Example.of(input));
			if (statoD.isPresent()) {
				dichiarazioneAntimafiaEntity.setA4gdStatoDicAntimafia(statoD.get());
				dichiarazioneAntimafia.setStato(new StatoDic());
				BeanUtils.copyProperties(dichiarazioneAntimafiaEntity.getA4gdStatoDicAntimafia(), dichiarazioneAntimafia.getStato());

			}

			BeanUtils.copyProperties(daoDichiarazioneAntimafia.save(dichiarazioneAntimafiaEntity), dichiarazioneAntimafia, "pdfFirmato");
			protocollaCertificazioneAntimafiaDto.setDichiarazioneAntimafia(dichiarazioneAntimafia);

			return protocollaCertificazioneAntimafiaDto;

		} catch (Exception ex) {
			logger.error("Errore nel salvataggio dei dati della Domanda Antimafia", ex);
			throw ex;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void protocollaDomanda(ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto) throws Exception {
		ProtocollaCertificazioneAntimafiaEvent event = new ProtocollaCertificazioneAntimafiaEvent(protocollaCertificazioneAntimafiaDto);
		eventBus.publishEvent(event);
	}

	@Override
	public void sincronizzazioneProtocollaDomanda(Dichiarazione dichiarazione) throws Exception {
		//Deep copy della dichiarazione tramite Deep Copy Constructor
		Dichiarazione dichiarazioneCopy = objectMapper.readValue(objectMapper.writeValueAsString(dichiarazione), Dichiarazione.class);

		// Check se la protocollazione è andata a buon fine
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		if(dichiarazioneCopy.getDatiDichiarazione().getDettaglioImpresa().getAziendeCollegate() != null && dichiarazioneCopy.getDatiDichiarazione().getDettaglioImpresa().getAziendeCollegate().size() > 0) {
			List<AziendaCollegata> aziendeFiltered = dichiarazioneCopy.getDatiDichiarazione().getDettaglioImpresa().getAziendeCollegate().stream()
					.filter(azienda -> azienda.isSelezionato())
					.collect(Collectors.toList());
			dichiarazioneCopy.getDatiDichiarazione().getDettaglioImpresa().setAziendeCollegate(aziendeFiltered);	
		} else {
			dichiarazioneCopy.getDatiDichiarazione().getDettaglioImpresa().setAziendeCollegate(new ArrayList<AziendaCollegata>());
		}

		dichiarazioneCopy.setDtProtocollazione(new Date());
		HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(dichiarazioneCopy), headers);
		// Chiamata asincrona, non serve leggere l'esito
		restTemplate.exchange(urlIntegrazioniSincronizzazioneAntimafia, HttpMethod.POST, requestEntity, String.class);
	}

	@Override
	@Transactional(readOnly = true)
	public StatoDichiarazioneCounter countEsitoDichiarazioni(StatoDichiarazioneFilter stati) throws Exception {
		StatoDichiarazioneCounter counter = new StatoDichiarazioneCounter();
		for (String stato : stati.getStatiDichiarazione()) {
			A4gtDichiarazioneAntimafia dichiarazione = new A4gtDichiarazioneAntimafia();
			A4gdStatoDicAntimafia a4gStato = new A4gdStatoDicAntimafia();
			a4gStato.setIdentificativo(stato);
			dichiarazione.setA4gdStatoDicAntimafia(a4gStato);
			Long c = daoDichiarazioneAntimafia.count(Example.of(dichiarazione));
			counter.setStatoDichirazioneCounter(stato, c);
		}
		return counter;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] downloadAllegatoFamiliareConvivente(AllegatoFamiliareConviventeFilter allegatoFamiliareConviventeFilter) {
		try {
			A4gtDichiarazioneAntimafia dichiarazioneIn = daoDichiarazioneAntimafia.getOne(allegatoFamiliareConviventeFilter.getIdDichiarazioneAntimafia());
			if (dichiarazioneIn.getA4gtAllegatoDicFamConv() != null && !dichiarazioneIn.getA4gtAllegatoDicFamConv().isEmpty()) {
				Set<A4gtAllegatoDicFamConv> allegati = dichiarazioneIn.getA4gtAllegatoDicFamConv();
				for (A4gtAllegatoDicFamConv a4gtAllegatoDicFamConv : allegati) {
					if (a4gtAllegatoDicFamConv.getId().equals(allegatoFamiliareConviventeFilter.getIdAllegatoFamiliariConviventi())) {
						ByteArrayResource bar = new ByteArrayResource(a4gtAllegatoDicFamConv.getPdfDicFamConv()) {
							@Override
							public String getFilename() {
								A4gdTipoAllegato a4gdTipoAllegatoCheck = tipoAllegatoDao.getOne(a4gtAllegatoDicFamConv.getA4gdTipoAllegato().getId());
								return String.format("%s%s.%s", "AllegatoFamiliariConviventi", a4gtAllegatoDicFamConv.getCfSoggettoImpresa(), a4gdTipoAllegatoCheck.getIdentificativo());
							}
						};
						//						return a4gtAllegatoDicFamConv.getPdfDicFamConv();
						return bar.getByteArray();
					}
				}
				throw new EntityNotFoundException("Nessun allegato familiari conviventi con id ".concat(allegatoFamiliareConviventeFilter.getIdAllegatoFamiliariConviventi().toString()));
			} else {
				throw new EntityNotFoundException("Non ci sono allegati familiari conviventi");
			}
		} catch (EntityNotFoundException e) {
			logger.error("Errore nel recupero degli allegati familiari conviventi per la dichiarazione con id ".concat(allegatoFamiliareConviventeFilter.getIdDichiarazioneAntimafia().toString()), e);
			return new byte[0];
		}
	}

	@Override
	@Transactional
	public Long creaNota(Nota nota) throws Exception {
		A4gtNota a4gtNota = new A4gtNota();
		BeanUtils.copyProperties(nota, a4gtNota);
		a4gtNota.setTipo(nota.getTipoNota().toString());
		a4gtNota.setDataInserimento(new Date());
		notaDao.save(a4gtNota);
		return a4gtNota.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Nota> leggiNote(NotaFilter notaFilter) throws Exception {
		A4gtNota a4gtNotaIn = new A4gtNota();
		BeanUtils.copyProperties(notaFilter, a4gtNotaIn);
		List<A4gtNota> noteRet = notaDao.findAll(Example.of(a4gtNotaIn));
		if (noteRet != null && !noteRet.isEmpty()) {
			return noteRet.stream().map(a4gtNota -> {
				Nota nota = new Nota();
				BeanUtils.copyProperties(a4gtNota, nota);
				nota.setTipoNota(TipoNotaEnum.valueOf(a4gtNota.getTipo()));
				return nota;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<ProcedimentiEnum> creaProcedimenti(Long idDichiarazione, List<ProcedimentiEnum> procedimenti) {
		for (ProcedimentiEnum procedimento : procedimenti) {
			A4gtProcedimentoAmf argtProcedimento = new A4gtProcedimentoAmf();
			argtProcedimento.setProcedimento(procedimento.name());
			A4gtDichiarazioneAntimafia dichiarazione = daoDichiarazioneAntimafia.getOne(idDichiarazione);
			procedimentoDao.deleteAll(dichiarazione.getA4gtProcedimentoAmfs());
			argtProcedimento.setA4gtDichiarazioneAntimafia(dichiarazione);
			procedimentoDao.save(argtProcedimento);
		}
		return procedimenti;
	}

	@Override
	public List<ProcedimentiEnum> recuperaProcedimenti(Long idDichiarazione) {
		A4gtProcedimentoAmf argtProcedimento = new A4gtProcedimentoAmf();
		A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia = new A4gtDichiarazioneAntimafia();
		a4gtDichiarazioneAntimafia.setId(idDichiarazione);
		argtProcedimento.setA4gtDichiarazioneAntimafia(a4gtDichiarazioneAntimafia);
		List<A4gtProcedimentoAmf> a4gtProcedimenti = procedimentoDao.findAll(Example.of(argtProcedimento));
		List<ProcedimentiEnum> procedimenti = new ArrayList<>();
		for (A4gtProcedimentoAmf a4gtProcedimentoAmf : a4gtProcedimenti) {
			procedimenti.add(ProcedimentiEnum.valueOf(a4gtProcedimentoAmf.getProcedimento()));
		}
		return procedimenti;
	}

	@Override
	public byte[] creazioneCsv(final List<Dichiarazione> dichiarazioniAntimafia) throws IOException {
		List<DichiarazioniAntimafiaCsv> dichiarazioniAntimafiaCsv = dichiarazioniAntimafia.stream()
				.map(domanda -> new DichiarazioniAntimafiaCsv(
						domanda.getAzienda().getCuaa(),
						domanda.getStato().getDescrizione(),
						domanda.getDatiDichiarazione().getDettaglioImpresa().getDenominazione()))
				.collect(Collectors.toList());
		// creazione file CSV in memoria
		CsvSchema dichiarazioniAntimafiaSchema = CsvSchema.builder()
				.addColumn("CUAA", CsvSchema.ColumnType.STRING)
				.addColumn("DESCRIZIONE_IMPRESA", CsvSchema.ColumnType.STRING)
				.addColumn("STATO", CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
		CsvMapper mapper = new CsvMapper();
		ObjectWriter domandeCollegateWriter = mapper
				.writerFor(DichiarazioniAntimafiaCsv.class)
				.with(dichiarazioniAntimafiaSchema);
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		domandeCollegateWriter.writeValues(csvByteArray).writeAll(dichiarazioniAntimafiaCsv);
		return csvByteArray.toByteArray();
	}

	@Override
	@Transactional
	public void saveOrUpdateDichiarazioneAntimafia(Long idDomandaAntimafia, String numeroProtocollazione, Date dataProtocollazione) throws Exception{
		Optional<A4gtDichiarazioneAntimafia> dichiarazioneAntimafiaOptional = daoDichiarazioneAntimafia.findById(idDomandaAntimafia);
		if (dichiarazioneAntimafiaOptional.isPresent()) {
			A4gtDichiarazioneAntimafia dichiarazioneAntimafiaEntity = dichiarazioneAntimafiaOptional.get();
			dichiarazioneAntimafiaEntity.setIdProtocollo(numeroProtocollazione);
			dichiarazioneAntimafiaEntity.setDtProtocollazione(new Date());
			daoDichiarazioneAntimafia.save(dichiarazioneAntimafiaEntity);
		}
	}
}
