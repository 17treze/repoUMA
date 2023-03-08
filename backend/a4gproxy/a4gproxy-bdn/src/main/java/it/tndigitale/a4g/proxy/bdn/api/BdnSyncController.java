package it.tndigitale.a4g.proxy.bdn.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.bdn.client.AnagraficaCapoClient;
import it.tndigitale.a4g.proxy.bdn.client.RegistroStallaClient;
import it.tndigitale.a4g.proxy.bdn.config.BdnConstants;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;
import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;
import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.ConsistenzaPascolo2015Dto;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.MovimentazionePascoloOviniDto;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAlPascoloPAC2015DAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAllevamentoDAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.MovimentazionePascoloDAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;
import it.tndigitale.a4g.proxy.bdn.service.BDNSyncService;
import it.tndigitale.a4g.proxy.bdn.service.manager.ConsistenzaAlPascoloPAC2015ManagerImpl;
import it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo.GetCapoEquinoResponse.GetCapoEquinoResult;
import it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla.ElencoEquiRegistriStallaResponse.ElencoEquiRegistriStallaResult;

@RestController
@RequestMapping("api/v1/sync")
@Api(value = "Servizio per la sincronizzazione con BDN")
public class BdnSyncController {

	private Logger logger = LoggerFactory.getLogger(BdnSyncController.class);

	@Autowired
	StatoSincronizzazioneBdnDAO statoSincronizzazioneDao;
	@Autowired
	ConsistenzaAlPascoloPAC2015DAO consistenzaPascoloDao;
	@Autowired
	MovimentazionePascoloDAO movimentazionePascoloDao;
	@Autowired
	BDNSyncService syncService;
	@Autowired
	ConsistenzaAllevamentoDAO consistenzaAllevamentoDao;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RegistroStallaClient registroStallaClient;
	@Autowired
	private AnagraficaCapoClient anagraficaCapoClient;
	
	@Autowired
	private ConsistenzaAlPascoloPAC2015ManagerImpl consistenzaAlPascoloPAC2015ManagerImpl;

	@PutMapping("")
	@ApiOperation("Richiede l'aggiornamento puntuale dei dati da BDN per anno campagna e/o cuaa")
	public Boolean aggiornaDatiBdnPerCuaa(@RequestBody() @ApiParam("Parametri per selezionare i dati da sincronizzare, annoCampagna e cuaa obbligatori") StatoSincronizzazioneDO statoSincronizzazione) {

		boolean esitoElaborazione = true;
		if (statoSincronizzazione.getCuaa() != null) {
			esitoElaborazione = syncService.syncDatiDomandaUnica(statoSincronizzazione.getCuaa(), statoSincronizzazione.getAnnoCampagna());
		} else {
			syncService.syncDatiDomandaUnicaPerAnno(statoSincronizzazione.getAnnoCampagna());
		}
		String stato = BdnConstants.SYNC_OK.getBdnConstants();

		if (!esitoElaborazione) {
			stato = BdnConstants.SYNC_KO.getBdnConstants();
		}
		statoSincronizzazioneDao.aggiornaStatoSincronizzazione(statoSincronizzazione.getAnnoCampagna(), statoSincronizzazione.getCuaa(), stato);
		return esitoElaborazione;

	}

	@PostMapping("")
	@ApiOperation("Richiede l'aggiornamento totale dei dati da BDN per anno campagna")
	public void aggiornaDatiBdnPerAnnoCampagna(@RequestBody() @ApiParam("Parametri per selezionare i dati da sincronizzare, annoCampagna obbligatoro") StatoSincronizzazioneDO statoSincronizzazione) throws Exception {
		Integer annoCampagna = statoSincronizzazione.getAnnoCampagna();
		logger.debug("aggiornaDatiBdnPerAnnoCampagna: inizio per anno {}", annoCampagna);
		syncService.sincronizzaCacheDatiDomandaUnicaPerAnno(annoCampagna);
	}

	@ApiOperation("Estrai i dati di consistenza pascolo per anno e codice")
	@GetMapping("/consistenzaPascolo")
	public List<ConsistenzaPascolo2015Dto> getConsistenzaPascolo(
			@ApiParam(value = "Parametri di ricerca in formato json: previsti annoCampagna e codPascolo (entrambi required)", required = true) @RequestParam(value = "params") String parametri)
					throws IOException {

		JsonNode params = objectMapper.readTree(parametri);
		BigDecimal annoCampagna = params.path("annoCampagna").decimalValue();
		String codPascolo = params.path("codPascolo").textValue();

		return consistenzaPascoloDao.getConsistenzaPascoloPerIstruttoria(annoCampagna, codPascolo);

	}

	@ApiOperation("Estrai i dati di movimentazione pascolo ovini per anno e codice")
	@GetMapping("/movimentiPascolo")
	public List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOvini(
			@ApiParam(value = "Parametri di ricerca in formato json: previsti annoCampagna e codPascolo (entrambi required)", required = true) @RequestParam(value = "params") String parametri)
					throws IOException {

		JsonNode params = objectMapper.readTree(parametri);
		BigDecimal annoCampagna = params.path("annoCampagna").decimalValue();
		String codPascolo = params.path("codPascolo").textValue();
		String codiceFiscale = params.path("codiceFiscale").textValue();
		if (codiceFiscale != null && !codiceFiscale.equals("")) {
			return movimentazionePascoloDao.getMovimentazionePascoloOviniPerIstruttoria(annoCampagna, codPascolo, codiceFiscale);
		} else {
			return movimentazionePascoloDao.getMovimentazionePascoloOviniPerIstruttoriaPerCodPascolo(annoCampagna, codPascolo);
		}
	}

	@ApiOperation("Estrai i dati di consistenza allevamento per anno e cuaa")
	@GetMapping("/consistenzaAllevamento")
	public List<ConsistenzaAllevamentoDO> getConsAllevamento(
			@ApiParam(value = "Parametri di ricerca in formato json: previsti annoCampagna e codiceFiscale (entrambi required)", required = true) @RequestParam(value = "params") String parametri)
					throws IOException {

		JsonNode params = objectMapper.readTree(parametri);
		String codFiscale = params.path("codiceFiscale").textValue();
		BigDecimal annoCampagna = params.path("annoCampagna").decimalValue();

		return consistenzaAllevamentoDao.getConsAllevamento(codFiscale, annoCampagna);

	}

	@ApiOperation("Richiede l'aggiornamento della cache dei dati dell'azienda da bdn")
	@PutMapping("/azienda/{codiceAzienda}")
	public void sincronizzaAzienda(@PathVariable @ApiParam(value = "Codice dell'azienda", required = true) String codiceAzienda) throws Exception {
		syncService.sincronizzaAzienda(codiceAzienda);
	}

	@ApiOperation("Metodo di test per il recupero dei registri stalla degli equidi")
	@GetMapping("/test/getEguiRegistriStalla")
	public ElencoEquiRegistriStallaResult getEguiRegistriStalla(@RequestParam String codiceAllevamento)
			throws Exception {
		return registroStallaClient.getElencoEquiRegistroStalla(codiceAllevamento, "storico");

	}

	@ApiOperation("Metodo di test per il recupero dei registri stalla degli equidi")
	@GetMapping("/test/getCapoEquino")
	public GetCapoEquinoResult getCapoEquino(@RequestParam String pCodiceElettronico, @RequestParam String pPassaporto, @RequestParam String pCodiceUeln)
			throws Exception {
		return anagraficaCapoClient.getCapoEquino(pCodiceElettronico, pPassaporto, pCodiceUeln);

	}
	
	@ApiOperation("Metodo di test per il salvataggio delle consistenze al pascolo")
	@GetMapping("/test/pascolo")
	public boolean consistenzaAlPascolo(@RequestParam String cuaa, @RequestParam Integer anno)
			throws Exception {
		logger.debug("consistenzaAlPascolo Controller: inizio per cuaa {} anno: {} ", cuaa, anno);

		return syncService.syncDatiDomandaUnica(cuaa, anno);

	}
	
	@ApiOperation("Metodo di test per il salvataggio delle consistenze al pascolo")
	@GetMapping("/test/insert-pascolo")
	public ErroreDO consistenzaAlPascoloInsert(@RequestParam String codicePascolo, @RequestParam Integer anno)
			throws Exception {
		logger.debug("consistenzaAlPascolo Controller: inizio per codice pascolo {} anno: {} ", codicePascolo, anno);

		return consistenzaAlPascoloPAC2015ManagerImpl.inserimentoConsistenzaAlPascoloPAC2015(codicePascolo, anno);

	}
}
