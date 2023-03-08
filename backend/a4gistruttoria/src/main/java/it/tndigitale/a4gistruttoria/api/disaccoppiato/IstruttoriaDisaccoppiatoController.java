package it.tndigitale.a4gistruttoria.api.disaccoppiato;

import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.PremioPagamentiStatoIstruttoriaNettoLordoDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.IstruttoriaDisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleIstruttoriaDisaccoppiatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1)
public class IstruttoriaDisaccoppiatoController {
	
	public static final String DATI_DOMANDA = "/datiDomanda";
	
	public static final String VERBALE = "/verbale";

	public static final String DATI_ISTRUTTORE = "/datiIstruttore";
	
	@Autowired
	private IstruttoriaDisaccoppiatoService istruttoriaDettaglioService;
	
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;
	
	@Autowired
	private VerbaleIstruttoriaDisaccoppiatoService verbaleIstruttoriaDisaccoppiatoService;
	
	@GetMapping("/{idIstruttoria}/controlli")
	@ApiOperation("Recupero esiti dei controlli di disaccoppiato dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public List<ControlloFrontend>  getControlliSostegno(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return istruttoriaDettaglioService.getControlliSostegno(idIstruttoria);
	}

	
	@GetMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Recupero i dati dell'istruttore relativi all'istruttoria di disaccoppiato")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public DatiIstruttoria  getDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return datiIstruttoreService.getDatiIstruttoreDisaccoppiato(idIstruttoria);
	}

	
	@PostMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Salvataggio dati istruttore relativi all'istruttoria di disaccoppiato")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria() && @abilitazioniPACComponent.checkEditaIstruttoria() && @permessiModificaDU.checkPermessiIstruttoria(#idIstruttoria)")
	public DatiIstruttoria  saveOrUpdateDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria,  @RequestBody DatiIstruttoria datiIstruttoria) throws Exception {
		return datiIstruttoreService.saveOrUpdateDatiIstruttore(idIstruttoria, datiIstruttoria);
	}	

	
	@PostMapping("/{idIstruttoria}" + DATI_ISTRUTTORE + "/pascoli")
	@ApiOperation("Salvataggio dati istruttore relativi alle informazioni sui pascoli per l'istruttoria di disaccoppiato ")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria() && @abilitazioniPACComponent.checkEditaIstruttoria() && @permessiModificaDU.checkPermessiIstruttoria(#idIstruttoria)")
	public List<DatiIstruttoriaPascoli>  saveOrUpdateDatiIstruttoriaPascoli(@PathVariable(value = "idIstruttoria") Long idIstruttoria,  @RequestBody List<DatiIstruttoriaPascoli> datiIstruttoriaPascoli) throws Exception {
		return datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(idIstruttoria, datiIstruttoriaPascoli);
	}

	
	@GetMapping("/{idIstruttoria}" + DATI_DOMANDA)
	@ApiOperation("Recupero Dati Domanda per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public List<ControlloFrontend>  getDatiDomanda(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return istruttoriaDettaglioService.getDatiDomanda(idIstruttoria);
	}
	
	@GetMapping("/{idIstruttoria}" + VERBALE)
	@ApiOperation("Recupero Dati Domanda per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public byte[] getVerbale(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return verbaleIstruttoriaDisaccoppiatoService.stampa(idIstruttoria);
	}
}
