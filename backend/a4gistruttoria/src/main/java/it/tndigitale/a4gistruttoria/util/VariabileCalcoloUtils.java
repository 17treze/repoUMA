package it.tndigitale.a4gistruttoria.util;

import java.io.IOException;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import javassist.NotFoundException;

@Service
public class VariabileCalcoloUtils {

	private static final Logger logger = LoggerFactory.getLogger(VariabileCalcoloUtils.class);

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private PassoTransizioneDao daoPassiLavSostegno;

	/**
	 * Metodo per il recupero della variabile "variabile" dall'output prodotto nel passo di lavorazione "passo" per la transizione "transizione". Nel caso in cui la variabile non esita, ritorna NULL
	 * 
	 * @param domanda
	 * @param sostegno
	 * @return
	 * @throws ElencoLiquidazioneException
	 * @throws IOException
	 */
	public VariabileCalcolo recuperaVariabileCalcolata(TransizioneIstruttoriaModel transizione, TipologiaPassoTransizione passo, TipoVariabile variabile) throws NotFoundException, IOException {

		Optional<PassoTransizioneModel> passoControlliFinaliOpt = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione).stream()
				.filter(p -> p.getCodicePasso().equals(passo)).findFirst();

		if (passoControlliFinaliOpt.isPresent()) {
			PassoTransizioneModel passoControlliFinali = passoControlliFinaliOpt.get();
			DatiOutput output = mapper.readValue(passoControlliFinali.getDatiOutput(), DatiOutput.class);
			Optional<VariabileCalcolo> variabileOpt = output.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(variabile)).findFirst();
			if (variabileOpt.isPresent()) {
				return variabileOpt.get();
			} else {
				logger.info("Impossibile recuperare la variabile di calcolo {}", variabile.name());
				return null;
			}
		} else {
			throw new NotFoundException("Impossibile recuperare il passo di lavorazione " + passo);
		}
	}
	
	/**
	 * 
	 * Metodo per il recupero della variabile "variabile" dall'input prodotto nel passo di lavorazione "passo" per la transizione "transizione". Nel caso in cui la variabile non esita, ritorna NULL
	 * 
	 * @param transizione
	 * @param passo
	 * @param variabile
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public VariabileCalcolo recuperaVariabileInput(TransizioneIstruttoriaModel transizione, TipologiaPassoTransizione passo, TipoVariabile variabile) throws NotFoundException, IOException {

		Optional<PassoTransizioneModel> passoControlliFinaliOpt = daoPassiLavSostegno.findByTransizioneIstruttoria(transizione).stream()
				.filter(p -> p.getCodicePasso().equals(passo)).findFirst();

		if (passoControlliFinaliOpt.isPresent()) {
			PassoTransizioneModel passoControlliFinali = passoControlliFinaliOpt.get();
			DatiInput input = mapper.readValue(passoControlliFinali.getDatiInput(), DatiInput.class);
			Optional<VariabileCalcolo> variabileOpt = input.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(variabile)).findFirst();
			if (variabileOpt.isPresent()) {
				return variabileOpt.get();
			} else {
				logger.error("Impossibile recuperare la variabile di input {}", variabile.name());
				return null;
			}
		} else {
			throw new NotFoundException(
					"Impossibile recuperare il passo di lavorazione " + passo);
		}
	}

}
