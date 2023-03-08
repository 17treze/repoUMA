package it.tndigitale.a4g.fascicolo.antimafia.api;

import java.util.List;

import it.tndigitale.a4g.fascicolo.antimafia.dto.KeyValueStringString;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller per gestione operazioni di consultazione su fascicolo AGS con
 * controllo permessi A4G
 *
 * @author B.Irler
 *
 */
@RestController
@RequestMapping(ApiUrls.CONSULTAZIONE_V1)
@Api(value = "Consultazioni di fascicolo", description = "Rappresenta le interrogazioni che si possono fare relative alle consultazioni di fascicolo", position = 0)
public class ConsultazioneController {

	private static Logger log = LoggerFactory.getLogger(ConsultazioneController.class);

	@Autowired
	private ConsultazioneService consultazioneService;
	
	@ApiOperation("Restituisce la lista di fascicoli su cui l'utente risulta abilitato e che corrispono ai criteri immessi nei parametri")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLI)
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public List<Fascicolo> ricercaFascicoli(
			@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params)
			throws Exception {
		log.debug("ricercaFascicoli: ricerca fascicoli con params {}", params);

		return consultazioneService.getFascicoli(params);
	}

	@ApiOperation("Restituisce la lista di fascicoli su cui l'utente risulta abilitato e che corrispono ai criteri immessi nei parametri")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLI_AZIENDA)
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public List<Fascicolo> ricercaFascicoliAziendeUtente() throws Exception {
		log.debug("ricercaFascicoli: ricerca fascicoli aziende utente ");

		return consultazioneService.getFascicoliAziendeUtente();
	}

	@ApiOperation("Restituisce la lista di fascicoli su cui l'utente risulta abilitato e che corrispono ai criteri immessi nei parametri")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLO + "/{cuaa}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaCuaa(#cuaa)")
	public Fascicolo caricaFascicoloByCuaa(@PathVariable(value = "cuaa") @ApiParam(value = "Cuaa dell'azienda", required = true) String cuaa) throws Exception {
		log.debug("ricercaFascicoli: ricerca fascicoli aziende utente ");

		return consultazioneService.getFascicolo(cuaa);
	}

	@ApiOperation("Restituisce i dati di sintesi del fascicolo")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLI + "/{id}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public Fascicolo caricaFascicolo(
			@PathVariable(value = "id") @ApiParam(value = "Identificativo del fascicolo", required = true) Long id)
			throws Exception {
		log.debug("caricaFascicolo({})", id);

		return consultazioneService.getFascicolo(id);
	}

	@ApiOperation("Verifica se l'utente loggato è Titolate/Legale Rappresentante dell'azienda inserita")
	@GetMapping( ApiUrls.AMBITO_PERSONE + "/{cfPersona}" + ApiUrls.AMBITO_AZIENDE + "/{cuaa}" + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
	public KeyValueStringString getAziendaPersonaRappresentante(
			@PathVariable(value = "cfPersona") @ApiParam(value = "Codice Fiscale della persona", required = true) String cfPersona,
			@PathVariable(value = "cuaa") @ApiParam(value = "Cuaa dell'azienda", required = true) String cuaa)
			throws Exception {
		log.debug("controllaPersonaRappresentanteAzienda({},{})", cfPersona, cuaa);

		return consultazioneService.getAziendaPersonaRappresentante(cfPersona, cuaa);
	}

	@ApiOperation("Verifica se l'azienda ha un fascicolo in ags")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLI + "/{cuaa}" + ApiUrls.FUNZIONE_FASCICOLO_VALIDO)
	public boolean controllaEsistenzaFascicoloValido(
			@PathVariable(value = "cuaa") @ApiParam(value = "Cuaa dell'azienda", required = true) String cuaa)
			throws Exception {
		log.debug("controllaEsistenzaFascicoloValido({})", cuaa);

		return consultazioneService.controllaEsistenzaFascicoloValido(cuaa);
	}
	
	@ApiOperation("Restituisce tutti i fascicoli legati agli enti di cui l'utente è appartenente")
	@GetMapping(ApiUrls.CONSULTAZIONE_FASCICOLI_ENTE)
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public List<Fascicolo> ricercaFascicoliEnte(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		log.debug("ricercaFascicoliEnte: ricerca fascicoli con params {}", params);
		return consultazioneService.getFascicoliEnti(params);
	}
}
