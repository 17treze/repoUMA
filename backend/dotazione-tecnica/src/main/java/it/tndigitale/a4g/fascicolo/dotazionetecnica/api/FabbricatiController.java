package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.client.DotazioneTecnicaAnagraficaClient;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoEdificialeClasseCatastaleNonAmmessaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoInvalidInputException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoParticellaEstintaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DatiCatastaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.proxy.client.model.InformazioniImmobileDto;
import it.tndigitale.a4g.proxy.client.model.InformazioniParticellaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.FabbricatiService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoAbstract;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.FabbricatoDto;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.FASCICOLO)
@Tag(name = "FabbricatiController", description="Gestione fabbricati fascicolo")
public class FabbricatiController {

	@Autowired
	private FabbricatiService fabbricatiService;

	@Operation(
			summary = "Inserisce o aggiorna un fabbricato nel fascicolo", 
			description = "", 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = DettaglioFabbricatoAbstract.class))))
	@PostMapping(path = "/{cuaa}" + ApiUrls.FABBRICATI)
	public Long postFabbricato(
			@PathVariable String cuaa,
			@RequestBody DettaglioFabbricatoAbstract dettaglioFabbricatoDto) {
		return fabbricatiService.postFabbricato(cuaa, dettaglioFabbricatoDto);
	}

	@Operation(summary = "Reperimento dati generici inerenti ai fabbricati del fascicolo", description = "")
	@GetMapping("/{cuaa}" + ApiUrls.FABBRICATI)
	public List<FabbricatoDto> getFabbricati(@PathVariable String cuaa, @RequestParam(required = false) Integer idValidazione) {
		return fabbricatiService.getFabbricati(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@Operation(
			summary = "Reperimento dati generici inerenti ai fabbricati del fascicolo", 
			description = "",
			responses = {
					@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DettaglioFabbricatoAbstract.class)))
			})
	@GetMapping(path = "/{cuaa}" + ApiUrls.FABBRICATI +  "/{id}")
	public DettaglioFabbricatoAbstract getFabbricato(
			@PathVariable String cuaa,
			@PathVariable Long id,
			@RequestParam(required = false) Integer idValidazione) {
		return fabbricatiService.getFabbricato(id, idValidazione == null ? 0 : idValidazione);
	}

	@Operation(summary = "Reperimento dati generici inerenti ai fabbricati del fascicolo", description = "")
	@DeleteMapping("/{cuaa}" + ApiUrls.FABBRICATI +  "/{id}")
	public void deleteFabbricato(@PathVariable String cuaa, @PathVariable Long id) {
		fabbricatiService.cancellaFabbricato(id);
	}


	@Operation(summary = "Reperimento dati inerenti ai fabbricati dal catasto", description = "")
	@GetMapping(path = ApiUrls.FABBRICATI + "/catasto")
	public DatiCatastaliDto getFromCatastoParticella(
			@RequestParam String numeroParticella,
			@RequestParam(required = false) String denominatore,
			@RequestParam TipologiaParticellaCatastale tipologia,
			@RequestParam Integer codiceComuneCatastale,
			@RequestParam(required = false) Integer subalterno
			) {
		try {
//			return fabbricatiService.getInfoFabbricati(numeroParticella, denominatore, tipologia, codiceComuneCatastale, subalterno);
			DatiCatastaliDto part = new DatiCatastaliDto();
			part.setTipologia(TipologiaParticellaCatastale.EDIFICIALE);
			part.setComune("Trento");
			part.setFoglio(9999);
			part.setSezione("A");
			part.setSub("1");
			part.setParticella("00234");
			return part;
		} catch (NoSuchElementException | EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
//		} catch (CatastoParticellaEstintaException | CatastoEdificialeClasseCatastaleNonAmmessaException e) {
//			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Operation(summary = "Reperimento dati inerenti ai fabbricati dal catasto", description = "")
	@GetMapping(path = ApiUrls.FABBRICATI + "/catasto/elenco-subalterni-particella")
	public List<String> getFromCatastoParticella(@RequestParam String numeroParticella,
												 @RequestParam Integer codiceComuneCatastale) {
		try {
//			return fabbricatiService.getElencoSubalterniParticella(numeroParticella, codiceComuneCatastale);
			List<String> sub = new ArrayList();
			sub.add("1");
			sub.add("2");
			sub.add("3");
			return sub;
		} catch (NoSuchElementException | EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
