/**
 * 
 */
package it.tndigitale.a4gutente.api;

import static it.tndigitale.a4gutente.dto.UtentiFilter.isValid;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.component.abilitazioni.AbilitazioniComponent;
import it.tndigitale.a4gutente.dto.CsvFile;
import it.tndigitale.a4gutente.dto.Distributore;
import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.dto.ProfiloUtente;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.dto.UtenteProfiloA4gDto;
import it.tndigitale.a4gutente.dto.UtentiFilter;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.Ruolo;
import it.tndigitale.a4gutente.service.IDomandaRegistrazioneService;
import it.tndigitale.a4gutente.service.IUtenteService;

/**
 * @author it417
 */
@RestController
@RequestMapping(ApiUrls.UTENTI_V1)
@Api(value = "Informazioni dell'utente", description = "Rappresenta le interrogazioni che si possono fare relative ai dati dell'utente che fa la chiamata")
public class UtenteController {
	
	private static final Logger log = LoggerFactory.getLogger(UtenteController.class);
	
	private static final String TOKEN_KEY_PORTALE = "Authorization";
	
	@Autowired
	private IUtenteService utenteService;
	
	@Autowired
	private IDomandaRegistrazioneService domandaService;
	@Autowired
	private IUtenteCompletoDao a4gtUtenteDao;
	
	@Autowired
	AbilitazioniComponent abilitazioniComponent;
	
	public UtenteController setComponents(IUtenteService utenteService, IDomandaRegistrazioneService domandaService) {
		this.utenteService = utenteService;
		this.domandaService = domandaService;
		return this;
	}
	
	@ApiOperation("Restituisce i dati di dettaglio dell'utente con identificativo specificato")
	@GetMapping(path = "/{identificativo}")
	public Utente carica(@ApiParam(value = "Identificativo dell'utente", required = true)
	@PathVariable(value = "identificativo")
	String identificativo) throws Exception {
		return utenteService.carica(identificativo);
	}
	
	@ApiOperation("Restituisce i dati di dettaglio dell'utente con ID specificato")
	@GetMapping(path = "/idutente/{id}")
	public Utente caricaById(@ApiParam(value = "Identificativo dell'utente", required = true)
	@PathVariable(value = "id")
	Long id) throws Exception {
		return utenteService.carica(id);
	}
	
	@ApiOperation("Restituisce i dati di dettaglio dell'utente")
	@GetMapping(value = ApiUrls.UTENTE_CORRENTE)
	public Utente caricaMieiDati(Principal principal, @RequestHeader
	HttpHeaders headers) {
		//		String principalName = principal.getName();
		//		log.debug("Chiamato sec da {}", principalName);
		
		String headerProperty = AccessoController.getHeaderProperty(headers, TOKEN_KEY_PORTALE);
		log.info("headerProperty: {}", headerProperty);
		String userName = abilitazioniComponent.getWso2Username(headerProperty);
		log.info("Chiamato sec da {}", userName);
		
		Utente utente = new Utente();
		//		utente.setIdentificativo(principalName);
		//		String headerProperty = AccessoController.getHeaderProperty(headers, Costanti.HEADER_CF);
		//		if (headerProperty != null && !headerProperty.trim().isEmpty()) {
		//			utente.setCodiceFiscale(headerProperty);
		//		}
		//		else { // l'utente non ha passato l'autenticazione lato server. Assegno il codice fiscale che trovo sul db 
		A4gtUtente utenteBaseDati = a4gtUtenteDao.findByIdentificativo(userName);
		if (utenteBaseDati == null) {
			utente.setCodiceFiscale("");
		}
		else {
			utente.setCodiceFiscale(utenteBaseDati.getCodiceFiscale());
		}
		//		}
		
		utente.setProfili(utenteService.caricaProfiliUtente());
		return utente;
	}
	
	@ApiOperation("Restituisce la lista dei codici funzione su cui risulto abilitato")
	@GetMapping(value = ApiUrls.UTENTE_RUOLI)
	public List<String> getRuoliUtente() {
		List<Ruolo> ruoli = utenteService.caricaRuoliUtente();
		if (ruoli == null) {
			return null;
		}
		
		return ruoli.stream().map(Ruolo::getIdentificativo).collect(Collectors.toList());
	}
	
	@ApiOperation("Restituisce la lista dei profili su cui risulto abilitato")
	@GetMapping(value = ApiUrls.UTENTE_PROFILI)
	public List<Profilo> getProfili() {
		return utenteService.caricaProfiliUtente();
	}
	
	@ApiOperation("Restituisce la lista dei codici ente su cui risulto abilitato")
	@GetMapping(value = ApiUrls.UTENTE_ENTI)
	public List<String> getEnti() {
		return utenteService.caricaEntiUtente();
	}
	
	@ApiOperation("Restituisce la lista dei codici delle mie aziende")
	@GetMapping(value = ApiUrls.UTENTE_AZIENDE)
	public List<String> getAziende() {
		return utenteService.caricaAziendeUtente();
	}
	
	@ApiOperation("Restituisce la lista dei distributori associati all'utente connesso")
	@GetMapping(value = ApiUrls.UTENTE_DISTRIBUTORI)
	public List<Distributore> getDistributori() {
		return utenteService.caricaDistributoriUtente();
	}
	
	@ApiOperation("Restituisce il distributore dato l'identificativo se l'utente è abilitato a visualizzarlo")
	@GetMapping(value = ApiUrls.UTENTE_DISTRIBUTORI + "/{id}")
	public Distributore getDistributoreById(@PathVariable(required = true)
	Long id) {
		return utenteService.caricaDistributoreUtente(id);
	}
	
	@ApiOperation("Verifica se l'utente puo' presentare domanda di registrazione utente")
	@GetMapping(value = ApiUrls.UTENTE_REGISTRABILE)
	public Boolean isRegistrabile() throws Exception {
		return domandaService.isUtenteRegistrabile();
	}
	
	@ApiOperation("Effettua la ricerca degli utenti")
	@GetMapping(value = "/ricerca")
	public List<Utente> ricerca(UtentiFilter filter) throws Exception {
		isValid(filter);
		return utenteService.ricerca(filter);
	}
	
	@ApiOperation("Avvio protocollazione utente")
	@PostMapping(value = ApiUrls.UTENTE_AVVIO_PROTOCOLLAZIONE)
	public Boolean avviaProtocollazioneUtente(@ApiParam(value = "nome utente")
	@RequestPart(value = "nome")
	String nome, @ApiParam(value = "cognome utente")
	@RequestPart(value = "cognome")
	String cognome, @ApiParam(value = "codice fiscale utente")
	@RequestPart(value = "codiceFiscale")
	String codiceFiscale,
			@ApiParam(value = "Informazioni di creazione del nuovo documento: metadati, file principale, allegati, informazioni sul mittente e fascicolo")
			@RequestPart(value = "info")
			String info, @ApiParam(value = "Documento da protocollare")
			@RequestPart(value = "documento")
			MultipartFile documento, @ApiParam(value = "Allegati al documento da protocollare")
			@RequestPart(value = "allegati")
			List<MultipartFile> allegati) throws Exception {
		utenteService.protocollaPrivacy(nome, cognome, codiceFiscale, info, documento, allegati);
		return true;
	}
	
	@ApiOperation("download di un file .csv per gli utenti a4g")
	@GetMapping("/utentia4gcsv")
	public ResponseEntity<Resource> downloadCsvA4gUtenze() throws Exception {
		
		CsvFile csvFile = utenteService.getUtentiCsv();
		Resource resource = new ByteArrayResource(csvFile.getCsvByteArray());
		String contentType = "application/octet-stream";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + csvFile.getCsvFileName());
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
	
	@ApiOperation("Restituisce la lista degli utenti che hanno profili Backoffice E Viticolo")
	@GetMapping(path = ApiUrls.UTENTE_PROFILI_BO_VITICOLO)
	@PreAuthorize("@abilitazioni.checkVisualizzaProfiliUtente()")
	public List<UtenteProfiloA4gDto> getAllUtentiA4gBackofficeViticolo(@RequestParam(required = false)
	List<ProfiloUtente> profiliToSearch) throws Exception {
		/** TODO:Per ora deve restituire sempre e solo gli utenti con profili backoffice e viticolo */
		profiliToSearch = new ArrayList<>();
		profiliToSearch.addAll(Arrays.asList(ProfiloUtente.backoffice, ProfiloUtente.viticolo));
		return utenteService.getAllUtentiProfilo(profiliToSearch);
	}
	
}
