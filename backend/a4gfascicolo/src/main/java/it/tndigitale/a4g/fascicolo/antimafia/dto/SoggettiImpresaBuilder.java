package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.tndigitale.a4g.fascicolo.antimafia.config.DateFormatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SoggettiImpresaBuilder {

	private static final Logger log = LoggerFactory.getLogger(SoggettiImpresaBuilder.class);
	
	@Autowired
	private DateFormatConfig dateFormatConfig;
	@Value("${a4gfascicolo.integrazioni.anagrafetributaria.uri}")
	private String urlIntegrazioniAnagrafeTributaria;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	public List<SoggettoImpresa> build(JsonNode personeSede) throws Exception {
		return build(personeSede, false);
	}


	public List<SoggettoImpresa> build(JsonNode personeSede, boolean skipException) throws Exception {
		ArrayList<SoggettoImpresa> soggettiImpresa = new ArrayList<>();
		Long totalePersoneSede = personeSede.path("totale").asLong();
		
		if (totalePersoneSede.compareTo(0L) > 0) {
			
			JsonNode personeSedeArray = personeSede.path("persona");
			
			// EFFETTUA LA CHIAMATA AD ANAGRAFE TRIBUTARIA
			for (JsonNode personaSede : personeSedeArray) {
				JsonNode personaFisicaSede = personaSede.path("personafisica");
				
				if (!personaFisicaSede.isNull()) {
					// Da PARIX mancano campi come indirizzo e datiNascita, quindi invoco anagrafe tributaria...
					String codiceFiscalePersonaSede = personaFisicaSede.path("codicefiscale").textValue();
					String resource = urlIntegrazioniAnagrafeTributaria.concat(codiceFiscalePersonaSede);
					String response;
					JsonNode result = null;
					try {
						response = restTemplate.getForObject(new URI(resource), String.class);

						result = objectMapper.readTree(response);
						if (result == null) {
							throw new Exception("Errore Anagrafica Tributaria: risposta vuota per CF: ".concat(codiceFiscalePersonaSede));
						}
					} catch (Exception e) {
						log.error("Si è verificato un errore durante il recupero dei soggetti con carica per il CF: ".concat(codiceFiscalePersonaSede), e);
						throw new Exception("Si è verificato un errore durante il recupero dei soggetti con carica");
					}
					JsonNode esito = result.path("esito");

					String codice = esito.path("codice").textValue();
					// TODO - gestire codici servizio esterno, costanti? enum?
					if (!codice.equals("012")) {
						StringBuilder descrizione = new StringBuilder(esito.path("descrizione").textValue());
						descrizione.append(": ").append(codiceFiscalePersonaSede);
						if (skipException) {
							continue;//AM-01-05-01 nel caso di errori nel reperire le informazioni delle aziende collegate si può proseguire con il soggetto successiva senza lanciare eccezione
						} else {
							throw new Exception("il soggetto ".concat(codiceFiscalePersonaSede).concat(" non è censito in Anagrafe Tributaria"));
						}
					}
					// mapping dei dati di Anagrafe Tributaria su Richiedente
					JsonNode risposta = result.path("risposta");
					JsonNode personaAnagrafeTributaria = risposta.path("persona");
					JsonNode personaFisicaElement = personaAnagrafeTributaria.path("personaFisica");
					JsonNode personaFisica = personaFisicaElement;

					SoggettoImpresa soggettoImpresa = new SoggettoImpresa();
					soggettoImpresa.setCodiceFiscale(personaFisica.path("codiceFiscale").textValue());
					soggettoImpresa.setNome(personaFisica.path("nome").textValue());
					soggettoImpresa.setCognome(personaFisica.path("cognome").textValue());
					soggettoImpresa.setComuneNascita(personaFisica.path("datiNascita").path("comune").textValue());
					soggettoImpresa.setProvinciaNascita(personaFisica.path("datiNascita").path("provincia").textValue());
					soggettoImpresa.setDataNascita(dateFormatConfig.convertiDataAnagrafeTributaria(personaFisica.path("datiNascita").path("data").asLong()));
					soggettoImpresa.setSesso(personaFisica.path("sesso").path("valore").textValue());
					soggettoImpresa.setCapResidenza(personaFisica.path("domicilioFiscale").path("ubicazione").path("cap").textValue());
					soggettoImpresa.setComuneResidenza(personaFisica.path("domicilioFiscale").path("ubicazione").path("comune").textValue());
					soggettoImpresa.setProvinciaResidenza(personaFisica.path("domicilioFiscale").path("ubicazione").path("provincia").textValue());
					soggettoImpresa.setIndirizzoResidenza(personaFisica.path("domicilioFiscale").path("ubicazione").path("toponimo").textValue());

					JsonNode carichePersonaFisicaElement = personaSede.path("cariche").path("carica");
					if (!carichePersonaFisicaElement.isNull()) {
						soggettoImpresa.setCarica(new ArrayList<>());
						for (JsonNode caricaIn : carichePersonaFisicaElement) {
							Carica carica = new Carica();
							carica.setCodice(caricaIn.path("ccarica").textValue());
							carica.setTipologia(caricaIn.path("descrizione").textValue());
							 
							// se data fine esiste
							String dataFineCaricaString = dateFormatConfig.convertiDataParix(caricaIn.path("dtfine").textValue());
							carica.setDataFine(dataFineCaricaString);
							carica.setDataInizio(dateFormatConfig.convertiDataParix(caricaIn.path("dtinizio").textValue()));
							
							Date dataFineCarica;
							if (!dataFineCaricaString.isEmpty()) {
								DateFormat formatterAMF = new SimpleDateFormat(DateFormatConfig.DATE_FORMAT_AMF);
								dataFineCarica = formatterAMF.parse(dataFineCaricaString);
								Date now = new Date();
								// se la carica è scaduta
								if (dataFineCarica.compareTo(now) < 0) {
									break;
								}
							}
							soggettoImpresa.getCarica().add(carica);
						}
					}
					if (!soggettoImpresa.carica.isEmpty()) {
						soggettiImpresa.add(soggettoImpresa);
					}
				}
			}
		}
		return soggettiImpresa;
	}

}
