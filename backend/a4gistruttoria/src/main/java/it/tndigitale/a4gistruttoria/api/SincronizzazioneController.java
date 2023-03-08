package it.tndigitale.a4gistruttoria.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.dto.SincronizzazioneFilter;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.AvviaElaborazioneAsyncService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaborazioneAsyncService;

@RestController
@RequestMapping(ApiUrls.SINCRONIZZAZIONE_V1)
@Api(value = "Gestione sincronizzazione dati istruttoria AGEA")
public class SincronizzazioneController {
	
	
	@Autowired
	private ElaborazioneAsyncService elaborazioneAsyncService;
	@Autowired
	private AvviaElaborazioneAsyncService avviaProcessoAsyncService;
	
	
	@ApiOperation("Avvia processo sincronizzazione dati istruttoria verso AGEA")
	@PostMapping() 
	@PreAuthorize("@abilitazioniPACComponent.checkEditaSTAT()")
	public Long avviaSincronizzazioni(@RequestBody SincronizzazioneFilter sincronizzazioneFilter) throws Exception {
		
		ProcessoAnnoCampagnaDomandaDto processoDomandaAnnoCampagnaDto = new ProcessoAnnoCampagnaDomandaDto();
		processoDomandaAnnoCampagnaDto.setCampagna(sincronizzazioneFilter.getCampagna());
		processoDomandaAnnoCampagnaDto.setTipoProcesso(sincronizzazioneFilter.getTipologiaSincronizzazione().getElaborazione());
		Long idProcesso = elaborazioneAsyncService.creaProcesso(processoDomandaAnnoCampagnaDto);
		processoDomandaAnnoCampagnaDto.setIdProcesso(idProcesso);
		avviaProcessoAsyncService.avviaProcesso(processoDomandaAnnoCampagnaDto);
		return idProcesso;
	}
}
