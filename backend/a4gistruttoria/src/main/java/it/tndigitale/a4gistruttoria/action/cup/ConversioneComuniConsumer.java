package it.tndigitale.a4gistruttoria.action.cup;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;

@Component
public class ConversioneComuniConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	private Map<String, String> comuni = new HashMap<>();
	
	
	public ConversioneComuniConsumer() {
		super();
		comuni.put("022233", "022075");
		comuni.put("022234", "022140");
		comuni.put("022235", "022082");
		comuni.put("022236", "022212");
		comuni.put("022237", "022004");
		comuni.put("022238", "022066");
		comuni.put("022239", "022019");
		comuni.put("022240", "022185");
		comuni.put("022241", "022055");
		comuni.put("022242", "022069");
		comuni.put("022243", "022031");
		comuni.put("022244", "022223");
		comuni.put("022245", "022084");
		comuni.put("022246", "022020");
		comuni.put("022247", "022151");
		comuni.put("022248", "022215");
		comuni.put("022249", "022187");		
	}




	@Override
	public void accept(CupHandler datiCUP, CUPGENERAZIONE cupDomanda) {
		cupDomanda.getLOCALIZZAZIONE().forEach(localizzazione -> {
			String comune = localizzazione.getComune();
			if (comuni.keySet().contains(comune)) {
				// System.err.println("Trovato comune " + comune + " per cuaa " + cuaa);
				comune = comuni.get(comune);
				// System.err.println("Sostituito comune vecchio " + comune + " per cuaa " + cuaa);
			}
			localizzazione.setComune(comune);
		});
		
	}

}
