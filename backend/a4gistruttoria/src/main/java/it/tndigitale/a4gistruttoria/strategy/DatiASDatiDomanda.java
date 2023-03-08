package it.tndigitale.a4gistruttoria.strategy;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioCalcoloACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.IDettaglioCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class DatiASDatiDomanda extends DatiDomandaAbstract {
	
	public DatiDomandaAccoppiato recupera(Long idIstruttoria) {
		return recuperaDatiDomanda(idIstruttoria);
	}
	
	@Override
	public Sostegno getIdentificativoSostegno() {
		return Sostegno.SUPERFICIE;
	}

	@Override
	protected IDettaglioCalcolo creaDettaglioCalcolo(List<DatiInput> datiInput, List<DatiOutput> datiOutput) {
		DettaglioCalcoloACS dettaglioCalcolo = new DettaglioCalcoloACS();
		dettaglioCalcolo.setM8(creaListaDettaglio(datiInput, datiOutput, "M8"));
		dettaglioCalcolo.setM9(creaListaDettaglio(datiInput, datiOutput, "M9"));
		dettaglioCalcolo.setM10(creaListaDettaglio(datiInput, datiOutput, "M10"));
		dettaglioCalcolo.setM11(creaListaDettaglio(datiInput, datiOutput, "M11"));
		dettaglioCalcolo.setM14(creaListaDettaglio(datiInput, datiOutput, "M14"));
		dettaglioCalcolo.setM15(creaListaDettaglio(datiInput, datiOutput, "M15"));
		dettaglioCalcolo.setM16(creaListaDettaglio(datiInput, datiOutput, "M16"));
		dettaglioCalcolo.setM17(creaListaDettaglio(datiInput, datiOutput, "M17"));
		return dettaglioCalcolo;
	}

	@Override
	protected Map<String, String> creaDatiSintesi(List<DatiInput> datiInput, List<DatiOutput> datiOutput) {
		// Questo dettaglio lavora solo sul primo elemento dei passi di lavorazione
		DatiOutput output = datiOutput.get(0);
		Map<String, String> sintesiCalcolo = new LinkedHashMap<>();
		sintesiCalcolo.put(TipoVariabile.ACSIMPCALCTOT.name(), getValoreVariabile(TipoVariabile.ACSIMPCALCTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACSIMPRIDTOT.name(), getValoreVariabile(TipoVariabile.ACSIMPRIDTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACSIMPRIDRITTOT.name(), getValoreVariabile(TipoVariabile.ACSIMPRIDRITTOT, output));
		return sintesiCalcolo;
	}

	@Override
	protected Map<String, String> creaListaDettaglio(List<DatiInput> datiInput, List<DatiOutput> datiOutput, String suffisso) {
		// Questo dettaglio lavora solo sul primo elemento dei passi di lavorazione
		DatiInput input = datiInput.get(0);
		DatiOutput output = datiOutput.get(0);
		if (input.getVariabiliCalcolo().stream()
				.anyMatch(p -> p.getTipoVariabile().equals(TipoVariabile.valueOf("ACSSUPIMP_".concat(suffisso))))) {

			Map<String, String> result = new LinkedHashMap<>();
			// Dati input
			result.put("ACSSUPIMP".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSSUPIMP_".concat(suffisso)), input));
			result.put("ACSSUPDETIST".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSSUPDETIST_".concat(suffisso)), input));
			result.put("ACSVAL".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSVAL_".concat(suffisso)), input));
			result.put("AGRATT".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("AGRATT"), input));
			result.put("ISCAMP".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ISCAMP"), input));
			result.put("DOMSIGECOCHIUSA".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("DOMSIGECOCHIUSA"), input));
			result.put("PERCRIT".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("PERCRIT"), input));
			String percritistr = getValoreVariabile(TipoVariabile.valueOf("PERCRITISTR"), input);
			if (percritistr != null) {
				result.put("PERCRITISTR".concat(INPUT_SUFFIX), percritistr);
			}
			result.put("OLIONAZ".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("OLIONAZ"), input));
			result.put("OLIOQUAL".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("OLIOQUAL"), input));
			// Dati output
			result.put("ACSSUPRIC".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSSUPRIC_".concat(suffisso)), output));
			result.put("ACSSUPDET".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSSUPDET_".concat(suffisso)), output));
			result.put("ACSSUPAMM".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSSUPAMM_".concat(suffisso)), output));
			result.put("ACSIMPRIC".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSIMPRIC_".concat(suffisso)), output));
			result.put("ACSIMPRID".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSIMPRID_".concat(suffisso)), output));
			result.put("ACSIMPAMM".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSIMPAMM_".concat(suffisso)), output));
			result.put("ACSIMPRIDRIT".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSIMPRIDRIT_".concat(suffisso)), output));
			result.put("ACSIMPCALC".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACSIMPCALC_".concat(suffisso)), output));
			return result;
		} else {
			return Collections.emptyMap();
		}
	}
}
