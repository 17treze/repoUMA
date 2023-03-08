package it.tndigitale.a4gistruttoria.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.ElencoLiquidazioneService;

@RestController
@RequestMapping(ApiUrls.ELENCO_LIQUIDAZIONE_ISTRUTTORIA_DU_V1)
@Api(value = "Elenco di liquidazione di istruttorie di domanda unica")
public class ElencoLiquidazioneController {
	
	public static final String VERBALE_PATH = "/verbale";
	
	@Autowired
	private ElencoLiquidazioneService liquidazioneService;

	@GetMapping("/{idElencoLiquidazione}" + VERBALE_PATH)
	@ApiOperation("Recupero istruttoria per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public byte[] caricaVerbaleLiquidazione(@PathVariable(value = "idElencoLiquidazione") Long idElencoLiquidazione) {
		return liquidazioneService.getVerbale(idElencoLiquidazione);
	}
}
