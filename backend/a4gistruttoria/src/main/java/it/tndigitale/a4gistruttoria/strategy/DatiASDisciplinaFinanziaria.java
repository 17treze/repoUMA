package it.tndigitale.a4gistruttoria.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class DatiASDisciplinaFinanziaria extends DatiDisciplinaFinanziariaAbstract {
	
	public Map<String, String> recupera(Long idIstruttoria) {
		return recuperaDisciplinaFinanziaria(idIstruttoria);
	}

	@Override
	public Sostegno getIdentificativoSostegno() {
		return Sostegno.SUPERFICIE;
	}
	
	@Override
	protected Map<String, String> creaDatiDisciplinaFinanziaria(DatiInput datiInput, DatiOutput datiOutput) {
		Map<String, String> result = new LinkedHashMap<>();
		// Dati input
		result.put(TipoVariabile.DFPERC.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFPERC, datiInput));
		result.put(TipoVariabile.DFFR.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFR, datiInput));
		result.put(TipoVariabile.BPSIMPCALCFIN.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.BPSIMPCALCFIN, datiInput));
		result.put(TipoVariabile.GREIMPCALCFIN.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.GREIMPCALCFIN, datiInput));
		result.put(TipoVariabile.GIOIMPCALCFIN.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.GIOIMPCALCFIN, datiInput));
		result.put("DF".concat(TipoVariabile.DISIMPCALC.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DISIMPCALC, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		
		
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M8.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M8, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M9.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M9, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M10.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M10, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M11.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M11, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M14.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M14, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M15.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M15, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M16.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M16, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALC_M17.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC_M17, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		
		result.put("DF".concat(TipoVariabile.ACSIMPCALC.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M8.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M8, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M9.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M9, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M10.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M10, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M11.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M11, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M14.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M14, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M15.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M15, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M16.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M16, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDO_M17.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDO_M17, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		//XXX in analisi BR1 EVO 6 è definita come ACSIMPCALCLORDO
		result.put("DF".concat(TipoVariabile.ACSIMPCALCLORDOTOT.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCLORDOTOT, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		
		result.put(TipoVariabile.DFIMPLIPAGACS_M8.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M8, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M9.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M9, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M10.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M10, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M11.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M11, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M14.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M14, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M15.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M15, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M16.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M16, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACS_M17.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACS_M17, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		
		result.put(TipoVariabile.DFFRAPPDIS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPDIS, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ, datiInput));
		result.put(TipoVariabile.DFFRAPPACS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS, datiInput));
		
		
		result.put(TipoVariabile.DFFRAPPACS_M8.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M8, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M9.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M9, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M10.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M10, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M11.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M11, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M14.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M14, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M15.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M15, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M16.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M16, datiInput));
		result.put(TipoVariabile.DFFRAPPACS_M17.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS_M17, datiInput));
		
		
		result.put(TipoVariabile.ACSIMPCALCTOT.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCTOT, datiInput));
		result.put(TipoVariabile.ACZIMPCALCTOT.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCTOT, datiInput));
		
		
		// Dati output
		result.put(TipoVariabile.DFIMPLIQACSLORDO.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIQACSLORDO, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M8.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M8, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M9.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M9, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M10.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M10, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M11.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M11, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M14.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M14, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M15.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M15, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M16.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M16, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACS_M17.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACS_M17, datiOutput));
		
		
		result.put(TipoVariabile.DFFRPAGACS.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M8.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M8, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M9.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M9, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M10.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M10, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M11.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M11, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M14.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M14, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M15.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M15, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M16.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M16, datiOutput));
		result.put(TipoVariabile.DFFRPAGACS_M17.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACS_M17, datiOutput));

		//TODO da rinominare secondo documento IDU-EVO-32-06 BR1 output DFIMPDFDFACS-M8....
		result.put(TipoVariabile.DFIMPDFDISACS_M8.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M8, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M9.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M9, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M10.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M10, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M11.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M11, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M14.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M14, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M15.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M15, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M16.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M16, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACS_M17.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACS_M17, datiOutput));

		result.put(TipoVariabile.DFIMPRIDACS.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPRIDACS, datiOutput));
		result.put(TipoVariabile.DFIMPLIQACS.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIQACS, datiOutput));


				
		//Dati ricavabili da output ma da visualizzare in input secondo BR
		result.putIfAbsent(TipoVariabile.DFPERC.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFPERC, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFR.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFR, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPDIS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPDIS, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPACS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPACZ.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ, datiOutput));
		return result;
	}
}
