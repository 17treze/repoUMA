package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.SoggettoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.legacy.FascicoloAgsService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDtoPaged;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;

@RestController
@RequestMapping(ApiUrls.FASCICOLO_AGS)
@Api(value = "API di gestione di un fascicolo aziendale di AGS")
public class FascicoloAgsController {

	@Autowired
	private FascicoloAgsService fascicoloAgsService;
	@Autowired
	private SoggettoService soggettoService;

	@GetMapping("/{cuaa}") 
	@ApiOperation("Reperimento informazioni Fascicolo aziendale - Recupero eventuali deleghe")
	public FascicoloAgsDto getFascicolo(
			@ApiParam(value = "CUAA del fascicolo", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
		return fascicoloAgsService.getFascicolo(cuaa, data);
	}

	@GetMapping
	@ApiOperation("Reperimento informazioni del Fascicolo aziendale tramite id Ags - Recupero eventuali deleghe")
	public FascicoloAgsDto getFascicoloById(@ApiParam(value = "ID ags del fascicolo", required = true) @RequestParam(value = "id") Long id) {
		return fascicoloAgsService.getFascicolo(id);
	}

	@GetMapping("/paged")
	@ApiOperation("Reperimento informazioni paginate del Fascicolo aziendale tramite condizioni in OR")
	public RisultatiPaginati<FascicoloAgsDtoPaged> getFascicoli(
			@ApiParam("Ricerca Fascicoli per CUAA o (inclusivo) per Denominazione") FascicoloAgsFilter filter,
			Paginazione paginazione,
			@ApiParam("I campi possibili per l'ordinamento sono CUAA o DENOMINAZIONE") Ordinamento ordinamento) {
		Optional<Ordinamento> optOrdinamento = Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null);
		if (!optOrdinamento.isPresent()) {
			Ordinamento ordinamentoDefault = new Ordinamento()
					.setOrdine(ordinamento.getOrdine())
					.setProprieta("cuaa");
			ordinamento = ordinamentoDefault;
		}
		return fascicoloAgsService.ricercaFascicolo(filter, paginazione, ordinamento);
	}

	@ApiOperation("Recupera i Soggetti registrati con ‘ruolo esteso’ di Titolare/Rappresentante Legale nel fascicolo aziendale ")
	@GetMapping("/{cuaa}/soggetti")
	public List<CaricaAgsDto> getPersoneConCarica(@ApiParam(value = "CUAA azienda", required = true) @PathVariable(value = "cuaa", required = true) String cuaa) {
		return soggettoService.getSoggettiFascicoloAziendale(cuaa);
	}

	@ApiOperation("Recupera i Soggetti registrati come EREDI in fascicolo aziendale di persona fisica")
	@GetMapping("/{cuaa}/eredi")
	public List<CaricaAgsDto> getEredi(@ApiParam(value = "CUAA azienda", required = true) @PathVariable(value = "cuaa", required = true) String cuaa) {
		return soggettoService.getEredi(cuaa);
	}

	@ApiOperation("Recupera il numero di movimentazioni di validazione terminate con successo e la data dell'ultima validazione positiva per l'anno di campagna indicato")
	@GetMapping("/{id}/movimenti-validazione")
	public MovimentoValidazioneFascicoloAgsDto getMovimentiValidazioneFascicolo(@ApiParam(value = "ID del fascicolo", required = true) @PathVariable(value = "id", required = true) Long id,
			@ApiParam(value = "Anno Campagna", required = true) @RequestParam Long campagna) {
		return fascicoloAgsService.getMovimentiValidazioneFascicolo(id, campagna);
	}
}
