package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloNonAssociabileLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("ASSOCIAZIONE_POLIGONI_VIGENTE")
public class AssociazionePoligoniALavorazioneDaDichiaratoBusiness extends AzioneLavorazioneBase<List<SuoloNonAssociabileLavorazioneDto>, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(AssociazionePoligoniALavorazioneDaDichiaratoBusiness.class);

	@Autowired
	private SuoloDao suoloDao;

	@Override
	protected List<SuoloNonAssociabileLavorazioneDto> eseguiAzione(BaseInputData inputData) {
		return associazionePoligoniLavorazioneDaDichiarato(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente());
	}

	public List<SuoloNonAssociabileLavorazioneDto> associazionePoligoniLavorazioneDaDichiarato(Long idLavorazione, Integer versione, String utente) {
		List<SuoloNonAssociabileLavorazioneDto> result = null;
		// 0. Carico la lavorazione
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(idLavorazione, utente);

		checkValidState(lavorazione);
		// 1 elimino associazioni correnti
		lavorazione = dissociaSuoloDaLavorazione(lavorazione);
		// 2. Carico i poligoni di dichiarato
		List<SuoloDichiaratoModel> suoliDichiaratoLavorazione = lavorazione.getSuoloDichiaratoModel();
		result = new ArrayList<SuoloNonAssociabileLavorazioneDto>();
		if (suoliDichiaratoLavorazione != null) {
			Map<SuoloModel, List<SuoloDichiaratoModel>> suoliNonAssociabili = new HashMap<SuoloModel, List<SuoloDichiaratoModel>>();
			for (SuoloDichiaratoModel suoloDichiarato : suoliDichiaratoLavorazione) {
				log.debug("Elaboro suolo dichiarato {}", suoloDichiarato.getId());
				List<SuoloModel> listaSuoliVigenteIntersecati = suoloVigenteDaDichiarato(suoloDichiarato, lavorazione.getCampagna());
				log.debug("per suolo dichiarato {} trovati suoli {}", suoloDichiarato.getId(), listaSuoliVigenteIntersecati);
				for (SuoloModel suoloVigente : listaSuoliVigenteIntersecati) {
					if (isSuoloAssociabile(suoloVigente, lavorazione)) { // se libero associo (se associato alla lavorazione in input va comunque bene)
						log.debug("Associo suolo vigente {} a lavorazione {}", suoloVigente.getId(), lavorazione.getId());
						associaSuoloALavorazione(lavorazione, suoloVigente);
					} else {
						log.debug("Scarto suolo vigente {} a lavorazione {}", suoloVigente.getId(), lavorazione.getId());
						List<SuoloDichiaratoModel> listaSuoliNonAssociabili = suoliNonAssociabili.get(suoloVigente);
						if (listaSuoliNonAssociabili == null) {
							listaSuoliNonAssociabili = new ArrayList<>();
							suoliNonAssociabili.put(suoloVigente, listaSuoliNonAssociabili);
						}
						listaSuoliNonAssociabili.add(suoloDichiarato);
					}
				}
				log.debug("FINE suolo dichiarato {}", suoloDichiarato.getId());
			}
			result = convert(suoliNonAssociabili);
		}
		// lavorazione = getLavorazioneSuoloDao().cleanSave(lavorazione);
		lavorazione.setDataUltimaModifica(getClock().now());
		lavorazione = getLavorazioneSuoloDao().saveAndFlush(lavorazione);
		return result;
	}

	protected List<SuoloModel> suoloVigenteDaDichiarato(SuoloDichiaratoModel suoloDichiarato, Integer campagna) {
		LocalDateTime data = getDataRiferimento();
		// intersects by suolo funziona su oracle ma non su h2
		return suoloDao.findByintersectsSuoloDichiarato(suoloDichiarato.getId(), campagna).stream().filter(s -> between(data, s.getDataInizioValidita(), s.getDataFineValidita()))
				.collect(Collectors.toList());
		// funziona su h2 ma non su oracle return suoloDao.findByintersects(suoloDichiarato.getShape()).stream().filter(s -> between(data, s.getDataInizioValidita(),
		// s.getDataFineValidita())).collect(Collectors.toList());
	}

	protected boolean between(LocalDateTime data, LocalDateTime localDate, LocalDateTime localDate2) {
		boolean result = Optional.ofNullable(localDate).map(i -> i.isBefore(data)).orElse(true);
		return result && Optional.ofNullable(localDate2).map(i -> i.isAfter(data)).orElse(true);
	}

	protected LocalDateTime getDataRiferimento() {
		return getClock().now();
	}

	private List<SuoloNonAssociabileLavorazioneDto> convert(Map<SuoloModel, List<SuoloDichiaratoModel>> suoliNonAssociabili) {
		return suoliNonAssociabili.keySet().stream().map(suoloVigente -> {
			SuoloNonAssociabileLavorazioneDto suolo = new SuoloNonAssociabileLavorazioneDto();
			SuoloNonAssociabileLavorazioneDto.SuoloVigente sv = suolo.newInstance();
			sv.setIdLavorazione(suoloVigente.getIdLavorazioneInCorso().getId());
			suolo.setIdSuoloDichiarato(suoliNonAssociabili.get(suoloVigente).stream().map(SuoloDichiaratoModel::getId).collect(Collectors.toList()));
			sv.setIdSuoloVigente(suoloVigente.getId());
			sv.setUtente(suoloVigente.getIdLavorazioneInCorso().getUtente());
			sv.setDataUltimaLavorazione(suoloVigente.getIdLavorazioneInCorso().getDataUltimaModifica());
			return suolo;
		}).collect(Collectors.toList());
	}

	private boolean isSuoloAssociabile(SuoloModel suoloVigente, LavorazioneSuoloModel lavorazione) {
		return (suoloVigente.getIdLavorazioneInCorso() == null || lavorazione.getId().equals(suoloVigente.getIdLavorazioneInCorso().getId()));
	}

	private void associaSuoloALavorazione(LavorazioneSuoloModel lavorazione, SuoloModel suoloVigente) {
		lavorazione.addSuoloInCorsoModel(suoloVigente);
	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per passaggio di stato a IN_CORSO {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));

		}

	}
}
