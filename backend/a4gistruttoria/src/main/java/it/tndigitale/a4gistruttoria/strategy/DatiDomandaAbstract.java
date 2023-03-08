package it.tndigitale.a4gistruttoria.strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.IDettaglioCalcolo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public abstract class DatiDomandaAbstract extends DatiDettaglioAbstract {

	private static final Logger logger = LoggerFactory.getLogger(DatiDomandaAbstract.class);

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	protected DatiDomandaAccoppiato recuperaDatiDomanda(Long idIstruttoria) {
		Set<PassoTransizioneModel> pls = recuperaPassiLavorazione(istruttoriaDao.getOne(idIstruttoria));
		DatiDomandaAccoppiato datiDomanda = new DatiDomandaAccoppiato();

		try {
			List<DatiInput> datiInput = pls.stream()
					.map(item -> CustomConverters.jsonConvert(item.getDatiInput(), DatiInput.class))
					.collect(Collectors.toList());
			List<DatiOutput> datiOutput = pls.stream()
					.map(item -> CustomConverters.jsonConvert(item.getDatiOutput(), DatiOutput.class))
					.collect(Collectors.toList());

			datiDomanda.setSintesiCalcolo(creaDatiSintesi(datiInput, datiOutput));
			datiDomanda.setDettaglioCalcolo(creaDettaglioCalcolo(datiInput, datiOutput));
		} catch (Exception e) {
			logger.error("Impossibile recuperare i dati di dettaglio per l'istruttoria {}", idIstruttoria, e);
		}
		return datiDomanda;
	}

	public abstract Sostegno getIdentificativoSostegno();

	protected abstract Map<String, String> creaListaDettaglio(List<DatiInput> datiInput, List<DatiOutput> datiOutput, String suffisso);

	protected abstract Map<String, String> creaDatiSintesi(List<DatiInput> datiInput, List<DatiOutput> datiOutput);

	protected abstract IDettaglioCalcolo creaDettaglioCalcolo(List<DatiInput> datiInput, List<DatiOutput> datiOutput);

	protected String getValoreVariabile(TipoVariabile tipoVariabile, DatiCalcoli datiCalcoli) {
		try {
			VariabileCalcolo v = datiCalcoli.getVariabiliCalcolo().stream()
					.filter(p -> p.getTipoVariabile().equals(tipoVariabile))
					.collect(CustomCollectors.toSingleton());
			return v.recuperaValoreString();
		} catch (IllegalStateException e) {
			logger.info("ATTENZIONE! Variabile {} non presente nella lista delle variabili!", tipoVariabile);
		}
		return null;
	}

	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		if (StatoIstruttoria.NON_AMMISSIBILE.equals(stato)
			|| StatoIstruttoria.CONTROLLI_CALCOLO_KO.equals(stato))
			return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
		if (stato.compareTo(StatoIstruttoria.CONTROLLI_CALCOLO_OK) > 0)
			return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
		return stato;
	}
}
