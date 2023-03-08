package it.tndigitale.a4gistruttoria.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class DatiAZDisciplinaFinanziaria extends DatiDisciplinaFinanziariaAbstract {
	
	public Map<String, String> recupera(Long idIstruttoria) {
		return recuperaDisciplinaFinanziaria(idIstruttoria);
	}

	@Override
	public Sostegno getIdentificativoSostegno() {
		return Sostegno.ZOOTECNIA;
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
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_310.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_310, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_311.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_311, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_313.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_313, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_322.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_322, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_315.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_315, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_316.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_316, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_318.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_318, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_320.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_320, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC_321.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC_321, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALC.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALC, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_310.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_310, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_311.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_311, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_313.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_313, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_322.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_322, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_315.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_315, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_316.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_316, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_318.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_318, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_320.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_320, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO_321.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO_321, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put("DF".concat(TipoVariabile.ACZIMPCALCLORDO.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCLORDO, datiInput)); // Aggiungo "DF" come prefisso per disambiguazione
		result.put(TipoVariabile.DFIMPLIPAGACZ_310.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_310, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_311.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_311, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_313.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_313, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_322.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_322, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_315.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_315, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_316.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_316, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_318.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_318, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_320.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_320, datiInput)); 
		result.put(TipoVariabile.DFIMPLIPAGACZ_321.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIPAGACZ_321, datiInput));
		
		result.put("DF".concat(TipoVariabile.ACSIMPCALC.name()).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALC, datiInput));
		result.put(TipoVariabile.DFFRAPPDIS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPDIS, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ, datiInput));
		
		//definisce l'ordine di visualizzazione delle variabili nell'esito calcoli
		result.put(TipoVariabile.DFFRAPPACZ_310.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_310, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_311.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_311, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_313.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_313, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_322.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_322, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_315.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_315, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_316.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_316, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_318.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_318, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_320.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_320, datiInput));
		result.put(TipoVariabile.DFFRAPPACZ_321.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ_321, datiInput));
		
		
		result.put(TipoVariabile.DFFRAPPACS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS, datiInput));
		result.put(TipoVariabile.ACZIMPCALCTOT.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACZIMPCALCTOT, datiInput));
		result.put(TipoVariabile.ACSIMPCALCTOT.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.ACSIMPCALCTOT, datiInput));
		
		
				
				
		
		
		// Dati output
		result.put(TipoVariabile.DFIMPLIQACZLORDO.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIQACZLORDO, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_310.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_310, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_311.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_311, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_313.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_313, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_322.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_322, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_315.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_315, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_316.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_316, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_318.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_318, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_320.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_320, datiOutput));
		result.put(TipoVariabile.DFFRPAGLORACZ_321.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGLORACZ_321, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_310.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_310, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_311.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_311, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_313.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_313, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_322.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_322, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_315.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_315, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_316.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_316, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_318.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_318, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_320.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_320, datiOutput));
		result.put(TipoVariabile.DFFRPAGACZ_321.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRPAGACZ_321, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_310.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_310, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_311.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_311, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_313.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_313, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_322.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_322, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_315.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_315, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_316.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_316, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_318.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_318, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_320.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_320, datiOutput));
		result.put(TipoVariabile.DFIMPDFDISACZ_321.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPDFDISACZ_321, datiOutput));
		result.put(TipoVariabile.DFIMPRIDACZ.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPRIDACZ, datiOutput));
		result.put(TipoVariabile.DFIMPLIQACZ.name().concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFIMPLIQACZ, datiOutput));

		//Dati ricavabili da output ma da visualizzare in input secondo BR
		result.putIfAbsent(TipoVariabile.DFPERC.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFPERC, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFR.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFR, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPDIS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPDIS, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPACZ.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACZ, datiOutput));
		result.putIfAbsent(TipoVariabile.DFFRAPPACS.name().concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.DFFRAPPACS, datiOutput));
		
		return result;
	}
}
