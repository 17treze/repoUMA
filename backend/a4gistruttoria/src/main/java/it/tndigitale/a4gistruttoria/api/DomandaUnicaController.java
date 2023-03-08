package it.tndigitale.a4gistruttoria.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.DomandaUnicaRicercaFilter;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.IstruttoriaFilter;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiDomandaUnica;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiPagamento;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.AnnulloDomandaUnicaService;
import it.tndigitale.a4gistruttoria.service.DomandaUnicaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.DichiarazioniDomandaUnicaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.AvviaElaborazioneAsyncService;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.ElaborazioneAsyncService;

@RestController
@RequestMapping(ApiUrls.DOMANDA_UNICA_V1)
@Api(value = "Gestione Domande Uniche")
public class DomandaUnicaController {

	@Autowired
	private DomandaUnicaService domandaUnicaService;

	@Autowired
	private DichiarazioniDomandaUnicaService dichiarazioniService;
	@Autowired
	private	AnnulloDomandaUnicaService annulloDomandaUnicaService;
	@Autowired
	private ElaborazioneAsyncService elaborazioneAsyncService;
	@Autowired
	private AvviaElaborazioneAsyncService avviaProcessoAsyncService;

	public DomandaUnicaController setComponents(DomandaUnicaService domandaUnicaService) {
		this.domandaUnicaService = domandaUnicaService;
		return this;
	}

	/* 
	 * 03/12/2019
	 * L'implementazione di questo servizio REST è avvenuto per la storia CI-02-01-02-03-01-01
	 * a monte del refactoring dei saldi. Andrà deprecato a valle del refatoring dei saldi.
	 */
	@ApiOperation("Recupera Dettaglio Istruttorie di una Domanda Unica")
	@GetMapping("{idDomanda}/istruttorie")
	public List<Istruttoria> getIstruttorie(@PathVariable(value = "idDomanda") Long idDomanda) throws Exception {
		return domandaUnicaService.getIstruttorie(idDomanda);
	}

	/* 
	 * Recupera delle istruttorie in base all'anno e al cuaa.
	 * Il servizio è stato pensato per essere utilizzato a valle del refactoring dei saldi.
	 */
	@ApiOperation("Recupera Dettaglio Istruttorie di una Domanda Unica")
	@GetMapping("istruttorie")
	@Deprecated
	public List<Istruttoria> getIstruttorie(IstruttoriaFilter istruttoriaFilter) throws Exception {
		return domandaUnicaService.getIstruttorie(istruttoriaFilter);
	}

	@ApiOperation("Mobile: Recupera Dettaglio Istruttorie di una Domanda Unica per numero domanda")
	@GetMapping("{numeroDomanda}/istruttorieByNumeroDomanda")
	public List<Istruttoria> getIstruttorie(@PathVariable(value = "numeroDomanda") BigDecimal numeroDomanda) throws Exception {
		return domandaUnicaService.getIstruttorie(numeroDomanda);
	}

	@ApiOperation("Recupera i dati delle dichiarazioni presenti in domanda")
	@GetMapping("{idDomanda}/dichiarazioni")
	public List<DichiarazioneDu> getDichiarazioniDomandaUnica(@PathVariable(value = "idDomanda") Long idDomanda) throws Exception {
		return dichiarazioniService.getDichiarazioni(idDomanda);
	}

	/* 
	 * L'implementazione di questo servizio REST è avvenuto per la storia CI-02-01-02-00 Consulta DU Sintesi Pagamenti
	 * 
	 */
	@ApiOperation("Recupera Sintesi Pagamenti di una Domanda Unica")
	@GetMapping("{idDomanda}/sintesipagamenti")
	public SintesiPagamento getSintesiPagamenti(@PathVariable(value = "idDomanda") Long idDomanda) throws Exception {
		return domandaUnicaService.getSintesiPagamento(idDomanda);
	}

	@ApiOperation("Recupera Sintesi Pagamenti di una Domanda Unica per numero domanda")
	@GetMapping("{numeroDomanda}/sintesipagamentiByNumeroDomanda")
	public SintesiPagamento getSintesiPagamenti(@PathVariable(value = "numeroDomanda") BigDecimal numeroDomanda) throws Exception {
		return domandaUnicaService.getSintesiPagamento(numeroDomanda);
	}

	@GetMapping("/ricerca")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	@ApiOperation("Ricerca paginata delle domande uniche secondo i criteri di ricerca impostati")
	public RisultatiPaginati<SintesiDomandaUnica> ricerca(
			DomandaUnicaRicercaFilter filter, Paginazione paginazione,
			Ordinamento ordinamento) throws Exception {
		return domandaUnicaService.ricerca(
				filter, paginazione,
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}

	@GetMapping("/count")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	@ApiOperation("Conteggio delle delle domande uniche secondo i criteri di ricerca impostati")
	public Long count(DomandaUnicaRicercaFilter filter) throws Exception {
		return domandaUnicaService.countDomande(filter);
	}

	@GetMapping("/ricevibilita/{campagna}")
	@ApiOperation("Avvia un processo sulle domande uniche presenti in ags")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU()")
	public Long avviaRicevibilitaDomandaUnica(@PathVariable(value = "campagna") Integer campagna) throws Exception {
		ProcessoAnnoCampagnaDomandaDto processoDomandaAnnoCampagnaDto = new ProcessoAnnoCampagnaDomandaDto();
		processoDomandaAnnoCampagnaDto.setCampagna(campagna);
		processoDomandaAnnoCampagnaDto.setTipoProcesso(TipoProcesso.RICEVIBILITA_AGS);
		Long idProcesso = elaborazioneAsyncService.creaProcesso(processoDomandaAnnoCampagnaDto);
		processoDomandaAnnoCampagnaDto.setIdProcesso(idProcesso);
		avviaProcessoAsyncService.avviaProcesso(processoDomandaAnnoCampagnaDto);
		return idProcesso;
	}

	@PostMapping("/{idDomanda}/annulla")
	@ApiOperation("Annulla l'istruttoria della domanda unica")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU() && @permessiModificaDU.checkPermessiDomandaUnica(#id)")
	public void annullaIstruttoriaDomanda(@PathVariable(required = true) Long idDomanda) throws Exception {
		annulloDomandaUnicaService.annullaIstruttoriaDomanda(idDomanda);
	}

	@PutMapping("/{campagna}/istruisci")
	@ApiOperation("Avvia un processo di istruttoria sulle domande uniche presenti in ags")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU()")
	public Long avviaIstruttoriaDomandaUnica(@PathVariable(value = "campagna") Integer campagna) throws Exception {
		ProcessoAnnoCampagnaDomandaDto processoDomandaAnnoCampagnaDto = new ProcessoAnnoCampagnaDomandaDto();
		processoDomandaAnnoCampagnaDto.setCampagna(campagna);
		processoDomandaAnnoCampagnaDto.setTipoProcesso(TipoProcesso.AVVIO_ISTRUTTORIA);
		Long idProcesso = elaborazioneAsyncService.creaProcesso(processoDomandaAnnoCampagnaDto);
		processoDomandaAnnoCampagnaDto.setIdProcesso(idProcesso);
		avviaProcessoAsyncService.avviaProcesso(processoDomandaAnnoCampagnaDto);
		return idProcesso;
	}
}
