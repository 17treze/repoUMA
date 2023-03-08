/**
 * 
 */
package it.tndigitale.a4gistruttoria.action;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandler;

/**
 * @author S.DeLuca
 *
 */
@Component
public class ParixConsumer implements Consumer<IstruttoriaAntimafiaChainHandler> {

	private static final Logger logger = LoggerFactory.getLogger(ParixConsumer.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RestTemplate restTemplate;
	
	// a4gistruttoria.proxy.uri = 		http://localhost:9004/a4gproxy/api/v1/
	// a4gistruttoria.proxy.parix.uri = 	http://localhost:9004/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto
	@Value("${a4gistruttoria.proxy.parix.uri}")
	private String parixUrl;
	
	@Override
	public void accept(IstruttoriaAntimafiaChainHandler istruttoriaAntimafiaChain) {
		try {
			JsonNode dettaglioImpresa = istruttoriaAntimafiaChain.getDatiDichiarazione().path("dettaglioImpresa");
			istruttoriaAntimafiaChain.setDettaglioImpresa(dettaglioImpresa);
			String cuaa = dettaglioImpresa.path("codiceFiscale").textValue();
			istruttoriaAntimafiaChain.setCuaa(cuaa);

			//get params
			ObjectNode inputRicerca = objectMapper.createObjectNode();
			inputRicerca.put("provinciaSede", dettaglioImpresa.path("estremiCCIAA").path("sede").textValue());
			inputRicerca.put("numeroREASede", dettaglioImpresa.path("estremiCCIAA").path("numeroIscrizione").textValue());
			String params =  "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(inputRicerca), StandardCharsets.UTF_8.name()));

			//controllo 1: chiamata parix dettaglio completo
			String responseAnagraficaImpresaDettaglioCompleto = restTemplate.getForObject(new URI(parixUrl.concat("/").concat(params)),String.class);

			JsonNode jsonResponse = objectMapper.readTree(responseAnagraficaImpresaDettaglioCompleto);
			JsonNode datiParix = jsonResponse.path("dati");
			String esitoParix = jsonResponse.path("header").path("esito").textValue();
			istruttoriaAntimafiaChain.setSoggettiParix(datiParix.path("datiimpresa").path("personesede").path("persona"));

			//risposta parix. if parix ko -> impresa cessata || servizio non disponibile
			if (!"OK".equals(esitoParix)) {
				String descrizioneErr = datiParix.path("errore").path("msgerr").textValue();
				if (descrizioneErr.equalsIgnoreCase("NESSUNA IMPRESA TROVATA")) {
					istruttoriaAntimafiaChain.setParixErrorMessage(cuaa.concat(" non valido in PARIX"));
				} else {
					logger.error("istruttoria antimafia - risposta KO dal servizio parix");
					throw new Exception("[EXT] - ".concat(descrizioneErr));
				}
			}
		} catch (Exception e) {
			logger.error("istruttoria antimafia - errore durante lo step di recupero dati da parix", e);
			throw new RuntimeException("istruttoria antimafia - errore durante lo step di recupero dati da parix", e);
		}
	}

}
