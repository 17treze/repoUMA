package it.tndigitale.a4gistruttoria.component.acs;

import it.tndigitale.a4gistruttoria.dto.DatiCatastaliRegione;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ControlloRegioni {

	private static final Logger logger = LoggerFactory.getLogger(ControlloRegioni.class);

	@Value("${superficie.controlloregistat.intervento122}")
	private String[] regioniIntervento122;

	@Value("${superficie.controlloregistat.intervento123}")
	private String[] regioniIntervento123;

	@Value("${superficie.controlloregistat.intervento124}")
	private String[] regioniIntervento124;

	@Value("${superficie.controlloregistat.intervento125}")
	private String[] regioniIntervento125;

	@Value("${a4gistruttoria.ags.uri}")
	private String agsUri;

	@Autowired
	private RestTemplate restTemplate;

	public boolean checkControlloRegioni(String codNazionale, CodiceInterventoAgs codiceInterventoAgs) {

		if (codNazionale == null)
			throw new IllegalArgumentException("codNazionale cannot be null.");

		if (codiceInterventoAgs == null)
			throw new IllegalArgumentException("identificativoIntervento cannot be null.");

		logger.debug("checkControlloRegioni per codNazionale {} intervento {}", codNazionale,
				codiceInterventoAgs.name());

		DatiCatastaliRegione regione = null;
		try {
			regione = restTemplate.getForObject(
					agsUri.concat("daticatastali/sezioni/{codNazionale}/regione"),
					DatiCatastaliRegione.class, 
					codNazionale);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
				logger.error("Errore nella chiamata di recupero dei dati catastali per il codNazionale ".concat(codNazionale), e);
				throw e;
			} else {
				logger.warn("Nessun dato catastale recuperato per il codNazionale ".concat(codNazionale), e);
			}
		}
		switch (codiceInterventoAgs) {
		case SOIA:
			return containsCodRegione(regioniIntervento122, regione);
		case GDURO:
			return containsCodRegione(regioniIntervento124, regione);
		case CPROT:
			return containsCodRegione(regioniIntervento123, regione);
		case LEGUMIN:
			return containsCodRegione(regioniIntervento125, regione);
		default:
			return true; // Default: Controllo regione positivo, ovvero "tutte le regioni" sono ammesse
		}
	}

	private static boolean containsCodRegione(String[] regioni, DatiCatastaliRegione regione) {
		return regione != null && Arrays.asList(regioni).contains(regione.getCodiceIstat());
	}
}
