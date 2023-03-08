package it.tndigitale.a4gistruttoria.action.cup;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;
import it.tndigitale.a4gistruttoria.dto.cup.DatiCUP;
import it.tndigitale.a4gistruttoria.service.CUPService;

@Component
public class CupConverterFunction  implements Function<DatiCUP, CUPGENERAZIONE> {

	private static final Logger logger = LoggerFactory.getLogger(CUPService.class);	

	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	
	@Autowired
	private PopolaDatiGeneraliProgettoConsumer datiGeneraliCons;
	
	@Autowired
	private PopolaLocalizzazioneConsumer localizzazioneCons;
	
	@Autowired
	private PopolaConcessioneIncentiviUnitaProduttiveConsumer ciupCons;

	@Autowired
	private PopolaAttivitaEconomicaConsumer attivitaEconomicaCons;

	@Autowired
	private PopolaFinanziamentoConsumer finanziamentoCons;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@Override
	public CUPGENERAZIONE apply(DatiCUP datiCupInput) {
		logger.debug("datiCupInput {}", datiCupInput);
		String cuaa = datiCupInput.getCuaa();
		if (cuaa.length() != 16) {// 16 codice fiscale, 11 partita iva
			while (cuaa.length() < 11) {
				cuaa = "0" + cuaa;
			}
		}
		logger.debug("Converto cuaa {}", cuaa);
		AnagraficaAzienda anagraficaAzienda = caricaDatiAnagrafica(cuaa);
		if (anagraficaAzienda == null) {
			logger.error("Nessun fascicolo trovato in ags per il cuaa {}", cuaa);
			throw new IllegalArgumentException("Nessun fascicolo trovato in ags per il cuaa ".concat(cuaa));
		}
		logger.debug("anagraficaAzienda {}", anagraficaAzienda);
		CupHandler handler = new CupHandler(datiCupInput, anagraficaAzienda);
		ObjectFactory of = new ObjectFactory();
		CUPGENERAZIONE generazione = of.createCUPGENERAZIONE();
		datiGeneraliCons.andThen(localizzazioneCons).andThen(ciupCons).andThen(attivitaEconomicaCons).andThen(finanziamentoCons).accept(handler, generazione);
		return generazione;
		
	}
	
	protected AnagraficaAzienda caricaDatiAnagrafica(String cuaa) {
		try {
			String resource = configurazione.getUriAgs().concat("fascicoli/").concat(cuaa).concat("/anagrafica");
			return restTemplate.getForObject(new URI(resource), AnagraficaAzienda.class);
		} catch (RestClientException | URISyntaxException e) {
			logger.error("Errore recuperando il fascicolo aziendale di " + cuaa, e);
			throw new RuntimeException("Errore recuperando il fascicolo aziendale di " + cuaa, e);
		}
	}

}
