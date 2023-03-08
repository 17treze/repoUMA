package it.tndigitale.a4g.srt.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import it.tndigitale.a4g.srt.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.srt.services.DomandeServiceImpl;
import org.springframework.web.server.ResponseStatusException;

import static it.tndigitale.a4g.srt.services.DomandeServiceImpl.TipologiaDatiPagamento.*;

@RestController
@RequestMapping("/api/v1/domande")
@Api(value = "DomandeController")
public class DomandeController {
	
	private static final Logger logger = LoggerFactory.getLogger(DomandeController.class);
	
	@Autowired
	private DomandeServiceImpl domandeService;
	
	@ApiOperation("Restituisce le domande per un bando PSR da parte di una azienda")
	@GetMapping(path = "domanda/{cuaa}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSR(#cuaa)")
	public List<PsrBandoDto> getPsrBandiImpresa(
			@ApiParam(value = "Cuaa impresa", required = true) @PathVariable(value = "cuaa") String cuaa) {
		return domandeService.getPsrBandiImpresaByCuaa(cuaa);
	}

	@ApiOperation("Restituisce la sintesi degli investimenti/contributi approvati per una domanda di bando PSR")
	@GetMapping(path = "progetto/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrProgettoCostiInvestimentiDto> getInvestimentiContributiProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) {
		return domandeService.getPsrInvestimentiContributiByIdProgetto(idProgetto);
	}

	@ApiOperation("Restituisce la lista ordinata per stati del progetto con quell'id ")
	@GetMapping(path = "progetto/{idProgetto}/storico", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List getStoricoStatiProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) {
		return domandeService.getPsrStoricoStati(idProgetto);
	}
	
	@ApiOperation("Restituisce la descrizione dell'impresa richiedente dato l'ID Progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-impresa/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrProgettoImpresaDescrizioneDto> getDescrizioneProgettoImpresa(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) {
		return domandeService.getPsrDescrizioneImpresaByIdProgetto(idProgetto);
	}
	
	@ApiOperation("Riepilogo totale costi e contributi per id progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-costi-contributi/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrCostiContributiDto> getRiepilogoCostiContributi(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) {
		return domandeService.getRiepilogoCostiContributi(idProgetto);
	}
	
	@ApiOperation("Riepilogo totale costi e contributi per id progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-dettaglio-domande/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public PsrDomandeDto getDettaglioDomandeByIdProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) throws Exception {
		return domandeService.getDettaglioDomandeByIdProgetto(idProgetto);
	}
	
	@ApiOperation("Restituisce la descrizione dei costi per id progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-descrizione-costi/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrProgettoDescrizioneDto> getDescrizioneDomandaByProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) throws Exception {
		return domandeService.getDescrizioneDomandaByProgetto(idProgetto);
	}

	@ApiOperation("Restituisce il dettaglio della finanziabilita di un'istruttoria per id progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-dettaglio/{idProgetto}/{tipologia}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrInterventoDettaglioDto> getDettaglioIstruttoriaByProgettoAndTipo(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto,
			@ApiParam(value = "Tipologia del dettaglio (SALDO,ACCONTO,FINANZIABILITA)", required = true) @PathVariable(value = "tipologia")
					DomandeServiceImpl.TipologiaDatiPagamento tipologia,
			@ApiParam(value = "ID domanda progetto (required if tipologia in (ACCONTO)", required = false) @RequestParam(value = "idDomandaPagamento", required = false) Integer idDomandaPagamento
																				   ) throws NoSuchFieldException {
		if (tipologia == ACCONTO && idDomandaPagamento == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idDomandaPagamento required when requesting tipologia 'ACCONTO'");
		}

		return domandeService.getDettaglioByProgettoAndTipo(idProgetto, tipologia, idDomandaPagamento);
	}

	@ApiOperation("Restituisce le fatture di un intervento")
	@GetMapping(path = "progetto-dettaglio/{idProgetto}/{tipologia}/intervento/{idIntervento}/fatture", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrFatturaDto> getFattureByInterventoAndProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto,
			@ApiParam(value = "Tipologia del dettaglio (SALDO,ACCONTO)", required = true) @PathVariable(value = "tipologia")
					DomandeServiceImpl.TipologiaDatiPagamento tipologia,
			@ApiParam(value = "ID intervento", required = true) @PathVariable(value = "idIntervento") Integer idIntervento,
			@ApiParam(value = "ID domanda progetto (required if tipologia in (ACCONTO)", required = false) @RequestParam(value = "idDomandaPagamento", required = false) Integer idDomandaPagamento
	) {
		if (tipologia == ACCONTO && idDomandaPagamento == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idDomandaPagamento required when requesting tipologia 'ACCONTO'");
		}

		return domandeService.getFattureByIdProgettoAndIntervento(idProgetto, tipologia, idIntervento, idDomandaPagamento);
	}

	@ApiOperation("Restituisce le varianti per un id progetto (numero domanda PSR)")
	@GetMapping(path = "progetto-dettaglio-domande-varianti/{idProgetto}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public List<PsrVarianteDto> getVariantiByIdProgetto(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto) throws Exception {
		return domandeService.getVariantiByProgetto(idProgetto);
	}

	@ApiOperation("Restituisce i totali di quella specifica variante")
	@GetMapping(path = "progetto-dettaglio-domande-varianti/{idProgetto}/{idVariante}/totali", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaByIdProgetto(#idProgetto)")
	public Map<String, BigDecimal> getTotaliVariante(
			@ApiParam(value = "ID progetto/numero domanda ", required = true) @PathVariable(value = "idProgetto") Integer idProgetto,
			@ApiParam(value = "ID variante ", required = true) @PathVariable(value = "idVariante") Integer idVariante
	) {
		return domandeService.getTotaliVariante(idProgetto, idVariante);
	}

	@ApiOperation("Restituisce la sintesi investimenti dell'ultima Variante approvata ")
	@GetMapping(path = "progetto-dettaglio-domande-varianti/{idVariante}/investimenti", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PsrInvestimentoDto> getSintesiInvestimentiByIdVariante(
			@ApiParam(value = "ID Variante ", required = true) @PathVariable(value = "idVariante") Integer idVariante) throws Exception
		{
			return domandeService.getInvestimentiByIdVariante(idVariante);

	}

}
