package it.tndigitale.a4g.uma.business.service.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.uma.dto.aual.FabbricatoAualDto;
import it.tndigitale.a4g.uma.dto.aual.MacchinaAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespFabbricatiAualDto;
import it.tndigitale.a4g.uma.dto.aual.RespMacchineAualDto;

@Component
public class UmaDotazioneTecnicaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.fascicolo.dotazionetecnica.url}")
	private String urlDotazioneTecnica;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
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
	        // rimuovere le occorrenze con data fine
	        return respFabbricati.getData().stream()
	        		.filter(item -> isFabbricatoValido(item))
	        		.collect(Collectors.toList());
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
	        // rimuovere le occorrenze con data fine
	        return respMacchine.getData().stream()
	        		.filter(item -> isMacchinaValida(item))
	        		.collect(Collectors.toList());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private boolean isFabbricatoValido(FabbricatoAualDto fabbricato) {
		try {
			return !sdf.parse(fabbricato.getDataFineCond()).before(new Date());
		}
		catch (ParseException ex) {
			logger.error(ex.getMessage());
			return false;
		}
	}

	private boolean isMacchinaValida(MacchinaAualDto macchina) {
		try {
			return !sdf.parse(macchina.getDataCess()).before(new Date());
		}
		catch (ParseException ex) {
			logger.error(ex.getMessage());
			return false;
		}
	}
}
