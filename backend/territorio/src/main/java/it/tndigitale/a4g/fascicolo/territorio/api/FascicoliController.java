package it.tndigitale.a4g.fascicolo.territorio.api;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.territorio.business.service.ControlliFascicoloAgsCompletoEnum;
import it.tndigitale.a4g.fascicolo.territorio.business.service.FascicoloService;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.territorio.api.ApiUrls;

@Api(value = "Controller per gestione operazioni su fascicolo dedicati alla comunicazione tra microservizi")
@RestController
@RequestMapping(ApiUrls.FASCICOLI_V1)
public class FascicoliController {

	@Autowired private FascicoloService fascicoloService;
	
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	@GetMapping("{cuaa}/controllo-completezza")
	public Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(
			@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true)
			final String cuaa) throws SQLException {
		try {
			var retList = new EnumMap<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto>(ControlliFascicoloAgsCompletoEnum.class);
			retList.putAll(fascicoloService.getControlloCompletezzaFascicolo(cuaa));
			return retList;
		} catch (SQLException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (NoSuchElementException nse) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, nse.getMessage());
		}
	}
}
