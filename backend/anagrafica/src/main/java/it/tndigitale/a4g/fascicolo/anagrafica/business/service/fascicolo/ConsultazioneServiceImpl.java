package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.Ruoli;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.ConsultazioneFascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ConsultazioneFascicoloFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.Fascicolo;
// import it.tndigitale.a4g.fascicolo.antimafia.rest.AgsClient;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;

/*
 * @author B.Irler
 */
@Service
public class ConsultazioneServiceImpl implements ConsultazioneService {
	
	@Autowired
	private UtenteClient utenteClient;
	
	@Autowired
	private ConsultazioneFascicoloDao fascicoloDao;
	
	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UtenteComponent utenteComponent;
	
	private static Logger logger = LoggerFactory.getLogger(ConsultazioneServiceImpl.class);
	
	private Map<Ruoli, Function<String, List<Fascicolo>>> mappaLogica = null;
	
	//	@Value("${a4gfascicolo.integrazioni.anagraficaimpresa.uri}")
	//	private String urlIntegrazioniAnagraficaImpresa;
	
	public ConsultazioneServiceImpl() {
		mappaLogica = new LinkedHashMap<Ruoli, Function<String, List<Fascicolo>>>();
		mappaLogica.put(Ruoli.RICERCA_FASCICOLO_NON_FILTRATA, x -> this.getTuttiFascicoli(x));
		mappaLogica.put(Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE, x -> this.getFascicoliEnti(x));
	}
	
	@Transactional(readOnly = true)
	@Override
	//@PreAuthorize("hasRole('a4gfascicolo.fascicolo.ricerca.tutti') or hasRole('a4gfascicolo.fascicolo.ricerca.utente') or hasRole('a4gfascicolo.fascicolo.ricerca.ente')")
	public List<Fascicolo> getFascicoli(String params) throws Exception {
		for (Ruoli abilitazione : mappaLogica.keySet()) {
			if (utenteComponent.haRuolo(abilitazione))
				return mappaLogica.get(abilitazione).apply(params);
		}
		return new ArrayList<Fascicolo>();
	}
	
	@Transactional(readOnly = true)
	@Override
	//@PostAuthorize("hasRole('a4gfascicolo.fascicolo.ricerca.tutti') or (hasRole('a4gfascicolo.fascicolo.ricerca.utente') and hasPermission(returnObject, 'TITOLARE')) or (hasRole('a4gfascicolo.fascicolo.ricerca.ente') and hasPermission(returnObject, 'ENTE'))")
	public Fascicolo getFascicolo(Long idFascicolo) throws Exception {
		logger.debug("getFascicolo({})", idFascicolo);
		// return agsClient.getFascicolo(idFascicolo);
		return fascicoloDao.getFascicolo(idFascicolo);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Fascicolo getFascicolo(String cuaa) throws Exception {
		logger.debug("getFascicolo({})", cuaa);
		ConsultazioneFascicoloFilter filter = new ConsultazioneFascicoloFilter();
		filter.setCuaa(cuaa);
		final List<Fascicolo> fascicoli = fascicoloDao.getFascicoli(filter);
		return fascicoli.isEmpty() ? null : fascicoli.get(0);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Fascicolo> getFascicoliEnti(String params) {
		logger.debug("getFascicoliEnti: params " + params);
		//		try {
		//			List<String> enti = utenteClient.getEntiUtente();
		//			if (CollectionUtils.isEmpty(enti))
		//				return new ArrayList<Fascicolo>();
		//
		//			ParamsRicercaFascicolo parametriCompleti = 
		//					objectMapper.readValue(params, ParamsRicercaFascicolo.class);
		//			parametriCompleti.setCaacodici(enti);
		//			return agsClient.getFascicoli(parametriCompleti);
		//		} catch (RuntimeException re) {
		//			throw re;
		//		} catch (Exception e) {
		//			throw new RuntimeException(e);
		//		}
		throw new RuntimeException("Not implemented");
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Fascicolo> getFascicoliAziendeUtente() throws Exception {
		logger.debug("getFascicoliAziendeUtente");
		List<String> listaCuaa = utenteClient.getAziendeUtente();
		listaCuaa = (listaCuaa != null) ? listaCuaa : new ArrayList<>();
		List<Fascicolo> result = new ArrayList<>();
		for (String cuaa : listaCuaa) {
			if (cuaa != null && !cuaa.isEmpty()) {
				ConsultazioneFascicoloFilter filter = new ConsultazioneFascicoloFilter();
				filter.setCuaa(cuaa);
				result.addAll(fascicoloDao.getFascicoli(filter));
			}
		}
		return result;
	}
	
	//	@Transactional(readOnly = true)
	//	@Override
	//	public KeyValueStringString getAziendaPersonaRappresentante(String cfPersona, String cuaa) throws Exception {
	//		// chiama parix
	//		JsonNode datiIntegrazioniAnagraficaImpresa = getResponseParix(urlIntegrazioniAnagraficaImpresa
	//				.concat(String.format(ApiUrls.INTEGRAZIONI_ANAGRAFICA_IMPRESA_PERSONENONCESSATEPERCF, cfPersona)));
	//		
	//		if (datiIntegrazioniAnagraficaImpresa == null)
	//			throw new UtenteException(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, cfPersona, cuaa));
	//		
	//		JsonNode schedePersona = datiIntegrazioniAnagraficaImpresa.at("/listapersone/schedapersona");
	//		
	//		Optional<JsonNode> res = null;
	//		
	//		if (schedePersona != null && schedePersona.elements().hasNext()) {
	//			
	//			res = StreamSupport.stream(schedePersona.spliterator(), false).filter(schedaPersona -> cuaa
	//					.equals(schedaPersona.path("estremiimpresa").path("codicefiscale").textValue())
	//					&& (StreamSupport
	//							.stream(schedaPersona.path("persona").path("cariche").path("carica").spliterator(), false)
	//							.filter(carica -> A4gfascicoloConstants.CARICA_RESPONSABILE
	//									.contains("-" + carica.path("ccarica").textValue() + "-"))).findAny().isPresent())
	//					.findAny();
	//			if (res.isPresent()) {
	//				return new KeyValueStringString(cuaa,
	//						res.get().path("estremiimpresa").path("denominazione").textValue());
	//			}
	//			else {
	//				throw new UtenteException(
	//						String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, cfPersona, cuaa));
	//			}
	//		}
	//		else {
	//			throw new UtenteException(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, cfPersona, cuaa));
	//		}
	//		
	//	}
	
	@Transactional(readOnly = true)
	@Override
	public boolean controllaEsistenzaFascicoloValido(String cuaa) throws Exception {
		logger.debug("controllaEsistenzaFascicoloValido: cuaa " + cuaa);
		//		// chiama ags
		//		if (agsClient.verificaFascicoloValido(cuaa) == false) {
		//			throw new UtenteException(String.format(MessaggiErrori.FASCICOLO_NON_VALIDO_AGS, cuaa));
		//		} else {
		//			return true;
		//		}
		throw new RuntimeException("Not implemented");
	}
	
	private List<Fascicolo> getTuttiFascicoli(String params) {
		logger.debug("getTuttiFascicoli: params " + params);
		//		try {
		//			return agsClient.getFascicoli(params);
		//		} catch (Exception e) {
		//			throw new RuntimeException(e);
		//		}
		throw new RuntimeException("Not implemented");
	}
	
	//	private JsonNode getResponseParix(String url) throws URISyntaxException, IOException, Exception {
	//		String responseIntegrazioniAnagraficaImpresa = restTemplate.getForObject(new URI(url), String.class);
	//		
	//		// recupero response da Parix: struttura di base sempre uguale
	//		JsonNode jsonIntegrazioniAnagraficaImpresa = objectMapper.readTree(responseIntegrazioniAnagraficaImpresa);
	//		JsonNode header = jsonIntegrazioniAnagraficaImpresa.path("header");
	//		String esitoIntegrazioniAnagraficaImpresa = header.path("esito").textValue();
	//		JsonNode datiIntegrazioniAnagraficaImpresa = jsonIntegrazioniAnagraficaImpresa.path("dati");
	//		
	//		if (!esitoIntegrazioniAnagraficaImpresa.equals("OK")) {
	//			
	//			JsonNode errore = datiIntegrazioniAnagraficaImpresa.path("errore");
	//			String messaggio = errore.path("msgerr").textValue();
	//			
	//			logger.error("getResponseParix({}) - msg {}", url, messaggio);
	//			return null;
	//		}
	//		return datiIntegrazioniAnagraficaImpresa;
	//	}
}