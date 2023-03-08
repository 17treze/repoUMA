package it.tndigitale.a4g.uma.api;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.service.lavorazioni.LavorazioniService;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneFabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@RestController
@RequestMapping(ApiUrls.RICHIESTE)
@Tag(name = "LavorazioniController", description = "API che gestisce il recupero e il salvataggio dei fabbisogni inerenti una richiesta di carburante")
public class LavorazioniController {

	private static final String NON_CI_SONO_LAVORAZIONI_DA_DICHIARARE = "Non ci sono lavorazioni da dichiarare";
	private static final String SPECIFICARE_UN_TIPO_DI_LAVORAZIONE = "Specificare un tipo di lavorazione";

	@Autowired
	private LavorazioniService lavorazioniService;

	@Operation(summary = "Salva o aggiorna le lavorazioni relative ad una richiesta di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PostMapping("{id}" + ApiUrls.FABBISOGNI)
	public void dichiaraFabbisogni(@PathVariable(required = true) Long id, @RequestBody(required = true) List<DichiarazioneDto> dichiarazioni) {

		if (CollectionUtils.isEmpty(dichiarazioni)) {
			throw new IllegalArgumentException(NON_CI_SONO_LAVORAZIONI_DA_DICHIARARE);
		}
		lavorazioniService.dichiaraFabbisogni(id, dichiarazioni);
	}

	@Operation(summary = "Salva o aggiorna le lavorazioni inerenti i fabbricati indicati tramite ID ", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PostMapping("{id}" + ApiUrls.FABBISOGNI + "-fabbricati")
	public void dichiaraFabbisogniFabbricati(@PathVariable(required = true) Long id,@RequestBody(required = true) List<DichiarazioneFabbricatoDto> dichiarazioni) {

		if (CollectionUtils.isEmpty(dichiarazioni)) {
			throw new IllegalArgumentException(NON_CI_SONO_LAVORAZIONI_DA_DICHIARARE);
		}
		lavorazioniService.dichiaraFabbisogniFabbricati(id, dichiarazioni);
	}

	@Operation(summary = "Calcola l'elenco dei raggruppamenti e delle lavorazioni che afferiscono ad una richiesta di carburante per ambito", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}" + ApiUrls.LAVORAZIONI)
	public List<RaggruppamentoLavorazioniDto> getCategorieLavorazioni(@PathVariable(required = true) Long id, AmbitoLavorazione ambito) {
		if (ambito == null) {
			throw new IllegalArgumentException(SPECIFICARE_UN_TIPO_DI_LAVORAZIONE);
		}
		return lavorazioniService.getCategorieLavorazioni(id, ambito);
	}

	@Operation(summary = "Ottiene l'elenco dei fabbisogni espressi per la richiesta di carburante per ogni lavorazione dato un ambito", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}" + ApiUrls.FABBISOGNI)
	public List<DichiarazioneDto> getFabbisogni(@PathVariable(required = true) Long id, LavorazioneFilter.Lavorazioni ambito) {
		if (ambito == null) {
			throw new IllegalArgumentException(SPECIFICARE_UN_TIPO_DI_LAVORAZIONE);
		}
		return lavorazioniService.getFabbisogni(id, ambito);
	}

	@Operation(summary = "Ottiene l'elenco dei fabbisogni espressi riguardanti i fabbricati e serre per la richiesta di carburante per ogni lavorazione dato un ambito", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}" + ApiUrls.FABBISOGNI + "-fabbricati")
	public List<DichiarazioneFabbricatoDto> getFabbisogniFabbricati(@PathVariable(required = true) Long id, LavorazioneFilter.LavorazioniFabbricati ambito) {
		if (ambito == null) {
			throw new IllegalArgumentException(SPECIFICARE_UN_TIPO_DI_LAVORAZIONE);
		}
		return lavorazioniService.getFabbisogniFabbricati(id, ambito);
	}

	@Operation(summary = "Elimina fabbisogni espressi per una richiesta per tipo di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@DeleteMapping("{id}" + ApiUrls.FABBISOGNI)
	public void deleteFabbisogni(@PathVariable(required = true) Long id, @RequestParam Set<TipoCarburante> tipiCarburante) {
		lavorazioniService.deleteFabbisogni(id, tipiCarburante);
	}
}
