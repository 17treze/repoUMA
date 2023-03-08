package it.tndigitale.a4g.richiestamodificasuolo.utente;

import static it.tndigitale.a4g.richiestamodificasuolo.Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI;
import static it.tndigitale.a4g.richiestamodificasuolo.Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE;
import static it.tndigitale.a4g.richiestamodificasuolo.Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE;
import static it.tndigitale.a4g.richiestamodificasuolo.Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.wololo.geojson.Feature;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagRilevato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.richiestamodificasuolo.RichiestaModificaSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.RichiestaModificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.AuthorizationException;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi. In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto cosi in ottica di estendere i controlli sui dati
 * anche
 * 
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {

	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private RichiestaModificaSuoloService richiestaService;

	@Autowired
	private FascicoloAbilitatiComponent fascicoloAbilitatiComponent;

	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;

	@Autowired
	private SuoloDichiaratoDao suoloDichiaratoDao;

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	private List<String> estensioniAccettate = Arrays.asList("pdf", "png", "jpeg", "jpg");

	public boolean checkSearchRichiestaModificaSuolo(RichiestaModificaSuoloFilter filter) {
		boolean result = false; // OTHERS

		// Scenario APPAG = BO
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI)) {
			result = true;
		}
		// Scenario CAA
		else if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE)) {

			try {
				fascicoloAbilitatiComponent.cuaaAbilitati(filter);
				result = true;
			} catch (Exception e) {
				throw AuthorizationException.ExceptionType.GENERIC_EXCEPTION.newAuthorizationExceptionInstance("Errore in preauthorize ricerca richieste modifica suolo ", e);
			}
		}
		return result;
	}

	public boolean checkAccessResourceRichiestaModificaSuolo(Long idRichiesta) {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI)) {
			result = true;
		}
		// Scenario CAA
		else if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			RichiestaModificaSuoloDto richiesta = richiestaService.ricercaDettaglio(idRichiesta);
			if (richiesta != null && richiesta.getAziendaAgricola() != null && richiesta.getAziendaAgricola().getCuaa() != null) {
				result = fascicoloAbilitatiComponent.isCuaaAbilitato(richiesta.getAziendaAgricola().getCuaa());
			}
		}
		return result;
	}

	public boolean checkEditRichiestaModificaSuolo(Long idRichiesta) {
		boolean result = false; // OTHERS
		// Scenario CAA
		if (utenteComponent.haRuolo(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			RichiestaModificaSuoloDto richiesta = richiestaService.ricercaDettaglio(idRichiesta);
			if (richiesta != null && richiesta.getAziendaAgricola() != null && richiesta.getAziendaAgricola().getCuaa() != null) {
				result = fascicoloAbilitatiComponent.isCuaaAbilitato(richiesta.getAziendaAgricola().getCuaa());
			}
		}
		return result;
	}

	public boolean checkEditDichiaratiRichiestaModificaSuolo(Long idRichiesta) {
		boolean result = false; // OTHERS
		// Scenario CAA
		if (utenteComponent.haRuolo(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			RichiestaModificaSuoloDto richiesta = richiestaService.ricercaDettaglio(idRichiesta);
			if (richiesta != null && richiesta.getAziendaAgricola() != null && richiesta.getAziendaAgricola().getCuaa() != null && (richiesta.getStato().equals(StatoRichiestaModificaSuolo.APERTA)
					|| richiesta.getStato().equals(StatoRichiestaModificaSuolo.LAVORABILE) || richiesta.getStato().equals(StatoRichiestaModificaSuolo.IN_LAVORAZIONE))) {
				result = fascicoloAbilitatiComponent.isCuaaAbilitato(richiesta.getAziendaAgricola().getCuaa());
			}
		}
		return result;
	}

	public boolean checkEditMessaggiRichiestaModificaSuolo(Long idRichiesta) {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI)) {
			result = true;
		}
		// Scenario CAA
		else if (utenteComponent.haRuolo(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			RichiestaModificaSuoloDto richiesta = richiestaService.ricercaDettaglio(idRichiesta);
			if (richiesta != null && richiesta.getAziendaAgricola() != null && richiesta.getAziendaAgricola().getCuaa() != null) {
				result = fascicoloAbilitatiComponent.isCuaaAbilitato(richiesta.getAziendaAgricola().getCuaa());
			}
		}
		return result;
	}

	public boolean checkReadMessaggiDichiarato() {
		boolean result = false; // OTHERS
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD) || utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD))
			result = true;
		return result;
	}

	public boolean checkEditMessaggiDichiarato() {
		boolean result = false; // OTHERS
		if (utenteComponent.haRuolo(Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI) || utenteComponent.haRuolo(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE))
			result = true;
		return result;
	}

	public boolean checkEditDocumentiRichiestaModificaSuolo(Long idRichiesta) {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI)) {
			result = true;
		}
		// Scenario CAA
		else if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			RichiestaModificaSuoloDto richiesta = richiestaService.ricercaDettaglio(idRichiesta);
			if (richiesta != null && richiesta.getAziendaAgricola() != null && richiesta.getAziendaAgricola().getCuaa() != null) {
				result = fascicoloAbilitatiComponent.isCuaaAbilitato(richiesta.getAziendaAgricola().getCuaa());
			}
		}
		return result;
	}

	public boolean checkReadDocumentiDichiarato() {
		return utenteComponent.haRuolo(VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI) || utenteComponent.haRuolo(VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE);
	}

	public boolean checkEditDocumentiDichiarato() {
		return utenteComponent.haRuolo(EDIT_MESSAGGIO_RICHIESTA_TUTTI) || utenteComponent.haRuolo(EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE);
	}

	public boolean extensionControlDocument(MultipartFile file) {
		return estensioniAccettate.contains(FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase());
	}

	public boolean checkEditLavorazioneSuolo() {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.EDIT_LAVORAZIONE_SUOLO)) {
			result = true;
		}
		return result;
	}

	public boolean checkAccessLavorazioniSuolo(Long idLavorazione) {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI)) {
			result = true;
		} else if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE)) {
			// TODO controllare se la lavorazione ha suoli di vite
			String utente = utenteComponent.username();
			LavorazioneSuoloDto lavorazione = lavorazioneSuoloService.ricercaLavorazioneUtente(idLavorazione, utente);
			if (lavorazione != null) {
				// Controllo se la lavorazione ha poligoni con tag vite
				SuoloDichiaratoLavorazioneFilter criteri = new SuoloDichiaratoLavorazioneFilter();
				criteri.setIsViticolo(true);
				criteri.setIdLavorazione(idLavorazione);
				RisultatiPaginati<SuoloDichiaratoLavorazioneDto> ris = lavorazioneSuoloService.ricercaSuoloDichiarato(criteri, new Paginazione(1, 0), new Ordinamento("idRichiesta", Ordine.DESC));
				if (ris.getCount() > 0) {
					result = true;
				} else {
					// oppure se la lavorazione non ha nessun poligono
					criteri = new SuoloDichiaratoLavorazioneFilter();
					criteri.setIdLavorazione(idLavorazione);
					ris = lavorazioneSuoloService.ricercaSuoloDichiarato(criteri, new Paginazione(1, 0), new Ordinamento("idRichiesta", Ordine.DESC));
					if (ris.getCount() == 0) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	public boolean searchSuoloDichiarato(SuoloDichiaratoLavorazioneFilter filter) {
		boolean result = false; // OTHERS

		// Scenario BO
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_POLIGONO_TUTTI)) {
			result = true;
		}
		// Scenario BO - VITICOLO
		else if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_POLIGONO_VITE)) {
			filter.setIsViticolo(true);
			result = true;
		}
		return result;
	}

	public boolean checkEditSuoloDichiarato(Long idSuoloDichiarato) {
		boolean result = false; // OTHERS

		// Scenario APPAG = BO
		if (utenteComponent.haRuolo(Ruoli.EDIT_POLIGONO_TUTTI)) {
			result = true;
			// Scenario BO-VITICOLO (solo suoli con tag VIT)
		} else if (utenteComponent.haRuolo(Ruoli.EDIT_POLIGONO_VITE)) {
			SuoloDichiaratoModel suoloDich = lavorazioneSuoloService.findByIdSuoloDichiarato(idSuoloDichiarato);
			if (suoloDich != null && (suoloDich.getTipoSuoloDichiarato().equals(TagDichiarato.VIT) || suoloDich.getTipoSuoloRilevato().equals(TagRilevato.VIT))) {
				result = true;
			}
		}
		return result;
	}

	public boolean checkEditSuoloDichiarato(String json, Long idLavorazione) {
		boolean result = false; // OTHERS
		// Scenario APPAG = BO
		if (utenteComponent.haRuolo(Ruoli.EDIT_POLIGONO_TUTTI)) {
			result = true;
			// Scenario BO-VITICOLO (solo suoli con tag VIT)
		} else if (utenteComponent.haRuolo(Ruoli.EDIT_POLIGONO_VITE)) {
			// 2 Valida geometria
			if (json.isEmpty()) {
				return false;
			}
			// 3 Trasforma geojson in geometry
			Feature feature = (Feature) GeoJSONFactory.create(json);
			GeoJSONReader reader = new GeoJSONReader();
			Geometry geometry = reader.read(feature.getGeometry());
			geometry.setSRID(sridEtrs89);
			Optional<LavorazioneSuoloModel> optLavorazione = lavorazioneSuoloDao.findById(idLavorazione);
			if (optLavorazione.isPresent()) {

				List<SuoloDichiaratoModel> suoli = suoloDichiaratoDao.findByContains(geometry, optLavorazione.get().getCampagna());
				for (SuoloDichiaratoModel suoloDich : suoli) {
					if (suoloDich != null && (suoloDich.getTipoSuoloDichiarato().equals(TagDichiarato.VIT) || suoloDich.getTipoSuoloRilevato().equals(TagRilevato.VIT))) {
						result = true;
					} else {
						return false;
					}
				}
			}
		}
		return result;
	}

	public boolean checkProfiloUtenteConUtente(ProfiloUtente profiloUtente) {
		boolean result = false;
		if ((ProfiloUtente.CAA == profiloUtente && utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAYER_CAA))
				|| (ProfiloUtente.BACKOFFICE == profiloUtente && utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAYER_BACKOFFICE))
				|| (ProfiloUtente.VITICOLO == profiloUtente && utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAYER_VITICOLO))) {
			result = true;
		}
		return result;
	}

	public boolean checkAccessRicercaLavorazioniSuolo() {
		boolean result = false; // OTHERS
		// Scenario BO/VITICOLO
		if (utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI) || utenteComponent.haRuolo(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE)) {
			result = true;
		}
		return result;
	}
}
