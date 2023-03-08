package it.tndigitale.a4g.ags.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import it.tndigitale.a4g.ags.utente.AbilitazioniComponent;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.DatiSettore;
import it.tndigitale.a4g.ags.dto.DomandaFilter;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiRicevibilita;
import it.tndigitale.a4g.ags.model.StatoDomanda;
import it.tndigitale.a4g.ags.service.DomandaService;
import it.tndigitale.a4g.ags.utils.DecodificaPACSettore;
import it.tndigitale.a4g.ags.utils.Messaggi;

/**
 * Controller per gestione operazioni su domande
 * 
 * @author S.DeLuca
 *
 */
@Api(value = "Controller per gestione operazioni su domande du")
@RestController
@RequestMapping(ApiUrls.DOMANDE_DU_V1)
public class DomandeRestController {

	private static final Logger logger = LoggerFactory.getLogger(DomandeRestController.class);

	private static final String ERRORE_RECUPERO_DOMANDA = "Errore di sistema in fase di recupero informazioni di domanda";
	private static final String CODICE_PAC_ISTRUTTORIA = "PAC1420";
	private static final String TIPO_DOMANDA = "DU";
	private static final String MODULO_RITIRO = "BPS_RITTOT_";

	@Autowired private AbilitazioniComponent abilitazioniComponent;
	@Autowired
	private DomandaService serviceDomanda;
	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private ObjectMapper objectMapper;

	@ApiOperation("Servizio che conta le domande trovate rispetto ai parametri di ricerca")
	@GetMapping("/conta")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	@Deprecated
	public Long contaDomande(
			@ApiParam(value = "Parametri di ricerca in formato json: previsti anno e stato (entrambi required)", required = true) 
			@RequestParam(value = "params") String parametri)
			throws IOException {
		// costruisco l'oggetto per la ricerca partendo dalla mappa di parametri ottenuti.
		DatiSettore datiSettore = objectMapper.readValue(parametri, DatiSettore.class);

		String moduloRitiroTotale = "BPS_RITTOT_";
		return serviceDomanda.countDomande(datiSettore, moduloRitiroTotale);
	}
	
	@ApiOperation("Restituisce i dati della domanda du")
	@GetMapping("/{numeroDomanda}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDettaglioDomandaDU(#numeroDomanda)")
	public DomandaUnica getDomanda(
			@ApiParam(value = "Identificativo della domanda in siap", required = true) @PathVariable Long numeroDomanda) {
		try {
			return serviceDomanda.getDomandaUnica(numeroDomanda);
		} catch (NoResultException e) {
			logger.error("getDomanda error: {}", e);
			throw new RuntimeException(DomandeRestController.ERRORE_RECUPERO_DOMANDA);
		}
	}

	@ApiOperation("Restituisce i dati delle domanda du in base alla lista di numero domanda fornita in input")
	@GetMapping("/elencoDomande")
	public List<DomandaUnica> getElencoDomande(
			@ApiParam(value = "Identificativo della domanda in siap", required = true) @RequestParam(value = "numeroDomandaList") List<Long> numeroDomandaList) {
		List<DomandaUnica> domandaUnicaListOutput = new ArrayList<>();
		numeroDomandaList.forEach(numeroDomanda -> {
			try {
				if (abilitazioniComponent.checkLetturaDettaglioDomandaDU(numeroDomanda)) {
					domandaUnicaListOutput.add(serviceDomanda.getDomandaUnica(numeroDomanda));
				} else {
					logger.warn("Utente [" + utenteComponent.utenza() + "] con permessi insufficienti per il reperimento dei dati della domanda=" + numeroDomanda);
				}
			} catch (Exception e) {
				logger.warn("domanda senza dettaglio [" + numeroDomanda + "]");
			}
		});

		return domandaUnicaListOutput;
	}

	@ApiOperation("Esegue il movimento per la domanda in input")
	@PutMapping("/{numeroDomanda}/eseguiMovimento")
	@ResponseBody
	@PreAuthorize("@abilitazioniComponent.checkMovimentaDomandaDU(#numeroDomanda, #tipoMovimento)")
	public ResponseEntity<String> eseguiMovimento(@ApiParam(value = "Numero domanda in siap", required = true) @PathVariable Long numeroDomanda, @RequestBody String tipoMovimento) throws Exception {
		try {
			String result = serviceDomanda.eseguiMovimentazioneDomanda(numeroDomanda, tipoMovimento);
			if (result != null && result.equalsIgnoreCase(Messaggi.OK_MOVIMENTO.getMessaggi())) {
				return new ResponseEntity<String>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(result, HttpStatus.NO_CONTENT);
			}

		} catch (NoResultException e) {
			return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation("Controlla se esiste una domanda in base ai parametri indicati")
	@GetMapping("/checkPresenzaDomandeFiltered")
	@ResponseBody
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public Boolean checkExistDomandaPerSettore(
			@ApiParam(value = "Parametri di ricerca in formato json: previsti anno e stato (entrambi required)", required = true) @RequestParam(value = "params") String parametri) throws IOException {

		JsonNode params = objectMapper.readTree(parametri);
		String codicePac = params.path("codicePac").asText();
		String tipoDomanda = params.path("tipoDomanda").asText();
		Long anno = params.path("anno").asLong();
		DecodificaPACSettore.PACKey pacKey = new DecodificaPACSettore.PACKey(codicePac, tipoDomanda);
		String cuaa = params.path("cuaa").asText();

		return serviceDomanda.checkExistDomandaPerSettore(codicePac, tipoDomanda, anno, cuaa);
	}


	@ApiOperation("Controlla se esiste una domanda in base ai parametri indicati")
	@GetMapping("/checkPresenzaDomandeFiltered/{anno}/{cuaa}")
	@ResponseBody
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public Boolean checkExistDomandaUnica(
			@ApiParam(value = "Anno scolastico di riferimento", required = true)
			@PathVariable("anno") Long anno,
			@ApiParam(value = "Cuaa (Codice fiscale o partita iva dell'azienda agricola)", required = true)
			@PathVariable("cuaa") String cuaa) {

		String codicePac = CODICE_PAC_ISTRUTTORIA;
		String tipoDomanda = TIPO_DOMANDA;

		return serviceDomanda.checkExistDomandaPerSettore(codicePac, tipoDomanda, anno, cuaa);
	}

	@ApiOperation("Restituisce la lista di domande du")
	@GetMapping
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public List<DomandaUnica> getDomande(@ApiParam(value = "Criteri di ricerca", required = true) @RequestParam("params") String params) {
		try {
			DomandaFilter domandaFilter = objectMapper.readValue(params, DomandaFilter.class);
			return serviceDomanda.getDomande(domandaFilter);
		} catch (NoResultException | IOException e) {
			logger.error("getDomanda error: {}", e);
			throw new RuntimeException(DomandeRestController.ERRORE_RECUPERO_DOMANDA);
		}
	}

	@ApiOperation("Restituisce la lista di domande uniche protocollate per la ricevibilità")
	@GetMapping("/protocollate/{campagna}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public List<Long> getDomandeProtocollate(@ApiParam(value = "Anno campagna di riferimento", required = true) @PathVariable("campagna") Integer campagna) {
		try {
			return serviceDomanda.getDomandeProtocollate(campagna);
		} catch (NoResultException e) {
			logger.error("getDomandeProtocollate error: {}", e);
			throw new RuntimeException(DomandeRestController.ERRORE_RECUPERO_DOMANDA);
		}
	}

	@ApiOperation("Effettua i controlli di ricevibilità ed esporta la domanda a A4G")
	@GetMapping("/exportDomandaProtocollata/{numeroDomanda}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDettaglioDomandaDU(#numeroDomanda) && @abilitazioniComponent.checkMovimentaDomandaDU(#numeroDomanda, 'A4GIST')&& @abilitazioniComponent.checkMovimentaDomandaDU(#numeroDomanda, 'NONRIC')")
	public DomandaUnica exportDomandaProtocollata(@ApiParam(value = "Numero della domanda", required = true) @PathVariable("numeroDomanda") Long numeroDomanda) {
		try {
			return serviceDomanda.riceviDomanda(numeroDomanda);
		} catch (NoResultException e) {
			logger.error("exportDomandaProtocollata error per la domanda {} {}", numeroDomanda, e);
			throw new RuntimeException(DomandeRestController.ERRORE_RECUPERO_DOMANDA);
		}
	}
	
	@ApiOperation("Restituisce il numero di domande da protocollare")
	@GetMapping("/count")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public Long count(DatiSettore datiSettore) {
		//setta valori di default se assenti
		if (StringUtils.isEmpty(datiSettore.getCodicePac())) {
			datiSettore.setCodicePac(CODICE_PAC_ISTRUTTORIA);
		}
		if (StringUtils.isEmpty(datiSettore.getTipoDomanda())) {
			datiSettore.setTipoDomanda(TIPO_DOMANDA);
		}		
		return serviceDomanda.countDomande(datiSettore, MODULO_RITIRO);
	}
	
	@ApiOperation("Forza il movimento per la domanda in input in PROTOCOLLATA")
	@PostMapping("/{numeroDomanda}/protocollata")
	@ResponseBody
	public Boolean spostaInProtocollato(@ApiParam(value = "Numero domanda in siap", required = true) @PathVariable Long numeroDomanda) throws Exception {
		return serviceDomanda.forzaMovimentazioneDomanda(numeroDomanda, StatoDomanda.PROTOCOLLATA.getDbAgsValue());
	}

	@ApiOperation("Forza il movimento di ricevibilita per la domanda in input e ne restituisce i dati")
	@PostMapping("/{numeroDomanda}/ricevi")
	@PreAuthorize("@abilitazioniComponent.checkMovimentaDomandaDU(#numeroDomanda, 'ricevibilita')")
	public DatiRicevibilita ricevi(@ApiParam(value = "Numero domanda in siap", required = true) @PathVariable Long numeroDomanda) throws Exception {
		return serviceDomanda.ricevi(numeroDomanda); //checkMovimentaDomandaDU
	}
}
