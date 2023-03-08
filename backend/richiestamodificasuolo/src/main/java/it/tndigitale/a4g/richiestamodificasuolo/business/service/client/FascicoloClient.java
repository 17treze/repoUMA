package it.tndigitale.a4g.richiestamodificasuolo.business.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.client.api.ConsultazioneControllerApi;
import it.tndigitale.a4g.fascicolo.client.model.Fascicolo;

@Component
public class FascicoloClient extends AbstractClient {

	@Value("${it.tndigit.a4g.richiestamodificasuolo.url.fascicolo.client}")
    private String urlFascicolo;
    
    public List<Fascicolo> ricercaFascicoli() throws Exception {
    	return getConsultazioneControllerApi().ricercaFascicoliUsingGET("{\"cuaa\":\"\"}");
    }

    private ConsultazioneControllerApi getConsultazioneControllerApi() {
         return restClientProxy(ConsultazioneControllerApi.class, urlFascicolo);
         
    }
	
}
