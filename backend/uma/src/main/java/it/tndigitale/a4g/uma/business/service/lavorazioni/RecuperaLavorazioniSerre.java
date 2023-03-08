package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@Component("RECUPERO_LAVORAZIONI_SERRE")
public class RecuperaLavorazioniSerre extends RecuperaLavorazioniStrategy {

	private static Logger logger = LoggerFactory.getLogger(RecuperaLavorazioniSerre.class);

	@Override
	public List<RaggruppamentoLavorazioniDto> getRaggruppamenti(RichiestaCarburanteModel richiestaCarburante) {
		logger.info("[Lavorazioni UMA] - recupero lavorazioni serre");
		return getRaggruppamentiFabbricati(richiestaCarburante, LavorazioneFilter.LavorazioniFabbricati.SERRE);
	}
}
