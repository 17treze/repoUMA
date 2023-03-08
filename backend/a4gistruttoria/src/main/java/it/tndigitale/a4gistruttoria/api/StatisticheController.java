package it.tndigitale.a4gistruttoria.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.component.IoItaliaInvioMessaggioFactory;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.dto.StatisticaFilter;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaMessaggioByStato;
import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaSenderService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.AvviaElaborazioneAsyncService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaborazioneAsyncService;

@RestController
@RequestMapping(ApiUrls.STATISTICHE_V1)
@Api(value = "Gestione statistiche")
public class StatisticheController {
	
	@Autowired
	private ElaborazioneAsyncService elaborazioneAsyncService;
	@Autowired
	private AvviaElaborazioneAsyncService avviaProcessoAsyncService;
	
	
	@ApiOperation("Avvia calcolo statistiche di istruttoria")
	@PostMapping
	@PreAuthorize("@abilitazioniPACComponent.checkEditaSTAT()")
	public Long avviaStatistiche(@RequestBody StatisticaFilter statisticheFilter) throws Exception {
		ProcessoAnnoCampagnaDomandaDto processoDomandaAnnoCampagnaDto = new ProcessoAnnoCampagnaDomandaDto();
		processoDomandaAnnoCampagnaDto.setCampagna(statisticheFilter.getCampagna());
		processoDomandaAnnoCampagnaDto.setTipoProcesso(statisticheFilter.getTipologiaStatistica().getElaborazione());
		Long idProcesso = elaborazioneAsyncService.creaProcesso(processoDomandaAnnoCampagnaDto);
		processoDomandaAnnoCampagnaDto.setIdProcesso(idProcesso);
		avviaProcessoAsyncService.avviaProcesso(processoDomandaAnnoCampagnaDto);
		return idProcesso;
	}
	
}
