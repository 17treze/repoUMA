package it.tndigitale.a4g.uma.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.service.richiesta.MacchineService;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;

@RestController
@RequestMapping(ApiUrls.RICHIESTE)
@Tag(name = "MacchineController", description = "API che gestisce le macchine dichiarate per ogni richiesta UMA")
public class MacchineController {

	@Autowired
	private MacchineService macchineService;

	@Operation(summary = "Salva o aggiorna i macchinari dichiarati in una richiesta", description = "Questo servizio sovrascrive tutte le macchine per quella domanda rimpiazzandole con quelle passate in input. Deve avere almeno una macchina con flag utilizzata true.")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PostMapping("{id}" + ApiUrls.MACCHINE)
	public List<MacchinaDto> dichiaraMacchinariDomanda(@PathVariable(required = true) Long id, @RequestBody(required = true) List<MacchinaDto> macchine) {

		if (CollectionUtils.isEmpty(macchine)) {
			throw new IllegalArgumentException("Non ci sono macchinari da dichiarare");
		}
		return macchineService.dichiaraMacchinari(id, macchine);
	}

	@Operation(summary = "Ottiene l'elenco delle macchine che afferiscono ad una domanda uma", description = "Controlla se esistono dei macchinari dichiarati in domanda. Se non ve ne sono mostra quelli presi dal fascicolo in dotazione tecnica")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}" + ApiUrls.MACCHINE)
	public List<MacchinaDto> getMacchinari(@PathVariable(required = true) Long id) {
		return macchineService.getMacchinari(id);
	}
}
