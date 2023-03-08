package it.tndigitale.a4gistruttoria.strategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.ControlliSostegno;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.util.CustomConverters;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;

public abstract class DatiControlliSostegnoAbstract extends DatiDettaglioAbstract {
	
	private static final Logger logger = LoggerFactory.getLogger(DatiControlliSostegnoAbstract.class);
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	IstruttoriaDao istruttoriaDao;
	
	public abstract Sostegno getIdentificativoSostegno();
	
	protected ControlliSostegno recuperaControlliSostegno(Long idIstruttoria) {
		ControlliSostegno controlliSostegno = new ControlliSostegno();
		Set<PassoTransizioneModel> pls = recuperaPassiLavorazione(istruttoriaDao.getOne(idIstruttoria));
		try {
			List<DatiSintesi> datiSintesi = pls.stream()
					.map(item -> CustomConverters.jsonConvert(item.getDatiSintesiLavorazione(), DatiSintesi.class))
					.collect(Collectors.toList());
			List<EsitoControllo> esitiControlli = datiSintesi.stream()
					.flatMap(item -> item.getEsitiControlli().stream())
					.collect(Collectors.toList());
	
			// filtro messaggi info
			controlliSostegno.setInfos(
					esitiControlli.stream()
						.filter(esito -> LivelloControllo.INFO.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList())
					);
	
			// filtro messaggi error
			controlliSostegno.setErrors(
					esitiControlli.stream()
						.filter(esito -> LivelloControllo.ERROR.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList())
					);
	
			// filtro messaggi success
			controlliSostegno.setSuccesses(
					esitiControlli.stream()
						.filter(esito -> LivelloControllo.SUCCESS.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList())
					);
	
			// filtro messaggi warning
			controlliSostegno.setWarnings(
					esitiControlli.stream()
						.filter(esito -> LivelloControllo.WARNING.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList())
					);
	
		} catch (Exception e) {
			logger.error("Si è verificato un errore durante la creazione dell'oggetto controlli sostegno", e);
		}
		return controlliSostegno;
	}
	
	protected abstract String buildStringaEsito(EsitoControllo esito);
	
	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		if (StatoIstruttoria.NON_AMMISSIBILE.equals(stato))
			return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
		if (StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(stato))
			return StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK;
		return stato;
	}
}
