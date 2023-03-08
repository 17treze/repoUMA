package it.tndigitale.a4gistruttoria.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4gistruttoria.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.Ruoli;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.domandaunica.RichiestaSuperficie;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DomandaUnicaDettaglio;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiEredeDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiFiltroDomandaDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiLavorazioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiPascoloDao;
import it.tndigitale.a4gistruttoria.repository.dao.DichiarazioneDomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaIntegrativaDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ElencoLiquidazioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.ErroreRicevibilitaDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.dao.InterventoDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PascoloParticellaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.dao.SostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiFiltroDomanda;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiPascolo;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisModel;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Quadro;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.DichiarazioniDomandaUnicaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.SuperficiImpegnateService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.strategy.DatiDomanda;
import it.tndigitale.a4gistruttoria.strategy.DatiDomandaStrategy;
import it.tndigitale.a4gistruttoria.util.AmbitoVariabile;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.LocalDateConverter;
import it.tndigitale.a4gistruttoria.util.TipoFiltroDomanda;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@EnableConfigurationProperties(A4gistruttoriaConfigurazione.class)
@Service
@Transactional
public class DomandeServiceImpl implements DomandeService {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(DomandeServiceImpl.class);

	private static final String DISACCOPPIATO = "richiestaDisaccoppiato";
	private static final String ACCOPPIATO_SUPERFICIE = "richiestaSuperfici";
	private static final String ACCOPPIATO_ZOOTECNIA = "richiestaZootecnia";
	private static final String RISERVA_NON_RICHIESTA = "NON RICHIESTA";

	/**
	 * Codifica Esito:
	 * 2 - da convocare;
	 * 3 - convocata;
	 * 4 - chiusa;
	 * 5 - verbalizzata;
	 * 7 - convocata-sospesa;
	 * 10 - sospesa;
	 **/
	private final static int ESITO_SIGECO_CHIUSA = 4;

	private final static int ESITO_SIGECO_VERBALIZZATA = 5;
	

	@Autowired
	private UtenteComponent utente;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private DatiFiltroDomandaDao daoDatiFiltroDomanda;
	@Autowired
	private DatiLavorazioneDao daoDatiLavorazione;
	@Autowired
	private SostegnoDao daoSostegno;
	@Autowired
	private ErroreRicevibilitaDao daoEsitoControllo;
	@Autowired
	private InterventoDao daoInterventoDu;
	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private AllevamentoImpegnatoDao daoRichiestaAllevamDu;
	@Autowired
	private DatiPascoloDao daoDatiPascolo;
	@Autowired
	private DichiarazioneDomandaUnicaDao dichiarazioneDomandaUnicaDao;
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	@Autowired
	private PassoTransizioneDao daoPassiLavSostegno;
	@Autowired
	private ElencoLiquidazioneDao daoElencoLiquidazione;
	@Autowired
	private PascoloParticellaDao daoPascoloParticella;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;
	@Autowired
	private ElencoPascoliService elencoPascoliService;
	@Autowired
	private DatiDomandaStrategy datiDomandaStrategy;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DomandaIntegrativaDao domandaIntegrativaDao;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Autowired
	private DatiEredeDao datiEredeDao;
	@Autowired
	private DichiarazioniDomandaUnicaService dichiarazioniService;
	@Autowired 
	private SuperficiImpegnateService superficiService;
	@Autowired
	private DisaccoppiatoService disaccoppiatoService;
	@Autowired
	private TransizioneIstruttoriaService transizioneService;
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@Autowired
	private UtenteClient utenteClient;

	@Value("${a4gistruttoria.proxy.agricoltore.uri}")
	private String agricoltoreAttivoUrl;
	@Value("${a4gistruttoria.proxy.sigeco.uri}")
	private String esitoSigecoUrl;
	
	private LinkedHashMap<Ruoli, Supplier<List<DatiDomandaIbanErrato>>> mappaLogica;
	
	public DomandeServiceImpl() {
		mappaLogica = new LinkedHashMap<>();
		mappaLogica.put(Ruoli.RICERCA_DOMANDE_NON_FILTRATA, this::getDatiDomandeIbanErratoNonFiltrata);
		mappaLogica.put(Ruoli.RICERCA_DOMANDE_FILTRO_ENTE, this::getDatiDomandeIbanErratoByCaa);
	}

	/**
	 * Metodo che recupera da proxy le informazioni relative ai titoli ed alle info generali dell'agricoltore
	 * 
	 * @throws Exception
	 */
	@Override
	public AgricoltoreSIAN recuperaInfoAgricoltoreSIAN(BigDecimal numeroDomanda) throws Exception {
		String resource = null;
		try {
			DomandaUnicaModel domandaUnicaModel = daoDomanda.findByNumeroDomanda(numeroDomanda);
			String codFiscale = domandaUnicaModel.getCuaaIntestatario();
			String jsonRequest = 
					"{ \"codFisc\":\"" + codFiscale + 
					"\", \"annoCamp\":" + domandaUnicaModel.getCampagna().toString() + "}";

			resource = agricoltoreAttivoUrl.concat("?params=").concat(URLEncoder.encode(jsonRequest, "UTF-8"));

			return restTemplate.getForObject(new URI(resource), AgricoltoreSIAN.class);
		} catch (Exception e) {
			logger.error("Errore recuperando info agricoltore sian da resource = {} per l'utente {}",
					resource, utente.utenza(), e);
			throw e;
		}
	}
	/**
	 * Metodo che prende le domande da ags e le inserisce nel database di istruttoria impostandole in stato ACQUISITA
	 */
	@Override
	@Transactional
	public DomandaUnica recuperaSostegniDomandaDU(DomandaUnicaModel domanda) {
		try {
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat(domanda.getNumeroDomanda().toString())
					.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
			DomandaUnica domandaUnica = restTemplate.getForObject(new URI(resource), DomandaUnica.class);
			if (domandaUnica != null) {
				Richieste richieste = domandaUnica.getRichieste();
				if (richieste != null) {
					if (richieste.getSostegniSuperficie() != null) {
						richieste.getSostegniSuperficie().forEach(sostegnoSuperficie -> {
							Long value = Long.valueOf(0);
							try {
								value = persistiSostegniSuperfici(domanda, sostegnoSuperficie);
							} catch (JsonProcessingException e) {
								logger.error("recuperaSostegniDomandaDU: errore per la domanda " + domanda.getId(), e);
								throw new RuntimeException(e);
							}
							logger.debug("Persistenza sostegno superficie: {}", value);
						});
					}

					if (richieste.getSostegniAllevamento() != null) {
						richieste.getSostegniAllevamento().forEach(sostegnoAllevamento -> {
							Long value = Long.valueOf(0);
							try {
								value = persistiSostegniAllevamento(domanda, sostegnoAllevamento);
							} catch (JsonProcessingException e) {
								throw new RuntimeException(e);
							}
							logger.debug("Persistenza sostegno allevamento: {}", value);
						});
					}

					if (richieste.getDatiPascolo() != null) {
						richieste.getDatiPascolo().forEach(datoPascolo -> {
							Long value = Long.valueOf(0);
							try {
								value = persistiDatiPascolo(domanda, datoPascolo);
							} catch (JsonProcessingException e) {
								throw new RuntimeException(e);
							}
							logger.debug("Persistenza dati pascolo: {}", value);
						});
					}

					if (richieste.getDichiarazioniDomandaUnica() != null) {
						richieste.getDichiarazioniDomandaUnica().forEach(dichirazione -> {
							Long value = persistiDichiarazioniDomandaUnica(domanda, dichirazione);
							logger.debug("Persistenza dichirazione: {}", value);
						});
					}
				}
			} else {
				throw new EntityNotFoundException("Nessun dto Richieste trovato");
			}
			return domandaUnica;
		} catch (ResourceAccessException ce) {
			logger.error("recuperaSostegniDomandaDU: per la domanda con id " + domanda.getId() + " per l'utente " + utente.utenza(), ce);
			throw new RuntimeException("Impossibile contattare il servizio AGS", ce);
		} catch (DataIntegrityViolationException de) {
			logger.error("recuperaSostegniDomandaDU: per la domanda con id " + domanda.getId() + " per l'utente " + utente.utenza(), de);
			throw new RuntimeException("Vincoli su database non rispettati", de);
		} catch (Exception e) {
			logger.error("recuperaSostegniDomandaDU: per la domanda con id " + domanda.getId() + " per l'utente " + utente.utenza(), e);
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	// Salvataggio dei dati in AG4T_RICHIESTE_SUPERFICIE
	private Long persistiSostegniSuperfici(DomandaUnicaModel domanda, SostegniSuperficieDto sostegniSuperficie) throws JsonProcessingException {

		A4gtRichiestaSuperficie richSuperficie = new A4gtRichiestaSuperficie();
		richSuperficie.setDomandaUnicaModel(domanda);
		richSuperficie.setIntervento(daoInterventoDu.findByIdentificativoIntervento(sostegniSuperficie.getCodIntervento()));
		richSuperficie.setSupRichiesta(new BigDecimal(sostegniSuperficie.getSupImpegnata()));
		richSuperficie.setSupRichiestaNetta(new BigDecimal(sostegniSuperficie.getSupImpegnata() * sostegniSuperficie.getCoeffTara()));
		richSuperficie.setCodiceColtura3(sostegniSuperficie.getCodColtura3());
		richSuperficie.setCodiceColtura5(sostegniSuperficie.getCodColtura5());
		richSuperficie.setInfoCatastali(mapper.writeValueAsString(sostegniSuperficie.getParticella()));
		InformazioniColtivazione infoColtivazione = new InformazioniColtivazione();
		infoColtivazione.setCodColtura3(sostegniSuperficie.getCodColtura3());
		infoColtivazione.setCodColtura5(sostegniSuperficie.getCodColtura5());
		infoColtivazione.setCodLivello(sostegniSuperficie.getCodLivello());
		infoColtivazione.setCoefficienteTara(sostegniSuperficie.getCoeffTara());
		infoColtivazione.setDescrizioneColtura(sostegniSuperficie.getDescColtura());
		infoColtivazione.setIdColtura(sostegniSuperficie.getIdColtura());
		infoColtivazione.setIdPianoColture(sostegniSuperficie.getIdPianoColture());
		infoColtivazione.setSuperficieDichiarata(sostegniSuperficie.getSupDichiarata());
		infoColtivazione.setDescMantenimento(sostegniSuperficie.getDescMantenimento());
		richSuperficie.setInfoColtivazione(mapper.writeValueAsString(infoColtivazione));
		RiferimentiCartografici rifCartografici = new RiferimentiCartografici();
		rifCartografici.setIdParcella(sostegniSuperficie.getIdParcella());
		rifCartografici.setIdIsola(sostegniSuperficie.getIdIsola());
		rifCartografici.setCodIsola(sostegniSuperficie.getCodIsola());
		richSuperficie.setRiferimentiCartografici(mapper.writeValueAsString(rifCartografici));
		daoRichiestaSuperficie.save(richSuperficie);
		return richSuperficie.getId();
	}

	// Salvataggio dei dati in A4GT_RICHIESTE_ALLEVAM_DU
	private Long persistiSostegniAllevamento(DomandaUnicaModel domanda, SostegniAllevamentoDto sostegniAllevamenti) throws JsonProcessingException {
		AllevamentoImpegnatoModel allevamentoImpegnato = new AllevamentoImpegnatoModel();
		allevamentoImpegnato.setDomandaUnica(domanda);
		allevamentoImpegnato.setIntervento(daoInterventoDu.findByIdentificativoIntervento(sostegniAllevamenti.getCodIntervento()));
		allevamentoImpegnato.setCodiceSpecie(sostegniAllevamenti.getSpecie());
		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		infoAllevamento.setCodiceAllevamento(sostegniAllevamenti.getCodIdAllevamento());
		infoAllevamento.setCodiceAllevamentoBdn(sostegniAllevamenti.getCodIdBdn());
		infoAllevamento.setComune(sostegniAllevamenti.getComune());
		infoAllevamento.setDescrizioneAllevamento(sostegniAllevamenti.getDescAllevamento());
		infoAllevamento.setIdAllevamento(sostegniAllevamenti.getIdAllevamento());
		infoAllevamento.setIndirizzo(sostegniAllevamenti.getIndirizzo());
		allevamentoImpegnato.setDatiAllevamento(mapper.writeValueAsString(infoAllevamento));

		DatiProprietario datiProprietario = new DatiProprietario();
		datiProprietario.setCodFiscaleProprietario(sostegniAllevamenti.getCodFiscaleProprietario());
		datiProprietario.setDenominazioneProprietario(sostegniAllevamenti.getDenominazioneProprietario());
		allevamentoImpegnato.setDatiProprietario(mapper.writeValueAsString(datiProprietario));

		DatiDetentore datiDetentore = new DatiDetentore();
		datiDetentore.setCodFiscaleDetentore(sostegniAllevamenti.getCodFiscaleDetentore());
		datiDetentore.setDenominazioneDetentore(sostegniAllevamenti.getDenominazioneDetentore());
		allevamentoImpegnato.setDatiDetentore(mapper.writeValueAsString(datiDetentore));
		daoRichiestaAllevamDu.save(allevamentoImpegnato);
		return allevamentoImpegnato.getId();
	}

	// Salvataggio dei dati in A4GT_DATI_PASCOLO
	private Long persistiDatiPascolo(DomandaUnicaModel domanda, DatiPascolo datiPascolo) throws JsonProcessingException {
		A4gtDatiPascolo datiPascoloDomanda = new A4gtDatiPascolo();
		datiPascoloDomanda.setDomandaUnicaModel(domanda);
		datiPascoloDomanda.setCodicePascolo(datiPascolo.getCodPascolo());
		datiPascoloDomanda.setDescrizionePascolo(datiPascolo.getDescPascolo());
		datiPascoloDomanda.setUbaDichiarate(new BigDecimal(datiPascolo.getUba()));
		datiPascoloDomanda.setParticelleCatastali(mapper.writeValueAsString(datiPascolo.getParticelle()));
		daoDatiPascolo.save(datiPascoloDomanda);
		return datiPascoloDomanda.getId();
	}

	// Salvataggio dei dati in A4GT_DICHIARAZIONI_DU
	private Long persistiDichiarazioniDomandaUnica(DomandaUnicaModel domanda, DichiarazioniDomandaUnica dichiarazioniDomandaUnica) {
		DichiarazioneDomandaUnicaModel dichDu = new DichiarazioneDomandaUnicaModel();
		dichDu.setDomandaUnicaModel(domanda);
		// TODO Lorenzo: questo set va sicuramente in errore: in fase di ricevibilita' in asg si traduce CodDocumento in Quadro e lo si ritorna ad istruttoria
		dichDu.setQuadro(Quadro.valueOf(dichiarazioniDomandaUnica.getCodDocumento()));
		dichDu.setCodice(dichiarazioniDomandaUnica.getIdCampo());
		dichDu.setDescrizione(dichiarazioniDomandaUnica.getDescCampo());
		if (dichiarazioniDomandaUnica.getValCheck() != null) {
			dichDu.setValoreBool(dichiarazioniDomandaUnica.getValCheck() ? new BigDecimal(1) : new BigDecimal(0));
		}
		if (dichiarazioniDomandaUnica.getValDate() != null) {
			dichDu.setValoreData(dichiarazioniDomandaUnica.getValDate());
		}
		if (dichiarazioniDomandaUnica.getValNumber() != null) {
			dichDu.setValoreNumero(new BigDecimal(dichiarazioniDomandaUnica.getValNumber()));
		}
		if (dichiarazioniDomandaUnica.getValString() != null) {
			dichDu.setValoreStringa(dichiarazioniDomandaUnica.getValString());
		}

		dichDu.setOrdine(dichiarazioniDomandaUnica.getOrdine());

		dichiarazioneDomandaUnicaDao.save(dichDu);
		return dichDu.getId();
	}

	@Override
	public InfoDomandaDU findByCuaaIntestatarioAndAnnoRiferimento(String cuaa, Integer campagna) {

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa, campagna);
		if (domanda != null) {
			InfoDomandaDU result = new InfoDomandaDU();
			result.setAnnoRiferimento(domanda.getCampagna().intValue());
			result.setCuaa(domanda.getCuaaIntestatario());
			result.setDataPresentazione(domanda.getDtPresentazione());
			result.setNumeroDomanda(domanda.getNumeroDomanda().longValue());
			//TODO: recuperare i valora dalla tabella della PAC
			result.setDescrizioneDomanda("DU - Domanda Unica");
			result.setPac("PAC1420 - PAC 2014 – 2020");
			result.setEnteCompilatore(domanda.getCodEnteCompilatore().toString().concat(" - ").concat(domanda.getDescEnteCompilatore()));
			result.setRagioneSociale(domanda.getRagioneSociale());
			return result;
		} else {
			throw new EntityNotFoundException(String.format("Dati errati per il recupero della domanda (CUAA: %s, Anno: %s)", cuaa, campagna));
		}
	}

	@Override
	public InfoIstruttoriaDomanda recuperaInfoIstruttoriaDomanda(DomandaUnicaModel domanda) throws ConnectException, EntityNotFoundException {
		try {
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
			DomandaUnica domandaUnica = restTemplate.getForObject(new URI(resource), DomandaUnica.class);

			if (domandaUnica != null) {
				return domandaUnica.getInfoIstruttoriaDomanda();
			} else {
				logger.error("recuperaInfoIstruttoriaDomanda: nessuna istruttoria trovata in ags per la domanda {}", domanda.getId());
				throw new EntityNotFoundException("Nessun dto domandaAgsIstruttoria trovato");
			}
		} catch (EntityNotFoundException enf) {
			logger.error("recuperaInfoIstruttoriaDomanda:EntityNotFoundException per domanda " + domanda.getId());
			throw enf;
		} catch (ResourceAccessException ce) {
			logger.error("recuperaInfoIstruttoriaDomanda: errore per domanda " + domanda.getId() + " utente " + utente.utenza(), ce);
			throw new ConnectException("Impossibile contattare il servizio AGS");
		} catch (Exception e) {
			logger.error("recuperaInfoIstruttoriaDomanda: errore per domanda " + domanda.getId() + " utente " + utente.utenza(), e);
			throw new EntityNotFoundException(e.getMessage());
		}
	}


	@Override
	public void salvaDatiFiltratiDomanda(Long idDomanda) throws Exception {
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findById(idDomanda);
		if (domandaOpt.isPresent()) {
			DomandaUnicaModel domanda = domandaOpt.get();

			for (TipoFiltroDomanda fd : TipoFiltroDomanda.values()) {
				A4gtDatiFiltroDomanda a4gtDatiFiltroDomanda = daoDatiFiltroDomanda.findByTipoFiltroAndDomandaUnicaModel_id(fd.getDescrizione(), idDomanda);
				if (a4gtDatiFiltroDomanda == null) {
					a4gtDatiFiltroDomanda = new A4gtDatiFiltroDomanda();
				}

				a4gtDatiFiltroDomanda.setTipoFiltro(fd.getDescrizione());
				switch (fd) {
				case PASCOLO:
					a4gtDatiFiltroDomanda.setValore(disaccoppiatoService.isPascoliImpegnati(domanda) ? "SI" : "NO");
					break;
				case GIOVANE:
					a4gtDatiFiltroDomanda.setValore(disaccoppiatoService.isGiovane(domanda) ? "SI" : "NO");
					break;
				case RISERVA:
					String riserva = disaccoppiatoService.getRiserva(domanda);
					a4gtDatiFiltroDomanda.setValore(riserva != null ? riserva : RISERVA_NON_RICHIESTA);
					break;
				default:
					logger.error("filtro non esistente");
				}

				a4gtDatiFiltroDomanda.setDomandaUnicaModel(domanda);
				daoDatiFiltroDomanda.save(a4gtDatiFiltroDomanda);
			}

			logger.debug("filtri domanda memorizzati");
		}
	}

	private Boolean getErroreCalcolo(String erroreCalcoloStr) {
		if (erroreCalcoloStr != null)
			if (erroreCalcoloStr.equals("SI")) {
				return Boolean.TRUE;
			} else if (erroreCalcoloStr.equals("NO")) {
				return Boolean.FALSE;
			}
		return null;
	}

	private InfoGeneraliDomanda getInfoGenerali(DomandaUnicaModel d) {
		InfoGeneraliDomanda infoGeneraliDomanda = new InfoGeneraliDomanda();
		infoGeneraliDomanda.setDataPresentazione(LocalDateConverter.to(d.getDtPresentazione()));
		infoGeneraliDomanda.setEnteCompilatore(d.getDescEnteCompilatore());
		infoGeneraliDomanda.setModulo(d.getDescModuloDomanda());
		infoGeneraliDomanda.setCuaaIntestatario(d.getCuaaIntestatario());
		infoGeneraliDomanda.setNumeroDomanda(d.getNumeroDomanda());
		infoGeneraliDomanda.setNumeroDomandaRettificata(d.getNumDomandaRettificata());
		infoGeneraliDomanda.setRagioneSociale(d.getRagioneSociale());
		infoGeneraliDomanda.setStato(d.getStato().name());
		infoGeneraliDomanda.setDataProtocollazione(d.getDtProtocollazione());
		infoGeneraliDomanda.setDataProtocollazOriginaria(d.getDtProtocollazOriginaria());
		infoGeneraliDomanda.setDataPresentazOriginaria(d.getDtPresentazOriginaria());

		return infoGeneraliDomanda;
	}

	/**
	 * contata il servizio di AGS per caricare i dati di calcolo delle domande per la campagna 2017
	 * 
	 * @param numeroDomanda
	 * @return
	 * @throws ConnectException
	 * @throws SQLException
	 */
	private String recuperaJsonCalcoliDomandaAgs(Long numeroDomanda) throws ConnectException, SQLException {
		// TODO Lorenzo stringa da spostare in AGS
		final String tipoSostegnoAgs = "premiSostegno.BPS_2017";
		
		try {
			
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat(numeroDomanda.toString()).concat("?expand=").concat(tipoSostegnoAgs);

			DomandaUnica domandaUnica = restTemplate.getForObject(new URI(resource), DomandaUnica.class);
			
			if (domandaUnica != null) {
				return domandaUnica.getCalcoliSostegno() != null && !domandaUnica.getCalcoliSostegno().isEmpty() ? 
						mapper.writeValueAsString(domandaUnica.getCalcoliSostegno()) :
						null;
			}
			
			return null;
		} catch (ResourceAccessException ce) {
			logger.error("recuperaCalcoliDomandaAgs: errore per la domanda numero " + numeroDomanda + " e il tipo sostegno ags " + tipoSostegnoAgs + " per utente "
					+ utente.utenza(), ce);
			throw new ConnectException("Impossibile contattare il servizio AGS");
		} catch (Exception e) {
			logger.error("recuperaCalcoliDomandaAgserrore per la domanda numero " + numeroDomanda + " e il tipo sostegno ags " + tipoSostegnoAgs + " per utente "
					+ utente.utenza(), e);
			throw new SQLException(e);
		}
	}
	
	@Override
	public boolean checkDomandaPerSettore(Integer anno, String cuaa) throws CalcoloSostegnoException {
		boolean domandePrec = true;
		try {
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU)
													    .concat("checkPresenzaDomandeFiltered/")
													    .concat(anno.toString() + "/")
														.concat(cuaa);
			Boolean esiste = restTemplate.getForObject(new URI(resource), Boolean.class);
			domandePrec = Boolean.TRUE.equals(esiste);

		} catch (Exception e) {
			logger.error(String.format("Errore nel recupero informazioni su domande precedenti da ags per il cuaa '%s'", cuaa));
			throw new CalcoloSostegnoException(e.getMessage());
		}
		return domandePrec;
	}

	/**
	 * il servizio carica i dati di calcolo sostegno da ags, prima verifica sul database se ci sono in caso contrario chiama AGS e se presenti gli inserisce nel database.
	 */
	@Override
	public CalcoloSostegnoAgs getDatiCalcoloSostegnoAgs(DomandaUnicaModel domanda) throws ConnectException {
		CalcoloSostegnoAgs calcoloAgs = null;
		
		try {
			// Recupero i dati direttamente dal DB eliminato
				// Se non presenti invoco il servizio da AGS
			String jsonCalcoloAgs = recuperaJsonCalcoliDomandaAgs(domanda.getNumeroDomanda().longValueExact());
			if (jsonCalcoloAgs != null) {
				List<CalcoloSostegnoAgs> listaCalcoli = mapper.reader().forType(new TypeReference<List<CalcoloSostegnoAgs>>() {
				}).readValue(jsonCalcoloAgs);
				calcoloAgs = listaCalcoli.get(0);
				// Aggiorno il db di istruttoria non più necessario
			} else {
				logger.info("Informazioni di calcolo AGS non recuperate per la domanda {}", domanda.getNumeroDomanda());
			}

		} catch (ResourceAccessException ce) {
			String errMessage = String.format("getDatiCalcoloSostegnoAgs: errore per la domanda %s e tipo BPS_2017 per utente %s",  domanda.getId(), utente.utenza());
			logger.error(errMessage, ce);
			throw new ConnectException("Impossibile contattare il servizio AGS");
		} catch (Exception e) {
			String errMessage = String.format("getDatiCalcoloSostegnoAgs: errore per la domanda %s e tipo BPS_2017 per utente %s",  domanda.getId(), utente.utenza());
			logger.error(errMessage, e);
			throw new NoResultException(e.getMessage());
		}
		return calcoloAgs;
	}

	@Override
	@Deprecated
	public DomandaUnicaDettaglio getDomandaDettaglio(Long id) throws Exception {
		DomandaUnicaDettaglio dud = new DomandaUnicaDettaglio();

		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findById(id);
		if (domandaOpt.isPresent()) {
			DomandaUnicaModel domanda = domandaOpt.get();

			BeanUtils.copyProperties(domandaOpt.get(), dud);
			InfoGeneraliDomanda infoGeneraliDomanda = getInfoGenerali(domanda);
			dud.setInfoGeneraliDomanda(infoGeneraliDomanda);
			dud.setSupImpegnataLorda(superficiService.getSupImpegnataLordaBPS(id));
			dud.setSupImpegnataNetta(superficiService.getSupImpegnataNettaBPS(id));
			dud.setDichiarazioni(dichiarazioniService.getDichiarazioni(id));

			IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegno(domanda, Sostegno.DISACCOPPIATO);

			dud.setStatoSostegno(istruttoria.getA4gdStatoLavSostegno().getIdentificativo());

			// Gestione Controlli e passi lavorazione Calcolo
			Optional<TransizioneIstruttoriaModel> transizioneIstruttoria = 
				transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();
			if (transizioneIstruttoria.isPresent()) {

				Set<PassoTransizioneModel> infoCalcoli = transizioneIstruttoria.get().getPassiTransizione();

				// List<PassoTransizioneModel> infoCalcoli = daoPassiLavSostegno.findByA4gtTransizioneSostegno(maxA4gtTransizioneSostegno);

				if (infoCalcoli != null && !infoCalcoli.isEmpty()) {
					dud.setControlliSostegno(getControlliSostegno(new ArrayList<>(infoCalcoli)));
					dud.setDatiDomanda(getDatiDomanda(new ArrayList<>(infoCalcoli)));
				}
			}

			// Gestione Controlli liquidabilita
			Optional<TransizioneIstruttoriaModel> a4gtTransizioneLiquidabilita = 
				transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria).stream().findAny();
			if (a4gtTransizioneLiquidabilita.isPresent()) {
				Set<PassoTransizioneModel> infoLiquidabilita = a4gtTransizioneLiquidabilita.get().getPassiTransizione();
				if (infoLiquidabilita != null && !infoLiquidabilita.isEmpty()) {
					List<ControlloFrontend> listControlliSostegno = dud.getControlliSostegno();
					if (listControlliSostegno != null) {
						listControlliSostegno.addAll(getControlliSostegno(new ArrayList<>(infoLiquidabilita)));
					} else {
						listControlliSostegno = getControlliSostegno(new ArrayList<>(infoLiquidabilita));
					}

					dud.setControlliSostegno(listControlliSostegno);
				}
			}

			// Gestione Controlli intersostegno e passo lavorazione Disciplina Finanziaria
			Optional<TransizioneIstruttoriaModel> a4gtTransizioneIntersostegno = 
				transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(
						istruttoria).stream().findAny();
			if (a4gtTransizioneIntersostegno.isPresent()) {

				Optional<PassoTransizioneModel> infoDisciplinaFinanziaria = a4gtTransizioneIntersostegno.get().getPassiTransizione().stream()
						.filter((p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))).findAny();

				if (infoDisciplinaFinanziaria.isPresent()) {
					PassoTransizioneModel passoTransizione = infoDisciplinaFinanziaria.get();
					List<ControlloFrontend> controlli = new ArrayList<>();
					TipologiaPassoTransizione passo = passoTransizione.getCodicePasso();
					addVariabiliCalcolo(passoTransizione.getDatiInput(), DatiInput.class, controlli, passo, "Input", "DATI IN INGRESSO", 1);
					addVariabiliCalcolo(passoTransizione.getDatiOutput(), DatiOutput.class, controlli, passo, "Output", "DATI IN USCITA", 2);

					List<ControlloFrontend> listaControlli = dud.getDatiDomanda();
					if (listaControlli != null) {
						listaControlli.addAll(controlli);
					} else {
						listaControlli = controlli;
					}

					dud.setDatiDomanda(listaControlli);
				}
				Set<PassoTransizioneModel> infoInterSostegno = a4gtTransizioneIntersostegno.get().getPassiTransizione();
				if (infoInterSostegno != null && !infoInterSostegno.isEmpty()) {
					List<ControlloFrontend> listControlliSostegno = dud.getControlliSostegno();
					if (listControlliSostegno != null) {
						listControlliSostegno.addAll(getControlliSostegno(new ArrayList<>(infoInterSostegno)));
					} else {
						listControlliSostegno = getControlliSostegno(new ArrayList<>(infoInterSostegno));
					}

					dud.setControlliSostegno(listControlliSostegno);

				}
			}

			/**
			 * Betty: Commentato 03.02.2020 per correzione caricamenti
			DatiIstruttoria datiIstruttoria = datiIstruttoreService.getDatiIstruttoriaDomandaIdDomanda(domandaOpt.get().getId());
			dud.setDatiIstruttoria(datiIstruttoria);
			 */
			//dud.setInformazioniDomanda(getInformazioniDomanda(domandaOpt.get()));
		}

		return dud;
	}

	private List<ControlloFrontend> getControlliSostegno(List<PassoTransizioneModel> infoCalcoli) throws Exception {

		List<ControlloFrontend> controlli = new ArrayList<>();

		for (PassoTransizioneModel infoCalcolo : infoCalcoli) {
			TipologiaPassoTransizione passo = infoCalcolo.getCodicePasso();

			String datiSintesi = infoCalcolo.getDatiSintesiLavorazione();
			if (datiSintesi != null && !datiSintesi.isEmpty()) {
				DatiSintesi in = mapper.readValue(datiSintesi, DatiSintesi.class);
				if (in != null && in.getEsitiControlli() != null) {
					for (EsitoControllo esito : in.getEsitiControlli()) {
						ControlloFrontend contr = ControlloFrontend.newControlloFrontend(passo, esito);
						if (contr != null) {
							controlli.add(contr);
						}
					}
				}
			}
		}
		Collections.sort(controlli);
		return controlli;
	}

	private List<ControlloFrontend> getDatiDomanda(List<PassoTransizioneModel> infoCalcoli) throws Exception {
		List<ControlloFrontend> controlli = new ArrayList<>();

		for (PassoTransizioneModel infoCalcolo : infoCalcoli) {
			TipologiaPassoTransizione passo = infoCalcolo.getCodicePasso();
			addVariabiliCalcolo(infoCalcolo.getDatiInput(), DatiInput.class, controlli, passo, "Input", "DATI IN INGRESSO", 1);
			addVariabiliCalcolo(infoCalcolo.getDatiOutput(), DatiOutput.class, controlli, passo, "Output", "DATI IN USCITA", 2);
		}

		// dati di sintesi
		int i = 0;

		BigDecimal val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPCALCFIN, AmbitoVariabile.OUTPUT);

		controlli.add(new ControlloFrontend(TipoVariabile.IMPCALCFIN.name(), TipoVariabile.IMPCALCFIN.name(), "Importo Calcolato finale",
				VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.IMPCALCFIN, val), ++i));

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(new ControlloFrontend(TipoVariabile.RIDTOT.name(), TipoVariabile.RIDTOT.name(), "Importo Riduzioni",
					VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.RIDTOT, val), ++i));
		}

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Premio base", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Greening", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Giovane", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Per ritardo", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Capping", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.SANZTOT, AmbitoVariabile.OUTPUT, TipoVariabile.SANZTOT.name(), "Importo Sanzioni",
				++i);

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZREC, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(new ControlloFrontend(TipoVariabile.SANZTOT.name(), "SANZIONI_BASE", "Premio base", VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.BPSIMPSANZ, val),
					++i));
		}

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPSANZ, AmbitoVariabile.OUTPUT, TipoVariabile.SANZTOT.name(), "Greening", ++i);

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZREC, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(
					new ControlloFrontend(TipoVariabile.SANZTOT.name(), "SANZIONI_GIOVANE", "Giovane", VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.GIOIMPSANZ, val), ++i));
		}

		controlli.stream().filter(c -> c.getOrdine1() == null || c.getOrdine2() == null || c.getOrdineControllo() == null).forEach(c -> {
			logger.debug(c.getCodice1());
		});
		;

		Collections.sort(controlli);
		return controlli;
	}

	private void addVariabileSintesiDatiDomanda(List<PassoTransizioneModel> infoCalcoli, List<ControlloFrontend> controlli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile,
                                                AmbitoVariabile ambitoVariabile, String pcodice2, String descrizioneControllo, Integer ordineControllo) throws Exception {
		VariabileCalcolo variabileCalcolo = recuperaVariabile(infoCalcoli, passo, tipoVariabile, ambitoVariabile);
		if (variabileCalcolo != null && (variabileCalcolo.recuperaValoreString(true) != null)) {
			controlli.add(new ControlloFrontend(pcodice2, variabileCalcolo.getTipoVariabile().name(), descrizioneControllo, variabileCalcolo.recuperaValoreString(), ordineControllo));
		}
	}

	private BigDecimal getValoreVariabile(List<PassoTransizioneModel> infoCalcoli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile, AmbitoVariabile ambitoVariabile) throws Exception {
		VariabileCalcolo variabileCalcolo = recuperaVariabile(infoCalcoli, passo, tipoVariabile, ambitoVariabile);
		BigDecimal val = new BigDecimal(0);

		if (variabileCalcolo != null && variabileCalcolo.recuperaValoreString(true) != null) {
			val = variabileCalcolo.getValNumber();
		}
		return val;
	}

	private <T extends DatiCalcoli> void addVariabiliCalcolo(String content, Class<T> valueType, List<ControlloFrontend> controlli, TipologiaPassoTransizione passo, String pCodice2, String pdescrizione2,
                                                             Integer order) throws IOException {
		if (content != null && !content.isEmpty()) {
			T d = mapper.readValue(content, valueType);
			if (d != null && d.getVariabiliCalcolo() != null) {
				for (VariabileCalcolo variabileCalcolo : d.getVariabiliCalcoloDaStampare()) {
					ControlloFrontend contr = new ControlloFrontend(passo, pCodice2, pdescrizione2, order, variabileCalcolo);
					controlli.add(contr);
				}
			}
		}
	}

	public <T extends DatiCalcoli> VariabileCalcolo mapVal(String content, Class<T> valueType, TipoVariabile tipoVariabile) throws Exception {
		if (content != null && !content.isEmpty()) {
			T d = mapper.readValue(content, valueType);
			Optional<VariabileCalcolo> variabileOp = d.getVariabiliCalcolo().stream().filter(x -> x.getTipoVariabile().equals(tipoVariabile)).findFirst();
			if (variabileOp.isPresent()) {
				return variabileOp.get();
			}
		}
		return null;
	}

	private VariabileCalcolo recuperaVariabile(List<PassoTransizioneModel> infoCalcoli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile, AmbitoVariabile ambitoVariabile) throws Exception {
		Optional<PassoTransizioneModel> passOp = infoCalcoli.stream().filter(x -> x.getCodicePasso().equals(passo)).findFirst();
		if (passOp.isPresent()) {
			PassoTransizioneModel passoVariabile = passOp.get();
			switch (ambitoVariabile) {
			case INPUT:
				return mapVal(passoVariabile.getDatiInput(), DatiInput.class, tipoVariabile);
			case OUTPUT:
				return mapVal(passoVariabile.getDatiOutput(), DatiInput.class, tipoVariabile);
			case SINTESI:
				return mapVal(passoVariabile.getDatiSintesiLavorazione(), DatiInput.class, tipoVariabile);
			default:
			}
		}
		return null;
	}

	@Override
	public Pagina<RichiestaSuperficie> getParticelleDomanda(Long id, Paginazione paginazione, String ordinamento) throws Exception {

		List<Ordinamento> criteriOrdinamento = null;
		if (ordinamento != null && !ordinamento.isEmpty()) {
			criteriOrdinamento = mapper.readValue(ordinamento, new TypeReference<List<Ordinamento>>() {
			});
		}

		Pageable pageable = ConvertitorePaginazione.converti(paginazione, criteriOrdinamento);

		Page<A4gtRichiestaSuperficie> res = null;
		if (ordinamento != null && !ordinamento.isEmpty()) {
			List<IRichiestaSuperficie> aa = daoRichiestaSuperficie.findByDomandaInterventoPaginata1(id,
					daoInterventoDu.findByIdentificativoIntervento(CodiceInterventoAgs.BPS).getId(), pageable);

			List<RichiestaSuperficie> richiestaSup = new ArrayList<>();
			aa.forEach(d -> {
				RichiestaSuperficie a = new RichiestaSuperficie();
				BeanUtils.copyProperties(d, a);
				Particella infoCat = new Particella();
				BeanUtils.copyProperties(d, infoCat);
				a.setInfoCatastali(infoCat);

				InformazioniColtivazione infoColt = new InformazioniColtivazione();
				BeanUtils.copyProperties(d, infoColt);
				a.setInfoColtivazione(infoColt);

				RiferimentiCartografici riferimentiCartografici = new RiferimentiCartografici();
				BeanUtils.copyProperties(d, riferimentiCartografici);
				a.setRiferimentiCartografici(riferimentiCartografici);

				richiestaSup.add(a);
			});
			Page<RichiestaSuperficie> p = new PageImpl<>(richiestaSup, pageable,
					daoRichiestaSuperficie.getCountfindByDomandaInterventoPaginata1(id, CodiceInterventoAgs.BPS));
			return ConvertitorePaginazione.converti(p);
		} else {

			res = daoRichiestaSuperficie.findByDomandaInterventoPaginata(id, CodiceInterventoAgs.BPS, pageable);
			List<RichiestaSuperficie> richiestaSup = new ArrayList<>();
			res.forEach(d -> {
				RichiestaSuperficie a = new RichiestaSuperficie();
				BeanUtils.copyProperties(d, a);

				try {
					a.setInfoCatastali(mapper.readValue(d.getInfoCatastali(), Particella.class));
					a.setInfoColtivazione(mapper.readValue(d.getInfoColtivazione(), InformazioniColtivazione.class));
					a.setRiferimentiCartografici(mapper.readValue(d.getRiferimentiCartografici(), RiferimentiCartografici.class));
				} catch (IOException e) {
					logger.error("getParticelleDomanda: errore leggendo i dati json della superficie richiesta " + d.getId(), e);
					throw new RuntimeException(e);
				}

				richiestaSup.add(a);
			});
			Page<RichiestaSuperficie> p = new PageImpl<>(richiestaSup, pageable, res.getTotalElements());
			return ConvertitorePaginazione.converti(p);
		}
	}

	/**
	 * Metodo che recupera da A4GProxy il flag di esito della domanda in SIGECO per l'anno specificato
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean recuperaEsitoSigeco(BigDecimal numeroDomanda) {

		String resource = null;
		Long esito = null;
		try {
			DomandaUnicaModel domandaUnicaModel = daoDomanda.findByNumeroDomanda(numeroDomanda);
			String jsonRequest = 
					"{\"anno\":" + domandaUnicaModel.getCampagna().toString() +
					", \"numeroDomanda\":" + numeroDomanda.toString() + "}";

			resource = esitoSigecoUrl.concat("?params=").concat(URLEncoder.encode(jsonRequest, "UTF-8"));

			esito = restTemplate.getForObject(new URI(resource), Long.class);

		} catch (Exception e) {
			logger.error("Errore nel recupero dell'esito Sigeco per la domanda {} per l'utente {}", numeroDomanda, utente.utenza());
			logger.error("Dettaglio errore chiamata esito sigeco", e);
		}

		return esito != null && (esito.equals(Long.valueOf(ESITO_SIGECO_CHIUSA)) || esito.equals(Long.valueOf(ESITO_SIGECO_VERBALIZZATA)));
	}

	/**
	 * Verifica se una domanda presenta impegni su particella identificate come pascolo
	 */
	@Override
	public Boolean isPascoliImpegnati(Long idDomanda) throws Exception {
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findById(idDomanda);
		if (domandaOpt.isPresent()) {
			List<A4gtPascoloParticella> listPascoloParticella = daoPascoloParticella.findByDomandaUnicaModel(domandaOpt.get());
			return !listPascoloParticella.isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public List<CuaaDenominazione> getCuaaDomandeCampagna(FiltroRicercaDomandeIstruttoria filter) {
		List<CuaaDenominazione> listaCuaa1;
		if (filter.getSostegno() != null && filter.getStatoSostegno() != null) {
			listaCuaa1 = this.istruttoriaDao.findCuaaByA4gdStatoLavSostegnoAndSostegno(
					filter.getStatoSostegno(), filter.getSostegno());
		} else {
			listaCuaa1 = this.daoDomanda.findCuaaDomandaUnicaByCampagna(filter.getCampagna());
		}
		if (listaCuaa1 != null) {
			listaCuaa1 = listaCuaa1.stream().distinct().collect(Collectors.toList());
		}
		return listaCuaa1;
	}

	@Override
	public RisultatiPaginati<String> getCuaaDomandeFiltrati(String statoSostegno, Sostegno sostegno, Integer annoCampagna, String cuaa, TipoIstruttoria tipo,
			it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione, it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento) {
		Page<String> lista = this.istruttoriaDao.findCuaaByA4gdStatoLavSostegnoAndA4gdSostegnoDuAndByCuaaLike(
				statoSostegno, sostegno, annoCampagna, cuaa, tipo,
				PageableBuilder.build().from(paginazione, ordinamento));
		return RisultatiPaginati.of(lista.getContent(), lista.getTotalElements());
	}
	
	@Override
	public RisultatiPaginati<String> getRagioneSocialeDomandeFiltrati(final String statoSostegno,
			final Sostegno sostegno, final Integer annoCampagna, final String ragioneSociale, TipoIstruttoria tipo,
			final it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione,
			final it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento) {
		Page<String> lista = this.istruttoriaDao.findRagioneSocialeByA4gdStatoLavSostegnoAndA4gdSostegnoDuAndByRagioneSocialeLike(
				statoSostegno, sostegno, annoCampagna, ragioneSociale, tipo,
				PageableBuilder.build().from(paginazione, ordinamento));
		return RisultatiPaginati.of(lista.getContent(), lista.getTotalElements());
	}

	@Override
	public List<String> getCodiciPascoli(Integer annoCampagna, String cuaa) {
		List<String> listaPascoli;

		listaPascoli = this.daoDatiPascolo.findDatiPascoloByAnnoAndCUAA(annoCampagna, cuaa);

		if (listaPascoli != null) {
			listaPascoli = listaPascoli.stream().distinct().collect(Collectors.toList());
		}

		return listaPascoli;

	}

	@Override
	public InfoLiquidabilita recuperaInfoLiquidabilita(Long numeroDomanda) throws ConnectException {
		try {

			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat(numeroDomanda.toString()).concat("?expand=infoLiquidabilita");

			DomandaUnica domandaUnica = restTemplate.getForObject(new URI(resource), DomandaUnica.class);

			if (domandaUnica != null) {
				return domandaUnica.getInfoLiquidabilita();
			} else {
				throw new EntityNotFoundException("Nessun dto domandaAgsIstruttoria trovato");
			}
		} catch (ResourceAccessException ce) {
			logger.error("getDatiCalcoloSostegnoAgs: errore per la domanda " + numeroDomanda + " per utente " + utente.utenza(), ce);
			throw new ConnectException("Impossibile contattare il servizio AGS");
		} catch (Exception e) {
			logger.error("getDatiCalcoloSostegnoAgs: errore per la domanda " + numeroDomanda + " per utente " + utente.utenza(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DomandaUnica> recuperaDomandeUniche(List<Long> numeroDomandaList) {
		try {
			List<String> newList = new ArrayList<>(numeroDomandaList.size());
			for (Long numeroDomanda : numeroDomandaList) {
				newList.add(String.valueOf(numeroDomanda));
			}
			String numeroDomandaCommaSeparated = String.join(",", newList);
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat("elencoDomande/").concat("?expand=infoLiquidabilita&").concat("numeroDomandaList=").concat(numeroDomandaCommaSeparated);

			ResponseEntity<DomandaUnica[]> response =
					restTemplate.getForEntity(
							new URI(resource),
							DomandaUnica[].class);
			if (response != null && response.hasBody()) {
				return Arrays.asList(response.getBody());
			}
			return new ArrayList<>();
		} catch (Exception e) {
			logger.error("recuperaInfoLiquidabilita: errore per le domande " + numeroDomandaList + " per utente " + utente.utenza(), e);
			throw new RuntimeException(e);
		}
	}


	@Transactional
	@Override
	public void elaboraDomandaPerIstruttoria(DomandaUnicaModel d, StringBuilder outErrore) {
		String dettaglioErrore = "";
		try {
			dettaglioErrore = "recuperaSostegniDomandaDU";
			DomandaUnica domanda = recuperaSostegniDomandaDU(d); // chiamata la servizio rest per il recupero dei dati da AGS
			SintesiRichieste sintesi = domanda.getRichieste().getSintesiRichieste();
			if (sintesi.isRichiestaDisaccoppiato()) {
				dettaglioErrore = "estraiParticellePascolo";
				elencoPascoliService.estraiParticellePascolo(d.getId());
				dettaglioErrore = "salvaDatiFiltratiDomanda";
				salvaDatiFiltratiDomanda(d.getId());
			}
			dettaglioErrore = "setA4gdStatoDomanda";
			d.setStato(StatoDomanda.IN_ISTRUTTORIA);
			daoDomanda.saveAndFlush(d);
			if (sintesi.isRichiestaDisaccoppiato()) {
				dettaglioErrore = "salvaRichiestaDisaccoppiato";
				IstruttoriaModel lavorazioneSostegno = new IstruttoriaModel();
				lavorazioneSostegno.setA4gdStatoLavSostegno(daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria()));
				lavorazioneSostegno.setSostegno(Sostegno.DISACCOPPIATO);
				lavorazioneSostegno.setDomandaUnicaModel(d);
				lavorazioneSostegno.setBloccataBool(false);
				istruttoriaDao.save(lavorazioneSostegno);
			}

			if (sintesi.isRichiestaZootecnia()) {
				dettaglioErrore = "salvaRichiestaZootecnia";
				IstruttoriaModel lavorazioneSostegno = new IstruttoriaModel();
				lavorazioneSostegno.setA4gdStatoLavSostegno(daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria()));
				lavorazioneSostegno.setSostegno(Sostegno.ZOOTECNIA);
				lavorazioneSostegno.setDomandaUnicaModel(d);
				lavorazioneSostegno.setBloccataBool(false);
				istruttoriaDao.save(lavorazioneSostegno);
			}

			if (sintesi.isRichiestaSuperfici()) {
				dettaglioErrore = "salvaRichiestaSuperfici";
				IstruttoriaModel lavorazioneSostegno = new IstruttoriaModel();
				lavorazioneSostegno.setA4gdStatoLavSostegno(daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria()));
				lavorazioneSostegno.setSostegno(Sostegno.SUPERFICIE);
				lavorazioneSostegno.setDomandaUnicaModel(d);
				lavorazioneSostegno.setBloccataBool(false);
				istruttoriaDao.save(lavorazioneSostegno);
			}
		} catch (Exception e) {
			logger.error("elaboraDomandaPerIstruttoria: errore elaborando la domanda con numero {}, dettaglio errore {}", d.getNumeroDomanda(), dettaglioErrore, e);
			outErrore.append(dettaglioErrore);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<A4gtRichiestaSuperficie> getRichiesteSuperficiePerIntervento(Long idDomanda, CodiceInterventoAgs intervento) throws Exception {

		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findById(idDomanda);
		if (domandaOpt.isPresent()) {
			return daoRichiestaSuperficie.findByDomandaIntervento(idDomanda, intervento);
		}
		return Collections.emptyList();
	}

	@Override
	@Transactional(readOnly = true)
	public RichiestaAllevamDuEsito getRichiestaAllevamDuEsito(Long idRichiestaAllevamentoEsito) throws Exception {

		Optional<EsitoCalcoloCapoModel> optEsito = esitoCalcoloCapoDao.findById(idRichiestaAllevamentoEsito);
		if (!optEsito.isPresent()) {
			throw new EntityNotFoundException("Esito con id ".concat(String.valueOf(idRichiestaAllevamentoEsito)).concat(" non trovato"));
		}
		RichiestaAllevamDuEsito richiestaAllevamDuEsito = new RichiestaAllevamDuEsito();
		BeanUtils.copyProperties(optEsito.get(), richiestaAllevamDuEsito);

		richiestaAllevamDuEsito.setEsito(String.valueOf(optEsito.get().getEsito()));
		return richiestaAllevamDuEsito;
	}

	@Override
	public byte[] getVerbaleLiquidazione(String codiceElenco) {

		ElencoLiquidazioneModel elenco = daoElencoLiquidazione.findByCodElenco(codiceElenco);
		return elenco.getVerbaleLiquidazione();

	}


	@Override
	@Transactional
	public Capo modificaCapo(Long id, Capo capo) {
		A4gtDomandaIntegrativa in = new A4gtDomandaIntegrativa();
		EsitoCalcoloCapoModel a4gtRichAllevamDuEsito = new EsitoCalcoloCapoModel();
		a4gtRichAllevamDuEsito.setId(capo.getId());
		in.setEsitoCalcoloCapo(a4gtRichAllevamDuEsito);
		Optional<A4gtDomandaIntegrativa> a4gtDomandaIntegrativaOpt = domandaIntegrativaDao.findOne(Example.of(in));
		if (a4gtDomandaIntegrativaOpt.isPresent()) {
			A4gtDomandaIntegrativa a4gtDomandaIntegrativa = a4gtDomandaIntegrativaOpt.get();
			a4gtDomandaIntegrativa.setControlloSuperato(capo.getControlloSuperato());
			a4gtDomandaIntegrativa.setDuplicato(capo.getDuplicato());
			a4gtDomandaIntegrativa.setStato(capo.getStato());
			domandaIntegrativaDao.save(a4gtDomandaIntegrativa);
		}
		return capo;
	}

	public void countDomande() throws Exception {
		// A4gtDomandaIntegrativa
		// domandaIntegrativaDao.findAll(example)
	}



	@Override
	@Transactional(readOnly = true)
	public Domanda getDomanda(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Id domanda obbligatorio");
		}
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findById(id);
		if (!domandaOpt.isPresent()) {
			return null;// NoContentObjectControllerAdvice
		}

		List<String> expandAmmissibili = EnumSet.allOf(TipoDettaglioDomanda.class).stream().map(TipoDettaglioDomanda::toString).collect(Collectors.toList());
		@SuppressWarnings("unchecked")
		List<String> expandParamsIn = CustomThreadLocal.getVariable("expand") != null ? (List<String>) CustomThreadLocal.getVariable("expand") : null;
		List<String> expandParams = CollectionUtils.isEmpty(expandParamsIn) ? expandAmmissibili : expandParamsIn;
		List<DatiDomanda> strategies = expandAmmissibili.stream().filter(expandParams::contains).map(datiDomandaStrategy::getDatiDomanda).collect(Collectors.toList());
		Domanda domanda = new Domanda();
		BeanUtils.copyProperties(domandaOpt.get(), domanda);
		domanda.setStatoDomanda(domandaOpt.get().getStato());
		strategies.forEach(s -> s.recupera(domanda));
		return domanda;
	}


	@Override
	public LocalDateTime getDataProtocollazioneDomanda(DomandaUnicaModel domanda) {
		Date dtProtocollazioneDomanda = domanda.getDtProtocollazOriginaria();
		if (dtProtocollazioneDomanda == null) {
			dtProtocollazioneDomanda = domanda.getDtProtocollazione();
		}
		return LocalDateConverter.fromDateTime(dtProtocollazioneDomanda);
	}

	@Override
	public boolean recuperaFlagConvSigeco(BigDecimal numeroDomanda) {

		String resource = null;
		Boolean esito = false;
		try {
			DomandaUnicaModel domandaUnicaModel = daoDomanda.findByNumeroDomanda(numeroDomanda);
			String cuaa = domandaUnicaModel.getCuaaIntestatario();
			String jsonRequest = 
					"{\"annoCampagna\":" + domandaUnicaModel.getCampagna().toString() +
					", \"numeroDomanda\":" + numeroDomanda.toString() + 
					", \"cuaa\":\"" + cuaa + "\"}";

			resource = esitoSigecoUrl.concat("/flagConv").concat("?params=").concat(URLEncoder.encode(jsonRequest, "UTF-8"));

			esito = restTemplate.getForObject(new URI(resource), Boolean.class);

		} catch (Exception e) {
			logger.error("Errore nel recupero dell'esito Sigeco per la domanda {} per l'utente {}", numeroDomanda, utente.utenza());
		}

		return esito;
	}

	@Override
	public Domanda aggiornaDomanda(Domanda domanda) throws Exception {
		DomandaUnicaModel domandaUnicaModel = daoDomanda.getOne(domanda.getId());
		BeanUtils.copyProperties(domanda, domandaUnicaModel);
		Optional.ofNullable(domanda.getStatoDomanda())
			.ifPresent(stato -> domandaUnicaModel.setStato(stato));
		Domanda domandaOut = new Domanda();
		try {
			DomandaUnicaModel domandaUnicaModelUpdated = daoDomanda.save(domandaUnicaModel);
			BeanUtils.copyProperties(domandaUnicaModelUpdated, domandaOut);
			domandaOut.setStatoDomanda(domandaUnicaModelUpdated.getStato());
		} catch (Exception e) {
			logger.error("Errore nell'aggiornamento della domanda {}", domanda.getId(), e);
			throw e;
		}

		return domandaOut;
	}

	@Override
	public DatiErede aggiornaErede(DatiErede erede) throws Exception {
		DatiErede datiEredeOut = new DatiErede();
		Optional<A4gtDatiErede> a4gtDatiEredeOpt = datiEredeDao.findById(erede.getId());

		if (!a4gtDatiEredeOpt.isPresent()) {
			throw new IllegalArgumentException("Erede con id ".concat(erede.getId().toString()).concat(" non trovato"));
		}
		A4gtDatiErede a4gtDatiErede = a4gtDatiEredeOpt.get();
		BeanUtils.copyProperties(erede, a4gtDatiErede);
		a4gtDatiErede.setIndirizzo(erede.getIndirizzoResidenza());
		a4gtDatiErede.setProvincia(erede.getProvResidenza());
		a4gtDatiErede.setCap(erede.getCapResidenza());
		a4gtDatiErede.setProvinciaNascita(erede.getProvNascita());
		a4gtDatiErede.setDtUltimoAggiornamento(new Date());
		try {
			A4gtDatiErede a4gtDatiEredeUpdated = datiEredeDao.save(a4gtDatiErede);
			BeanUtils.copyProperties(a4gtDatiEredeUpdated, datiEredeOut);
			datiEredeOut.setIndirizzoResidenza(a4gtDatiEredeUpdated.getIndirizzo());
			datiEredeOut.setProvResidenza(a4gtDatiEredeUpdated.getProvincia());
			datiEredeOut.setCapResidenza(a4gtDatiEredeUpdated.getCap());
			datiEredeOut.setProvNascita(a4gtDatiEredeUpdated.getProvinciaNascita());
			datiEredeOut.setDtUltimoAggiornamento(a4gtDatiEredeUpdated.getDtUltimoAggiornamento());
		} catch (Exception e) {
			logger.error("Errore nell'aggiornamento dell erede {}", erede.getId(), e);
			throw e;
		}
		return datiEredeOut;
	}

	@Override
	public DatiErede creaErede(Long idDomanda, DatiErede datiErede) throws Exception {
		A4gtDatiErede a4gtDatiErede = new A4gtDatiErede();
		DomandaUnicaModel domandaUnicaModel = daoDomanda.getOne(idDomanda);
		if (!CollectionUtils.isEmpty(domandaUnicaModel.getA4gtDatiEredes())) {
			throw new IllegalArgumentException("Dati Erede gia' presenti per la domanda ".concat(idDomanda.toString()));
		}

		a4gtDatiErede.setDomandaUnicaModel(domandaUnicaModel);
		BeanUtils.copyProperties(datiErede, a4gtDatiErede);
		a4gtDatiErede.setIndirizzo(datiErede.getIndirizzoResidenza());
		a4gtDatiErede.setProvincia(datiErede.getProvResidenza());
		a4gtDatiErede.setCap(datiErede.getCapResidenza());
		a4gtDatiErede.setProvinciaNascita(datiErede.getProvNascita());
		a4gtDatiErede.setDtUltimoAggiornamento(new Date());

		A4gtDatiErede a4gtDatiEredeSalvato = new A4gtDatiErede();
		DatiErede datiEredeOutput = new DatiErede();
		try {
			a4gtDatiEredeSalvato = datiEredeDao.save(a4gtDatiErede);
			BeanUtils.copyProperties(a4gtDatiEredeSalvato, datiEredeOutput);
			datiEredeOutput.setIndirizzoResidenza(a4gtDatiEredeSalvato.getIndirizzo());
			datiEredeOutput.setProvResidenza(a4gtDatiEredeSalvato.getProvincia());
			datiEredeOutput.setCapResidenza(a4gtDatiEredeSalvato.getCap());
			datiEredeOutput.setProvNascita(a4gtDatiEredeSalvato.getProvinciaNascita());
			datiEredeOutput.setDtUltimoAggiornamento(a4gtDatiEredeSalvato.getDtUltimoAggiornamento());
		} catch (Exception e) {
			logger.error("Errore nella creazione dell erede relativo alla domanda {}", idDomanda, e);
			throw e;
		}
		return datiEredeOutput;
	}

	@Override
	@Transactional
	public void annullaIstruttoriaDomanda(Long id) throws Exception {

		if (checkDomandaInPagamento(id)) {
			String message = String.format("Impossibile annullare l'istruttoria della domanda %d: esistono sostegni in pagamento o pagati.", id);
			logger.info(message);
			throw new Exception("ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO");
		}

		Long numeroDomandaAgs = daoDomanda.findById(id).orElseThrow(() -> new Exception("ANNULLA_ISTRUTTORIA_DOMANDA_NON_TROVATA")).getNumeroDomanda().longValue();
		try {
			eliminaDatiIstruttoria(id);
		} catch (Exception e) {
			String message = String.format("Impossibile annullare l'istruttoria della domanda %d: problema nella cancellazione dei dati di domanda", id);
			logger.error(message, e);
			throw new Exception("ANNULLA_ISTRUTTORIA_ELIMINAZIONE_DATI_ISTRUTTORIA");
		}

		try {
			annullaDomandaSuAgs(numeroDomandaAgs);
		} catch (Exception e) {
			String message = String.format("Impossibile annullare l'istruttoria della domanda %d: impossibile movimentare la domanda su AGS", id);
			logger.error(message, e);
			throw new Exception("ANNULLA_ISTRUTTORIA_MOVIMENTAZIONE_AGS");
		}
	}

	@Override
	public Boolean checkDomandaInPagamento(Long id) throws Exception {
		List<IstruttoriaModel> lavorazioni = istruttoriaDao.findByDomandaUnicaModelId(id);
		return !lavorazioni.isEmpty()
				&& lavorazioni.stream().anyMatch(p -> StatoIstruttoria.PAGAMENTO_AUTORIZZATO.getStatoIstruttoria().equals(p.getA4gdStatoLavSostegno().getIdentificativo()));
	}

	/**
	 * Metodo che elimina i dati di istruttoria di una domanda. Vengono, quindi, eliminati tutti i dati relativi all'istruttoria della domanda e i dati della domanda stessa.
	 * 
	 * @param id
	 */
	private void eliminaDatiIstruttoria(Long id) {
		try {
			daoDomanda.deleteById(id);
		} catch (Exception e) {
			logger.error("Impossibile eliminare i dati dell'istruttoria della domanda {}", id);
			throw e;
		}
	}

	/**
	 * "Sincronizza" l'annullamento della domanda sul vecchio sistema AGS riportandola in stato "PROTOCOLLATA"
	 * 
	 * @param numeroDomanda
	 */
	private void annullaDomandaSuAgs(Long numeroDomanda) {
		String uriForzaMovimentazioneDomanda = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat("{numeroDomanda}").concat(ApiUrls.AGS_FORZA_MOVIMENTO);
		String requestBody = String.valueOf(StatoDomanda.PROTOCOLLATA);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		try {
			restTemplate.exchange(uriForzaMovimentazioneDomanda, HttpMethod.POST, entity, String.class, numeroDomanda.toString());
		} catch (Exception e) {
			logger.error("Impossibile forzare su AGS lo stato della domanda {} in PROTOCOLLATA", numeroDomanda);
			throw e;
		}
	}

	@Override
	public Float getSanzioneAnnoPrecedente(final DomandaUnicaModel domanda, final boolean isGiovaneAgricoltore) {
		Float result = null;
		DomandaUnicaModel domandaAnnoPrecedente = this.getDomandaAnnoPrecedente(domanda);
		IstruttoriaModel lastIstruttoria = null;
		if (domandaAnnoPrecedente != null) {
			lastIstruttoria = istruttoriaComponent.getUltimaIstruttoria(
					domandaAnnoPrecedente, Sostegno.DISACCOPPIATO);
			try {
				result = recuperaSanzioneAnnoPrecedenteByIstruttoria(lastIstruttoria, isGiovaneAgricoltore);
			} catch (Exception e) {
				logger.error("Impossibile recuperare la sanzione dell'anno precedente per la domanda {}", domanda.getId());
			}
		}
		return result;
	}

	private Float recuperaSanzioneAnnoPrecedenteByIstruttoria(final IstruttoriaModel istruttoria, final boolean isGiovaneAgricoltore) throws Exception {
		// Recupero la domanda in base all'istruttoria
		DatiIstruttoreDisModel datiIstruttoria = istruttoria.getDatiIstruttoreDisModel();
		if (isGiovaneAgricoltore) {
			if (datiIstruttoria != null && datiIstruttoria.getBpsSanzAnnoPrec() != null && datiIstruttoria.getBpsImpSan() != null) {
				return datiIstruttoria.getBpsImpSan().floatValue();
			} // else
			if (datiIstruttoria != null && datiIstruttoria.getGioSanzAnnoPrec() != null && datiIstruttoria.getGioImpSan() != null) {
				return datiIstruttoria.getGioImpSan().floatValue();
			}
		}
		// Verifico che ci sia stata una lavorazione per il sostegno disaccoppiato
		StatoIstruttoria stato = istruttoria.getStato();
		if (StatoIstruttoria.isStatoCalcoloValido(stato)) {
			// Se la domanda ha un calcolo consolidato (ovvero è almeno LIQUIDABILE, o NON AMMISSIBILE), recupero il passo di lavorazione adeguato
			// Per questo, mi trovo a "modificare" lo stato da cui recuperare l'ultima transizione valida ovvero
			// identificare se recuperare la transizione del CALCOLO_KO oppure CALCOLO_OK
			StatoIstruttoria newStato = stato.compareTo(
					StatoIstruttoria.LIQUIDABILE) >= 0 ? StatoIstruttoria.CONTROLLI_CALCOLO_OK
					: StatoIstruttoria.CONTROLLI_CALCOLO_KO;
			
			TransizioneIstruttoriaModel ultimaTransizioneUtile = transizioneService.caricaUltimaTransizione(istruttoria, newStato);
			if (ultimaTransizioneUtile != null) {
				// Recupero dalla transizione il passo di lavorazione SANZIONI_BPS
				List<PassoTransizioneModel> passi = ultimaTransizioneUtile.getPassiTransizione().stream()
						.filter(p -> p.getCodicePasso().equals(
								isGiovaneAgricoltore ? TipologiaPassoTransizione.GIOVANE_AGRICOLTORE : TipologiaPassoTransizione.SANZIONI_BPS))
						.collect(Collectors.toList());
				if (!passi.isEmpty()) {
					// Se esiste la variabile BPS/GIOYCSANZANNIPREC, allora c'è stata una sanzione nell'anno precedente
					DatiInput input = objectMapper.readValue(passi.get(0).getDatiInput(), DatiInput.class);
					DatiOutput output = objectMapper.readValue(passi.get(0).getDatiOutput(), DatiOutput.class);
					Boolean isSanzAnniPrec = input.getVariabiliCalcolo().stream()
						.filter(p -> p.getTipoVariabile().equals(isGiovaneAgricoltore ? TipoVariabile.GIOYCSANZANNIPREC : TipoVariabile.BPSYCSANZANNIPREC))
						.collect(CustomCollectors.collectOne())
						.map(VariabileCalcolo::getValBoolean)
						.orElse(false);
					Float impSanzAnniPrec = output.getVariabiliCalcolo().stream()
						.filter(p -> p.getTipoVariabile().equals(isGiovaneAgricoltore ? TipoVariabile.GIOIMPSANZ : TipoVariabile.BPSIMPSANZ))
						.collect(CustomCollectors.collectOne())
						.map(item -> item.getValNumber().floatValue())
						.orElse(null);
					Boolean isYellowCard = output.getVariabiliCalcolo().stream()
							.filter(p -> p.getTipoVariabile().equals(isGiovaneAgricoltore ? TipoVariabile.GIOYELLOWCARD : TipoVariabile.BPSYELLOWCARD))
							.collect(CustomCollectors.collectOne())
							.map(VariabileCalcolo::getValBoolean)
							.orElse(false);
					if (isSanzAnniPrec || (impSanzAnniPrec != null && impSanzAnniPrec > 0)) {
						// Se c'è stata una sanzione ne recupero il valore leggendo la variabile BPS/GIOIMPSANZ
						return isYellowCard ? output.getVariabiliCalcolo().stream()
							.filter(p -> p.getTipoVariabile().equals(isGiovaneAgricoltore ? TipoVariabile.GIOIMPSANZ : TipoVariabile.BPSIMPSANZ))
							.collect(CustomCollectors.collectOne())
							.map(item -> item.getValNumber().floatValue())
							.orElse(null) : Float.valueOf(0);
					}
				}
			}
		}
		return null;
	}

	@Override
	public DomandaUnicaModel getDomandaAnnoPrecedente(DomandaUnicaModel domanda) {
		Integer campagna = domanda.getCampagna().intValue() - 1;

		return daoDomanda.findByCuaaIntestatarioAndCampagna(domanda.getCuaaIntestatario(), campagna);
	}

	@Override
	public boolean checkDomandaAnnoPrecedenteIstruttoriaSostegnoConclusa(DomandaUnicaModel domanda, Sostegno sostegno) {
		// Verifico che esista a sistema una domanda per l'anno precedente, e, se
		// questa esiste, verifico che sia in stato NON_AMMISSIBILE o LIQUIDABILE e successivi
		boolean result = false;

		// Recupero i dati da A4G
		// Ovvero verifico innanzitutto che esista una domanda per questo cuaa per l'anno richiesto
		DomandaUnicaModel domandaAnnoPrecedente = getDomandaAnnoPrecedente(domanda);
		if (domandaAnnoPrecedente != null) { 
			// Verifico che ci sia stata una lavorazione per il sostegno disaccoppiato
			Optional<IstruttoriaModel> iap = domandaAnnoPrecedente.getA4gtLavorazioneSostegnos().stream()
					.filter(p -> p.getSostegno().equals(sostegno))
					.filter(istruttoria -> !TipoIstruttoria.ANTICIPO.equals(istruttoria.getTipologia()))
					.sorted(Comparator.comparingLong(IstruttoriaModel::getId).reversed())
					.findFirst();
			if (iap.isPresent()) {
				// BRIDUSDC130: Verifica che l'istruttoria della domanda dell'anno precedente dell'intestatario della domanda per il sostegno disaccoppiato sia conclusa
				result = StatoIstruttoria.isStatoCalcoloValidoConDebiti(iap.get().getStato());
			}
		}
		return result;
	}

	private List<Fascicolo> getFascicoliEnti() {
		String paramsInput = "{}"; // Nessun filtro
//		il servizio referenziato da resource "Restituisce tutti i fascicoli legati agli enti di cui l'utente e' appartenente";
//		quindi non di tutti i CAA ma solo quelli di cui l'utente e' appartenente anche se non si applica nessun filtro come sopra.
		logger.debug("getFascicoliEnti: params " + paramsInput);
		try {
			List<String> enti = utenteClient.getEntiUtente();
			if (CollectionUtils.isEmpty(enti))
				return new ArrayList<Fascicolo>();

			FascicoloAgsFilter parametriCompleti =
					objectMapper.readValue(paramsInput, FascicoloAgsFilter.class);
			parametriCompleti.setCaacodici(enti);
			String params = objectMapper.writeValueAsString(parametriCompleti);
			logger.debug("getFascicoli: params = {}", params);
			return getFascicoli(params);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<Fascicolo> getFascicoli(String params) throws UnsupportedEncodingException {
		try {
			String encoded = URLEncoder.encode(params, "UTF-8");
			String full = configurazione.getUriAgs() + "fascicoli/?params=" + encoded;
			logger.debug("getFascicoliDaAGS " + full + " - " + params);
			ResponseEntity<List<Fascicolo>> response = restTemplate.exchange(
					new URI(full), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Fascicolo>>() {});
			List<Fascicolo> respFascicoli = response.getBody();
			return Optional.ofNullable(respFascicoli).orElse(new ArrayList<>());
		} catch (RestClientException | URISyntaxException e) {
			logger.error("Eccezione chiamando getFascicoli ",e);
		}
		return Collections.emptyList();
	}

	// Recupero i fascicoli associati ai CAA di riferimento dell'utente, in modo da avere i cuaa su cui lavorare
	private List<CuaaDenominazione> getCuaaLiquidabileKo() {
		List<CuaaDenominazione> cuaaNonLiquidabili = istruttoriaDao.findCuaaByA4gdStatoLavSostegnoAndSostegno(
				StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
				Sostegno.DISACCOPPIATO);
		cuaaNonLiquidabili.addAll(istruttoriaDao.findCuaaByA4gdStatoLavSostegnoAndSostegno(
				StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
				Sostegno.SUPERFICIE));
		cuaaNonLiquidabili.addAll(istruttoriaDao.findCuaaByA4gdStatoLavSostegnoAndSostegno(
				StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
				Sostegno.ZOOTECNIA));
		cuaaNonLiquidabili.stream().distinct().collect(Collectors.toList());
		return cuaaNonLiquidabili;
	}
	
	private List<Fascicolo> getCrossFascicoliConCuaaNonLiquidabili(
			List<Fascicolo> fascicoli,
			List<CuaaDenominazione> cuaaNonLiquidabili) {
		return fascicoli.stream()
			// Considero buoni tutti i fascicoli che non sono in stato "CHIUSO"
			.filter(p -> !p.getStato().equals("CHIUSO"))
			// Filtro di conseguenza tutti i fascicoli tenendo solamente quelli dei cuaa che hanno domande non ancora liquidate
			.filter(fascicolo -> cuaaNonLiquidabili.stream()
					.anyMatch(cuaa -> cuaa.getCuaa().equals(fascicolo.getCuaa())))
			.collect(Collectors.toList());
	}
	
	/*
	 * Data in input una lista di domande, per ciascuna domanda recupera da AGS l'iban di fascicolo
	 * e l'iban di domanda e ritorna una lista di domande aggiornata.
	 * Questa versione effettua 1 chiamata per un numero di domanda per ottenere informazioni di liquidabilita'.
	 * Questa versione attualmente non viene utilizzata a discapito di popolaDomandaConIbanFascicoloIbanDomandaAtomico che fa un'unica chiamata per n
	 * domande per reperire le informazioni di liquidabilita'.
	 * TODO 	Questo metodo andrà eliminato in un secondo momento.
	 */
	@Deprecated
	private List<DatiDomandaIbanErrato> popolaDomandaConIbanFascicoloIbanDomanda(List<DomandaUnicaModel> domandeConIbanErrato) {
		List<DatiDomandaIbanErrato> result = new ArrayList<>();
		domandeConIbanErrato.stream().forEach(item -> {
			DatiDomandaIbanErrato d = new DatiDomandaIbanErrato();
			d.setCuaa(item.getCuaaIntestatario());
			d.setDescrizioneImpresa(item.getRagioneSociale());
			d.setTipoDomanda("DU");
			d.setNumeroDomanda(item.getNumeroDomanda().intValue());
			d.setAnnoCampagna(item.getCampagna().intValue());

			try {
				InfoLiquidabilita infoLiquidabilita = recuperaInfoLiquidabilita(item.getNumeroDomanda().longValue());
				d.setIbanDomanda(infoLiquidabilita.getIban());
				d.setIbanFascicolo(infoLiquidabilita.getIbanFascicolo());
				d.setIbanValido(infoLiquidabilita.getIbanValido());
				result.add(d);
			} catch (Exception e) {
				logger.error("Errore nel recupero delle informazioni IBAN per la domanda AGS numero {}", item.getNumeroDomanda().intValue());
//				throw new RuntimeException(e); //correttiva #196:  in caso di errore la domanda non venga aggiunta alla lista
			}
		});
		return result;
	}

	/*
	 * Data in input una lista di domande, per ciascuna domanda recupera da AGS l'iban di fascicolo
	 * e l'iban di domanda e ritorna una lista di domande aggiornata.
	 * Questa versione, passando un elenco di numero domanda, utilizza un'unica chiamata per reperire le informazioni
	 * di tutte le domande.
	 */
	private List<DatiDomandaIbanErrato> popolaDomandaConIbanFascicoloIbanDomandaAtomico(List<DomandaUnicaModel> domandeConIbanErrato) {
		List<DatiDomandaIbanErrato> result = new ArrayList<>();
		List<Long> numeroDomandaList = new ArrayList<>();
		domandeConIbanErrato.stream().forEach(item -> {
			DatiDomandaIbanErrato d = new DatiDomandaIbanErrato();
			d.setCuaa(item.getCuaaIntestatario());
			d.setDescrizioneImpresa(item.getRagioneSociale());
			d.setTipoDomanda("DU");
			d.setNumeroDomanda(item.getNumeroDomanda().intValue());
			d.setAnnoCampagna(item.getCampagna().intValue());
			numeroDomandaList.add(item.getNumeroDomanda().longValue());
			result.add(d);
		});
		List<DomandaUnica> domandaUnicaList = recuperaDomandeUniche(numeroDomandaList);
		domandaUnicaList.forEach(domandaUnica -> {
			for (int i = 0; i < result.size(); i++) {
				if (BigDecimal.valueOf(result.get(i).getNumeroDomanda()).equals(domandaUnica.getInfoGeneraliDomanda().getNumeroDomanda())) {
					InfoLiquidabilita infoLiquidabilita = domandaUnica.getInfoLiquidabilita();
					result.get(i).setIbanDomanda(infoLiquidabilita.getIban());
					result.get(i).setIbanFascicolo(infoLiquidabilita.getIbanFascicolo());
					result.get(i).setIbanValido(infoLiquidabilita.getIbanValido());
					break;
				}
			}
		});
		return result;
	}
	
	@Override
	public List<DatiDomandaIbanErrato> getDatiDomandeIbanErrato() {
		for (Entry<Ruoli, Supplier<List<DatiDomandaIbanErrato>>> entry : mappaLogica.entrySet()) {
			if (utente.haRuolo(entry.getKey()))
				return entry.getValue().get();
		}
		return new ArrayList<>();
	}
	
	List<DatiDomandaIbanErrato> getDatiDomandeIbanErratoNonFiltrata() {
		// Recupero tutti i cuaa che hanno una domanda in stato CONTROLLI_LIQUIDABILE_KO per tutti i sostegni
		List<CuaaDenominazione> cuaaNonLiquidabili = getCuaaLiquidabileKo();
		
		List<DomandaUnicaModel> domandeConIbanErrato = new ArrayList<>();
		cuaaNonLiquidabili.stream()
				.map(item -> recuperaDomandeConIbanErrato(item.getCuaa()))
				.forEach(item -> domandeConIbanErrato.addAll(item));
		return popolaDomandaConIbanFascicoloIbanDomandaAtomico(domandeConIbanErrato);
	}

	@Transactional(readOnly = true)
	List<DatiDomandaIbanErrato> getDatiDomandeIbanErratoByCaa() {
		List<Fascicolo> fascicoli;
		try {
			fascicoli = getFascicoliEnti();
		} catch (Exception e) {
			logger.error("Impossibile recuperare i fascicoli associati ai CAA di riferimento dell'utente che ha effettuato l'accesso");
			fascicoli = new ArrayList<>();
		}
		// Recupero tutti i cuaa che hanno una domanda in stato CONTROLLI_LIQUIDABILE_KO per tutti i sostegni
		List<CuaaDenominazione> cuaaNonLiquidabili = getCuaaLiquidabileKo();
		fascicoli = getCrossFascicoliConCuaaNonLiquidabili(fascicoli, cuaaNonLiquidabili);
		
		// Recupero quindi la domanda più recente con l'anomalia IBAN associata ai cuaa che ho messo da parte
		List<DomandaUnicaModel> domandeConIbanErrato = new ArrayList<>();
		fascicoli.forEach(item -> recuperaDomandeConIbanErrato(item.getCuaa()).forEach(domandeConIbanErrato::add));
		
		// Per ciascuna domanda recupero da AGS l'iban di fascicolo e l'iban di domanda in modo da produrre l'output desiderato
		return popolaDomandaConIbanFascicoloIbanDomandaAtomico(domandeConIbanErrato);
	}
	
	private List<DomandaUnicaModel> recuperaDomandeConIbanErrato(String cuaa) {
		
		List<DomandaUnicaModel> result = new ArrayList<>();
		
		// Recupero tutte le domande associate al cuaa che risultano non ancora liquidate (potrebbero essere in istruttorie diverse)
		List<DomandaUnicaModel> domandeNonAncoraLiquidate = daoPassiLavSostegno.findDomandeByCuaaDomandaNonAncoraLiquidata(cuaa);
		
		domandeNonAncoraLiquidate.forEach(domanda -> {
			// Per ciascuna domanda, verifico che nel passo lavorazione esista l'anomalia BRIDUSDL037_iban = true 
			// e che di conseguenza l'esito sia DUF_023 oppure DUF_024
			// Se la domanda rispetta detti requisiti la restituisco
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByIdDomandaNonAncoraLiquidata(
					domanda.getId(), 
					org.springframework.data.domain.Sort.by(Direction.DESC, "id"));
			
			if (!passi.isEmpty() && 
				(passi.get(0).getCodiceEsito().equalsIgnoreCase(CodiceEsito.DUF_023.getCodiceEsito()) ||
				passi.get(0).getCodiceEsito().equalsIgnoreCase(CodiceEsito.DUF_024.getCodiceEsito()))
				)
				result.add(passi.get(0).getTransizioneIstruttoria().getIstruttoria().getDomandaUnicaModel());
		});
		
		return result;
	}

	@Override
	public List<StatoDomanda> elencoStatiPossibili() {
		return new ArrayList<StatoDomanda>(Arrays.asList(StatoDomanda.values()));
	}
}
