package it.tndigitale.a4g.fascicolo.antimafia.service;

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.config.DateFormatConfig;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.framework.client.ClientServiceBuilder;

@Service
public class PassaggioVerificaPeriodicaService {
	private static Logger logger = LoggerFactory.getLogger(PassaggioVerificaPeriodicaService.class);
	
	@Autowired
	AntimafiaServiceImpl antimafiaService;
	
	@Value("${verificaperiodica.utenzatecnica.username}")
	private String utenzaTecnica;
	
	@Value("${a4gistruttoria.proxy.uri}")
	private String a4gproxyUrl;
	
	@Autowired
	public ClientServiceBuilder clientServiceBuilder;
	
	@Autowired
	private DateFormatConfig dateFormatConfig;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public RestTemplate getRestTemplateUtenzaApplicativa() {
		return clientServiceBuilder.buildWith(() -> utenzaTecnica);
	}
	
	@Scheduled(cron = "${cron.expression.verificaperiodica}")
	@Transactional
	@Async("threadGestioneProcessiScheduler")
	public void passaggioVerificaPeriodica() throws Exception {
		logger.info("Passaggio Verifica Periodica in data {} ", Calendar.getInstance());
			RestTemplate restTemplateUtenzaTecnica = getRestTemplateUtenzaApplicativa();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			DichiarazioneFilter dichiarazioneFilter = new DichiarazioneFilter();
			dichiarazioneFilter.setStatiDichiarazione(Arrays.asList(StatoDichiarazioneEnum.CONTROLLATA.getIdentificativoStato(), StatoDichiarazioneEnum.CONTROLLO_MANUALE.getIdentificativoStato(),
					StatoDichiarazioneEnum.PROTOCOLLATA.getIdentificativoStato()));
			
			List<Dichiarazione> dichiarazioniAntimafia = antimafiaService.getDichiarazioni(dichiarazioneFilter);
			if (CollectionUtils.isEmpty(dichiarazioniAntimafia)) {
				logger.debug("Nessuna dichiarazione Antimafia trovata su cui fare il passaggio in Verifica Periodica");
				return;
			}
			dichiarazioniAntimafia.stream()
			// escludi passaggio automatico per domande in esito positivo AM-03-07-01REV 
			.filter(dichiarazione -> !(dichiarazione.getStato().getIdentificativo().equals(StatoDichiarazioneEnum.POSITIVO.getIdentificativoStato())))
			.forEach(dichiarazione -> {
				try {
					String sincronizzazione = restTemplateUtenzaTecnica.getForObject(new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat(dichiarazione.getId().toString())), String.class);
					if (!StringUtils.isEmpty(sincronizzazione)) {
						JsonNode jsonSincronizzazione = objectMapper.readTree(sincronizzazione);
						Date dataInizVali = dateFormatConfig.convertiJsonIsoDate(jsonSincronizzazione.get("dataInizVali").asText());
						Calendar calDataInizVali = Calendar.getInstance();
						calDataInizVali.setTime(dataInizVali);
						// adding days into Date in Java
						calDataInizVali.add(Calendar.DATE, 180);
						if (Calendar.getInstance().compareTo(calDataInizVali) > 0) {
							logger.info("Passaggio Verifica Periodica dichiarazione con id {} ", dichiarazione.getId());
							// passaggio a VERIFICA PERIODICA
							dichiarazione.getStato().setId(null);
							dichiarazione.getStato().setDescrizione(null);
							dichiarazione.getStato().setIdentificativo(StatoDichiarazioneEnum.VERIFICA_PERIODICA.getIdentificativoStato());
	
							logger.debug("Passaggio Verifica Periodica della dichiarazione antimafia");
							antimafiaService.aggiornaDichiarazione(dichiarazione);
							
							// aggiornamento DATA_FINE_VALI a sysdate
							((ObjectNode) (jsonSincronizzazione)).put("dataFineVali", dateFormatConfig.convertiDataFull((new Date()).getTime()));
							URI uriSincronizzazione = new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat(dichiarazione.getId().toString()));
							HttpEntity<String> entitySincronizzazione = new HttpEntity<>(objectMapper.writeValueAsString(jsonSincronizzazione), headers);
							logger.debug("Sincronizzazione: Aggiornamento DATA_FINE_VALI a sysdate");
							restTemplateUtenzaTecnica.exchange(uriSincronizzazione, HttpMethod.PUT, entitySincronizzazione, String.class);
						}
					}
				} catch (Exception e) {
					logger.error("Errore passaggio automatico a verifica periodica dichiarazione con ID {}", dichiarazione.getId(), e);
				}
			});
	}
}
