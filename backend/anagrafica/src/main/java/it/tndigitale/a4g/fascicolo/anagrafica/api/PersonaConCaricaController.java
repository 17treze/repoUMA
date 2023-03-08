package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.DocumentoIdentitaInvalidoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaFisicaOGiuridicaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DocumentoIdentitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.TipoDocumentoIdentitaEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaGiuridicaConCaricaDto;

@RestController
@RequestMapping(ApiUrls.API_V1)
@Api(value = "API per persone fisiche con carica nella persona giuridica A4G")
public class PersonaConCaricaController {
	
	@Autowired
	PersonaFisicaOGiuridicaConCaricaService personaFisicaOGiuridicaConCaricaService;

	@GetMapping("{cuaa}/carica/persona-fisica")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public List<PersonaFisicaConCaricaDto> getPersonaFisicaConCarica(
			@ApiParam(value = "Cuaa della persona giuridica", required = true)
			@PathVariable(value="cuaa", required=true)
			@Size(min = 11, max = 11) String cuaa,
			@RequestParam(required = false) Integer idValidazione,
			@RequestParam(required = false) boolean completa) throws Exception {
		return personaFisicaOGiuridicaConCaricaService.getPersonaFisicaConCarica(cuaa, idValidazione == null ? 0 : idValidazione, completa);
	}
	
	@GetMapping("{cuaa}/carica/persona-giuridica")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public List<PersonaGiuridicaConCaricaDto> getPersoneGiuridicheConCarica(
			@ApiParam(value = "Cuaa della persona giuridica", required = true)
			@PathVariable(value="cuaa", required=true)  @Size(min = 11, max = 11) final String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		return personaFisicaOGiuridicaConCaricaService.getPersoneGiuridicheConCarica(cuaa, idValidazione == null ? 0 : idValidazione);
	}
	
	@GetMapping("{cuaa}/carica/possibili-rappresentanti-legali")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
    public List<PersonaFisicaConCaricaDto> getPossibiliRappresentantiLegali(
    		@ApiParam(value = "Cuaa della persona giuridica", required = true)
    		@PathVariable(value="cuaa", required = true)
    		@Size(min = 11, max = 11) String cuaa) throws Exception {
        return personaFisicaOGiuridicaConCaricaService.getPossibiliRappresentantiLegali(cuaa);
    }
	
	@PostMapping("{cuaa}/carica/firmatario")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
    public void salvaFirmatario(
    		@ApiParam(value = "Cuaa del fascicolo", required = true)
    		@PathVariable(value="cuaa", required = true)
    		@Size(min = 11, max = 11) String cuaa,
    		@RequestParam(required = true) String codiceFiscale,
    		@RequestParam(required = true) String tipoDocumento,
    		@RequestParam(required = true) String numeroDocumento,
    		@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRilascio,
    		@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataScadenza,
    		@RequestParam(required = true) MultipartFile documento) {
        try {
        	DocumentoIdentitaDto documentoIdentitaDto = new DocumentoIdentitaDto();
        	documentoIdentitaDto.setCodiceFiscale(codiceFiscale);
        	documentoIdentitaDto.setDataRilascio(dataRilascio);
        	documentoIdentitaDto.setDataScadenza(dataScadenza);
        	documentoIdentitaDto.setDocumento(documento.getBytes());
        	documentoIdentitaDto.setNumeroDocumento(numeroDocumento);
        	documentoIdentitaDto.setTipoDocumento(TipoDocumentoIdentitaEnum.valueOf(tipoDocumento));
			personaFisicaOGiuridicaConCaricaService.salvaFirmatario(cuaa, documentoIdentitaDto);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}  catch (DocumentoIdentitaInvalidoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    }
	
	@ApiOperation("Recupera il documento d'identità associato al firmatario del fascicolo")
	@GetMapping("{cuaa}/carica/firmatario/documento-identita")
	@PreAuthorize("@abilitazioniComponent.checkPermessiVisualizzazioneFirmatario(#cuaa)")
	public DocumentoIdentitaDto getDocumentoIdentitaFirmatario(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return personaFisicaOGiuridicaConCaricaService.getDocumentoIdentitaFirmatario(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@GetMapping("personagiuridica/{cuaa}/carica/firmatario")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
    public PersonaFisicaConCaricaDto getFirmatario(
    		@ApiParam(value = "Cuaa dell'azienda", required = true)
    		@PathVariable(value="cuaa", required = true)
    		@Size(min = 11, max = 16) String cuaa) {
		try {	
	        return personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
    }
}