package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.GruppiLavorazioneDao;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@Component("RECUPERO_LAVORAZIONI_ALTRO")
public class RecuperaLavorazioniAltro extends RecuperaLavorazioniStrategy {

	private static Logger logger = LoggerFactory.getLogger(RecuperaLavorazioniAltro.class);

	@Autowired
	private GruppiLavorazioneDao gruppoLavorazioneDao;

	// Le altre lavorazioni sono statiche. Non provengono dal fascicolo.

	@Override
	List<RaggruppamentoLavorazioniDto> getRaggruppamenti(RichiestaCarburanteModel richiesta) {
		logger.info("[Lavorazioni UMA] - Recupero altre lavorazioni");
		return buildDto(gruppoLavorazioneDao.findByAmbitoLavorazione(AmbitoLavorazione.ALTRO));
	}
}
