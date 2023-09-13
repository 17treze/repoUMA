package it.tndigitale.a4g.uma.business.service.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespFascicoloAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespSoggettoAualDto;
import it.tndigitale.a4g.uma.dto.aual.SoggettoAualDto;

@Component
public class UmaAnagraficaClient extends AbstractClient {

	private static final Logger logger = LoggerFactory.getLogger(UmaAnagraficaClient.class);
	
	@Value("${it.tndigit.a4g.uma.fascicolo.anagrafica.url}")
	private String urlAnagrafica;
	
	public FascicoloAualDto getFascicolo(String cuaa) {
		
        final String uri = urlAnagrafica + "/trovaFascicoloFS6?cuaa=" + cuaa;
        
		try {
	        RestTemplate restTemplate = new RestTemplate();
	        URL url = new URL(uri);
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
	        http.setRequestProperty("Content-Type", "application/json");
	        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	        logger.info(response.getBody());
	        ObjectMapper objectMapper = new ObjectMapper();
	        RespFascicoloAualDto respFascicolo = objectMapper.readValue(response.getBody(), new TypeReference<RespFascicoloAualDto>(){});
	        return respFascicolo.getData();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public SoggettoAualDto getSoggetto(String codiFisc) {
		
        final String uri = urlAnagrafica + "/dettaglioSoggettoFS6?cuaa=" + codiFisc;
        
		try {
	        RestTemplate restTemplate = new RestTemplate();
	        URL url = new URL(uri);
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
	        http.setRequestProperty("Content-Type", "application/json");
	        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	        logger.info(response.getBody());
	        ObjectMapper objectMapper = new ObjectMapper();
	        RespSoggettoAualDto respSoggetto = objectMapper.readValue(response.getBody(), new TypeReference<RespSoggettoAualDto>(){});
	        return respSoggetto.getData();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
