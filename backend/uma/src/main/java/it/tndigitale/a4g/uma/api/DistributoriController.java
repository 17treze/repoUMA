package it.tndigitale.a4g.uma.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.service.distributori.DistributoriService;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;
import it.tndigitale.a4g.uma.dto.distributori.PresentaPrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelieviFilter;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.DISTRIBUTORI)
@Tag(name = "DistributoriController", description = "API che gestisce le operazioni che competono ai distributori di carburante")
public class DistributoriController {

	@Autowired
	private DistributoriService distributoriService;

	@Operation(summary = "Recupera i distributori dell'utente connesso che hanno effettuato consegne di carburante durante l'anno solare", description = "Vengono recuperati solo i distributori che hanno prelievi non ancora consegnati. Per l'istruttore UMA vengono recuperati tutti")
	@GetMapping
	public List<DistributoreDto> getDistributori(Long campagna) {
		return distributoriService.getDistributori(campagna);
	}

	@Operation(summary = "Recupera i prelievi di carburante associati ad un distributore", description = "Non recupera i prelievi legati ad una richiesta che ha una dichiarazione consumi in stato protocollata")
	@PreAuthorize("@abilitazioniComponent.checkRicercaPrelievoDistributore(#id)")
	@GetMapping("/{id}" + ApiUrls.PRELIEVI)
	public List<PrelievoDto> getPrelievi(@PathVariable Long id ,@ParameterObject PrelieviFilter filter) {
		filter.setId(id);
		return distributoriService.getPrelievi(filter);
	}

	@Operation(summary = "Dichiara un prelievo di carburante. Indicare l'identificativo del distributore", description = "")
	@PreAuthorize("@abilitazioniComponent.checkCreaPrelievoDistributore(#id)")
	@PostMapping("/{id}" + ApiUrls.PRELIEVI)
	public Long postPrelievo(@PathVariable Long id, @RequestBody PresentaPrelievoDto presentaPrelievoDto) {

		if (presentaPrelievoDto.getPrelievo() != null && presentaPrelievoDto.getPrelievo().getData() == null) {
			throw new IllegalArgumentException("La data del prelievo è obbligatoria!");
		}

		return distributoriService.postPrelievi(id, presentaPrelievoDto.getPrelievo(), presentaPrelievoDto.getIdRichiesta());
	}

	@Operation(summary = "Aggiorna un prelievo di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaPrelievoDistributore(#id, #idPrelievo)")
	@PutMapping("/{id}" + ApiUrls.PRELIEVI + "/{idPrelievo}") 
	public Long putPrelievo(@PathVariable Long id, @PathVariable Long idPrelievo, @RequestBody PrelievoDto prelievoDto) {
		if (prelievoDto.getData() == null) {
			throw new IllegalArgumentException("La data del prelievo è obbligatoria!");
		}

		return distributoriService.aggiornaPrelievo(id, prelievoDto, idPrelievo);
	}

	@Operation(summary = "Aggiorna almeno un prelievo di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaPrelieviDistributore(#id, #prelievi)")
	@PutMapping("/{id}" + ApiUrls.PRELIEVI) 
	public void putPrelievi(@PathVariable Long id, @RequestBody List<PrelievoDto> prelievi) {
		distributoriService.aggiornaPrelievi(id, prelievi);
	}

	@Operation(summary = "Elimina un prelievo di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaPrelievoDistributore(#id, #idPrelievo)")
	@DeleteMapping("/{id}" + ApiUrls.PRELIEVI + "/{idPrelievo}")
	public void deletePrelievo(@PathVariable Long id, @PathVariable Long idPrelievo) {
		distributoriService.deletePrelievo(idPrelievo);
	}
}
