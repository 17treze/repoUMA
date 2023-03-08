package it.tndigitale.a4g.ags.api;

import java.util.List;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.ags.dto.Utente;
import it.tndigitale.a4g.ags.dto.UtenteFilter;
import it.tndigitale.a4g.ags.service.UtenteService;

/**
 * Controller per gestione operazioni su utente corrente
 * 
 * @author B.Irler
 *
 */
@RestController
@RequestMapping(ApiUrls.UTENTI_V1)
@Api(value = "Controller per gestione operazioni su utente corrente")
public class UtenteRestController {

	private static final Logger logger = LoggerFactory.getLogger(UtenteRestController.class);

	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private UtenteComponent utenteComp;
	
	@ApiOperation("Restituisce l'elenco delle utenze associate all'utente chiamante in AGS (come filtro codice fiscale)")
	@GetMapping(ApiUrls.FUNZIONE_UTENZE)
	public List<Utente> getUtente() throws Exception {
		String username = utenteComp.utenza();
		logger.debug("getUtente: username =  {}", username);
		// costruisco l'oggetto per la ricerca partendo dalla mappa di parametri ottenuti
		return utenteService.getUtenti(username);
	}

	@ApiOperation("Consente di eseguire la ricerca degli utenti di AGS")
	@GetMapping
	public List<Utente> getUtente(UtenteFilter filtri) throws Exception {
		return utenteService.getUtenti(filtri);
	}
}
