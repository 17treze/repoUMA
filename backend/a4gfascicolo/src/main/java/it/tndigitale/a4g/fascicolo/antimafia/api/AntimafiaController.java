package it.tndigitale.a4g.fascicolo.antimafia.api;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AggiornaDichiarazioneEsito;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliareConviventeFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventi;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventiResult;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DatiChiusuraExNovoDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Nota;
import it.tndigitale.a4g.fascicolo.antimafia.dto.NotaFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.PageResultWrapper;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Pagination;
import it.tndigitale.a4g.fascicolo.antimafia.dto.ProcedimentiEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Sort;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneCounter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.ioitalia.IoItaliaSenderService;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaService;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@RestController
@RequestMapping(ApiUrls.ANTIMAFIA_V1)
@Api(value = "Gestione dichiarazione antimafia")
public class AntimafiaController {

	@Autowired
	private AntimafiaService serviceAntimafia;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UtenteComponent utenteComponent;

	private static String CSV_DICHIARAZIONI_ANTIMAFIA_FILENAME = "export-dichiarazioni-antimafia.csv";

	@ApiOperation("Restituisce la dichiarazione antimafia per id")
	@GetMapping("/{id}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public Dichiarazione getDichiarazione(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id) throws Exception {
		return serviceAntimafia.getDichiarazione(id);
	}


	@ApiOperation("Restituisce la dichiarazione antimafia per cuaa")
	@GetMapping("/domanda/{cuaa}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public Dichiarazione getDichiarazione(@PathVariable @ApiParam(value = "Cuaa dell'azienda", required = true) String cuaa) throws Exception {
		return serviceAntimafia.getDichiarazioneByCuaa(cuaa);
	}

	@ApiOperation("Eliminazione della dichiarazione antimafia in stato BOZZA e dei suoi figli per id. ")
	@DeleteMapping("/{id}")
	@PreAuthorize("@abilitazioniComponent.checkCancellaAntimafia()")
	public Long eliminaDichiarazione(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id) throws Exception {
		return serviceAntimafia.eliminaDichiarazione(id);
	}

	@ApiOperation("Crea la dichiarazione antimafia")
	@PostMapping
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(#dichiarazione, null)")
	public Long creaDichiarazione(@RequestBody() Dichiarazione dichiarazione) throws Exception {
		return serviceAntimafia.creaDichiarazione(dichiarazione);
	}

	@ApiOperation("Aggiorna la dichiarazione antimafia e gli eventuali allegati")
	@PutMapping("/{id}")
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(#dichiarazione, #id)")
	public AggiornaDichiarazioneEsito aggiornaDichiarazione(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id,
			@RequestBody() @ApiParam(value = "Dati della dichiarazione", required = true) Dichiarazione dichiarazione) throws Exception {
		dichiarazione.setId(id);
		return serviceAntimafia.aggiornaDichiarazione(dichiarazione);
	}

	@ApiOperation("Restituisce le dichiarazioni antimafia che corrispono ai criteri immessi nei parametri (oggetto Dichiarazione in formato JSON)")
	@GetMapping
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	@Deprecated
	public List<Dichiarazione> getDichiarazioni(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		DichiarazioneFilter dichiarazioneInput = objectMapper.readValue(params, DichiarazioneFilter.class);
		return serviceAntimafia.getDichiarazioni(dichiarazioneInput);
	}

	@ApiOperation("Export CSV delle dichiarazioni antimafia che corrispono ai criteri immessi nei parametri (oggetto Dichiarazione in formato JSON)")
	@GetMapping("/csv")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public byte[] getCsvDichiarazioniAntimafia(
			HttpServletResponse response,
			@RequestParam(value = "params")
			@ApiParam(value = "Parametri di ricerca", required = true)
			String params) throws Exception {
		DichiarazioneFilter dichiarazioneInput = objectMapper.readValue(params, DichiarazioneFilter.class);
		List<Dichiarazione> dichiarazioni = serviceAntimafia.getDichiarazioni(dichiarazioneInput);
		byte[] csvBytes = serviceAntimafia.creazioneCsv(dichiarazioni);
		response.addHeader(
				"Content-Disposition",
				String.format("attachment; filename=%s", CSV_DICHIARAZIONI_ANTIMAFIA_FILENAME));
		response.addHeader("Access-Control-Expose-Headers",
				"Content-Disposition");
		return csvBytes;
	}

	@ApiOperation("Restituisce le dichiarazioni antimafia che corrispono ai criteri immessi nei parametri (oggetto Dichiarazione in formato JSON)- versione Paginata")
	@GetMapping("/page")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public PageResultWrapper<Dichiarazione> getDichiarazioniPaginata(@ApiParam(value = "Parametri di ricerca", required = false) DichiarazionePaginataFilter dichiarazioneInput, @ApiParam(value = "Parametri di paginazione", required = false) Pagination pagination, @ApiParam(value = "Parametri di ordinamento", required = false) Sort sort) throws Exception {
		return serviceAntimafia.getDichiarazioniPaginata(dichiarazioneInput, pagination, sort);
	}

	@PutMapping(value = "/{id}/allegatoFamiliariConviventi")
	@ApiOperation("Effettua il caricamento del modello precompilato, nel formato pdf, per la \"Dichiarazione Sostitutiva di Certificazione Familiari Conviventi\" ")
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(null, #id)")
	public AllegatoFamiliariConviventiResult allegaFamiliariConviventi(
			@ApiParam(value = "Metadati del documento")
			@RequestPart(value = "datiFamiliareConvivente") String datiFamiliareConvivente,
			@ApiParam(value = "Modello precompilato sui famigliari conviventi")
			@RequestPart(value = "documento") MultipartFile documento, 
			@ApiParam(value = "Identificativo della dichiarazione antimafia")
			@PathVariable Long id) throws Exception {
		AllegatoFamiliariConviventi allegatoFamiliariConviventi = objectMapper.readValue(datiFamiliareConvivente, AllegatoFamiliariConviventi.class);
		return serviceAntimafia.allegaFamiliariConviventi(allegatoFamiliariConviventi, documento, id);
	}

	@PutMapping(value = "/{id}/protocolla")
	@ApiOperation("Protocolla i documenti della dichiarazione antimafia")
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(null, #id)")
	public Dichiarazione protocollaDomanda(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id) throws Exception {
		return serviceAntimafia.processoProtocollazioneDomanda(id);
	}

	@GetMapping("/count")
	@ApiOperation("restituisce il count delle domande antimafia divise per stati")
	public StatoDichiarazioneCounter count(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		StatoDichiarazioneFilter stati = objectMapper.readValue(params, StatoDichiarazioneFilter.class);
		return serviceAntimafia.countEsitoDichiarazioni(stati);
	}

	@GetMapping("/{id}/allegatoFamiliariConviventi/{idAllegato}")
	@ApiOperation("Restituisce il file allegato dall'utente per il familiare convivente")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public byte[] downloadAllegatoFamiliareConvivente(@PathVariable("id") Long id, @PathVariable("idAllegato") Long idAllegato) {
		AllegatoFamiliareConviventeFilter filter = new AllegatoFamiliareConviventeFilter();
		filter.setIdAllegatoFamiliariConviventi(idAllegato);
		filter.setIdDichiarazioneAntimafia(id);
		return serviceAntimafia.downloadAllegatoFamiliareConvivente(filter);
	}

	@ApiOperation(value = "Crea la nota per la dichiarazione antimafia.", notes = "tipoNota : (RIFIUTO_DICHIARAZIONE_ANTIMAFIA)")
	@PutMapping("/{id}/note")
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(null, #id)")
	public Long creaNota(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id,
			@ApiParam(value = "Dettagli sulla nota", required = true) @RequestBody() Nota nota) throws Exception {
		nota.setChiaveEsterna(id.toString());
		nota.setUtente(utenteComponent.utenza());
		return serviceAntimafia.creaNota(nota);
	}

	@ApiOperation(value = "Legge le note della dichiarazione antimafia.", notes = "tipoNota : (RIFIUTO_DICHIARAZIONE_ANTIMAFIA)")
	@GetMapping("/{id}/note")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public List<Nota> leggiNote(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id,
			@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		NotaFilter notaFilter = objectMapper.readValue(params, NotaFilter.class);
		notaFilter.setChiaveEsterna(id.toString());
		return serviceAntimafia.leggiNote(notaFilter);
	} 


	@ApiOperation("Inserisce i PROCEDIMENTI per la dichiarazione antimafia")
	@PostMapping("/{idDichiarazione}/procedimenti")
	@PreAuthorize("@abilitazioniComponent.checkEditaAntimafia(null, #idDichiarazione)")
	public List<ProcedimentiEnum> inserisciProcedimenti(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long idDichiarazione, @RequestBody() List<ProcedimentiEnum> procedimenti) throws Exception {
		return serviceAntimafia.creaProcedimenti(idDichiarazione,procedimenti);
	}	

	@ApiOperation("Recupera i PROCEDIMENTI per la dichiarazione antimafia")
	@GetMapping("/{idDichiarazione}/procedimenti")
	@PreAuthorize("@abilitazioniComponent.checkLetturaAntimafia()")
	public List<ProcedimentiEnum> recuperaProcedimenti(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long idDichiarazione) throws Exception {
		return serviceAntimafia.recuperaProcedimenti(idDichiarazione);
	}
	
	@ApiOperation("Chiusra e creazione nuova dichiarazione antimafia")
	@PostMapping("/{id}/chiudiRicreaDichiarazione")
	//@PreAuthorize("@abilitazioniComponent.checkChiusuraCreaAntimafia(#dichiarazione, #id)")
	public AggiornaDichiarazioneEsito chiudiAndRicreaDichiarazione(@PathVariable Long id,
			@RequestBody() DatiChiusuraExNovoDichiarazioneAntimafia datiDichiarazioneChiusuraExNovo) throws Exception {
		datiDichiarazioneChiusuraExNovo.getDaChiudere().setId(id);		
		return serviceAntimafia.chiudiRecreaDichiarazione(datiDichiarazioneChiusuraExNovo.getDaChiudere(),datiDichiarazioneChiusuraExNovo.getExNovo());
	}
}
