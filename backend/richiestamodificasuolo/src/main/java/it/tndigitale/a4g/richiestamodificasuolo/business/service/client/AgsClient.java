package it.tndigitale.a4g.richiestamodificasuolo.business.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.client.api.FascicoloRestControllerApi;
import it.tndigitale.a4g.ags.client.model.Fascicolo;

@Component
public class AgsClient extends AbstractClient {

	@Autowired
	private UtenteControllerClient utenteClient;
	@Autowired
	private ObjectMapper objectMapper;

    @Value("${it.tndigit.a4g.richiestamodificasuolo.url.ags.client}")
    private String urlAGS;
    
    public List<Fascicolo> ricercaFascicoli() throws Exception {
    	List<String> enti = utenteClient.entiAbilitati();
    	ParamsRicercaFascicolo parametri = new ParamsRicercaFascicolo();
    	parametri.setCaacodici(enti);
    	String params = objectMapper.writeValueAsString(parametri);
    	return getFascicoloControllerApi().fascicoliUsingGET(params);
    }

    // PersonaController
    private FascicoloRestControllerApi getFascicoloControllerApi() {
        return restClientProxy(FascicoloRestControllerApi.class, urlAGS);
    }

    private class ParamsRicercaFascicolo {
    	private String denominazione;
    	private String cuaa;
    	private List<String> caacodici;

    	public String getDenominazione() {
    		return denominazione;
    	}

    	public void setDenominazione(String denominazione) {
    		this.denominazione = denominazione;
    	}

    	public String getCuaa() {
    		return cuaa;
    	}

    	public void setCuaa(String cuaa) {
    		this.cuaa = cuaa;
    	}

    	public List<String> getCaacodici() {
    		return caacodici;
    	}

    	public void setCaacodici(List<String> caacodici) {
    		this.caacodici = caacodici;
    	}

    	@Override
    	public String toString() {
    		return String.format("ParamsRicercaFascicolo [denominazione=%s, cuaa=%s, caacodici=%s]", denominazione, cuaa, caacodici);
    	}
    }
}
