package it.tndigitale.a4gutente.api;

import java.util.List;

import it.tndigitale.a4gutente.dto.Distributore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.component.AccessoComponent;
import it.tndigitale.a4gutente.dto.CounterStato;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneCreataResponse;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.dto.EnteCAA;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.service.IDomandaRegistrazioneService;

/**
 * @author it417
 *
 */
@RestController
@RequestMapping(ApiUrls.DOMANDE_V1)
@Api(value = "Domanda di registrazione utente", description = "Rappresenta le operazioni relative alle domande di registrazione degli utenti", position = 0)
public class DomandaRegistrazioneUtenteController {
	
	@Autowired
	private IDomandaRegistrazioneService domandaService;
	
	@Autowired
	private AccessoComponent accessoComponent;
	
	@ApiOperation("Registro la domanda di registrazione nuovo utente")
	@PostMapping
	public Long registraDomanda(@RequestBody() DatiDomandaRegistrazioneUtente domandaRegistrazioneUtente) throws Exception {
		return domandaService.registraDomanda(domandaRegistrazioneUtente);
	}
	
	@ApiOperation("Registro la domanda di registrazione nuovo utente")
	@PutMapping
	public Long aggiornaDomanda(@RequestBody() DatiDomandaRegistrazioneUtente domandaRegistrazioneUtente) throws Exception {
		return domandaService.aggiornaDomanda(domandaRegistrazioneUtente);
	}
	
	// non restituisce allegati
	@ApiOperation("Restituisce la domanda per id")
	@GetMapping("/{id}")
	public DatiDomandaRegistrazioneUtente getDomanda(@PathVariable Long id) throws Exception {
		return domandaService.getDomanda(id);
	}
	
	@ApiOperation("Restituisce la lista di caa/sedi")
	@GetMapping(ApiUrls.ELENCO_CAA)
	public List<EnteCAA> ricercaUfficiCaa()
			//@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = false) String params)
			throws Exception {
		return domandaService.getCAA(null);
	}

	@ApiOperation("Restituisce la lista di distributori")
	@GetMapping(ApiUrls.ELENCO_DISTRIBUTORI)
	public List<Distributore> ricercaDistributori() throws Exception {
		return domandaService.getDistributori(null);
	}
	
	@ApiOperation("Restituisce la lista di dipartimenti")
	@GetMapping(ApiUrls.ELENCO_DIPATIMENTI)
	public List<String> ricercaDipartimento()
			throws Exception {
		return domandaService.getDipartimenti();
	}

	@ApiOperation("Effettua l'export della domanda in formato PDF/A")
	@GetMapping("/{id}/export")
	public byte[] stampaDomanda(@PathVariable Long id) throws Exception {
		return domandaService.stampa(id);
	}

	@ApiOperation("Firma, attraverso i dati di accesso dell'utente, il modulo di domanda di registrazione")
	@PostMapping("/{id}/firma")
	public Long firmaDomanda(@PathVariable Long id, @RequestHeader HttpHeaders headers) throws Exception {
		DatiAutenticazione datiAutenticazione = accessoComponent.caricaDatiAutenticazione(headers);
		return domandaService.firma(id, datiAutenticazione);
	}

	@PutMapping(value = "/{id}/protocolla")
	@ApiOperation("Protocolla la domanda di registrazione utente e la relativa firma")
	public DatiDomandaRegistrazioneUtente protocolla(@PathVariable Long id) throws Exception {
		domandaService.protocolla(id);
		return domandaService.getDomanda(id);
	}

	@GetMapping
	@ApiOperation("Ricerca le domande di accesso al sistema secondo dei criteri di ricerca (stato)")
	@PreAuthorize("@utenteComponent.checkUsernameFilter(#filter.codiceFiscale) or @abilitazioni.checkVisualizzaDomande()")
	public RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> ricercaDomande(DomandaRegistrazioneUtenteFilter filter,
																				   Paginazione paginazione,
																				   Ordinamento ordinamento) throws Exception {
		return domandaService.ricercaDomande(filter,
											 Paginazione.getOrDefault(paginazione),
											 Ordinamento.getOrDefault(ordinamento));
	}


	@GetMapping(path = "/counters")
	@ApiOperation("Conta domande di accesso al sistema secondo dei criteri di ricerca (stato)")
	@PreAuthorize("@abilitazioni.checkVisualizzaDomande()")
	public List<CounterStato> contaDomande(DomandaRegistrazioneUtenteFilter filter) throws Exception {
		return domandaService.contaDomande(filter);
	}
	
	@PutMapping(value = "/{id}/presaincarico")
	@ApiOperation("Aggiorna lo stato della domanda da protocollata a in lavorazione")
	@PreAuthorize("@abilitazioni.checkEditaDomande()")
	public DatiDomandaRegistrazioneUtente prendiInCarico(@PathVariable Long id) throws Exception {
		domandaService.presaInCarico(id);
		return domandaService.getDomanda(id);
	}

	@PutMapping("/{idDomanda}/approva")
	@ApiOperation("Approva una richiesta di accesso utente")
	@PreAuthorize("@abilitazioni.checkEditaDomande()")
	public Long approva(@ApiParam(value="Identificativo della domanda", required = true)
						@PathVariable Long idDomanda,
						@RequestBody RichiestaDomandaApprovazione richiesta) throws Exception {
		return domandaService.approva(richiesta.setIdDomanda(idDomanda));
	}

	@PutMapping("/{idDomanda}/rifiuta")
	@ApiOperation("Rifiuta una richiesta di accesso utente")
	@PreAuthorize("@abilitazioni.checkEditaDomande()")
	public Long rifiuta(@ApiParam(value="Identificativo della domanda", required = true)
						@PathVariable Long idDomanda,
						@RequestBody RichiestaDomandaRifiuta richiesta) throws Exception {
		return domandaService.rifiuta(richiesta.setIdDomanda(idDomanda));
	}
	
	@ApiOperation("Funzione che effettua la registrazione utenti tramite salvataggio dati utente e firma del documento in maniera atomica")
	@PostMapping(value = "/registrazione-firma")
	public DomandaRegistrazioneCreataResponse registrazioneFirmaDocumento(
			@RequestBody() DatiDomandaRegistrazioneUtente domandaRegistrazioneUtente,
			@RequestHeader HttpHeaders headers) throws Exception  {
		DatiAutenticazione datiAutenticazione = accessoComponent.caricaDatiAutenticazione(headers);
		return domandaService.registrazioneFirmaDocumento(domandaRegistrazioneUtente, datiAutenticazione);
	}
	
	@GetMapping(path = "/ultima-domanda-utente-corrente")
	@ApiOperation("Ricerca le domande di accesso al sistema secondo dei criteri di ricerca (stato e tipo)")
//	@PreAuthorize("@utenteComponent.checkUsernameFilter(#utenteComponent.utenza()) or @abilitazioni.checkVisualizzaDomande()")
//	@PreAuthorize("@abilitazioni.checkVisualizzaDomande()")
	public DatiDomandaRegistrazioneUtente ultimaDomandaUtenteCorrentePerDataProtocollazione(@RequestParam(value = "statoDomanda", required = false) StatoDomandaRegistrazioneUtente statoDomanda, @RequestParam(value = "tipoDomanda", required = false)TipoDomandaRegistrazione tipoDomanda) throws Exception {
		return domandaService.ultimaDomandaUtenteCorrentePerDataProtocollazione(statoDomanda, tipoDomanda);
	}
}
