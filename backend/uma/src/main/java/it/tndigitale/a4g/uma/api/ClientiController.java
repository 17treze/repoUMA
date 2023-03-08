package it.tndigitale.a4g.uma.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.service.clienti.ClientiService;
import it.tndigitale.a4g.uma.business.service.clienti.RicercaClientiService;
import it.tndigitale.a4g.uma.dto.clienti.ClienteConsumiDto;
import it.tndigitale.a4g.uma.dto.clienti.ClienteDto;
import it.tndigitale.a4g.uma.dto.clienti.FatturaClienteDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;
import javassist.NotFoundException;

@RestController
@RequestMapping(ApiUrls.CONSUMI)
@Tag(name = "ClienteController", description = "API che gestisce i clienti conto terzi di una dichiarazione consumi")
public class ClientiController {

	@Autowired
	private RicercaClientiService ricercaClientiService;
	@Autowired
	private ClientiService clientiService;

	@Operation(summary = "Reperimento dei clienti contoterzi in base a un filtro di ricerca", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDichiarazioneConsumi(#id)")
	@GetMapping("/{id}" + ApiUrls.CLIENTI)
	public List<ClienteDto> getClienti(@PathVariable Long id) {
		return ricercaClientiService.getClienti(id);
	}

	@Operation(summary = "Reperimento dei clienti contoterzi in base al proprio id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(#id, #idCliente)")
	@GetMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}")
	public ClienteConsumiDto getCliente(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) {
		return clientiService.getCliente(idCliente);
	}

	@Operation(summary = "Importazione in uma dei dati del cliente di un contoterzista", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping(path=("/{id}" + ApiUrls.CLIENTI), consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
	public Long importaClienti (@PathVariable Long id, @RequestParam Long idFascicolo, @RequestPart(value = "allegati", required=true) List<MultipartFile> allegati) {
		checkAllegati(allegati);
		return clientiService.importaDatiCliente(id, idFascicolo, allegati);
	}

	@Operation(summary = "Valida i dati del cliente di un contoterzista", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping(path=("/{id}" + ApiUrls.CLIENTI + ApiUrls.VALIDA))
	public void validaCliente (@PathVariable Long id, @RequestParam Long idFascicolo) {
		clientiService.validaCliente(id, idFascicolo);
	}

	@Operation(summary = "Elimina il cliente di una dichiarazione consumi by id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(#id, #idCliente)")
	@DeleteMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}")
	public void eliminaCliente(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) {
		clientiService.eliminaCliente(idCliente);
	}


	@Operation(summary = "Ottiene l'elenco delle lavorazioni a superficie del cliente indicato", description = "Controlla se esistono delle lavorazioni già dichiarate in domanda. Se non ve ne sono,le reperisce dal fascicolo")
	@PreAuthorize("@abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(#id, #idCliente)")
	@GetMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.LAVORAZIONI)
	public List<RaggruppamentoLavorazioniDto> getLavorazioniSuperficie(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) {
		return clientiService.getLavorazioniSuperficie(idCliente);
	}


	@Operation(summary = "Reperisce le dichiarazioni a superficie per il cliente specificato", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(#id, #idCliente)")
	@GetMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.FABBISOGNI)
	public List<DichiarazioneDto> getFabbisogniSuperficie(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) {
		return clientiService.getFabbisogniSuperficie(idCliente);
	}

	@Operation(summary = "Recupera i fabbisogni della richiesta di carburante per il cliente specificato", description = "")
	//	@PreAuthorize("@abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(#id, #idCliente)")
	@GetMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.FABBISOGNI + "-richiesta")
	public List<DichiarazioneDto> getFabbisogniRichiestaCliente(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) {
		return clientiService.getFabbisogniRichiestaCliente(idCliente);
	}

	@Operation(summary = "Dichiara le lavorazioni a superficie per il cliente specificato", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(#id, #idCliente)")
	@PostMapping("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.FABBISOGNI)
	public void dichiaraFabbisogniSuperficie(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente,  @RequestBody(required = true) List<DichiarazioneDto> dichiarazioni) {
		clientiService.dichiaraFabbisogniSuperficie(idCliente, dichiarazioni);
	}

	@Operation(summary = "Reperisce tutti gli allegati di un cliente", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(#id, #idCliente)")
	@GetMapping(path=("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.ALLEGATI))
	public List<FatturaClienteDto> getAllegatiCliente(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente) throws NotFoundException {		
		return ricercaClientiService.getAllegatiCliente(idCliente);
	}

	@Operation(summary = "Salva gli allegati di un cliente", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(#id, #idCliente)")
	@PostMapping(path=("/{id}" + ApiUrls.CLIENTI + "/{idCliente}" + ApiUrls.ALLEGATI), consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
	public void caricaAllegati(@PathVariable(required = true) Long id, @PathVariable(required = true) Long idCliente, @RequestPart(value = "allegati", required=true) List<MultipartFile> allegati) {		
		checkAllegati(allegati);
		clientiService.salvaAllegati(idCliente, allegati);
	}

	private void checkAllegati (List<MultipartFile> allegati) {
		if (!CollectionUtils.isEmpty(allegati)) {
			if (allegati.size() <= 5) {
				allegati.forEach(allegato -> {
					// Verifico che gli allegati siano pdf
					if (!allegato.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)) {
						throw new IllegalArgumentException("Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf");
					}
					if (allegato.getOriginalFilename().length() > 50) {
						throw new IllegalArgumentException("Il nome del file supera il limite massimo di 50 caratteri");
					}
				});
			} else {
				throw new IllegalArgumentException("Non è possibile inserire più di 5 allegati");
			}
		} else {
			throw new IllegalArgumentException("E’ obbligatorio inserire almeno un allegato");
		}
	}
}
