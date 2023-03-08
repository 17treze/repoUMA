package it.tndigitale.a4gistruttoria.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;
import it.tndigitale.a4gistruttoria.util.CustomConverters;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

public abstract class DatiDisciplinaFinanziariaAbstract extends DatiDettaglioAbstract {

	private static final Logger logger = LoggerFactory.getLogger(DatiDisciplinaFinanziariaAbstract.class);

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TransizioneIstruttoriaService transizioneAccoppiatoService;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	public abstract Sostegno getIdentificativoSostegno();

	protected Map<String, String> recuperaDisciplinaFinanziaria(Long idIstruttoria) {
		Map<String, String> disciplinaFinanziaria = new LinkedHashMap<>();
		PassoTransizioneModel passo = recuperaPassoDisciplinaFinanziaria(istruttoriaDao.getOne(idIstruttoria));
		if (passo != null) {
			DatiInput datiInput = CustomConverters.jsonConvert(passo.getDatiInput(), DatiInput.class);
			DatiOutput datiOutput = CustomConverters.jsonConvert(passo.getDatiOutput(), DatiOutput.class);
			disciplinaFinanziaria = creaDatiDisciplinaFinanziaria(datiInput, datiOutput);
		}
		return disciplinaFinanziaria;
	}

	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		return StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK;// è lo stato in cui vengono memorizzati i dati di input e output relativi la disciplina finanziaria
	}

	protected PassoTransizioneModel recuperaPassoDisciplinaFinanziaria(IstruttoriaModel istruttoria) {
		try {
			StatoIstruttoria stato = adjustStatoLavorazioneSostegno(StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));
			TransizioneIstruttoriaModel transizione = transizioneAccoppiatoService.caricaUltimaTransizione(istruttoria,stato);
			return transizione.getPassiTransizione().stream().filter(p -> TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA.equals(p.getCodicePasso())).collect(CustomCollectors.toSingleton());
		} catch (EntityNotFoundException enfe) {
			logger.info("recuperaPassoDisciplinaFinanziaria: transizione della disciplina non trovata per l'istruttoria {} ", istruttoria.getId());
			return null;
		} catch (Exception e) {
			logger.error("recuperaPassoDisciplinaFinanziaria: errore caricando le transizioni e i passi di lavorazione dell'istruttoria {}", istruttoria.getId(), e);
			throw new RuntimeException(recuperaCodiceErrorePerSostegno(getIdentificativoSostegno()));
		}
	}

	protected String getValoreVariabile(TipoVariabile tipoVariabile, DatiCalcoli datiCalcoli) {
		try {
			VariabileCalcolo v = datiCalcoli.getVariabiliCalcolo().stream().filter(p -> p.getTipoVariabile().equals(tipoVariabile)).collect(CustomCollectors.toSingleton());
			return v.recuperaValoreString();
		} catch (IllegalStateException e) {
			logger.error("ATTENZIONE! Variabile {} non presente nella lista delle variabili!", tipoVariabile);
		}
		return null;
	}

	protected abstract Map<String, String> creaDatiDisciplinaFinanziaria(DatiInput datiInput, DatiOutput datiOutput);
}
