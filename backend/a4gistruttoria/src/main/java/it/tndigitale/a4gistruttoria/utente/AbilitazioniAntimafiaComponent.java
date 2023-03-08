package it.tndigitale.a4gistruttoria.utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gistruttoria.Ruoli;


/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi relativi all'istruttoria antimafia.
 * In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto
 * cosi in ottica di estendere i controlli sui dati anche
 * 
 * @author it417
 *
 */
@Component("abilitazioniAntimafiaComponent")
public class AbilitazioniAntimafiaComponent {
	
	@Autowired
	private UtenteComponent utenteComponent;
	
	public boolean checkEditaIstruttoriaAntimafia() {
		return utenteComponent.haUnRuolo(Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA);
	}

	public boolean checkCancellaIstruttoriaAntimafia() {
		return utenteComponent.haUnRuolo(Ruoli.CANCELLA_ISTRUTTORIA_ANTIMAFIA);
	}	
	
	public boolean checkVisualizzaIstruttoriaAntimafia() {
		return utenteComponent.haUnRuolo(Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA, Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_ENTE, Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_UTENTE);
	}	
}
