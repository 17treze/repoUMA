package it.tndigitale.a4g.zootecnia.business.service.utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.zootecnia.business.service.client.FascicoloAnagraficaClient;
/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi. In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto cosi in ottica di estendere i controlli sui dati
 * anche
 * 
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {
	@Autowired private FascicoloAnagraficaClient fascicoloAnagraficaClient;
	
	public boolean checkAperturaFascicolo(final String cuaa) {
		return fascicoloAnagraficaClient.checkAperturaFascicolo(cuaa);
	}
	
	public boolean checkLetturaFascicolo(final String cuaa) {
		return fascicoloAnagraficaClient.checkLetturaFascicolo(cuaa);
	}
}