package it.tndigitale.a4gistruttoria.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.proxy.client.api.SincronizzazioneControllerApi;
import it.tndigitale.a4g.proxy.client.model.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.client.model.SuperficiAccertateDto;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;

@Service
public class SincronizzazioneAgeaService {

	private static final Logger logger = LoggerFactory.getLogger(SincronizzazioneAgeaService.class);
	
	@Autowired
	private ConsumeExternalRestApi consumeExternalRestApi;
	
	@Autowired
	RestTemplate restTemplate;
	
	public void pulisciDatiSuperficiAccertate(Integer annoCampagna) throws Exception {
		logger.debug("Avvio cancellazione dati esistenti per superfici accertate per l'anno campagna {}", annoCampagna);
		getApi().pulisciSuperficiAccertateUsingPOST(Long.valueOf(annoCampagna));
	}
	
	public void creaDatiSuperficiAccertate(SuperficiAccertateDto superficiAccertate) {
		logger.debug("Avvio scrittura dati per superfici accertate per la domanda numero {}", superficiAccertate.getIdentificativoDomanda());
		getApi().creaSuperficiAccertateUsingPOST(superficiAccertate);
	}
	
	public void pulisciDatiPagamenti(Integer annoCampagna) throws Exception {
		logger.debug("Avvio cancellazione dati esistenti per i pagamenti per l'anno campagna {}", annoCampagna);
		getApi().pulisciDatiPagamentiUsingPOST(Long.valueOf(annoCampagna));
	}
	
	public void creaDatiPagamenti(DatiPagamentiDto datiPagamenti) {
		logger.debug("Avvio scrittura dati per pagamenti per la domanda numero {}", datiPagamenti.getNumeroDomanda());
		getApi().creaDatiPagamentiUsingPOST(datiPagamenti);
		logger.debug("Terminato scrittura dati per pagamenti per la domanda numero {}", datiPagamenti.getNumeroDomanda());
	}
	
	private SincronizzazioneControllerApi getApi() {
		return consumeExternalRestApi.restClientProxy(SincronizzazioneControllerApi.class);
	}
}
