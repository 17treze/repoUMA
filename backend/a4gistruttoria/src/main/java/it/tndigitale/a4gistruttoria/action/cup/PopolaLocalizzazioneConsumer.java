package it.tndigitale.a4gistruttoria.action.cup;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.LOCALIZZAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;

@Component
public class PopolaLocalizzazioneConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	private static final Logger logger = LoggerFactory.getLogger(PopolaLocalizzazioneConsumer.class);
	
	public static final List<String> province04 = Arrays.asList("022", "021");
	
	@Override
	public void accept(CupHandler handler, CUPGENERAZIONE generazione) {
		AnagraficaAzienda anagraficaAzienda = handler.getAnagraficaAzienda();
		ObjectFactory of = new ObjectFactory();
		LOCALIZZAZIONE localizzazione = of.createLOCALIZZAZIONE();
		localizzazione.setStato("05");
		String provincia = anagraficaAzienda.getProvinciaResidenza();
		if (province04.contains(provincia)) {
			localizzazione.setRegione("04");
		} else {
			logger.error("Trovata provincia non prevista: {} per l'azienda con cuaa {}", provincia, anagraficaAzienda.getCuaa());
			throw new RuntimeException("Trovata provincia non prevista: " + provincia + " per l'azienda con cuaa " + anagraficaAzienda.getCuaa());
		}
		localizzazione.setProvincia(anagraficaAzienda.getProvinciaResidenza());
		localizzazione.setComune(anagraficaAzienda.getProvinciaResidenza().concat(anagraficaAzienda.getComuneResidenza()));
		generazione.getLOCALIZZAZIONE().add(localizzazione);
	} 

}
