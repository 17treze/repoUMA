package it.tndigitale.a4g.fascicolo.anagrafica.business.service.utente;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.Ruoli;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi.
 * In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto
 * cosi in ottica di estendere i controlli sui dati anche
 * 
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {
	
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private CaaService caaService;
	@Autowired
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private UtenteClient abilitazioniUtente;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	
	enum EnteOperationType {
		RICERCA, APERTURA_MODIFICA
	}
	
//	------------------------- METODI PRIVATI --------------------------
//	per gestire un fascicolo autonomo:
//	- si deve avere profilo azienda (checkApriFascicoloAutogestito())
//	- l'utente deve avere diritto di firma sul fascicolo
//	- la detenzione deve essere in proprio (fascicoloService.isDetenzioneInProprioModel()) e non avere un mandato
	private boolean isGestioneFascicoloAutonomo(final String cuaa) {
		return checkApriFascicoloAutogestito()
				&& isAventeDirittoFirma(cuaa)
				&& fascicoloService.isDetenzioneInProprioModel(cuaa);
	}
	
//	in base al tipo di operazione (RICERCA, APERTURA_MODIFICA) verifica che:
//	- l'utente abbia uno dei due ruoli ed e' un utente del caa (ente);
//	altrimenti
//	- se e' una persona fisica
//	altrimenti
//	- e' un caso di gestione autonoma del fascicolo
	private boolean auxCheckFascicoloOperation(
			final String cuaa, final Integer idValidazione, final EnteOperationType enteOperationType) {
		if (cuaa == null || idValidazione == null) {
			throw new NullPointerException();
		}
		Ruoli ruolo = enteOperationType.equals(EnteOperationType.RICERCA) ? Ruoli.RICERCA_FASCICOLO_ENTE : Ruoli.APERTURA_FASCICOLO_ENTE;
	
		if (utenteComponent.haUnRuolo(ruolo)) {
			return true;
		}
		if (isAccessoPersonaFisica(cuaa)) {
			// Commentare questo if o scrivere logica di chiede ad a4gutente le aziende dell'utente connesso
			// (vedi it.tndigitale.a4g.fascicolo.antimafia.utente.AbilitazioniComponent#checkEditaAntimafia)
			// dove viene usato abilitazioniUtente.getAziendeUtente();
			return true;
		}
		return isGestioneFascicoloAutonomo(cuaa);
	}
	

	// I fascicoli trasferiti *ad altro organismo pagatore* non devono essere visualizzati dai CAA
	private boolean checkFascicoloAltroOp(final String cuaa, final Integer idValidazione) {
		try {
			FascicoloModel fascicoloModel = fascicoloService.getFascicoloModelOrThrow(cuaa, idValidazione);
			if (fascicoloModel.getOrganismoPagatore().equals(OrganismoPagatoreEnum.ALTRO_OP) && fascicoloModel.getStato().equals(StatoFascicoloEnum.TRASFERITO) &&
					caaService.isAccessoCaa(cuaa, idValidazione)) {
				return false;
			}
			return true;
		} catch (Exception enfe) {
			return false;
		}
	}

//	------------------------- METODI PUBBLICI --------------------------
//	controllo permessi di lettura:
//	- se l'utente connesso puo' visionare tutti i fascicoli ed eseguire la ricerca senza filtri (RICERCA_FASCICOLO_TUTTI)
//	oppure
//	- se l'utente ha i permessi di ricerca su quel fascicolo
	public boolean checkLetturaFascicolo(final String cuaa, final Integer idValidazione) {
		if (cuaa == null || idValidazione == null) {
			throw new NullPointerException();
		}
		if (utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_TUTTI) || utenteComponent.haUnRuolo(Ruoli.VISUALIZZAZIONE_FASCICOLO_SOLA_LETTURA_TUTTI)) {
			return true;
		}
		boolean validCaaAndAltroOp = checkFascicoloAltroOp(cuaa, idValidazione);
		return validCaaAndAltroOp && auxCheckFascicoloOperation(cuaa, idValidazione, EnteOperationType.RICERCA);	
	}
	
//	verifica se l'utente connesso puo' consultare il fascicolo live
	public boolean checkLetturaFascicolo(final String cuaa) {
		boolean validCaaAndAltroOp = checkFascicoloAltroOp(cuaa, 0);
		return validCaaAndAltroOp && checkLetturaFascicolo(cuaa, 0);
	}

	public boolean checkLetturaFascicoloPerRevocaImmediata(final String idProtocollo) {
//		idProtocollo e' presente nella tabella A4GT_REVOCA_IMMEDIATA ed e' nel formato 'PAT_TEST/RFS151-14/06/2022-0002978'
		try {
//		1. reperire id revoca immediata
			Optional<RevocaImmediataModel> revocaImmediataModelOpt = revocaImmediataDao.findByIdProtocollo(idProtocollo);
			if (revocaImmediataModelOpt.isEmpty()) {
				return false;
			}
//		2. reperire detenzione associata
			RevocaImmediataModel revocaImmediataModel = revocaImmediataModelOpt.get();
			DetenzioneModel detenzioneModel = revocaImmediataModel.getMandato();
//		3. reperire cuaa e idValidazione dal fascicolo
			return checkLetturaFascicolo(detenzioneModel.getFascicolo().getCuaa(), detenzioneModel.getFascicolo().getIdValidazione());
		} catch (Exception e) {
			return false;
		}
	}
	
//	Possibilita di consultare solo i fascicoli associati all'utente
	public boolean checkRicercaFascicoloUtente() {
		return utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_UTENTE);
	}
	
//	Verifica se l'utente connesso dispone di uno dei seguenti ruoli:
//	- di consultare i fascicoli associati all'utente (RICERCA_FASCICOLO_UTENTE),
//	- di consultare i fascicoli dell'ente (RICERCA_FASCICOLO_ENTE)
//	- di consultare tutti i fascicoli ed eseguire la ricerca senza filtri (RICERCA_FASCICOLO_TUTTI)
	public boolean checkRicercaFascicolo() {
		return (utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_TUTTI)
				|| utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_ENTE)
				|| utenteComponent.haUnRuolo(Ruoli.RICERCA_FASCICOLO_UTENTE));
	}

	/**
	 * LORENZO MARTINELLI
	 * Questo codice non ha alcun senso. In particolare non va bene chiamare getEntiUtenteConnesso che ritorna la
	 * lista di id degli sportelli che saranno sempre diversi da tutti i CUAA
	 * MA non va neppure usato getAziendeUsingGET in anagraficaUtenteClient perchè è una replica di dati in a4gutente che
	 * DEVE ESSERE ELIMINATA. Owner dell'informazione se una persona fisica è il titolare dell'azienda è il fascicolo ovvero
	 * il microservizio anagrafica
	 * */
	public boolean isAccessoPersonaFisica(final String cuaa) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
//		titolare impresa individuale
		if (utenteConnesso.getCodiceFiscale().equals(cuaa)) {
			return true;
		}
//		profilo aziendale e primo firmatario
		return fascicoloService.controlloProfiloAziendaEPrimoFirmatario(cuaa);
//		return fascicoloService.controlloProfiloAziendaERappresentanteLegale(cuaa, 0);
	}
	
//	dato il cuaa ne verifica se e' una persona fisica oppure un rappresentante legale con profilo azienda
//	public boolean checkPersonaFisicaORappresentanteLegaleAzienda(final String cuaa) {
//		if (isAccessoPersonaFisica(cuaa)) {
//			return true;
//		} else {
//			return fascicoloService.controlloProfiloAziendaERappresentanteLegale(cuaa, 0);
//		}
//	}
	
//	verifica se l'utente connesso può aprire un fascicolo
	public boolean checkAperturaFascicolo(final String cuaa, Integer idValidazione) {
		return auxCheckFascicoloOperation(cuaa, idValidazione, EnteOperationType.APERTURA_MODIFICA);
	}
	
//	Il sistema deve consentire la visualizzazione dei dati del primo firmatario solo se l'utente connesso può aprire un fascicolo o al "Responsabile fascicolo PAT Appag"
	public boolean checkPermessiVisualizzazioneFirmatario(final String cuaa) {
		if (checkAperturaFascicolo(cuaa) || hasProfiloResponsabileFascicoloPat()) {
			return true;
		 }
		return false;
	}
	
//	verifica se l'utente connesso può aprire un fascicolo
	public boolean checkAperturaFascicolo(final String cuaa) {
		return checkAperturaFascicolo(cuaa, 0);
	}
	
//	verifica se l'utente connesso ha il ruolo di "revoca ordinaria mandato di un fascicolo"
	public boolean checkRevocaOrdinariaMandato(final String cuaa) {
		return utenteComponent.haUnRuolo(Ruoli.REVOCA_ORDINARIA_MANDATO_ENTE);
	}
	
//	verifica se l'utente connesso ha il ruolo di "Revoca immediata mandato di un fascicolo"
	public boolean checkRichiestaRevocaImmediataMandato(final String cuaa) {
		return utenteComponent.haUnRuolo(Ruoli.RICHIESTA_REVOCA_IMMEDIATA_AZIENDA_MANDATO)
				&& checkAperturaFascicolo(cuaa);
	}
	
	public boolean checkListaRichiesteRevovaImmediataMandato(final Boolean isValutata) {
		return utenteComponent.haUnRuolo(Ruoli.CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_ENTE) || utenteComponent.haUnRuolo(Ruoli.CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_VALUTATE_TUTTI);
	}
	
	public boolean checkValutaRevocaImmediataMandato(final String cuaa) {
		if (utenteComponent.haUnRuolo(Ruoli.ACCETTAZIONE_REVOCA_IMMEDIATA_MANDATO)) {
			return caaService.isAccessoCaa(cuaa, 0);
		}
		return false;
	}
	
//	verifica se si possiede il ruolo della "Modifica mandato dello sportello"
	public boolean checkModificaSportelloMandato(final String cuaa) {
		if (utenteComponent.haUnRuolo(Ruoli.MODIFICA_SPORTELLO_MANDATO)) {
			return caaService.isAccessoCaa(cuaa, 0);
		}
		return false;
	}
	
//	Il sistema deve consentire l’aggiornamento(inserimento/rimozione)della modalità di pagamento (AGGIORNAMENTO_MODALITA_PAGAMENTO_FASCICOLO_ENTE) esclusivamente dall’operatore CAA associato allo sportello CAA che detiene il mandato.
	public boolean checkPermessiAggiornamentoModoPagamento(final String cuaa) {
		 if (utenteComponent.haUnRuolo(Ruoli.AGGIORNAMENTO_MODALITA_PAGAMENTO_FASCICOLO_ENTE) && caaService.isAccessoCaa(cuaa, 0)) {
			 return true;
		 }
		 return isGestioneFascicoloAutonomo(cuaa);
	}
	
//	Il sistema deve consentire la visualizzazione dei dati di pagamento solo all'ente o al "Responsabile fascicolo PAT Appag"
	public boolean checkPermessiVisualizzazioneModoPagamento(final String cuaa) {
		if (utenteComponent.haUnRuolo(Ruoli.VISUALIZZAZIONE_MODALITA_PAGAMENTO_FASCICOLO_ENTE) && (caaService.isAccessoCaa(cuaa, 0) || hasProfiloResponsabileFascicoloPat())) {
			return true;
		 }
		return checkPermessiAggiornamentoModoPagamento(cuaa);
	}
	
//	verifica se:
//	- cuaa di persona giuridica: se l'utente connesso ne e' firmatario
//	- cuaa di persona fisica: se l'utente connesso ha codice fiscale uguale al cuaa
	public boolean isAventeDirittoFirma(final String cuaa) {
		if (fascicoloService.isUtenteFirmatarioFascicolo(cuaa, 0)) {
			return true;
		} else {
			RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
			return utenteConnesso.getCodiceFiscale().equals(cuaa);
		}
	}
	
//	verifica se l'utente connesso:
//	-  ha diritto di firma
//	o
//	- se puo' aprire un fascicolo 
	public boolean isAventeDirittoFirmaOrCaa(final String cuaa) {
		return isAventeDirittoFirma(cuaa) || checkAperturaFascicolo(cuaa);
	}
	
	public boolean checkLetturaDichiarazioniAssociative(final String cuaa) throws Exception {
		boolean canView =  false;

		List<String> cuaaAbilitati = null;
		if (utenteComponent.haUnRuolo(Ruoli.DICHIARAZIONI_ASSOCIATIVE_VISUALIZZA_UTENTE)) {
			cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
			if (cuaaAbilitati != null) {
				canView = cuaaAbilitati.contains(cuaa);
			}
		}
		return canView;
	}

//	controllo di lettura di dichiarazione associativa e possibilità di modificarle (DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE)
	public boolean checkEditDichiarazioniAssociative(final String cuaa) throws Exception {
		return checkLetturaDichiarazioniAssociative(cuaa) && utenteComponent.haUnRuolo(Ruoli.DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE);
	}
	
//	controllo di permessi per l'apertura di un fascicolo autogestito. Lo può fare un utente con profilo azienda
	public boolean checkApriFascicoloAutogestito() {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
		return fascicoloService.containsProfiloAzienda(utenteConnesso.getProfili()) || fascicoloService.containsProfiloAutomazioneApprovazione(utenteConnesso.getProfili());
	}
	
	
	public boolean isCaa() {
		try {
			return !CollectionUtils.isEmpty(abilitazioniUtente.getEntiUtente());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasProfiloResponsabileFascicoloPat() {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
		return fascicoloService.containsProfiloResponsabileFascicoloPat(utenteConnesso.getProfili());
	}
}
