package it.tndigitale.a4gistruttoria.api.zootecnia;

import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.*;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.dto.zootecnia.EsitoCapiFilter;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleIstruttoriaZootecniaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.IstruttoriaAccoppiatoZootecniaService;
import it.tndigitale.a4gistruttoria.strategy.DatiAZControlliSostegno;
import it.tndigitale.a4gistruttoria.strategy.DatiAZDatiDomanda;
import it.tndigitale.a4gistruttoria.strategy.DatiAZDisciplinaFinanziaria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1)
public class IstruttoriaAccoppiatoZootecniaController {

	public static final String DATI_ISTRUTTORE = "/datiIstruttore";

	@Autowired
	private DatiIstruttoreService datiIstruttoreService;
	@Autowired
	private DatiAZDatiDomanda datiAZDatiDomanda;
	@Autowired
	private DatiAZDisciplinaFinanziaria datiAZDisciplinaFinanziaria;
	@Autowired
	private DatiAZControlliSostegno datiAZControlliSostegno;
	@Autowired
	private VerbaleIstruttoriaZootecniaService verbaleIstruttoriaZootecniaService;
	@Autowired
	private IstruttoriaAccoppiatoZootecniaService istruttoriaAccoppiatoZootecniaService;

	private static Logger logger = LoggerFactory.getLogger(IstruttoriaAccoppiatoZootecniaController.class);
	
	@GetMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Recupero Dati Istruttoria ACC. Zootecnia per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public DatiIstruttoriaACZ getDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria)
			throws Exception {
		return datiIstruttoreService.getDatiIstruttoreZootecnia(idIstruttoria);
	}

	@PostMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Salvataggio Dati Istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria() && @abilitazioniPACComponent.checkEditaIstruttoria() && @permessiModificaDU.checkPermessiIstruttoria(#idIstruttoria)")
	public DatiIstruttoriaACZ saveOrUpdateDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria,
			@RequestBody DatiIstruttoriaACZ datiIstruttoriaAcz) throws Exception {
		return datiIstruttoreService.saveOrUpdateDatiIstruttore(idIstruttoria, datiIstruttoriaAcz);
	}

	@ApiOperation("Recupera i dati di dettaglio degli esiti dei controlli dell'istruttoria accoppiato zootecnia")
	@GetMapping("{idIstruttoria}/esiticalcoli")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public DatiDomandaAccoppiato getEsitiCalcoli(@PathVariable Long idIstruttoria) throws Exception {
		return datiAZDatiDomanda.recupera(idIstruttoria);
	}

	@ApiOperation("Recupera  i dati della disciplina finanziaria dell'istruttoria accoppiato zootecnia")
	@GetMapping("{idIstruttoria}/disciplina")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public Map<String, String> getDisciplina(@PathVariable Long idIstruttoria) throws Exception {
		return datiAZDisciplinaFinanziaria.recupera(idIstruttoria);
	}

	@ApiOperation("Recupera i dati di dettaglio delle anomalie dell'istruttoria accoppiato zootecnia")
	@GetMapping("{idIstruttoria}/esiticontrolli")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public ControlliSostegno getEsitiControlli(@PathVariable Long idIstruttoria) throws Exception {
		return datiAZControlliSostegno.recupera(idIstruttoria);
	}

	@GetMapping("/{idIstruttoria}/verbale")
	@ApiOperation("Recupero Dati Domanda per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public byte[] getVerbale(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return verbaleIstruttoriaZootecniaService.stampa(idIstruttoria);
	}
	
	@GetMapping("/{idIstruttoria}" + "/allevamenti")
	@ApiOperation("Restituisce gli allevamenti impegnati")
	public List<Capi> getAllevamentiImpegnati(@PathVariable Long idIstruttoria) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getAllevamentiImpegnati(idIstruttoria);
	}
	
	@GetMapping("/{idIstruttoria}" + "/allevamenti/richiesti")
	@ApiOperation("Restituisce gli allevamenti impegnati")
	public List<Capi> getAllevamentiRichiesti(@PathVariable Long idIstruttoria) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getAllevamentiImpegnati(idIstruttoria,true);
	}
	
	@GetMapping("/{idIstruttoria}" + "/allevamenti/capi")
	@ApiOperation("Ricerca paginata degli esiti calcolo capi zootecnia")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public RisultatiPaginati<Capo> ricercaEsitiCapi(
			EsitoCapiFilter filter, Paginazione paginazione,
			Ordinamento ordinamento)   {
		return istruttoriaAccoppiatoZootecniaService.ricercaEsitiCapi(
				filter, paginazione,
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}
	
	@PutMapping("/{idIstruttoria}" + ApiUrls.CAPI + "/{idCapoRichiesto}")
	@ApiOperation("Aggiorna dati capo richiesto")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria() && @permessiModificaDU.checkPermessiIstruttoria(#idIstruttoria)")
	public Capo modificaCapo(@PathVariable("idIstruttoria") Long idIstruttoria, @PathVariable("idCapoRichiesto") Long idCapoRichiesto, @RequestBody() Capo capo) throws Exception {
		capo.setId(idCapoRichiesto);
		return istruttoriaAccoppiatoZootecniaService.modificaCapo(capo);
	}
	
	@GetMapping("/{annoCampagna}/allevamenti/capi/impegnati")
    public ResponseEntity<Resource> getCapiImpegnatiPerAGEA(@PathVariable Integer annoCampagna,HttpServletRequest request) throws Exception {

        // create file as Resource
        Resource resource = istruttoriaAccoppiatoZootecniaService.getCapiImpegnatiPerAGEA(annoCampagna);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CAPI_ZOOTECNIA_APPAG.csv")
                .body(resource);
    }
	
	@GetMapping(value = "/{annoCampagna}/etichettaturacarne")
	@ApiOperation("Restituisce le etichettature carni per l'azienda in input")
	public List<EtichettaturaCarne> getEtichettaturaCarne(@PathVariable String annoCampagna, @RequestParam("cuaa") String cuaa) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getEtichettaturaCarne(annoCampagna, cuaa);
	}
	
	@GetMapping(value = "/{annoCampagna}/produzionelatte")
	@ApiOperation("Restituisce i dettagli della produzione del latte di un'azienda")
	public List<ProduzioneLatte> getProduzioneLatte(@PathVariable(value = "annoCampagna") Integer annoCampagna, @RequestParam("cuaa") String cuaa) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getProduzioneLatte(annoCampagna, cuaa);
	}
	
	@GetMapping(value = "/{annoCampagna}/alpeggio")
	@ApiOperation("Restituisce le registrazioni di alpeggio dell'azienda")
	public List<Alpeggio> getRegistrazioniAlpeggio(@PathVariable(value = "annoCampagna") Integer annoCampagna, @RequestParam("cuaa") String cuaa) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getRegistrazioniAlpeggio(annoCampagna, cuaa);
	}
	
	@GetMapping("/{annoCampagna}/allevamenti/capi/interventi/count")
	@ApiOperation("Restituisce il numero di capi impegnati per ogni singolo intervento zootecnico")
	public List<StatisticaZootecnia> getCapiPerIntervento(@PathVariable(value = "annoCampagna") Integer annoCampagna) throws Exception {
		return istruttoriaAccoppiatoZootecniaService.getCapiPerIntervento(annoCampagna);
	}
	
}
