package it.tndigitale.a4g.richiestamodificasuolo.business.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.CaaAgsControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;

@Component
public class AnagraficaFascicoloClient extends AbstractClient {

    @Value("${it.tndigit.a4g.richiestamodificasuolo.url.fascicolo.anagrafica.client}")
    private String urlAnagrafica;
    
    public List<SportelloFascicoloDto> ricercaFascicoli() throws Exception {
    	//return getFascicoloControllerApi().ricercaFascicoloUsingGET(null, 1000, null, 0, null, null, null).getRisultati();
    	return getFascicoloControllerApi().getFascicoliEnteUtenteConnessoUsingGET("MANDATO");
    }

    private CaaAgsControllerApi getFascicoloControllerApi() {
    	
        return restClientProxy(CaaAgsControllerApi.class, urlAnagrafica);
    }
    
}
