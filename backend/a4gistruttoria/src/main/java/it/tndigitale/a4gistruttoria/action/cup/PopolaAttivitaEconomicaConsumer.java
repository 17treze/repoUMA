package it.tndigitale.a4gistruttoria.action.cup;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.cup.dto.ATTIVECONOMICABENEFICIARIOATECO2007;
import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;

@Component
public class PopolaAttivitaEconomicaConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	private static final Logger logger = LoggerFactory.getLogger(PopolaAttivitaEconomicaConsumer.class);
	
	protected static final List<String> province04 = Arrays.asList("022", "021");
	
	@Override
	public void accept(CupHandler handler, CUPGENERAZIONE generazione) {
		AnagraficaAzienda anagraficaAzienda = handler.getAnagraficaAzienda();
		String attivita = anagraficaAzienda.getAttivita();
		if (attivita != null && !attivita.isEmpty()) {
			ObjectFactory of = new ObjectFactory();
			ATTIVECONOMICABENEFICIARIOATECO2007 atteco = of.createATTIVECONOMICABENEFICIARIOATECO2007();
			String divisione = attivita.substring(0, 2);
			int divInt = Integer.parseInt(divisione);
			if (divInt < 4) {
				atteco.setSezione("A");
			} else if (44 < divInt && divInt < 48) {
				atteco.setSezione("G");
			} else if (93 < divInt && divInt < 97) {
				atteco.setSezione("S");
			} else {
				logger.error("Divisione {} non gestita per il cuaa {}", divisione, anagraficaAzienda.getCuaa());
				throw new RuntimeException("Divisione " + divisione + " non gestita per il cuaa "  + anagraficaAzienda.getCuaa());
			}
			atteco.setDivisione(divisione);
			String classe = attivita.substring(divisione.length());
			int classeNum = Integer.parseInt(classe);
			if (classeNum > 9) {
				if (classeNum > 70) {
					classeNum = classeNum / 10;
					atteco.setCategoria(divisione + "." + classeNum + "." + classe.substring(("" + classeNum).length()));
				}
				atteco.setClasse(divisione + "." + classeNum);
				classeNum = classeNum / 10;
				atteco.setGruppo(divisione + "." + classeNum);
			} else {
				atteco.setGruppo(divisione + "." + classe);
			}
			generazione.setATTIVECONOMICABENEFICIARIOATECO2007(atteco);
		}
	} 

}
