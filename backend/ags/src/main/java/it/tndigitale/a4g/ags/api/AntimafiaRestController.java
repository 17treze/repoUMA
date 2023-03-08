package it.tndigitale.a4g.ags.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.EsitoAntimafia;
import it.tndigitale.a4g.ags.service.AntimafiaService;

/**
 * Controller per gestione degli esiti sulle domande antimafia.
 * 
 * @author S.DeLuca
 *
 */
@Api(value = "Controller per gestione degli esiti e delle certificazioni antimafia.")
@RestController
@RequestMapping
public class AntimafiaRestController {

	@Autowired
	private AntimafiaService antimafiaService;
	@Autowired
	private ObjectMapper objectMapper;

	@ApiOperation("Inserisce o aggiorna l'esito per il pagamento della dichiarazione antimafia.")
	@PutMapping(ApiUrls.ESITI_ANTIMAFIA_V1)
	// TODO @PreAuthorize("@abilitazioniComponent.checkMovimentaDomandaDU(#numeroDomanda, #tipoMovimento)")
	public void salva(@RequestBody() @ApiParam(value = "Dati esito antimafia", required = true) List<EsitoAntimafia> esitiAntimafia) throws Exception {
		antimafiaService.salva(esitiAntimafia);
	}
	
	@ApiOperation("Recupero gli esiti in base ai params")
	@GetMapping(ApiUrls.ESITI_ANTIMAFIA_V1)
	public List<EsitoAntimafia> getEsiti(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		EsitoAntimafia esitoAntimafia = objectMapper.readValue(params, EsitoAntimafia.class);
		return antimafiaService.recuperaEsiti(esitoAntimafia);
	}
	
	@ApiOperation("Cancellazione record da tabella TPAGA_ESITI_ANTIMAFIA per CUAA")
	@DeleteMapping(ApiUrls.ESITI_ANTIMAFIA_V1)
	public void cancella(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca. Implementato solo per CUAA ", required = true) String params) throws Exception {
		EsitoAntimafia esitoAntimafia = objectMapper.readValue(params, EsitoAntimafia.class);
		antimafiaService.cancella(esitoAntimafia);
	}	
	
	@ApiOperation("Sincronizzazione tabella TPAGA_CERTIFICAZIONI_ANTIMAFIA")
	@PutMapping(ApiUrls.CERTIFICAZIONI)
	public void sincronizzaCertificazioni(@RequestBody() @ApiParam(value = "Lista cuaa da aggiornare", required = true) List<String> cuaaList) throws Exception {
		antimafiaService.sincronizzaCert(cuaaList);
	}	

}
