package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.LavorazioneSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("MODIFICA")
public class ModificaLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, LavorazioneSuoloDto> {

	private static final Logger log = LoggerFactory.getLogger(ModificaLavorazioneBusiness.class);
	private static final List<StatoLavorazioneSuolo> listaStatiConsentiti = Arrays.asList(StatoLavorazioneSuolo.IN_CREAZIONE, StatoLavorazioneSuolo.IN_CORSO, StatoLavorazioneSuolo.SOSPESA,
			StatoLavorazioneSuolo.IN_MODIFICA, StatoLavorazioneSuolo.CHIUSA, StatoLavorazioneSuolo.CONSOLIDATA_SU_A4S, StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS,
			StatoLavorazioneSuolo.ERRORE_CONSOLIDAMENTO, StatoLavorazioneSuolo.CONSOLIDATA_AGS_PROBLEMI_PARTICELLE, StatoLavorazioneSuolo.CONSOLIDATA_AGS_PROBLEMI_SUOLO);

	@Autowired
	private LavorazioneSuoloMapper lavorazioneSuoloMapper;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(LavorazioneSuoloDto lavorazioneSuoloDto) {
		return updateLavorazioneSuolo(lavorazioneSuoloDto);
	}

	public LavorazioneSuoloModel updateLavorazioneSuolo(LavorazioneSuoloDto lavorazioneSuoloDto) {
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(lavorazioneSuoloDto.getId(), lavorazioneSuoloDto.getUtente());
		chechValidState(lavorazione);

		lavorazione = lavorazioneSuoloMapper.fromDtoToModelUpdate(lavorazioneSuoloDto, lavorazione);
		lavorazione.setDataUltimaModifica(getClock().now());
		return getLavorazioneSuoloDao().save(lavorazione);
	}

	protected LavorazioneSuoloModel dissociaSuoloDaLavorazione(LavorazioneSuoloModel lavorazione) {
		List<SuoloModel> suoloLavorazione = lavorazione.getListaSuoloInCorsoModel();
		for (SuoloModel suolo : new ArrayList<>(suoloLavorazione)) {
			lavorazione.removeSuoloInCorsoModel(suolo);
		}
		return getLavorazioneSuoloDao().cleanSave(lavorazione);
		// return getLavorazioneSuoloDao().saveAndFlush(lavorazione);
	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!listaStatiConsentiti.contains(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per modifica {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente la modifica"))));

		}

	}
}
