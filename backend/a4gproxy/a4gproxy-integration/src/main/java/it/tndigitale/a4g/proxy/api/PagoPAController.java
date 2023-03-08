package it.tndigitale.a4g.proxy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.dto.PagoPaIbanDettaglioDto;
import it.tndigitale.a4g.proxy.services.pagopa.PagoPaService;
import it.tndigitale.a4g.proxy.services.pagopa.VerificaIbanException;

@RestController
@RequestMapping(ApiUrls.PAGOPA)
@Api(value = "Interfaccia per la comunicazione con PagoPA")
public class PagoPAController {

	@Autowired
	private PagoPaService pagoPaService;

	@ApiOperation("invia una richiesta di validazione di un codice bancario iban")
	@GetMapping("/{iban}/validate-iban")
	public Boolean validateIban(@ApiParam(value = "codice iban da verificare", required = true) @PathVariable(value = "iban", required = true) String iban) {
		try {
			return pagoPaService.validateIban(iban);
		} catch (WebClientResponseException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		}
	}

	@ApiOperation("Invia a Pago PA una richiesta di informazioni del codice iban fornito")
	@PostMapping("/{cuaa}/check-iban/persona-fisica")
	public PagoPaIbanDettaglioDto checkIbanPersonaFisica(@ApiParam(value = "cuaa", required = true) @PathVariable(value = "cuaa", required = true) String cuaa,
			@ApiParam(value = "codice iban da verificare", required = true) @RequestParam(value = "iban", required = true) String iban) {

		// viene restituita in caso dal server pagoPA arrivi un 501 (errore PSP non registrato su PagoPA)

		try {
			return pagoPaService.checkIbanPersonaFisica(iban, cuaa);
		} catch (WebClientResponseException e) {
			PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = new PagoPaIbanDettaglioDto();
			pagoPaIbanDettaglioDto.setIban(iban);
			pagoPaIbanDettaglioDto.setBic("false"); // imposto false per indicare che non è certificato da pagoPA
			if (e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)) {
				return pagoPaIbanDettaglioDto;
			}
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		} catch (VerificaIbanException ex) {
			if (ex.getHttpStatus().equals(HttpStatus.NOT_EXTENDED)) {
				throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Il conto dell'istituto di credito non e' valido");
			}
			throw new ResponseStatusException(ex.getHttpStatus(), ex.getMessage());
		}
	}

	@ApiOperation("Ritorna informazioni bancarie fake")
	@PostMapping("/{iban}/check-iban-fake")
	public PagoPaIbanDettaglioDto checkIbanFake(@ApiParam(value = "codice iban da verificare", required = true) @PathVariable(value = "iban", required = true) String iban) {
		try {
			return pagoPaService.checkIbanFake(iban);
		} catch (WebClientResponseException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codice iban invalido");
			}
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		} catch (VerificaIbanException ex) {
			throw new ResponseStatusException(ex.getHttpStatus(), ex.getMessage());
		}
	}

	@ApiOperation("Invia a Pago PA una richiesta di informazioni del codice iban fornito")
	@PostMapping("/{cuaa}/check-iban/persona-giuridica-partita-iva")
	public PagoPaIbanDettaglioDto checkIbanPersonaGiuridicaEPartitaIva(@ApiParam(value = "cuaa", required = true) @PathVariable(value = "cuaa", required = true) String cuaa,
			@ApiParam(value = "codice iban da verificare", required = true) @RequestParam(value = "iban", required = true) String iban,
			@ApiParam(value = "partita iva in caso di persona giuridica", required = true) @RequestParam(required = true) String partitaIva) {

		// viene restituita in caso dal server pagoPA arrivi un 501 (errore PSP non registrato su PagoPA)
		PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = new PagoPaIbanDettaglioDto();
		pagoPaIbanDettaglioDto.setIban(iban);
		try {
			return pagoPaService.checkIbanPersonaGiuridicaEPartitaIva(iban, cuaa, partitaIva);
		} catch (WebClientResponseException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)) {
				return pagoPaIbanDettaglioDto;
			}
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		} catch (VerificaIbanException ex) {
			if (ex.getHttpStatus().equals(HttpStatus.NOT_EXTENDED)) {
				throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Il conto dell'istituto di credito non e' valido");
			}
			throw new ResponseStatusException(ex.getHttpStatus(), ex.getMessage());
		}
	}

	@ApiOperation("Invia a Pago PA una richiesta di informazioni del codice iban fornito")
	@PostMapping("/{partitaIva}/check-iban/partita-iva")
	public PagoPaIbanDettaglioDto checkIbanPartitaIva(@ApiParam(value = "partitaIva", required = true) @PathVariable(value = "partitaIva", required = true) String partitaIva,
			@ApiParam(value = "codice iban da verificare", required = true) @RequestParam(value = "iban", required = true) String iban) {
		// viene restituita in caso dal server pagoPA arrivi un 501 (errore PSP non registrato su PagoPA)
		PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = new PagoPaIbanDettaglioDto();
		pagoPaIbanDettaglioDto.setIban(iban);
		pagoPaIbanDettaglioDto.setBic("false"); // imposto false per indicare che non è certificato da pagoPA
		try {
			return pagoPaService.checkIbanPartitaIva(iban, partitaIva);
		} catch (WebClientResponseException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED)) {
				// throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "La nuova modalità di pagamento è stata archiviata correttamente anche se non è stata certificata da PagoPA");
				return pagoPaIbanDettaglioDto;
			}
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		} catch (VerificaIbanException ex) {
			if (ex.getHttpStatus().equals(HttpStatus.NOT_EXTENDED)) {
				throw new ResponseStatusException(HttpStatus.NOT_EXTENDED, "Il conto dell'istituto di credito non e' valido");
			}
			throw new ResponseStatusException(ex.getHttpStatus(), ex.getMessage());
		}
	}

}
