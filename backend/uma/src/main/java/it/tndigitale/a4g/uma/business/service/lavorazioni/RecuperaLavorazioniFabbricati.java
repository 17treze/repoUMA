package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.FabbricatoAgsDto;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatoGruppiDao;
import it.tndigitale.a4g.uma.dto.richiesta.FabbricatoAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@Component("RECUPERO_LAVORAZIONI_FABBRICATI")
public class RecuperaLavorazioniFabbricati extends RecuperaLavorazioniStrategy {

	private static Logger logger = LoggerFactory.getLogger(RecuperaLavorazioniFabbricati.class);

	@Autowired
	private FabbricatoGruppiDao fabbricatoGruppiDao;

	// recupero fabbricati da dotazione tecnica e non dal db A4G in quanto non sappiamo qual è la correlazione fabbricato-gruppo 

	@Override
	public List<RaggruppamentoLavorazioniDto> getRaggruppamenti(RichiestaCarburanteModel richiestaCarburante) {
		logger.info("[Lavorazioni UMA] - recupero lavorazioni fabbricati");
		return getRaggruppamentiFabbricati(richiestaCarburante, LavorazioneFilter.LavorazioniFabbricati.FABBRICATI);
	}

	public final Function<FabbricatoAgsDto, Optional<FabbricatoGruppiModel>> tipoToFabbricatoGruppoModel = fabbricatoAgsDto -> {
		Optional<FabbricatoGruppiModel> fabbricatoGruppiModelOpt = Optional.ofNullable(fabbricatoGruppiDao.findByCodiceFabbricato(fabbricatoAgsDto.getTipoFabbricatoCodice()));
		if (fabbricatoGruppiModelOpt.isPresent()) {
			return Optional.of(fabbricatoGruppiModelOpt.get());
		}
		return Optional.empty();
	};

	public final Function<FabbricatoAualDto, Optional<FabbricatoGruppiModel>> tipoAualToFabbricatoGruppoModel = fabbricatoAualDto -> {
		Optional<FabbricatoGruppiModel> fabbricatoGruppiModelOpt = Optional.ofNullable(fabbricatoGruppiDao.findByCodiceFabbricato("000007"));
		if (fabbricatoGruppiModelOpt.isPresent()) {
			return Optional.of(fabbricatoGruppiModelOpt.get());
		}
		return Optional.empty();
	};

}
