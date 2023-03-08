package it.tndigitale.a4gistruttoria.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.client.model.EsitoAntimafia;
import it.tndigitale.a4gistruttoria.action.antimafia.DomandePsrHandler;
import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoEvent;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoPublisher;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Ags;

@Service
public class ImportaDatiSuperficieService {
	
	private static Logger logger = LoggerFactory.getLogger(ImportaDatiSuperficieService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUrl;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DomandePsrHandler domandePsrHandler;
	@Autowired
	private ConsumeExternalRestApi4Ags consumeExternalRestApi4Ags;
	@Autowired
	private ProcessoPublisher processoControlloAntimafiaPublisher;
	
	public List<DomandaCollegata> importaDatiSuperficieOneShot(DomandeCollegateImport domandeCollegateImport) throws Exception {
		registraProcesso(domandeCollegateImport.getCuaa().size());
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(domandeCollegateImport), StandardCharsets.UTF_8.name()));
		String domandePSR = restTemplate.getForObject(new URI(agsUrl.concat("domandePSR").concat("/").concat(params)), String.class);
		List<DomandaCollegata> domandeCollegate = new ArrayList<>();
		if (StringUtils.isEmpty(domandePSR)) {
			chiudiProcesso();
			return domandeCollegate;
		}
		JsonNode jsonDomandePSR = objectMapper.readTree(domandePSR);
		logger.debug("Inserimento Dati Superficie");
		
		for (JsonNode domandaPSR : jsonDomandePSR) {
			domandePsrHandler.elaboraDomanda(domandaPSR, domandeCollegate);
		}
		sincronizzaDomandeAgs(domandeCollegate);
		chiudiProcesso();
		return domandeCollegate;
	}

	public List<DomandaCollegata> importaDatiSuperficie(DomandeCollegateImport domandeCollegateImport) {
		registraProcesso(domandeCollegateImport.getCuaa().size());
		DomandeCollegateImport paramsDto = new DomandeCollegateImport();
		List<DomandaCollegata> domandeCollegate = new ArrayList<>();
		for (String cuaa: domandeCollegateImport.getCuaa()) {
			try {
				BeanUtils.copyProperties(domandeCollegateImport, paramsDto);
				paramsDto.setCuaa(Arrays.asList(cuaa));
				String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(paramsDto), StandardCharsets.UTF_8.name()));
				String domandePSR = restTemplate.getForObject(new URI(agsUrl.concat("domandePSR").concat("/").concat(params)), String.class);
				domandePsrHandler.elaboraDomanda(domandePSR, domandeCollegate);
			} catch (Exception e) {
				logger.error("Errore nel recupero ed elborazione domanda PSR per il CUAA {}",cuaa,e);
			}
		}
		sincronizzaDomandeAgs(domandeCollegate);
		chiudiProcesso();
		return domandeCollegate;
	}
	
	private void sincronizzaDomandeAgs(List<DomandaCollegata> domandeCollegate) {
		// sincronizzazione domande con AGS - le date vengono settate a null 
		List<EsitoAntimafia> esitiAntimafia = new ArrayList<>();
		domandeCollegate.forEach(domandaCollegata -> {
			EsitoAntimafia esitoAntimafia = new EsitoAntimafia();
			esitoAntimafia.setCuaa(domandaCollegata.getCuaa());
			esitoAntimafia.setTipoDomanda(domandaCollegata.getTipoDomanda());
			esitoAntimafia.setIdDomanda(domandaCollegata.getIdDomanda());
			esitoAntimafia.setDtInizioEsitoNegativo(null);
			esitoAntimafia.setDtInizioSilenzioAssenso(null);
			esitoAntimafia.setDtFineEsitoNegativo(null);
			esitoAntimafia.setDtFineSilenzioAssenso(null);
			esitiAntimafia.add(esitoAntimafia);
		});
		consumeExternalRestApi4Ags.sincronizzaEsitiAntimafiaAgs(esitiAntimafia);
	}
	
	private void registraProcesso(int num) {
		DatiElaborazioneProcesso datiElaborazioneProcesso = new DatiElaborazioneProcesso();
		datiElaborazioneProcesso.setTotale("0");
		datiElaborazioneProcesso.setGestite(new ArrayList<>());
		datiElaborazioneProcesso.setConProblemi(new ArrayList<>());
		datiElaborazioneProcesso.setDaElaborare(String.valueOf(num));

		ProcessoEvent processoEvent = new ProcessoEvent();
		processoEvent.setTipoProcesso(TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);
		processoEvent.setStatoProcesso(StatoProcesso.RUN);
		processoEvent.setPercentualeAvanzamento(BigDecimal.ZERO);
		processoEvent.setDtInizio(new Date());
		processoEvent.setDatiElaborazioneProcesso(datiElaborazioneProcesso);
		processoControlloAntimafiaPublisher.handleEvent(processoEvent);
	}
	
	private void chiudiProcesso() {
		ProcessoEvent processoEvent = new ProcessoEvent();
		processoEvent.setId((Long) CustomThreadLocal.getVariable("idProcesso"));
		processoEvent.setStatoProcesso(StatoProcesso.OK);
		processoEvent.setDtFine(new Date());
		processoEvent.setPercentualeAvanzamento(BigDecimal.valueOf(100L));
		processoControlloAntimafiaPublisher.handleEvent(processoEvent);	
	}

}
