package it.tndigitale.a4g.uma.business.service.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.aual.FabbricatoAualDto;
import it.tndigitale.a4g.uma.dto.aual.MacchinaAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespFabbricatiAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespMacchineAualDto;

@Component
public class UmaDotazioneTecnicaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.fascicolo.dotazionetecnica.url}")
	private String urlDotazioneTecnica;

	private static final Logger logger = LoggerFactory.getLogger(UmaDotazioneTecnicaClient.class);
	
	// al momento di creazione della richiesta di carburante importa i fabbricati significativi per cui si portrebbe richiedere carburante
	public List<FabbricatoAualDto> getFabbricati(String cuaa) {

        final String uri = urlDotazioneTecnica + "/leggiFabbricatiFS6?cuaa=" + cuaa;

		try {
	        RestTemplate restTemplate = new RestTemplate();
	        URL url = new URL(uri);
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
	        http.setRequestProperty("Content-Type", "application/json");
	        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	        logger.info(response.getBody());
	        ObjectMapper objectMapper = new ObjectMapper();
	        RespFabbricatiAualDto respFabbricati = objectMapper.readValue(response.getBody(), new TypeReference<RespFabbricatiAualDto>(){});
	        return respFabbricati.getData();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}		
	}
	
	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
	public List<MacchinaAualDto> getMacchine(String cuaa) {
		
        final String uri = urlDotazioneTecnica + "/leggiMacchineFS6?cuaa=" + cuaa;

		try {
	        RestTemplate restTemplate = new RestTemplate();
	        URL url = new URL(uri);
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
	        http.setRequestProperty("Content-Type", "application/json");
	        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	        logger.info(response.getBody());
	        ObjectMapper objectMapper = new ObjectMapper();
	        RespMacchineAualDto respMacchine = objectMapper.readValue(response.getBody(), new TypeReference<RespMacchineAualDto>(){});
	        return respMacchine.getData();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
