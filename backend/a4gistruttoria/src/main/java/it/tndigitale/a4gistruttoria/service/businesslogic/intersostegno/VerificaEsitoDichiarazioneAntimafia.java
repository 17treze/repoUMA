package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@Component
public class VerificaEsitoDichiarazioneAntimafia implements Function<IstruttoriaModel, Boolean> {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private Clock clock;

	private static final Logger logger = LoggerFactory.getLogger(VerificaEsitoDichiarazioneAntimafia.class);

	private Function<IstruttoriaModel, Optional<DichiarazioneAntimafia>> dichiarazioniAntimafia = istruttoria -> Optional.ofNullable(istruttoria.getDomandaUnicaModel().getCuaaIntestatario())
			.map(cuaa -> String.format("%s%s%s", "{ \"azienda\":{\"cuaa\":\"", cuaa, "\"}, \"stato\" : {\"identificativo\": \"CONTROLLATA\"}}"))
			.map(request -> {
				try {
					return configurazione.getUriFascicolo().concat("antimafia?params=").concat(URLEncoder.encode(request, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage());
					return null;
				}
			})
			.map(resource -> {
				try {
					logger.debug("dichiarazioniAntimafia: chiamata {}", resource);
					return restTemplate.getForObject(new URI(resource), String.class);
				} catch (RestClientException | URISyntaxException e) {
					logger.error(e.getMessage());
					return null;
				}
			}).map(response -> {
				try {
					logger.debug("QuadroDichiarazione del cuaa {}: {}", istruttoria.getDomandaUnicaModel().getCuaaIntestatario(), response);
					return response != null ? objectMapper.readValue(response, new TypeReference<List<DichiarazioneAntimafia>>() {
					}) : new ArrayList<DichiarazioneAntimafia>();

				} catch (Exception e) {
					logger.error("Errore leggendo le dichiarazioni antimafia per l'azienda {}: {}", istruttoria.getDomandaUnicaModel().getCuaaIntestatario(), e.getMessage());
					return null;
				}
			})
			.filter(dichiarazioni -> !dichiarazioni.isEmpty())
			.map(dich -> dich.get(0));

	private Function<IstruttoriaModel, Boolean> domandeCollegate = istruttoria ->

	Optional.of(istruttoria.getDomandaUnicaModel())
	.map(domanda -> String.format("%s%s%s%s%s", "{ \"cuaa\":\"", domanda.getCuaaIntestatario(), "\", \"tipoDomanda\": \"DOMANDA_UNICA\", \"idDomanda\":\"", domanda.getNumeroDomanda(), "\" }"))
	.map(request -> {
		try {
			return configurazione.getUriIstruttoria().concat("antimafia/domandecollegate?params=").concat(URLEncoder.encode(request, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Errore costruendo richiesta per domande collegate {}: {}", request, e.getMessage());
			return null;
		}
	})
	.map(resource -> {
		try {
			return restTemplate.getForObject(new URI(resource), String.class);
		} catch (RestClientException | URISyntaxException e) {
			logger.error("Errore leggendo le domande collegate {}: {}", resource, e.getMessage());
			return null;
		}
	})
	.map(response -> {
		try {
			logger.debug("Verifico domande collegate per numero domanda {} ", istruttoria.getDomandaUnicaModel().getNumeroDomanda());

			return response != null ? objectMapper.readValue(response, new TypeReference<List<DomandaCollegata>>() {
			}) : new ArrayList<DomandaCollegata>();
		} catch (Exception e) {
			logger.error("Errore leggendo le domande collegate per l'azienda {}: {}", istruttoria.getDomandaUnicaModel().getCuaaIntestatario(), e.getMessage());
			return null;
		}
	})
	.map(domande -> domande.stream())
	.orElse(new ArrayList<DomandaCollegata>().stream())
	.anyMatch(d -> (d.getDtFineSilenzioAssenso() != null && d.getDtInizioSilenzioAssenso() != null && d.getDtFineSilenzioAssenso().after(clock.nowDate())
	&& d.getDtInizioSilenzioAssenso().before(clock.nowDate()))
			|| (d.getDtFineEsitoNegativo() != null && d.getDtInizioEsitoNegativo() != null && d.getDtFineEsitoNegativo().after(clock.nowDate()) && d.getDtInizioEsitoNegativo().before(clock.nowDate())));

	/**
	 * Contatta il servizio di ricerca QuadroDichiarazione
	 * Antimafia passando il cuaa per controllo della dichiarazione. 
	 * Se esiste la dichiarazione viene contattato il servizio di ricerca delle domande
	 * collegate per tipo DOMANDA_UNICA filtrandola per data silenzio assenso e data esito negativo
	 */
	@Override
	public Boolean apply(IstruttoriaModel istruttoria) {
		String cuaa = istruttoria.getDomandaUnicaModel().getCuaaIntestatario();
		boolean stoPagandoAnticipi = TipoIstruttoria.ANTICIPO.equals(istruttoria.getTipologia());
		logger.debug("Verifico esito antimafia azienda {} con anticipo {}", cuaa, stoPagandoAnticipi);
		// evolutiva #231 - BRIDUSDS050 deve essere sempre VERA ovvero per il CUAA c'è sempre un esito NEGATIVO anche se non è vero.
		if (stoPagandoAnticipi) {
			// return dichiarazioniAntimafia.apply(istruttoria).isPresent();
			// INC000001601400
			return true;
		} else {
			  Boolean result = dichiarazioniAntimafia.apply(istruttoria).isPresent() || domandeCollegate.apply(istruttoria);
			  if(!result) {
				  logger.debug("Nessuna dichiarazione controllata trovata per cuaa {}", cuaa);
			  }
			  
			  return result;	
		}
	}
}
