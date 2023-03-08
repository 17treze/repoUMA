/**
 * 
 */
package it.tndigitale.a4gistruttoria.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
import it.tndigitale.a4gistruttoria.dto.CertificazioneAntimafiaFilter;
import it.tndigitale.a4gistruttoria.dto.CsvFile;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegataFilter;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.dto.EsitiBdna;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.Pagination;
import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.dto.SogliaAcquisizioneFilter;
import it.tndigitale.a4gistruttoria.dto.Sort;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaDto;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafiaConEsiti;
import it.tndigitale.a4gistruttoria.dto.antimafia.SogliaAcquisizione;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.AntimafiaService;
import it.tndigitale.a4gistruttoria.service.ProcessoService;

/**
 * @author S.DeLuca1
 *
 */
@RestController
@RequestMapping(ApiUrls.ANTIMAFIA_V1)
@Api(value = "Gestione istruttoria antimafia")
public class AntimafiaController {

	private static Logger logger = LoggerFactory.getLogger(AntimafiaController.class);

	@Autowired
	private AntimafiaService antimafiaService;
	@Autowired
	private ProcessoService processoService;
	@Autowired
	private ObjectMapper objectMapper;

	@ApiOperation("Avvia il controllo antimafia per gli id passati in input.")
	@PostMapping("/controlla")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public void controlla(@RequestBody() List<Long> ids) throws Exception {
		// Verifichiamo che non sia un processo dello stesso tipo o di tipo importazione domande psr superficie già in corso
		ProcessoFilter processoFilter = new ProcessoFilter();
		processoFilter.setStatoProcesso(StatoProcesso.RUN);
		List<TipoProcesso> tipiProcesso = new ArrayList<TipoProcesso>();
		tipiProcesso.add(TipoProcesso.CONTROLLO_ANTIMAFIA);
		tipiProcesso.add(TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);
		List<Processo> processi = processoService.getProcessi(processoFilter, tipiProcesso);
		if (processi != null && !processi.isEmpty() && processi.get(0).getTipoProcesso() == TipoProcesso.CONTROLLO_ANTIMAFIA) {
			throw new RuntimeException("BRIAMPRT001", new Exception("Impossibile procedere poichè è già in corso un movimento di controllo massivo. Riprovare più tardi."));
		} else if (processi != null && !processi.isEmpty() && processi.get(0).getTipoProcesso() == TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE) {
			// Non c'è una BR specifica
			throw new RuntimeException("IMPPSRSUPERR", new Exception("Impossibile procedere poichè è già in corso un'acquisizione massiva delle domande PSR Superficie. Riprovare più tardi."));
		}
		antimafiaService.avviaControllo(ids);
		logger.info("Avvia Controllo Dichiarazioni partito per i seguenti id :".concat(ids.stream().map(e -> e.toString().concat(" - ")).reduce("", String::concat)));
	}

	@ApiOperation("Restituisce i dati elaborati dal controllo domanda antimafia.")
	@GetMapping("/{id}")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia()")
	public DatiElaborazioneProcesso getElaborazioneControlloAntimafia(@PathVariable Long id) throws Exception {
		return antimafiaService.getElaborazioneControlloAntimafia(id);
	}

	@ApiOperation("Importa i dati PSR strutturali EU.")
	@PutMapping("/importadatisuperficie")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public void importaDatiSuperficie(
			@RequestBody() @ApiParam("Fornire in input la lista di cuaa, l'estremo inferiore per la data di presentazione e l'estremo inferiore per l'importo, in formato JSON") DomandeCollegateImport domandeCollegateImport)
					throws Exception {
		// Verifichiamo che non sia un processo dello stesso tipo o di tipo importazione domande psr superficie già in corso
		ProcessoFilter processoFilter = new ProcessoFilter();
		processoFilter.setStatoProcesso(StatoProcesso.RUN);
		List<TipoProcesso> tipiProcesso = new ArrayList<TipoProcesso>();
		tipiProcesso.add(TipoProcesso.CONTROLLO_ANTIMAFIA);
		tipiProcesso.add(TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);
		List<Processo> processi = processoService.getProcessi(processoFilter, tipiProcesso);
		if (processi != null && !processi.isEmpty() && processi.get(0).getTipoProcesso() == TipoProcesso.CONTROLLO_ANTIMAFIA) {
			throw new RuntimeException("BRIAMPRT001", new Exception("Impossibile procedere poichè è già in corso un movimento di controllo massivo. Riprovare più tardi."));
		} else if (processi != null && !processi.isEmpty() && processi.get(0).getTipoProcesso() == TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE) {
			// Non c'è una BR specifica
			throw new RuntimeException("IMPPSRSUPERR", new Exception("Impossibile procedere poichè è già in corso un'acquisizione massiva delle domande PSR Superficie. Riprovare più tardi."));
		} 
		antimafiaService.importaDatiSuperficie(domandeCollegateImport);
	}

	@ApiOperation("Importa i dati PSR strutturali EU.")
	@PutMapping("/importadatistrutturali")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public List<DomandaCollegata> importaDatiStrutturali(
			@RequestBody() @ApiParam("Fornire in input la lista di cuaa, l'estremo inferiore per la data di presentazione e l'estremo inferiore per l'importo, in formato JSON") DomandeCollegateImport domandeCollegateImport)
					throws Exception {
		return antimafiaService.importaDatiStrutturali(domandeCollegateImport);
	}

	@ApiOperation("Importa i dati domanda unica.")
	@PutMapping("/importadatidu")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public List<DomandaCollegata> importaDatiDU(
			@ApiParam("Fornire in input l'estremo inferiore per la data di presentazione e l'estremo inferiore per l'importo, in formato JSON") 
			@RequestPart(value = "info") String info,
			@ApiParam("Fornire in input il csv formattato così: CUAA;importo richiesto X (formato XXXX,YY); data Y (formato DD/MM/YYYY)") 
			@RequestPart(value = "csv") MultipartFile csv)
					throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(info, DomandeCollegateImport.class);
		return antimafiaService.importaDatiDU(domandeCollegateImport, csv);
	}

	@ApiOperation("Recupero domande in stato CONTROLLATO con relative azioni")
	@GetMapping("/certificazioni")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia()")
	public PageResultWrapper<DichiarazioneAntimafiaConEsiti> getDichiarazioniAntimafiaConEsiti(@ApiParam(value = "Parametri di filtro", required = false) CertificazioneAntimafiaFilter filter, @ApiParam(value = "Parametri di paginazione", required = false) Pagination pagination,@ApiParam(value = "Parametro di ordinamento", required = false) Sort sort) throws Exception {
		return antimafiaService.getDomandeCollegateConEsiti(filter,pagination,sort);
	}

	@ApiOperation("Recupero del dettaglio della domanda collegata")
	@GetMapping("/domandecollegate/{id}")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia()")
	public DomandaCollegata getDettaglioDomandeCollegate(@PathVariable Long id) throws Exception {
		return antimafiaService.getDettaglioDomandeCollegate(id);
	}

	@ApiOperation("Recupera una trasmissione a bdna in base ai parametri")
	@GetMapping("/domandecollegate/trasmissione")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia()")
	public List<TrasmissioneBdnaDto> getTrasmissioni(@ApiParam(value = "Parametri di ricerca - Trasmissione Bdna Filter", required = true) TrasmissioneBdnaFilter trasmissioneBdnaFilter) throws Exception {
		return antimafiaService.getTrasmissioniBdna(trasmissioneBdnaFilter);

	}

	@ApiOperation("Creazione file CSV da esportare - Crea una nuova trasmissione bdna")
	@PostMapping(value = "/domandecollegate/trasmissione", produces = { "text/csv" })
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public ResponseEntity<byte[]> esportaDati(@RequestBody @ApiParam(value = "Parametri identificativo dell'export da eseguire", required = true) DomandeCollegateExport domandeCollegateExport) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();

		try {
			CsvFile csvFile = antimafiaService.creaCsvFile(domandeCollegateExport);
			responseHeaders.set("Content-Disposition", "attachment; filename=".concat(csvFile.getCsvFileName()));
			// https://stackoverflow.com/questions/47160234/angular-4-new-httpclient-content-disposition-header
			// header inserito per poter leggere il nome del file da angular
			responseHeaders.set("Access-Control-Expose-Headers", "Content-Disposition");
			return new ResponseEntity<>(csvFile.getCsvByteArray(), responseHeaders, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
		}
	}

	@ApiOperation("Aggiorna stato trasmissione bdna")
	@PutMapping("/domandecollegate/trasmissione/{id}")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public void aggiornaTrasmissione(@PathVariable @ApiParam(value = "Identificativo trasmissione bdna", required = true) Long id , @RequestBody() @ApiParam("trasmissione da aggiornare") TrasmissioneBdnaDto trasmissioneBdnaDto) throws Exception {
		trasmissioneBdnaDto.setId(id);
		antimafiaService.aggiornaTrasmissioneBdna(trasmissioneBdnaDto);
	}

	@ApiOperation("Cancella trasmissione bdna")
	@DeleteMapping("/domandecollegate/trasmissione/{id}")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public void cancellaTrasmissione(@PathVariable @ApiParam(value = "Identificativo trasmissione bdna", required = true) Long id) throws Exception {
		antimafiaService.cancellaTrasmissioneBdna(id);
	}

	@ApiOperation("Aggiorna la domanda collegata")
	@PutMapping("/domandecollegate/{id}")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public DomandaCollegata aggiornaDomandaCollegata(@PathVariable @ApiParam(value = "Identificativo della domanda collegata", required = true) Long id,
			@RequestBody() @ApiParam(value = "Dati della domanda collegata", required = true) DomandaCollegata domandaCollegata) throws Exception {
		domandaCollegata.setId(id);
		return antimafiaService.aggiornaDomandaCollegate(domandaCollegata);
	}

	@ApiOperation(value = "Restituisce le domande collegate all'istruttoria antimafia che corrispono ai criteri immessi nei parametri di ricerca (oggetto DomandaCollegata in formato JSON)", notes = "tipoDomanda : (PSR_SUPERFICIE_EU | PSR_STRUTTURALI_EU | DOMANDA_UNICA)")
	@GetMapping("/domandecollegate")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia()")
	public List<DomandaCollegata> getDomandeCollegate(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		DomandaCollegataFilter domandaCollegataFilter = objectMapper.readValue(params, DomandaCollegataFilter.class);
		return antimafiaService.getDomandeCollegate(domandaCollegataFilter);
	}

	@ApiOperation(value = "Restituisce lo stato di avanzamento del processo in esecuzione, filtrato per tipo e stato, nonchè la percentuale avanzamento")
	@GetMapping("/statoavanzamento")
	public DatiElaborazioneProcesso getStatoAvanzamento(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		ProcessoFilter processoFilter = objectMapper.readValue(params, ProcessoFilter.class);
		return antimafiaService.getStatoAvanzamento(processoFilter);

	}

	@ApiOperation(value = "Restituisco la soglia di acquisizione in base al tipo di domanda")
	@GetMapping("/soglie")
	public SogliaAcquisizione getSogliaAquisizione(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca", required = true) String params) throws Exception {
		SogliaAcquisizioneFilter sogliaAcquisizioneFilter = objectMapper.readValue(params, SogliaAcquisizioneFilter.class);
		return antimafiaService.getSogliaAcquisizione(sogliaAcquisizioneFilter);
	}

	@ApiOperation("Importa esiti BDNA")
	@PutMapping("/importaesitibdna")
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public EsitiBdna importaEsitiBDNA(
			@ApiParam("Fornire in input il csv formattato così: Data Inserimento; Codice Fiscale; Numero Protocollo; Stato Richiesta; Valore Appalto;Tipo Richiesta;Tipo Motivazione Richiesta; Username; Note Elaborazione") 
			@RequestPart(value = "csv") MultipartFile csv
			) throws Exception {
		return antimafiaService.importaEsitiBDNA(csv);
	}

	@ApiOperation("Creazione file CSV da esportare")
	@PostMapping(value = "/domandecollegate/esportaBDNA", produces = { "text/csv" })
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public ResponseEntity<byte[]> esportaEsitiBDNA() throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			CsvFile csvFile = antimafiaService.getDichiarazioniAntimafiaConDomandeCollegate();
			responseHeaders.set("Content-Disposition", "attachment; filename=".concat(csvFile.getCsvFileName()));
			responseHeaders.set("Access-Control-Expose-Headers", "Content-Disposition");
			return new ResponseEntity<>(csvFile.getCsvByteArray(), responseHeaders, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
		}
	}

	@ApiOperation("Scarica file CSV")
	@GetMapping(value = "/domandecollegate/trasmissione/{id}/esporta", produces = { "text/csv" })
	@PreAuthorize("@abilitazioniAntimafiaComponent.checkVisualizzaIstruttoriaAntimafia() && @abilitazioniAntimafiaComponent.checkEditaIstruttoriaAntimafia()")
	public ResponseEntity<byte[]> scaricaCsv(@PathVariable Long id) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		try {
			CsvFile csvFile = antimafiaService.scaricaCsv(id);
			responseHeaders.set("Content-Disposition", "attachment; filename=".concat(csvFile.getCsvFileName()));
			responseHeaders.set("Access-Control-Expose-Headers", "Content-Disposition");
			return new ResponseEntity<>(csvFile.getCsvByteArray(), responseHeaders, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes());
		}
	}

}
