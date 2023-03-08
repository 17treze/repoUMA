package it.tndigitale.a4gistruttoria.action;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.client.api.AnagrafeZootecnicaControllerApi;
import it.tndigitale.a4g.proxy.client.model.ArrayOfClsCapoMacellato;
import it.tndigitale.a4g.proxy.client.model.ArrayOfClsCapoOvicaprino;
import it.tndigitale.a4g.proxy.client.model.ArrayOfClsCapoVacca;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;

@Component
public class CallElencoCapiAction {
	
	@Autowired
	private RestTemplate restTemplate;
	@Value("${a4gistruttoria.proxy.uri}")
	private String proxyUri;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ConsumeExternalRestApi consumeExternalRestApi;

	private static Logger log = LoggerFactory.getLogger(CallElencoCapiAction.class);
	
	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public ArrayOfClsCapoVacca getVaccheLatte(CapiAziendaPerInterventoFilter filter) {
		return getAnagrafeZootecnicaController().getBoviniCarneLatteDetenutiUsingGET(
				filter.getCuaa(),
				filter.getCampagna(),
				filter.getIntervento().name(),
				filter.getCuaaSubentrante(),
				filter.getIdAllevamento());
	}

	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public ArrayOfClsCapoMacellato getCapiMacellati(CapiAziendaPerInterventoFilter filter) {
		// Trava qui i dati e non ritornare ArrayOfClsCapoMacellato
		return getAnagrafeZootecnicaController().getBoviniMacellatiDetenutiUsingGET(
				filter.getCuaa(),
				filter.getCampagna(),
				filter.getIntervento().name(),
				filter.getCuaaSubentrante(),
				filter.getIdAllevamento());
	}

	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public ArrayOfClsCapoOvicaprino getOviCaprini(CapiAziendaPerInterventoFilter filter) {
		return getAnagrafeZootecnicaController().getOviCapriniDetenutiUsingGET(
				filter.getCuaa(),
				filter.getCampagna(),
				filter.getIntervento().name(),
				filter.getCuaaSubentrante(),
				filter.getIdAllevamento());
	}

	private AnagrafeZootecnicaControllerApi getAnagrafeZootecnicaController() {
		return consumeExternalRestApi.restClientProxy(AnagrafeZootecnicaControllerApi.class);
	}
	
	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public JsonNode getElencoCapiMacellati(CapiAziendaPerInterventoFilter filter) {
		try {
			UriComponents buildAndExpand = buildUrl(filter,"/bovini/macellati");
			log.debug("Chiamata al servizio capi BDN di proxy: {}", buildAndExpand.toUriString());
			String responseElencoCapi = restTemplate.getForObject(buildAndExpand.toUriString(), String.class);
			return objectMapper.readTree(responseElencoCapi);
		} catch (IOException  e) {
			throw new RuntimeException("Fallita chiamata al servizioget capi BDN",e);
		}
	}

	public UriComponents buildUrl(CapiAziendaPerInterventoFilter filter,String specie) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(proxyUri)
				.path("/zootecnia")
				.path("/{cuaa}")
				.path("/{campagna}")
				.path("/capi").path(specie)
				.queryParam("intervento", filter.getIntervento().name());
		if (filter.getIdAllevamento() != null) {
			builder.queryParam("idAllevamento", filter.getIdAllevamento());
		}
		if (filter.getCuaaSubentrante() != null) {
			builder.queryParam("cuaaSubentrante", filter.getCuaaSubentrante());
		}
		return builder.buildAndExpand(filter.getCuaa(),filter.getCampagna());
	}
	
	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public JsonNode getElencoOviCaprini(CapiAziendaPerInterventoFilter filter) {
		try {
			UriComponents buildAndExpand = buildUrl(filter,"/ovicaprini");
			log.debug("Chiamata al servizio capi BDN di proxy: {}", buildAndExpand.toUriString());
			String responseElencoCapi = restTemplate.getForObject(buildAndExpand.toUriString(), String.class);
			return objectMapper.readTree(responseElencoCapi);
		} catch (IOException  e) {
			throw new RuntimeException("Fallita chiamata al servizioget capi BDN",e);
		}
	}
	
	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(1000))
	public JsonNode getElencoVaccheLatte(CapiAziendaPerInterventoFilter filter) {
		try {
			UriComponents buildAndExpand = buildUrl(filter,"/bovini/lattecarne");
			log.debug("Chiamata al servizio capi BDN di proxy: {}", buildAndExpand.toUriString());
			String responseElencoCapi = restTemplate.getForObject(buildAndExpand.toUriString(), String.class);
			return objectMapper.readTree(responseElencoCapi);
		} catch (Exception  e) {
			throw new RuntimeException("Fallita chiamata al servizioget capi BDN",e);
		}
	}
	
}
