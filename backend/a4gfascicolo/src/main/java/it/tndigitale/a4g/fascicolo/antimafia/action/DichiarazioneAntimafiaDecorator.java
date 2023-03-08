package it.tndigitale.a4g.fascicolo.antimafia.action;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneService;

@Component
public class DichiarazioneAntimafiaDecorator implements FunctionDichiarazioneAntimafiaDecorator {

	@Autowired
	private ConsultazioneService consultazioneService;
	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(DichiarazioneAntimafiaDecorator.class);

	/**
	 * Restituisce le domanda antimafia in stato 'RIFIUTATA' o 'VERIFICA PERIODICA' in base alle deleghe dell'operatore CAA
	 */
	@Override
	public List<Dichiarazione> apply(List<Dichiarazione> listDichiarazione) {
		try {
			// Funzione che per ogni domanda crea un input per la ricerca del fascicolo a partire dai dati dell'azienda per cui è stata fatta la domanda antimafia
			Function<List<Dichiarazione>, List<Fascicolo>> functionFascicoloDomande = fascicoloDomande -> fascicoloDomande.stream().map(dichiarazione -> {
				Fascicolo fascicolo = new Fascicolo();
				fascicolo.setCuaa(dichiarazione.getAzienda().getCuaa());
				fascicolo.setDenominazione(dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getDenominazione());
				return fascicolo;
			}).collect(Collectors.toList());

			// Funzione che per ogni fascicolo va a recuperare quei fascicoli su cui l'operatore CAA può effettuare operazioni
			Function<List<Fascicolo>, List<Fascicolo>> functionFascicoliEnteUtente = fascicoliEnteUtente -> {
				List<Fascicolo> fascicoloToReturn = new ArrayList<>();

				fascicoliEnteUtente.forEach(f -> {
					try {
						ParamsRicercaFascicolo filtro = new ParamsRicercaFascicolo();
						filtro.setCuaa(f.getCuaa());
						/*
						 * Filtro commentato perche' possono verificarsi casi in
						 * cui la denominazione è diversa tra quanto presente in
						 * AGS e PARIX. Ad esempio caso cuaa = 02333890222 dove
						 * in un sistema c'è una denominazione azienda con apici
						 * e nell'altro no.
						 * 
						 *	filtro.setDenominazione(f.getDenominazione());
			 			 */
						String filtroString = objectMapper.writeValueAsString(filtro);
						List<Fascicolo> fascicoli = consultazioneService.getFascicoliEnti(filtroString);
						fascicoli.forEach(fas -> fascicoloToReturn.add(fas));
					} catch (Exception e) {
						logger.error("Errore: ", e);
					}
				});

				return fascicoloToReturn;
			};

			// Funzione che prende in input una lista di facicoli in relazione con utenteEnte, una lista di domande(decorata, e cioè con stato RIFIUTATA o VERIFIA_PERIODICA)
			// e restituisce una lista di domande per cui l'operatore CAA ha delega
			BiFunction<List<Dichiarazione>, List<Fascicolo>, List<Dichiarazione>> functionDomandeEnteUtente = (dInput, fInput) -> dInput.stream()
					.filter(dichiarazione -> (fInput.stream().filter(f -> dichiarazione.getAzienda().getCuaa().equals(f.getCuaa())).count()) > 0).collect(Collectors.toList());

			listDichiarazione = functionDomandeEnteUtente.apply(listDichiarazione, functionFascicoloDomande.andThen(functionFascicoliEnteUtente).apply(listDichiarazione));
		} catch (Exception e) {
			logger.error("Errore: ", e);
			throw e;
		}
		return listDichiarazione;
	}
}
