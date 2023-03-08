package it.tndigitale.a4g.proxy.api;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.dto.zootecnia.AnagraficaAllevamentoDto;
import it.tndigitale.a4g.proxy.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4g.proxy.dto.zootecnia.CapiCarneLattePerInterventoFilter;
import it.tndigitale.a4g.proxy.dto.zootecnia.CapiMacellazionePerInterventoFilter;
import it.tndigitale.a4g.proxy.dto.zootecnia.CapiOvicapriniPerInterventoFilter;
import it.tndigitale.a4g.proxy.dto.zootecnia.ConsistenzaUbaOviniDto;
import it.tndigitale.a4g.proxy.services.AnagrafeZootecnicaAllevamentiService;
import it.tndigitale.a4g.proxy.services.AnagrafeZootecnicaService;
import it.tndigitale.a4g.proxy.ws.bdn.ArrayOfClsCapoMacellato;
import it.tndigitale.a4g.proxy.ws.bdn.ArrayOfClsCapoOvicaprino;
import it.tndigitale.a4g.proxy.ws.bdn.ArrayOfClsCapoVacca;
import it.tndigitale.a4g.proxy.ws.bdn.ClsPremioValidazioneResponse;

@RestController
@RequestMapping(ApiUrls.ZOOTECNIA)
@Api(value = "Servizi di accesso alla banca dati nazionale dei dati Zootecnici (BDN)")
public class AnagrafeZootecnicaController {
	@Autowired
	private AnagrafeZootecnicaService anagrafeServioe;

	@Autowired
	private AnagrafeZootecnicaAllevamentiService anagrafeAllevamentiService;

	private static Logger log = LoggerFactory.getLogger(AnagrafeZootecnicaController.class);


	@ApiOperation("Permette di ottenere la lista di detenzioni di vacche da latte di un certo detentore")
	@GetMapping("/{cuaa}/{campagna}/capi/bovini/lattecarne")
	public ArrayOfClsCapoVacca getBoviniCarneLatteDetenuti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@PathVariable(name = "campagna", required = true) Integer campagna,
			CapiCarneLattePerInterventoFilter capiAziendaFilter) {
		return getCapi(cuaa, campagna, capiAziendaFilter).getListaVacche();
	}

	@ApiOperation("Permette di ottenere la lista di detenzioni di capi macellati di un certo detentore")
	@GetMapping("/{cuaa}/{campagna}/capi/bovini/macellati")
	public ArrayOfClsCapoMacellato getBoviniMacellatiDetenuti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@PathVariable(name = "campagna", required = true) Integer campagna,
			CapiMacellazionePerInterventoFilter capiAziendaFilter) {
		return getCapi(cuaa, campagna, capiAziendaFilter).getListaCapiMacellati();
	}

	@ApiOperation("Permette di ottenere la lista di detenzioni di ovicaprini di un certo detentore")
	@GetMapping("/{cuaa}/{campagna}/capi/ovicaprini")
	public ArrayOfClsCapoOvicaprino getOviCapriniDetenuti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@PathVariable(name = "campagna", required = true) Integer campagna,
			CapiOvicapriniPerInterventoFilter capiAziendaFilter) {
		return getCapi(cuaa, campagna, capiAziendaFilter).getListaCapiOvicaprini();
	}

	private ClsPremioValidazioneResponse getCapi(String cuaa, Integer campagna, CapiAziendaPerInterventoFilter capiAziendaFilter) {
		capiAziendaFilter.setCuaa(cuaa);
		capiAziendaFilter.setCampagna(campagna);
		ClsPremioValidazioneResponse capi = anagrafeServioe.getCapiDetenuti(capiAziendaFilter);

		if (capi != null && (!StringUtils.isEmpty(capi.getErrCod()) || !StringUtils.isEmpty(capi.getErrDescr()))) {
			log.warn("Il servizio di BDN e' andato in errore codice[" + capi.getErrCod() + "]");
			throw new RuntimeException("Il servizio di BDN e' andato in errore codice[" + capi.getErrCod() + "] : " + capi.getErrDescr());
		}
		return capi;
	}

	@ApiOperation("Permette di ottenere la lista di allevamenti di un certo detentore")
	@GetMapping("/{cuaa}/anagrafica-allevamenti")
	public List<AnagraficaAllevamentoDto> getAllevamentiDetenuti(
			@ApiParam(value = "Cuaa del detentore di allevamenti", required = true) @PathVariable(name = "cuaa", required = true) String cuaa,
			//          @ApiParam(value = "data richiesta nel formato dd/MM/yyyy", required = true) @RequestParam(value = "dataRichiesta") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataRichiesta
			@ApiParam(value = "data richiesta nel formato iso 8601 (YYYY-MM-DD o YYYYMMDD)", required = true) @RequestParam(value = "dataRichiesta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRichiesta

			) {
		return anagrafeAllevamentiService.getAllevamenti(cuaa, dataRichiesta);
	}

	@ApiOperation("Interroga il servizio ConsistenzaUBAOvini - tra data inizio e data fine indicati con responsabilità di Detentore")
	@GetMapping("/{cuaa}/consistenza-uba-ovini")
	public List<ConsistenzaUbaOviniDto> getConsistenzaUbaOvini(
			@ApiParam(value = "Cuaa del detentore di allevamenti", required = true) @PathVariable(name = "cuaa", required = true) String cuaa,
			@ApiParam(value = "data richiesta nel formato iso 8601 (YYYY-MM-DD o YYYYMMDD)", required = true) @RequestParam(value = "dataInizio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInizio,
			@ApiParam(value = "data richiesta nel formato iso 8601 (YYYY-MM-DD o YYYYMMDD)", required = true) @RequestParam(value = "dataFine") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFine
			) {

		return anagrafeAllevamentiService.getConsistenzaUbaOvini(cuaa, dataInizio, dataFine, "D");
	}
}
