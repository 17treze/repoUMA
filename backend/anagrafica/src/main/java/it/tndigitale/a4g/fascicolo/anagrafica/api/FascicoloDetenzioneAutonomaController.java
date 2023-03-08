package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ControlliFascicoloAnagraficaCompletoEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaCAAException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloInvalidConditionException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FirmaDocumentoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ValidazioneFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloCreationResultDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ModoPagamentoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;

@RestController
@RequestMapping(ApiUrls.FASCICOLO_DETENZIONE_AUTONOMA)
@Api(value = "Servizio che ha la responsabilita di gestire il ciclo di vita del fascicolo dalla sua apertura fino alla sua chiusura")
public class FascicoloDetenzioneAutonomaController {

	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private ValidazioneFascicoloService validazioneFascicoloService;

	@ApiOperation("Dato un codice fiscale verifica se sia possibile aprire un fascicolo per quel codice")
	@GetMapping("/{codiceFiscale}/verifica/dati-apertura")
	// @PreAuthorize("@abilitazioniComponent.checkApriFascicoloAutogestito()")
	public DatiAperturaFascicoloDto verificaAperturaFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole verificare se vi sono le condizioni per aprire un nuovo fascicolo", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) {
		try {
			return fascicoloService.verificaAperturaFascicoloCruscotto(codiceFiscale);
		} catch (FascicoloValidazioneException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di aprire un nuovo fascicolo e resitituisce il nuovo cuaa")
	@PostMapping("/{codiceFiscale}/apri")
	@PreAuthorize("@abilitazioniComponent.checkApriFascicoloAutogestito()")
	public FascicoloCreationResultDto apri(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true) @PathVariable(value = "codiceFiscale", required = true) String codiceFiscale)
			throws FascicoloValidazioneException {
		try {
			return fascicoloService.apriFascicoloCruscotto(codiceFiscale);
		} catch (FascicoloValidazioneException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	@ApiOperation("Inserimento IBAN")
	@PutMapping("/{cuaa}/modo-pagamento")
	@PreAuthorize("@abilitazioniComponent.checkPermessiAggiornamentoModoPagamento(#cuaa)")
	public ModoPagamentoDto inserimentoModoPagamento(@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
			@RequestBody(required = true) ModoPagamentoDto modoPagamento) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		modoPagamento.setId(fascicoloService.inserimentoModoPagamento(cuaa, modoPagamento).getId());
		return modoPagamento;
	}

	@ApiOperation("Rimozione iban dal fascicolo ")
	@DeleteMapping("/{cuaa}/modo-pagamento/{id}")
	@PreAuthorize("@abilitazioniComponent.checkPermessiAggiornamentoModoPagamento(#cuaa)")
	public void rimozioneModoPagamento(@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa,
			@PathVariable @ApiParam(value = "Identificativo modo pagamento", required = true) final Long id) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		fascicoloService.rimozioneModoPagamento(cuaa, id);
	}

	@ApiOperation("Elenco modi di pagamento associato al fascicolo")
	@GetMapping("/{cuaa}/modo-pagamento")
	@PreAuthorize("@abilitazioniComponent.checkPermessiAggiornamentoModoPagamento(#cuaa)")
	public List<ModoPagamentoDto> elencoModoPagamento(@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa, @RequestParam(required = false) Integer idValidazione) {
		return fascicoloService.elencoModoPagamento(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza-detenzione-autonoma")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicoloDetenzioneAutonoma(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true) @PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			return validazioneFascicoloService.getControlloCompletezzaFascicoloDetenzioneAutonoma(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation("Upload della scheda di validazione firmata dall'azienda")
	@PutMapping("/{cuaa}/report-scheda-validazione-detenzione-autonoma")
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirmaOrCaa(#cuaa)")
	public void postReportSchedaValidazioneDetenzioneAutonoma(
			@ApiParam(value = "Cuaa del fascicolo al quale si riferisce la scheda di validazione") @PathVariable(required = true, value = "cuaa") String cuaa,
			@RequestBody(required = true) byte[] schedaValidazioneFirmata) throws IOException {
		try {
			validazioneFascicoloService.salvaSchedaValidazioneDetenzioneAutonoma(cuaa, schedaValidazioneFirmata);
		} catch (NoSuchElementException e) {
			// cuaa in input non corrisponde ad un fascicolo
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FirmaDocumentoException e) {
			// errore nella firma del documento
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (FascicoloNonValidabileException e) {
			// stato del fascicolo non conforme
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
		}
	}

	@ApiOperation("Restituisce la scheda di validazione del fascicolo firmata")
	@GetMapping("/{cuaa}/report-scheda-validazione-detenzione-autonoma")
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirmaOrCaa(#cuaa)")
	public byte[] getReportValidazioneFascicoloDetenzioneAutonoma(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true) @PathVariable(required = true) final String cuaa) {
		try {
			return validazioneFascicoloService.getReportValidazioneFascicoloDetenzioneAutonoma(cuaa);
		} catch (IOException | RestClientException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FascicoloInvalidConditionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Restituisce la scheda di validazione in bozza del fascicolo con detenzione autonoma")
	@GetMapping("/{cuaa}/report-scheda-validazione-bozza-detenzione-autonoma")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public byte[] getSchedaValidazioneBozzaDetenzioneAutonoma(@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true) @PathVariable(required = true) final String cuaa) {
		try {
			return validazioneFascicoloService.getReportValidazioneFascicoloBozzaDetenzioneAutonoma(cuaa);
		} catch (IOException | RestClientException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FascicoloInvalidConditionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
