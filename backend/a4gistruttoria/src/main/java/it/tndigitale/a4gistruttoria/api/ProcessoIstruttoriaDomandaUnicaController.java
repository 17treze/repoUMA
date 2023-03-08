package it.tndigitale.a4gistruttoria.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.dto.ProcessoIstruttoriaDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoLiquidazioneIstruttorieDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.AvviaElaborazioneAsyncService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaborazioneAsyncService;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1)
@Api(value = "Elaborazioni asincrone su istruttorie dei sostegni richiesti in domanda unica")
public class ProcessoIstruttoriaDomandaUnicaController {

	private static final Logger logger = LoggerFactory.getLogger(ProcessoIstruttoriaDomandaUnicaController.class);
	
	public static final String LIQUIDAZIONE_PATH = "/liquidazione";

	@Autowired
	private ElaborazioneAsyncService service;
	@Autowired
	private AvviaElaborazioneAsyncService avviaElaborazioneAsyncService;
	
	@PostMapping()
	@ApiOperation("Avvia un processo su una lista di istruttorie")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU() && @permessiModificaDU.checkPermessiIstruttorie(#inputProcessoIstruttorie.getIdIstruttorie(), #inputProcessoIstruttorie.getTipoProcesso())")
	public Long avviaProcessoSuIstruttorie(@RequestBody() InputProcessoIstruttorieDto inputProcessoIstruttorie) throws Exception {
		logger.debug("avviaProcessoSuIstruttorie - start per tipo processo {}", inputProcessoIstruttorie.getTipoProcesso());
		// if forzato per l'inizializzazione del processo a run

		Long idProcesso = service.creaProcesso(inputProcessoIstruttorie);
		inputProcessoIstruttorie.setIdProcesso(idProcesso);
		avviaElaborazioneAsyncService.avviaProcesso(inputProcessoIstruttorie);

		logger.debug("avviaProcessoSuIstruttorie - end con id {}", idProcesso);
		return idProcesso;
	}
	
	@PostMapping(LIQUIDAZIONE_PATH)
	@ApiOperation("Avvia un processo su una lista di istruttorie")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU() && @permessiModificaDU.checkPermessiIstruttorie(#inputProcessoLiquidazioneIstruttorie.getIdIstruttorie())")
	public Long avviaProcessoLiquidazioneSuIstruttorie(@RequestBody() InputProcessoLiquidazioneIstruttorieDto inputProcessoLiquidazioneIstruttorie) throws Exception {
		logger.debug("avviaProcessoSuIstruttorie - start per tipo processo {}", inputProcessoLiquidazioneIstruttorie.getTipoProcesso());
		// if forzato per l'inizializzazione del processo a run

		Long idProcesso = service.creaProcesso(inputProcessoLiquidazioneIstruttorie);
		inputProcessoLiquidazioneIstruttorie.setIdProcesso(idProcesso);
		avviaElaborazioneAsyncService.avviaProcesso(inputProcessoLiquidazioneIstruttorie);
		logger.debug("avviaProcessoSuIstruttorie - end con id {}", idProcesso);
		return idProcesso;
	}

	@ApiOperation(value = "Restituisce lo stato di avanzamento del processo in esecuzione(RUN), filtrato per tipo, nonchè la percentuale avanzamento")
	@GetMapping("/inesecuzione")
	public List<ProcessoIstruttoriaDto> getProcessiInEsecuzione(TipoProcesso tipoProcesso)throws Exception {
		return service.getProcessiInEsecuzione(tipoProcesso);

	}
}
