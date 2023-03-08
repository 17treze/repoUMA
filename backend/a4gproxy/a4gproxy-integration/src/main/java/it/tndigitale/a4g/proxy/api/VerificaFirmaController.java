package it.tndigitale.a4g.proxy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.proxy.dto.InfoVerificaFirma;
import it.tndigitale.a4g.proxy.services.VerificaFirmaException;
import it.tndigitale.a4g.proxy.services.VerificaFirmaService;
import it.tndigitale.ws.verificafirmedigitali.WarningResponseType;

@RestController
@RequestMapping(ApiUrls.VERIFICAFIRMA)
@Api(value = "Firma Digitale")
public class VerificaFirmaController {

	@Autowired
	private VerificaFirmaService verificaFirmaService;

	@ApiOperation("Metodo verifica Firma")
	@PostMapping
	public WarningResponseType verificaFirma(@RequestParam(value = "documentoFirmato") MultipartFile documentoFirmato)
			throws Exception {
		return verificaFirmaService.verificaFirma(documentoFirmato);
	}

	@ApiOperation("Verifica la firma al documento e se presente controlla che il codice fiscale del firmatario coincida con quello ricevuto in input - restituisce data della firma")
	@PostMapping(value = "/singola/{codiceFiscale}")
	public InfoVerificaFirma verificaFirmaSingola(@RequestParam(value = "documentoFirmato") MultipartFile documentoFirmato,
			@PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		try {
			return verificaFirmaService.verificaFirmaSingola(documentoFirmato, codiceFiscale);
		} catch (VerificaFirmaException vfe) {
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, vfe.getMessage(), vfe);
		}
	}
	
	@ApiOperation("Verifica la firma al documento e se presente controlla che il codice fiscale del firmatario coincida con quello ricevuto in input - restituisce data della firma")
	@PostMapping(value = "/firmatari")
	public List<InfoVerificaFirma> verificaFirmaMultipla(@RequestParam(value = "documentoFirmato") MultipartFile documentoFirmato, @RequestParam List<String> codiceFiscaleList) throws Exception {
		try {
			return verificaFirmaService.verificaFirmaMultipla(documentoFirmato, codiceFiscaleList);
		} catch (VerificaFirmaException vfe) {
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, vfe.getMessage(), vfe);
		}
	}
}
