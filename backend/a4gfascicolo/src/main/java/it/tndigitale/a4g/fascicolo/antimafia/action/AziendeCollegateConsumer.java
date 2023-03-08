package it.tndigitale.a4g.fascicolo.antimafia.action;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tndigitale.a4g.fascicolo.antimafia.dto.AziendaCollegata;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DettaglioImpresaBuilder;

@Component
public class AziendeCollegateConsumer  implements Consumer<AziendaCollegata> {
	
	private static final Logger logger = LoggerFactory.getLogger(AziendeCollegateConsumer.class);
	private static final String ESITO = "esito";
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DettaglioImpresaBuilder dettaglioImpresaBuilder;
	@Value("${a4gfascicolo.integrazioni.anagraficaimpresa.uri}")
	private String urlIntegrazioniAnagraficaImpresa;
	
	@Override
	public void accept(AziendaCollegata azienda) {
		String cuaa=azienda.getCodiceFiscale();
		try {
			String responseIntegrazioniAnagraficaImpresa = restTemplate.getForObject(new URI(urlIntegrazioniAnagraficaImpresa.concat(cuaa)), String.class);

			// mapping dei dati di Anagrafe Tributaria su Richiedente
			JsonNode jsonIntegrazioniAnagraficaImpresa = objectMapper.readTree(responseIntegrazioniAnagraficaImpresa);
			JsonNode header = jsonIntegrazioniAnagraficaImpresa.path("header");
			String esitoIntegrazioniAnagraficaImpresa = header.path(ESITO).textValue();
			JsonNode datiIntegrazioniAnagraficaImpresa = jsonIntegrazioniAnagraficaImpresa.path("dati");
			if (!esitoIntegrazioniAnagraficaImpresa.equals("OK")) {
				JsonNode errore = datiIntegrazioniAnagraficaImpresa.path("errore");
				String messaggio = errore.path("msgerr").textValue();
				logger.error("[EXT] - ".concat(messaggio));
				throw new Exception("L'impresa ".concat(cuaa).concat(" non risulta attiva presso la Camera di Commercio"));
			}

			JsonNode estremiImpresa = datiIntegrazioniAnagraficaImpresa.path("listaimprese").path("estremiimpresa");
			JsonNode datiIscrizioneREA = StreamSupport
					.stream(estremiImpresa.spliterator(), false)
					.flatMap(e -> StreamSupport.stream(e.path("datiiscrizionerea").spliterator(), false))
					.filter(i -> "TN".equals(i.path("cciaa").asText()))
					.max(new Comparator<JsonNode>() {
						@Override
						public int compare(JsonNode h1, JsonNode h2) {
							return Long.compare(h1.path("data").asLong(), h2.path("data").asLong());
						}
					}).orElseThrow(() -> new Exception("Impresa non trovata"));

			String numeroREASede = datiIscrizioneREA.path("nrea").textValue();
			String provinciaSede = datiIscrizioneREA.path("cciaa").textValue();
			ObjectNode inputRicerca = objectMapper.createObjectNode();
			inputRicerca.put("provinciaSede", provinciaSede);
			inputRicerca.put("numeroREASede", numeroREASede);
			String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(inputRicerca), StandardCharsets.UTF_8.name()));
			String responseIntegrazioniAnagraficaImpresaDettaglioCompleto = restTemplate.getForObject(new URI(urlIntegrazioniAnagraficaImpresa.concat("dettagliocompleto").concat("/").concat(params)),
					String.class);
			JsonNode jsonIntegrazioniAnagraficaImpresaDettaglioCompleto = objectMapper.readTree(responseIntegrazioniAnagraficaImpresaDettaglioCompleto);
			JsonNode headerIntegrazioniAnagraficaImpresaDettaglioCompleto = jsonIntegrazioniAnagraficaImpresaDettaglioCompleto.path("header");
			String esitoIntegrazioniAnagraficaImpresaDettaglioCompleto = headerIntegrazioniAnagraficaImpresaDettaglioCompleto.path(ESITO).textValue();
			JsonNode datiIntegrazioniAnagraficaImpresaDettaglioCompleto = jsonIntegrazioniAnagraficaImpresaDettaglioCompleto.path("dati");
			if (!esitoIntegrazioniAnagraficaImpresaDettaglioCompleto.equals("OK")) {
				JsonNode errore = datiIntegrazioniAnagraficaImpresaDettaglioCompleto.path("errore");
				String messaggio = errore.path("msgerr").textValue();
				logger.error("[EXT] - ".concat(messaggio));
				throw new Exception("L'impresa ".concat(cuaa).concat(" non risulta attiva presso la Camera di Commercio"));
			}		
			JsonNode datiImpresa = datiIntegrazioniAnagraficaImpresaDettaglioCompleto.path("datiimpresa");
			azienda.setDettaglioImpresa(dettaglioImpresaBuilder.build(datiImpresa,true));
		} catch (Exception e) {
			throw new RuntimeException("Errore durante lo step di recupero dati dell'aziende collegata ".concat(cuaa).concat(". ").concat(e.getMessage()), e);
		}
		
	}

}
