package it.tndigitale.a4g.uma.business.service.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

import it.tndigitale.a4g.uma.dto.aual.RespTerritorioAualDto;
import it.tndigitale.a4g.uma.dto.aual.TerritorioAualDto;

@Component
public class UmaTerritorioClient extends AbstractClient {
	
	@Value("${it.tndigit.a4g.uma.fascicolo.territorio.url}")
	private String urlTerritorio;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private static final Logger logger = LoggerFactory.getLogger(UmaTerritorioClient.class);
	
	//	// reperisce le colture solo in provincia di TN e BZ
	//	// filtra solo le particelle opportune - Refactor: fai in modo che le restituisca già pronte interrogando solo il servizio rest
	//	public List<ParticellaDto> getColture(String cuaa, LocalDateTime data) {
	//		List<ParticellaDto> pianoColturale = getPianoColturaleControllerApi().getPianoColturale(cuaa, data, null, null, null);
	//		// UMA-01-02-06-A-RC-1: Scarta le particelle che hanno conduzione Altro e Atto 314 e TASK-UMA-37: scarta conduzione Altro e Atto 394
	//		if (CollectionUtils.isEmpty(pianoColturale)) {return new ArrayList<>();}
	//		return pianoColturale.stream()
	//				.filter(pc -> !(314 == pc.getConduzioneDto().getCodiceAtto() && TitoloConduzione.ALTRO.name().equals(pc.getConduzioneDto().getTitolo().name())))
	//				.filter(pc -> !(394 == pc.getConduzioneDto().getCodiceAtto() && TitoloConduzione.ALTRO.name().equals(pc.getConduzioneDto().getTitolo().name())))
	//				// escludi in ciascuna particella le colture che hanno criterio di mantenimento "NESSUNA_PRATICA"
	//				.map(pc -> {
	//					List<ColturaDto> coltureFiltrate = pc.getColture().stream().filter(c -> !CriterioMantenimento.NESSUNA_PRATICA.equals(c.getCriterioMantenimento())).collect(Collectors.toList());
	//					return pc.setColture(coltureFiltrate);
	//				})
	//				.collect(Collectors.toList());
	//	}
	//
	//	private PianoColturaleControllerApi getPianoColturaleControllerApi() {
	//		return restClientProxy(PianoColturaleControllerApi.class, urlTerritorio);
	//	}
	
	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
	public List<TerritorioAualDto> getColture(String cuaa, LocalDateTime data) {
		
		final String uri = urlTerritorio + "/leggiConsistenzaFS7?cuaa=" + cuaa;
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			URL url = new URL(uri);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Content-Type", "application/json");
			ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
			logger.info(response.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			RespTerritorioAualDto responseAual = objectMapper.readValue(response.getBody(),
					new TypeReference<RespTerritorioAualDto>() {
					});
			// rimuovere le particelle con data fine o con nessun mantenimento e convertire il codice qualità (null -> 000)
			return responseAual.getData().stream().collect(Collectors.toList());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
	public List<TerritorioAualDto> getColturePerRichiestaCarburante(String cuaa, LocalDateTime data) {
		
		final String uri = urlTerritorio + "/leggiConsistenzaFS7?cuaa=" + cuaa;
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			URL url = new URL(uri);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("Content-Type", "application/json");
			ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
			logger.info(response.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			RespTerritorioAualDto responseAual = objectMapper.readValue(response.getBody(),
					new TypeReference<RespTerritorioAualDto>() {
					});
			// rimuovere le particelle con data fine o con nessun mantenimento e convertire il codice qualità (null -> 000)
			return responseAual.getData().stream().filter(item -> isColturaValida(item)).collect(Collectors.toList());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean isColturaValida(TerritorioAualDto terreno) {
		try {
			if (terreno.getDataFineCond() == null) {
				return terreno.getListaUtilizzoTerreno().stream().anyMatch(
								ut -> ut.getListaUtilizzoSuolo().stream().anyMatch(us -> us.getTipoMantenimento() == null)); // da rivedere!!!
			}
			return sdf.parse(terreno.getDataFineCond()).after(new Date())
					&& terreno.getListaUtilizzoTerreno().stream().anyMatch(
							ut -> ut.getListaUtilizzoSuolo().stream().anyMatch(us -> us.getTipoMantenimento() == null)); // da rivedere!!!
		}
		catch (ParseException ex) {
			logger.error(ex.getMessage());
			return false;
		}
	}
}
