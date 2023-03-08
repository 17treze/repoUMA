package it.tndigitale.a4g.richiestamodificasuolo.api;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoJobFME;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.TempClipSuADLDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RicercaLavorazioniSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodificheSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloNonAssociabileLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.TempClipSuADLDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneErrorDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneInCorsoDto;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiUrls.LAVORAZIONE_SUOLO)
@Api(value = "Controller per il recupero delle informazioni delle lavorazioni suolo")
public class LavorazioneSuoloController {

	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;

	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private TempClipSuADLDao tempClipSuADLDao;

	@Autowired
	private Clock clock;
	/*
	 * Crud Lavorazione
	 */

	// GET
	@GetMapping("/{idLavorazione}")
	@ApiOperation("Ricerca lavorazione suolo per l'idLavorazione passato solo se la lavorazione è associata all'utente")
	@PreAuthorize("@abilitazioniComponent.checkAccessLavorazioniSuolo(#idLavorazione)")
	public LavorazioneSuoloDto getLavorazione(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione) {
		String utente = utenteComponent.username();
		return lavorazioneSuoloService.ricercaLavorazioneUtente(idLavorazione, utente);

	}

	// POST
	@PostMapping
	@ApiOperation("Inserisce una nuova lavorazione suolo")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<String> createLavorazioneSuolo(@RequestBody String json) {
		JSONObject jsonObj = new JSONObject(json);
		Integer annoCampagna = jsonObj.getInt("annoCampagna");
		LavorazioneSuoloModel newLavorazione = lavorazioneSuoloService.createLavorazioneSuolo(annoCampagna);
		return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(newLavorazione.getId()));
	}

	// DELETE
	@DeleteMapping("/{idLavorazione}")
	@ApiOperation("Elimina una lavorazione suolo")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> deleteLavorazioneSuolo(@PathVariable @ApiParam(value = "Identificativo della LavorazioneSuolo", required = true) Long idLavorazione) {
		lavorazioneSuoloService.deleteLavorazioneSuolo(idLavorazione);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// PUT
	@PutMapping("/{idLavorazione}")
	@ApiOperation("Aggiorna lavorazione suolo ")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> updateLavorazioneSuolo(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione,
			@RequestBody LavorazioneSuoloDto lavorazioneSuoloDto) {
		String utente = utenteComponent.username();
		lavorazioneSuoloDto.setId(idLavorazione);
		lavorazioneSuoloService.updateLavorazioneSuolo(lavorazioneSuoloDto, utente);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// COPIA LAVORAZIONE
	@PostMapping("/copiaLavorazione")
	@ApiOperation("Copia lavorazione suolo ")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> copyLavorazioneSuolo(@RequestBody String json) {
		JSONObject jsonObj = new JSONObject(json);
		Integer campagna = jsonObj.getInt("campagna");
		Long idLavorazione = jsonObj.getLong("idLavorazione");
		String utente = utenteComponent.username();

		LavorazioneSuoloModel newLavorazione = lavorazioneSuoloService.copyLavorazioneSuolo(idLavorazione, campagna, utente);

		Long idNuovaLavorazione = newLavorazione.getId();
		// LavorazioneSuoloModel lavorazione = lavorazioneSuoloService.ricercaLavorazioneUtenteModel(idNuovaLavorazione, utente);

		return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(newLavorazione.getId()));

	}

	@GetMapping("/nonConcluse")
	@ApiOperation("Ricerca lavorazioni suolo associate all'utente")
	// FILTRO IMPLICITO SULL'UTENTE, NON NECESSARIA PREAUTHORIZE
	public RisultatiPaginati<LavorazioneSuoloDto> ricercaLavorazioniNonConcluse(Paginazione paginazione, Ordinamento ordinamento) {
		// successivamente è previsto che l'utente diventi un parametro perchè qualsiasi utente potrà ricercare lavorazioni di qualsiasi utente
		String currentUser = utenteComponent.username();
		return lavorazioneSuoloService.ricercaLavorazioniNonConcluseNonConsolidateAgs(currentUser, paginazione,
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}

	// SUOLO VIGENTE
	@GetMapping("/{idLavorazione}/suolo")
	@ApiOperation("Ricerca uso suolo per l'idLavorazione passato solo se la lavorazione è associata all'utente")
	@PreAuthorize("@abilitazioniComponent.checkAccessLavorazioniSuolo(#idLavorazione)")
	public RisultatiPaginati<SuoloDto> getSuolo(@PathVariable @ApiParam(value = "Identificativo della lavorazione in corso", required = true) Long idLavorazione, Paginazione paginazione,
			Ordinamento ordinamento) {
		return lavorazioneSuoloService.getSuolo(idLavorazione, paginazione, Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(new Ordinamento("id", Ordine.DESC)));
	}

	// PUT SUOLO VIGENTE
	@PutMapping("/{idPoligono}/rimuoviAssociazionePoligono")
	@ApiOperation("Rimuove l'associazione di un poligono suolo vigente dalla lavorazione.")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> removeUsoSuoloDaLavorazione(@PathVariable @ApiParam(value = "Identificativo del poligono", required = true) Long idPoligono, @RequestBody SuoloDto usoSuoloDto) {
		lavorazioneSuoloService.removePoligonoDaLavorazione(usoSuoloDto.getIdLavorazioneInCorso().getId(), idPoligono);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// POST SUOLO VIGENTE
	@PostMapping("/{idLavorazione}/associaSuoloDaDichiarato")
	@ApiOperation("Associa i poligoni di suolo vigente alla lavorazione intersecando i poligoni di dichiarato già associati alla lavorazione")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<List<SuoloNonAssociabileLavorazioneDto>> associaSuoloDaDichiarato(@PathVariable("idLavorazione") Long idLavorazione,
			@RequestBody AssociazioneSuoloDaDichiaratoCommand command) {
		List<SuoloNonAssociabileLavorazioneDto> suoliNonAssociati = lavorazioneSuoloService.associazionePoligoniLavorazioneDaDichiarato(idLavorazione, command.getVersione(), utenteComponent.utenza());
		return ResponseEntity.ok(suoliNonAssociati);
	}

	@GetMapping("/{idLavorazione}/validate")
	@ApiOperation("Valida se la lavorazione è corretta")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	// non applico filtri sull'utente perchè attualmente non necessari
	public ValidazioneLavorazioneErrorDto validaLavorazione(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione) {
		return lavorazioneSuoloService.validaLavorazione(idLavorazione);
	}

	/*
	 * Api Workspace
	 */
	@PostMapping("/{idLavorazione}/workspace")
	@ApiOperation("Inserisce i poligoni di suolo vigenti nel proprio workspace")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> insertLavorazioneInWorkspace(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione,
			@RequestParam(required = false) @ApiParam(value = "Verifica intersezione pascoli") boolean verificaPascoli) {

		return lavorazioneSuoloService.insertOrUpdateOrRiprendiLavorazioneInWorkspace(idLavorazione, utenteComponent.username(), verificaPascoli, false, false, null);
	}

	@PutMapping("/{idLavorazione}/workspace")
	@ApiOperation("Aggiorna i poligoni editati nel proprio workspace")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> updateLavorazioneInWorkspace(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione,
			@RequestParam(required = false) @ApiParam(value = "Verifica intersezione pascoli") boolean verificaPascoli, @RequestBody String geoJson) {

		return lavorazioneSuoloService.insertOrUpdateOrRiprendiLavorazioneInWorkspace(idLavorazione, utenteComponent.username(), verificaPascoli, true, false, geoJson);
	}

	@PutMapping("/{idLavorazione}/ADL")
	@ApiOperation("Aggiorna i poligoni dell'area di lavoro")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> updateADL(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione, @RequestBody String geoJson) {
		return lavorazioneSuoloService.insertOrUpdateOrADL(idLavorazione, utenteComponent.username(), geoJson);
	}

	// TempClipDaADL
	@GetMapping("/{idLavorazione}/clipDaADL")
	@ApiOperation("Ricerca clip da ADL per l'idLavorazione passato solo se la lavorazione è associata all'utente")
	@PreAuthorize("@abilitazioniComponent.checkAccessLavorazioniSuolo(#idLavorazione)")
	public RisultatiPaginati<TempClipSuADLDto> getTempClipSuADL(@PathVariable @ApiParam(value = "Identificativo della lavorazione in corso", required = true) Long idLavorazione,
			Paginazione paginazione, Ordinamento ordinamento) {
		return lavorazioneSuoloService.getTempClipSuADL(idLavorazione, paginazione, Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(new Ordinamento("id", Ordine.DESC)));
	}

	public static class AssociazioneSuoloDaDichiaratoCommand {
		Integer versione;

		public Integer getVersione() {
			return versione;
		}

		public void setVersione(Integer versione) {
			this.versione = versione;
		}
	}

	@PutMapping("/{idLavorazione}/aggiungiPoligonoDaPunto")
	@ApiOperation("Ricerca il poligono di suolo a partire dal punto cliccato in mappa")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> cercaPoligonoDaPuntoInMappa(@PathVariable @ApiParam(value = "Identificativo della lavorazione in corso", required = true) Long idLavorazione, @RequestBody String json) {

		String username = utenteComponent.username();
		lavorazioneSuoloService.cercaPoligoniLavorazioneDaClickInMappa(idLavorazione, null, username, json);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// SUOLO DICHIARATO
	@PutMapping("/{idLavorazione}/aggiungiPoligonoDichiaratoDaPunto")
	@ApiOperation("Ricerca il poligono di suolo a partire dal punto cliccato in mappa")
	@PreAuthorize("@abilitazioniComponent.checkEditSuoloDichiarato(#json, #idLavorazione)")
	public ResponseEntity<?> cercaPoligonoDichiaratoDaPuntoInMappa(@PathVariable @ApiParam(value = "Identificativo della lavorazione in corso", required = true) Long idLavorazione,
			@RequestBody String json) {

		String username = utenteComponent.username();
		lavorazioneSuoloService.cercaPoligonoDichiaratoDaClickInMappa(idLavorazione, null, username, json);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/codificheSuolo")
	@ApiOperation("Ricerca delle codifiche suolo - cod uso e stato colt")
	public CodificheSuoloDto getCodificheSuolo() {
		return lavorazioneSuoloService.getCodificheSuolo();
	}

	@GetMapping("/{idLavorazione}/validaLavorazioneInCorso")
	@ApiOperation("Valida se la lavorazione è corretta")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	// verificare se possibile e sensato unire all'altro validate
	public ValidazioneLavorazioneInCorsoDto validaLavorazioneInCorso(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione) {
		return lavorazioneSuoloService.validaLavorazioneInCorso(idLavorazione);
	}

	@GetMapping("/{idLavorazione}/refreshStatoJobFME")
	@ApiOperation("Verifica lo stato del job FME e restituisce una LavorazioneSuoloDto aggiornato")
	@PreAuthorize("@abilitazioniComponent.checkAccessLavorazioniSuolo(#idLavorazione)")
	public LavorazioneSuoloDto refreshStatoJobFME(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione,
			@RequestParam(value = "tipoJobFme") @ApiParam(value = "Identificativo della lavorazione suolo", required = true) TipoJobFME tipoJobFme) {
		String utente = utenteComponent.username();
		return lavorazioneSuoloService.getRefreshStatoJobFME(idLavorazione, tipoJobFme, utente);
	}

	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	@PutMapping("/{idLavorazione}/lavorazioneInModifica")
	public ResponseEntity<?> lavorazioneInModifica(@PathVariable @ApiParam(value = "Identificativo della lavorazione in corso", required = true) Long idLavorazione,
			@RequestParam("statoLavorazioneInModifica") StatoLavorazioneSuolo statoInModifica) {
		String utente = utenteComponent.username();
		return lavorazioneSuoloService.cambiaStatoLavorazioneInModifica(idLavorazione, statoInModifica, utente);
	}

	@PutMapping("/{idLavorazione}/consolidamentoA4S")
	@ApiOperation("consolidamentoA4S")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> consolidamentoInA4S(@PathVariable("idLavorazione") Long idLavorazione) {
		lavorazioneSuoloService.consolidamentoInA4S(idLavorazione, utenteComponent.username());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{idLavorazione}/riprendiLavorazione")
	@ApiOperation("consolidamentoA4S")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> riprendiLavorazione(@PathVariable("idLavorazione") Long idLavorazione,
			@RequestParam(required = false) @ApiParam(value = "Verifica intersezione pascoli") boolean verificaPascoli) {
		return lavorazioneSuoloService.insertOrUpdateOrRiprendiLavorazioneInWorkspace(idLavorazione, utenteComponent.username(), verificaPascoli, false, true, null);
	}

	@PutMapping("/{idLavorazione}/consolidamentoAGS")
	@ApiOperation("consolidamentoAGS")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> consolidamentoInAGS(@PathVariable("idLavorazione") Long idLavorazione) {
		lavorazioneSuoloService.consolidamentoInAGS(idLavorazione, utenteComponent.username());
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PostMapping("/{idLavorazione}/ritagliasuADL")
	@ApiOperation("Ritaglia i poligoni di workspace sull'area di lavoro")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> ritagliasuADL(@PathVariable("idLavorazione") Long idLavorazione) {
		lavorazioneSuoloService.ritagliasuADL(idLavorazione, utenteComponent.utenza());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/")
	@ApiOperation("Ricerca lavorazioni suolo sulla base dei parametri passati")
	@PreAuthorize("@abilitazioniComponent.checkAccessRicercaLavorazioniSuolo()")
	public RisultatiPaginati<LavorazioneSuoloDto> getRicercaLavorazione(RicercaLavorazioniSuoloFilter filter, Paginazione paginazione, Ordinamento ordinamento) {
		String currentUser = utenteComponent.username();
		return lavorazioneSuoloService.ricercaLavorazioniFilter(filter, paginazione, Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY),
				currentUser);

	}

	@DeleteMapping("/{idLavorazione}/areaDiLavoro")
	@ApiOperation("Elimina area di lavoro al click del pulsante")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public void eliminaAreaLavoro(@PathVariable("idLavorazione") Long idLavorazione) {
		lavorazioneSuoloService.eliminaAreaDiLavoroLavorazione(idLavorazione);
	}

	// Cambio campagna LAVORAZIONE
	@PutMapping("/{idLavorazione}/cambioCampagna")
	@ApiOperation("Cambio campagna lavorazione suolo ")
	@PreAuthorize("@abilitazioniComponent.checkEditLavorazioneSuolo()")
	public ResponseEntity<?> cambioCampagnaLavorazioneSuolo(@PathVariable @ApiParam(value = "Identificativo della lavorazione suolo", required = true) Long idLavorazione, @RequestBody String json) {
		String utente = utenteComponent.username();
		JSONObject jsonObj = new JSONObject(json);
		Integer annoCampagna = jsonObj.getInt("annoCampagna");
		LavorazioneSuoloModel newLavorazione = lavorazioneSuoloService.cambiaCampagnaLavorazioneSuolo(idLavorazione, annoCampagna, utente);
		return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(newLavorazione.getId()));
	}

}
