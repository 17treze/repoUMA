package it.tndigitale.a4gutente.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.component.AccessoComponent;
import it.tndigitale.a4gutente.config.Costanti;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.Firma;
import it.tndigitale.a4gutente.service.IFirmaService;

@RestController
@RequestMapping(ApiUrls.UTENTI_V1)
@Api(value = "Informazioni dell'utente", description = "Rappresenta le interrogazioni che si possono fare relative ai dati dell'accesso dell'utente che fa la chiamata", position = 0)
public class AccessoController {

	private static final Logger log = LoggerFactory.getLogger(AccessoController.class);
	
	@Autowired
	private AccessoComponent accessoComponent;
	
	@Autowired
	private IFirmaService firmaService;

	@GetMapping(value = "/log")
	@ApiOperation("Restituisce OK e fa log")
	public String log() {
		log.debug("Chiamato metodo pubblico");
		return "OK";
	}

	@GetMapping(value = "/headers")
	@ApiOperation("Restituisce i parametri degli header della request")
	public Map<String, List<String>> headers(@RequestHeader HttpHeaders headers) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		headers.forEach((k, v) -> result.put(k, v));
		return result;
	}


	@GetMapping(value = ApiUrls.UTENTE_DATI)
	@ApiOperation("Restituisce i dati anagrafici recuperandoli dall'header della request")
	public DatiAnagrafici caricaMieiDatiDaHeader(
			Principal principal,
			@RequestHeader HttpHeaders headers
		) {
		String principalName = principal.getName();
		log.debug("Chiamato sec da {}", principalName);
		DatiAnagrafici datiAnagrafici = new DatiAnagrafici();
		datiAnagrafici.setCodiceFiscale(getHeaderProperty(headers, Costanti.HEADER_CF));
		datiAnagrafici.setNome(getHeaderProperty(headers, Costanti.HEADER_NOME));
		datiAnagrafici.setCognome(getHeaderProperty(headers, Costanti.HEADER_COGNOME));
		datiAnagrafici.setEmail(getHeaderProperty(headers, Costanti.HEADER_MAIL));
		return datiAnagrafici;
	}
	
	protected static String getHeaderProperty(HttpHeaders headers, String nome) {
		List<String> header = headers.get(nome);
		return (header == null || header.isEmpty()) ? null : header.get(0);
	}

	@ApiOperation("Esegue l'operazione di firma del documento come previsto dai servizi online, a partire dai dati di accesso del sistema")
	@PostMapping(value = ApiUrls.FIRMA)
	public Firma firma(@RequestHeader HttpHeaders headers, @ApiParam(value = "Documento da firmare", required = true) @RequestParam("documento") MultipartFile documento) throws Exception {
		DatiAutenticazione datiAutenticazione = accessoComponent.caricaDatiAutenticazione(headers);
		return firmaService.firma(documento.getName(), datiAutenticazione, documento.getBytes());
	}
}
