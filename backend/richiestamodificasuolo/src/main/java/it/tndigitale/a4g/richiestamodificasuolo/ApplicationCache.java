package it.tndigitale.a4g.richiestamodificasuolo;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.client.model.Fascicolo;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.FascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.EntiListCache;
import it.tndigitale.a4g.richiestamodificasuolo.dto.MandatiListCache;
import it.tndigitale.a4g.richiestamodificasuolo.exception.AuthorizationException;

@Component
@EnableScheduling
public class ApplicationCache {

	private static final Logger log = LoggerFactory.getLogger(ApplicationCache.class);
	
	@Autowired
	private FascicoloClient fascicoloClient;
	
	@Autowired
	private UtenteComponent utenteComponent;
	
	@Autowired
	private Clock clock;
	
	@Value("${it.tndigit.cache.mandati.expire.hours}")
    private String expireTime;

	private HashMap<String,EntiListCache> utenteEntiCache = new HashMap<>();
	
	private HashMap<String,MandatiListCache> enteMandatiCache = new HashMap<>();

	public List<Fascicolo> getMandatiUtente() throws Exception {
		String utente = utenteComponent.utenza();
		
		EntiListCache entiListCached = utenteEntiCache.get(utente);
		List<Fascicolo> listFascicoli = new ArrayList<>();
		List<String> enti;
		if (entiListCached == null || entiListCached.getEnti().isEmpty()) {
			listFascicoli = fascicoloClient.ricercaFascicoli();
			enti = fascicoloClient.ricercaFascicoli().stream().filter(x -> x.getCaacodice()!=null).map(Fascicolo::getCaacodice).distinct().collect(Collectors.toCollection(ArrayList::new));
			EntiListCache value = new EntiListCache();
			value.setEnti(enti);
			value.setUltimoAggiornamento(clock.now());
			utenteEntiCache.put(utente, value);
		}else enti = entiListCached.getEnti();

		return popolaEnteMandatiCache(utente, enti, listFascicoli);
	}
	
	public void clearCacheAll() {
		utenteEntiCache.clear();
		enteMandatiCache.clear();
	}

	public void refreshCacheAll() {
		try {

			if (utenteEntiCache.size() > 0) {
				utenteEntiCache.entrySet().removeIf(e ->
						(Duration.between(utenteEntiCache.get(e.getKey()).getUltimoAggiornamento(), clock.now()).toHours() > Long.parseLong(expireTime)));
			}

			if (enteMandatiCache.size() > 0) {
				enteMandatiCache.entrySet().removeIf(e ->
						(Duration.between(enteMandatiCache.get(e.getKey()).getUltimoAggiornamento(), clock.now()).toHours() > Long.parseLong(expireTime)));
			}

			} catch (Exception e){
			log.error("Errore nella pulizia della cache dei mandati", e);
		}
	}

	private List<Fascicolo> popolaEnteMandatiCache(String utente, List<String> enti, List<Fascicolo> listFascicoli){

		List<Fascicolo> listaMandati = new ArrayList<>();

		for (String e : enti) {
			if (enteMandatiCache.get(e) != null) {
				listaMandati.addAll(enteMandatiCache.get(e).getFascicoli());
			} else {
				try {
					if (listFascicoli.isEmpty()) {
						listFascicoli = fascicoloClient.ricercaFascicoli();
					}
					Map<String, List<Fascicolo>> fascicoliPerEnte = listFascicoli.stream().filter(x -> x.getCaacodice()!=null).collect(Collectors.groupingBy(Fascicolo::getCaacodice));
					fascicoliPerEnte.keySet().forEach( ente -> {
						if (enteMandatiCache.get(ente) == null ) {
							MandatiListCache value = new MandatiListCache();
							value.setFascicoli(fascicoliPerEnte.get(ente));
							value.setUltimoAggiornamento(clock.now());
							enteMandatiCache.put(ente, value);
							listaMandati.addAll(fascicoliPerEnte.get(ente));
						}
					});
				} catch (Exception e1) {
					throw AuthorizationException.ExceptionType.GENERIC_EXCEPTION.newAuthorizationExceptionInstance("Errore nel recupero dei mandati per l'utente ".concat(utente), e1);
				}
			}
		}
		return listaMandati;
	}
}
