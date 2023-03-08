package it.tndigitale.a4g.zootecnia.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.zootecnia.business.service.ControlliFascicoloZootecniaCompletoEnum;
import it.tndigitale.a4g.zootecnia.business.service.ZootecniaService;
import it.tndigitale.a4g.zootecnia.dto.AnagraficaAllevamentoDto;

@RestController
@RequestMapping(ApiUrls.ZOOTECNIA)
@Tag(name = "ZootecniaController", description = "API per zootecnia")
public class ZootecniaController {

	@Autowired
	private ZootecniaService zootecniaService;

	@Operation(summary = "Permette di ottenere la lista di allevamenti di un certo detentore")
	@GetMapping("/{cuaa}/anagrafica-allevamenti")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public List<AnagraficaAllevamentoDto> getAllevamenti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return zootecniaService.getAllevamenti(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@Operation(summary = "Aggiorna la lista di allevamenti e strutture associate per il proprietario alla data richiesta", description = "utilizzare il formato YYYY-MM-dd")
	@PutMapping("/{cuaa}/anagrafica-allevamenti/aggiorna")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void aggiornaAllevamenti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@Schema(required = true, example = "2020-12-31" , description = "utilizzare il formato yyyy-mm-dd")
			@RequestParam(value = "dataRichiesta", required = true) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRichiesta) {
		zootecniaService.aggiornaAllevamenti(cuaa, dataRichiesta);
	}
}
