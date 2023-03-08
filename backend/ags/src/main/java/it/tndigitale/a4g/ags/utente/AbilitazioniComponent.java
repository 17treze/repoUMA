package it.tndigitale.a4g.ags.utente;

import java.util.List;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi.
 * In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto
 * cosi in ottica di estendere i controlli sui dati anche
 * 
 * @author it417
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {
	
	private static Logger log = LoggerFactory.getLogger(AbilitazioniComponent.class);
	
	@Autowired
	private UtenteComponent utenteComponent;
	
	@Autowired
	private UtenteClient abilitazioniUtente;
	
	
	@Autowired
	private DomandaDao daoDomanda;

	
	public boolean checkLetturaFascicolo() {
		return utenteComponent.haRuolo(Ruoli.VISUALIZZA_FASCICOLO);
	}

	public boolean checkLetturaDomandaPSR() {
		return utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_PSR);
	}

	public boolean checkLetturaDomandaDU() {
		return utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU) 
				|| utenteComponent.haRuolo(Ruoli.VISUALIZZA_LIQUIDABILITA_DOMANDA_DU)
				|| utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU_AZD);
	}

	public boolean checkLetturaDettaglioDomandaDU(Long numeroDomanda) throws Exception {
		boolean canView = utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU);
		if (!canView) {
			if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_LIQUIDABILITA_DOMANDA_DU)) {
				InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(numeroDomanda);
				List<String> caaAbilitati = abilitazioniUtente.getEntiUtente();
				canView = infoGeneraliDomanda != null && caaAbilitati.contains(infoGeneraliDomanda.getCodEnteCompilatore());
			}
			if (!canView && utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU_AZD)) {
				InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(numeroDomanda);
				List<String> cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
				canView = cuaaAbilitati.contains(infoGeneraliDomanda.getCuaaIntestatario());
			}
		}
		if (!canView) {
			log.warn("utente {} NON abilitato alla lettura della domanda {}", utenteComponent.utenza(), numeroDomanda);
		}
		return canView;
	}

	/**
	 * Per la ricerca o si tratta di un istruttore che puo' visionare tutto
	 * oppure di una azienda e in questo caso il cuaa da filtrare 
	 * è obbligatorio
	 * 
	 * @param filtro dati di filtro tramite verificare la sicurezza
	 * 
	 * @return Se l'utente puo' eseguire la ricerca impostata o no
	 * 
	 * @throws Exception
	 */
	public boolean checkRicercaDomandaDU(DomandaUnicaFilter filtro) throws Exception {
		boolean canView = utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU);
		String cuaaDaCercare = null;
		if (!canView) {
			if (!canView && utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_DU_AZD)) {
				cuaaDaCercare = filtro.getCuaa();
				List<String> cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
				canView = (cuaaDaCercare != null) && (cuaaAbilitati.contains(cuaaDaCercare));
			}
		}
		if (!canView) {
			log.warn("utente {} NON abilitato alla ricerca delle domande con cuaa {}", utenteComponent.utenza(), cuaaDaCercare);
		}
		return canView;
	}

	public boolean checkMovimentaDomandaDU(Long numeroDomanda, String tipoMovimento) {
		log.debug("checkMovimentaDomandaDU per utente {}, su domanda {}, movimento {}", utenteComponent.utenza(), numeroDomanda, tipoMovimento);
		return utenteComponent.haRuolo(Ruoli.MOVIMENTA_DOMANDA_DU);
	}
	
	public boolean checkLetturaPianiColture() {
		return utenteComponent.haRuolo(Ruoli.VISUALIZZA_PIANICOLTURE);
	}

	public boolean getCheckLetturaAnagraficaAziendale() {
		return utenteComponent.haRuolo(Ruoli.VISUALIZZA_ANAGRAFICA_AZIENDALE);
	}
}
