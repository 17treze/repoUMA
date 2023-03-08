package it.tndigitale.a4g.uma.api;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
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
import it.tndigitale.a4g.uma.business.service.trasferimenti.TrasferimentiCarburanteService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.AziendaDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.trasferimenti.PresentaTrasferimentoDto;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentiFilter;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentoDto;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.TRASFERIMENTI)
@Tag(name = "TrasferimentiCarburanteController", description = "API che gestisce i trasferimenti di carburante tra aziende")
public class TrasferimentiCarburanteController {

	@Autowired
	private TrasferimentiCarburanteService trasferimentiCarburanteService;

	@Operation(summary = "Recupera i trasferimenti di carburante tra una azienda ed un' altra", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()") 
	@GetMapping
	public CarburanteTotale<TrasferimentoDto> getTrasferimenti(@ParameterObject TrasferimentiFilter trasferimentiFilter) {

		Assert.isTrue(trasferimentiFilter.getCampagna() != null, "E' obbligatorio indicare un anno di campagna");
		Assert.isTrue(trasferimentiFilter.getCuaaDestinatario() != null || trasferimentiFilter.getCuaaMittente() != null, "E' obbligatorio indicare almeno uno tra il mittente e il destinatario");

		return trasferimentiCarburanteService.getTrasferimenti(trasferimentiFilter);
	}

	@Operation(summary = "Recupera un trasferimento di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()") 
	@GetMapping("/{id}")
	public TrasferimentoDto getTrasferimento(@PathVariable(required = true) Long id) {
		return trasferimentiCarburanteService.getTrasferimento(id);
	}

	@Operation(summary = "Elimina un trasferimento di carburante")
	@PreAuthorize("@abilitazioniComponent.checkModificaTrasferimentoCarburante(#id)")
	@DeleteMapping("/{id}")
	public void cancellaTrasferimento(@PathVariable(required = true) Long id) {
		trasferimentiCarburanteService.cancellaTrasferimento(id);
	}

	@Operation(summary = "Effettua un trasferimento di carburante. Restituisce l'identificativo del trasferimento", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()") 
	@PostMapping
	public Long dichiaraTrasferimento(@RequestBody(required = true) PresentaTrasferimentoDto presentaTrasferimentoDto) {
		return trasferimentiCarburanteService.dichiara(presentaTrasferimentoDto);
	}

	@Operation(summary = "Verifica se è possibile effettuare un trasferimento di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()") 
	@PostMapping(ApiUrls.VALIDA)
	public AziendaDto validaTrasferimento(@RequestBody(required = true) PresentaTrasferimentoDto presentaTrasferimentoDto) {
		return trasferimentiCarburanteService.valida(presentaTrasferimentoDto);
	}


	@Operation(summary = "Aggiorna un trasferimento di carburante by id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaTrasferimentoCarburante(#id)") 
	@PutMapping("/{id}")
	public void aggiornaTrasferimento(@PathVariable(required = true) Long id, @RequestBody(required = true) CarburanteDto carburanteTrasferito) {
		trasferimentiCarburanteService.aggiorna(id, carburanteTrasferito);
	}

}
