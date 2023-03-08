package it.tndigitale.a4gistruttoria.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.Capo;
import it.tndigitale.a4gistruttoria.dto.CuaaDenominazione;
import it.tndigitale.a4gistruttoria.dto.DatiDomandaIbanErrato;
import it.tndigitale.a4gistruttoria.dto.DatiErede;
import it.tndigitale.a4gistruttoria.dto.Domanda;
import it.tndigitale.a4gistruttoria.dto.FiltroRicercaDomandeIstruttoria;
import it.tndigitale.a4gistruttoria.dto.InfoDomandaDU;
import it.tndigitale.a4gistruttoria.dto.Pagina;
import it.tndigitale.a4gistruttoria.dto.Paginazione;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDuEsito;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ParticellePascoloFilter;
import it.tndigitale.a4gistruttoria.dto.domandaunica.RichiestaSuperficie;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DomandaUnicaDettaglio;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.ElencoPascoliService;


/**
 * Controller per gestire operazioni sulle domande
 * 
 * @author Alessandro.Manganiello
 *
 */
@RestController
@RequestMapping(ApiUrls.DOMANDE_V1)
@Deprecated
public class DomandeRestController {

	private static final Logger logger = LoggerFactory.getLogger(DomandeRestController.class);
	private static final String ERRORE_PAGINAZIONE_DOMANDE = "Errore di sistema in fase di recupero domande";
	private static final String ERRORE_PAGINAZIONE_PARTICELLA = "Errore di sistema in fase di recupero particelle";
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private ElencoPascoliService pascoliService;
	@Autowired
	DomandaUnicaDao domandaUnicaDao;

	/**
	 * Servizio REST per il recupero della lista dei possibili stati in cui può trovarsi una domanda
	 * 
	 * @params null
	 * @return Una lista contenete tutti i possibili stati di una domanda
	 */
	@GetMapping(ApiUrls.ELENCO_STATI)
	@ApiOperation("Restituisce l'elenco degli stati possibili")
	public List<StatoDomanda> elencoStati() {
		return new ArrayList<StatoDomanda>(
				Arrays.asList(StatoDomanda.values()));
	}

	@GetMapping(ApiUrls.ELENCO_PASCOLI)
	@ApiOperation("Servizio per la restituzione dei pascoli (usato da bdn)")
	public List<String> getElencoPascoli(@RequestParam(value = "annoCampagna") Integer annoCampagna, @RequestParam(value = "cuaa") String cuaa) throws Exception {

		List<String> ret;

		try {
			if (annoCampagna == null) {
				throw new NoResultException();
			}
			ret = serviceDomande.getCodiciPascoli(annoCampagna, cuaa);
			return ret;
		} catch (Exception e) {
			logger.error("Errore in recupero pascoli per cuaa {}", cuaa, e);

		}
		return new ArrayList<>();

	}

	@GetMapping(ApiUrls.ELENCO_CUAA_CAMPAGNA)
	@ApiOperation("Servizio per il recupero dell'elenco cuaa per i filtri (LO USA ANCHE LA BDN NON CAMBIARLO)")
	public List<CuaaDenominazione> getCuaaDomandeCampagna(
			@RequestParam(value = "params")
			@ApiParam("Parametri per la restituzione di domande in base allo stato e al settore")
			String parametri) {
		try {
			FiltroRicercaDomandeIstruttoria filter = objectMapper.readValue(parametri, FiltroRicercaDomandeIstruttoria.class);
			return serviceDomande.getCuaaDomandeCampagna(filter);
		} catch (Exception e) {
			logger.error("Errore in recupero domande filtrate. Filtro: {} ", parametri, e);
		}
		return new ArrayList<>();
	}

	@GetMapping(ApiUrls.ELENCO_CUAA_FILTRATI)
	@ApiOperation("Servizio per il recupero dell'elenco cuaa per l'autocomplete dei filtri")
	public RisultatiPaginati<String> getCuaaDomandeFiltrati(
			@RequestParam(value = "stato-sostegno") @ApiParam("stato sostegno") String statoSostegno,
			@RequestParam(value = "sostegno") @ApiParam("tipo sostegno") Sostegno sostegno,
			@RequestParam(value = "anno-campagna") @ApiParam("anno campagna") Integer annoCampagna,
			@RequestParam(value = "cuaa") @ApiParam("descrizione cuaa") String cuaa,
			@RequestParam(value = "tipo") @ApiParam("Tipo Istruttoria") TipoIstruttoria tipo,
			it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione,
			it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento) throws Exception {
		return serviceDomande.getCuaaDomandeFiltrati(
				statoSostegno, sostegno, annoCampagna, cuaa, tipo,
				it.tndigitale.a4g.framework.pagination.model.Paginazione.getOrDefault(paginazione),
				it.tndigitale.a4g.framework.pagination.model.Ordinamento.getOrDefault(ordinamento));
	}

	@GetMapping(ApiUrls.ELENCO_RAGIONESOCIALE_FILTRATI)
	@ApiOperation("Servizio per il recupero dell'elenco ragione sociale impresa per l'autocomplete dei filtri")
	public RisultatiPaginati<String> getRagioneSocialeDomandeFiltrati(
			@RequestParam(value = "stato-sostegno") @ApiParam("stato sostegno") String statoSostegno,
			@RequestParam(value = "sostegno") @ApiParam("tipo sostegno") Sostegno sostegno,
			@RequestParam(value = "anno-campagna") @ApiParam("anno campagna") Integer annoCampagna,
			@RequestParam(value = "ragione-sociale") @ApiParam("ragione sociale") String ragioneSociale,
			@RequestParam(value = "tipo") @ApiParam("Tipo Istruttoria") TipoIstruttoria tipo,
			it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione,
			it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento) throws Exception {		
		return serviceDomande.getRagioneSocialeDomandeFiltrati(
				statoSostegno, sostegno, annoCampagna, ragioneSociale,tipo,
				it.tndigitale.a4g.framework.pagination.model.Paginazione.getOrDefault(paginazione),
				it.tndigitale.a4g.framework.pagination.model.Ordinamento.getOrDefault(ordinamento));
	}

	@GetMapping("/{numeroDomanda}" + ApiUrls.INFO_SIAN)
	@ApiOperation("Servizio per il recupero delle informazioni sinconizzate con SIAN relative all'intestatario della domanda")
	public AgricoltoreSIAN getInformazioniAgricoltoreSian(@PathVariable BigDecimal numeroDomanda) throws Exception {
		return serviceDomande.recuperaInfoAgricoltoreSIAN(numeroDomanda);
	}

	@GetMapping(ApiUrls.DATI_DU + "/{cuaa}")
	@ApiOperation("Servizio per il recupero delle informazioni aggiuntive della Domanda DU relative al CUAA del richiedente")
	public InfoDomandaDU getInfoDomandaDU(@PathVariable String cuaa, @RequestParam(name = "anno") @ApiParam("Anno di riferimento") Integer anno) {

		return serviceDomande.findByCuaaIntestatarioAndAnnoRiferimento(cuaa, anno);
	}

	@GetMapping("/{id}/" + ApiUrls.DOMANDA_DETTAGLIO)
	@ApiOperation("Servizio per la restituzione dei dettagli di una domanda")
	@Deprecated
	public DomandaUnicaDettaglio getDomandaDettaglio(@PathVariable Long id) throws Exception {
		return serviceDomande.getDomandaDettaglio(id);
	}


	@GetMapping("/{id}/" + ApiUrls.DOMANDA_PARTICELLE)
	@ApiOperation("Servizio per la restituzione di una pagina di particelle per domanda")
	public Pagina<RichiestaSuperficie> getParticelleDomanda(@PathVariable Long id, @RequestParam(name = "paginazione")
	@ApiParam("Parametri per la paginazione") String paginazione,
	@RequestParam(name = "ordinamento") @ApiParam("Parametri per l'ordinamento") String ordinamento) {
		try {
			Paginazione criteriPaginazione = Paginazione.of();
			if (paginazione != null && !paginazione.isEmpty()) {
				criteriPaginazione = objectMapper.readValue(paginazione, Paginazione.class);
			} else {
				criteriPaginazione.setPagina(0);
			}

			return serviceDomande.getParticelleDomanda(id, criteriPaginazione, ordinamento);

		} catch (Exception e) {
			logger.error("Errore in recupero superfici");
			throw new NoResultException(DomandeRestController.ERRORE_PAGINAZIONE_PARTICELLA);
		}
	}

	@ApiOperation("Restituisce il documento richiesto")
	@GetMapping(ApiUrls.STAMPA_ELENCO_LIQUIDAZIONE)
	public byte[] stampaPdfElencoLiquidazione(@RequestParam("params") String parametri) throws IOException {
		JsonNode params = objectMapper.readTree(parametri);
		String codiceElenco = params.get("codiceElenco").textValue();
		return serviceDomande.getVerbaleLiquidazione(codiceElenco);
	}

	@ApiOperation("Servizio per l'aggiornamento dei dati utili al filtering delle domande nel cruscotto di sostegno. Il servizio considera solo le domande nello stato 'IN_ISTRUTTORIA'.")
	@PutMapping("/datiFiltri")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaRielaboraDati()")
	public Long aggiornaCacheFiltriDomanda() throws Exception {
		try {
			List<DomandaUnicaModel> listDom = domandaUnicaDao.findByStato(StatoDomanda.IN_ISTRUTTORIA);
			listDom.forEach(domanda -> {
				Long idDomanda = domanda.getId();
				try {
					serviceDomande.salvaDatiFiltratiDomanda(idDomanda);
				} catch (Exception e) {
					logger.error("Errore aggiornando i filtri della domanda " + idDomanda, e);
				}
			});
		} catch (Exception e) {
			logger.error("Errore rielaborando i filtri", e);
			throw new RuntimeException("Errore rielaborando i filtri");
		}
		return 0L;
	}

	@ApiOperation("Servizio per l'aggiornamento dei dati utili al filtering della domanda specificata nel path.")
	@PutMapping("/datiFiltri/{idDomanda}")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaRielaboraDati()")
	public Long aggiornaCacheFiltriDomanda(@PathVariable Long idDomanda) throws Exception {
		try {
			serviceDomande.salvaDatiFiltratiDomanda(idDomanda);
		} catch (Exception e) {
			logger.error("Errore aggiornando i filtri della domanda " + idDomanda, e);
			throw new RuntimeException("Errore rielaborando i filtri");
		}
		return 0L;
	}

	@ApiOperation("Servizio per l'aggiornamento dei dati di pascoli (reimporta i dati, pertanto e' necessario svuotare la tabella pascoli_particella")
	@PutMapping("/pascolo")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaRielaboraDati()")
	public Long getParticellePascolo(ParticellePascoloFilter particellePascoloFilter) throws Exception {
		List<DomandaUnicaModel> listDom = new ArrayList<>();
		if (particellePascoloFilter.getAnno() != null) {
			listDom = domandaUnicaDao.findByCampagna(particellePascoloFilter.getAnno());
		} else {
			listDom = domandaUnicaDao.findAll();
		}
		try {
			listDom.forEach(domanda -> {
				Long idDomanda = domanda.getId();
				try {
					pascoliService.estraiParticellePascolo(idDomanda);
					pascoliService.estraiPascoliAziendali(domanda);
					serviceDomande.salvaDatiFiltratiDomanda(idDomanda);
				} catch (Exception e) {
					logger.error("Errore rielaborando i pascoli della domanda " + idDomanda, e);
				}
			});

		} catch (Exception e) {
			logger.error("Errore rielaborando i pascoli", e);
			throw new RuntimeException("Errore rielaborando i pascoli");
		}
		return 0L;
	}

	@ApiOperation("Servizio per l'aggiornamento dei dati di pascoli (reimporta i dati, pertanto e' necessario svuotare la tabella pascoli_particella")
	@PutMapping("/pascolo/{idDomanda}")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaRielaboraDati()")
	public Long getParticellePascolo(@PathVariable Long idDomanda) throws Exception {
		DomandaUnicaModel domanda = domandaUnicaDao.findById(idDomanda).orElseThrow(() -> new EntityNotFoundException("Nessuna domanda unica con id ".concat(idDomanda.toString())));
		try {
			pascoliService.estraiParticellePascolo(idDomanda);
			pascoliService.estraiPascoliAziendali(domanda);
			serviceDomande.salvaDatiFiltratiDomanda(idDomanda);
		} catch (Exception e) {
			logger.error("Errore rielaborando i pascoli della domanda " + idDomanda, e);
			throw new RuntimeException("Errore rielaborando i pascoli");
		}
		return 0L;
	}


	@PutMapping("/{id}" + ApiUrls.CAPI + "/{idcaporichiesto}")
	@ApiOperation("Modifica il capo per la domanda")
	public Capo modificaCapo(@PathVariable("id") Long id, @PathVariable("idcaporichiesto") Long idcaporichiesto, @RequestBody() Capo capo) throws Exception {
		capo.setId(idcaporichiesto);
		return serviceDomande.modificaCapo(id, capo);
	}

	@GetMapping("/{id}/richiesteallevamduesito/{idRichiestaAllevamentoEsito}")
	@ApiOperation("Restituisce gli esiti del calcolo zootecnia trovate tramite id")
	public RichiestaAllevamDuEsito getRichiestaAllevamDuEsito(@PathVariable Long id, @PathVariable Long idRichiestaAllevamentoEsito) throws Exception {
		return serviceDomande.getRichiestaAllevamDuEsito(idRichiestaAllevamentoEsito);
	}

	@PutMapping("/{id}/avvioIstruttoria")
	@ApiOperation("Esegue l'avvio istruttoria per una singola domanda")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU()")
	public String avviaIstruttoriaDomanda(@PathVariable Long id) {

		Optional<DomandaUnicaModel> domandaOpt = domandaUnicaDao.findById(id);
		if (domandaOpt.isPresent()) {
			StringBuilder sb = new StringBuilder();
			serviceDomande.elaboraDomandaPerIstruttoria(domandaOpt.get(), sb);
			return sb.toString();
		} else {
			return "Domanda con ID " + id + " non presente nella banca dati";
		}

	}

	@GetMapping("/{id}")
	@ApiOperation("Servizio per la restituzione delle informazioni di una domanda")
	public Domanda getDomanda(@PathVariable Long id) throws Exception {
		return serviceDomande.getDomanda(id);
	}

	@PutMapping("/{id}")
	@ApiOperation("Aggiorna domanda unica")
	public Domanda aggiornaDomanda(@PathVariable(required = true) Long id, @RequestBody(required = true) Domanda domanda) throws Exception {
		domanda.setId(id);
		return serviceDomande.aggiornaDomanda(domanda);
	}

	@PostMapping("/{id}/eredi")
	@ApiOperation("Crea un nuovo erede")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU()")
	public DatiErede creaErede(@PathVariable(name = "id") Long idDomanda, @RequestBody(required = true) DatiErede erede) throws Exception {
		return serviceDomande.creaErede(idDomanda, erede);
	}

	@PutMapping("/{id}/eredi/{iderede}")
	@ApiOperation("Servizio per aggiorare i dati di un erede")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaDU()")
	public DatiErede aggiornaErede(@PathVariable(name = "id") Long id, @PathVariable(name = "iderede") Long idErede, @RequestBody(required = true) DatiErede erede) throws Exception {
		erede.setId(idErede);
		return serviceDomande.aggiornaErede(erede);
	}

	@GetMapping("/ibanerrato")
	@ApiOperation("Servizio che recupera le domande che non risultano ancora liquidabili (ovvero che hanno un IBAN non congruente con quanto specificato in fascicolo)")
	public List<DatiDomandaIbanErrato> getDatiDomandeIbanErrato() {
		return serviceDomande.getDatiDomandeIbanErrato();
	}

}
