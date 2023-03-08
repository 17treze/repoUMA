package it.tndigitale.a4gistruttoria.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiDomandaAccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioCalcoloACZ;
import it.tndigitale.a4gistruttoria.dto.lavorazione.IDettaglioCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class DatiAZDatiDomanda extends DatiDomandaAbstract {
	
	private static Map<String, String> variabiliInterventoPerUba;
	static {
		variabiliInterventoPerUba = new HashMap<>();
		variabiliInterventoPerUba.put("310", "LAT");
		variabiliInterventoPerUba.put("311", "LAT");
		variabiliInterventoPerUba.put("313", "LAT");
		variabiliInterventoPerUba.put("322", "LAT");
		variabiliInterventoPerUba.put("315", "MAC");
		variabiliInterventoPerUba.put("316", "MAC");
		variabiliInterventoPerUba.put("318", "MAC");
		variabiliInterventoPerUba.put("320", "OVI");
		variabiliInterventoPerUba.put("321", "OVI");
	}

	@Override
	public Sostegno getIdentificativoSostegno() {
		return Sostegno.ZOOTECNIA;
	}

	public DatiDomandaAccoppiato recupera(Long idIstruttoria) {
		return recuperaDatiDomanda(idIstruttoria);
	}

	@Override
	protected Map<String, String> creaDatiSintesi(List<DatiInput> datiInput, List<DatiOutput> datiOutput) {
		Map<String, String> sintesiCalcolo = new LinkedHashMap<>();
		// Questo dettaglio lavora solo sul primo elemento dei passi di lavorazione
		DatiOutput output = datiOutput.get(0);
		sintesiCalcolo.put(TipoVariabile.ACZIMPCALCTOT.name(), getValoreVariabile(TipoVariabile.ACZIMPCALCTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACZIMPRIDTOT.name(), getValoreVariabile(TipoVariabile.ACZIMPRIDTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACZIMPRIDSANZTOT.name(), getValoreVariabile(TipoVariabile.ACZIMPRIDSANZTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACZIMPRIDRITTOT.name(), getValoreVariabile(TipoVariabile.ACZIMPRIDRITTOT, output));
		sintesiCalcolo.put(TipoVariabile.ACZIMPDEBCTOT.name(), getValoreVariabile(TipoVariabile.ACZIMPDEBCTOT, output));
		return sintesiCalcolo;
	}

	@Override
	protected IDettaglioCalcolo creaDettaglioCalcolo(List<DatiInput> datiInput, List<DatiOutput> datiOutput) {
		DettaglioCalcoloACZ dettaglioCalcolo = new DettaglioCalcoloACZ();
		dettaglioCalcolo.setInt310(creaListaDettaglio(datiInput, datiOutput, "310"));
		dettaglioCalcolo.setInt311(creaListaDettaglio(datiInput, datiOutput, "311"));
		dettaglioCalcolo.setInt313(creaListaDettaglio(datiInput, datiOutput, "313"));
		dettaglioCalcolo.setInt315(creaListaDettaglio(datiInput, datiOutput, "315"));
		dettaglioCalcolo.setInt316(creaListaDettaglio(datiInput, datiOutput, "316"));
		dettaglioCalcolo.setInt318(creaListaDettaglio(datiInput, datiOutput, "318"));
		dettaglioCalcolo.setInt320(creaListaDettaglio(datiInput, datiOutput, "320"));
		dettaglioCalcolo.setInt321(creaListaDettaglio(datiInput, datiOutput, "321"));
		dettaglioCalcolo.setInt322(creaListaDettaglio(datiInput, datiOutput, "322"));
		return dettaglioCalcolo;
	}

	@Override
	protected Map<String, String> creaListaDettaglio(List<DatiInput> datiInput, List<DatiOutput> datiOutput, String suffisso) {
		// Questo dettaglio lavora solo sul primo elemento dei passi di lavorazione
		DatiInput input = datiInput.get(0);
		DatiOutput output = datiOutput.get(0);
		if (input.getVariabiliCalcolo().stream()
				.anyMatch(p -> p.getTipoVariabile().equals(TipoVariabile.valueOf("ACZCAPIRIC_".concat(suffisso))))) {

			Map<String, String> result = new LinkedHashMap<>();
			// Dati input
			result.put("ACZCAPIRIC".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPIRIC_".concat(suffisso)), input));
			result.put("ACZCAPIDUP".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPIDUP_".concat(suffisso)), input));
			result.put("ACZCAPIRICNET".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPIRICNET_".concat(suffisso)), input));
			result.put("ACZCAPISANZ".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPISANZ_".concat(suffisso)), input));
			result.put("ACZCAPIACC".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPIACC_".concat(suffisso)), input));
			result.put("ACZUBA".concat(variabiliInterventoPerUba.get(suffisso)).concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZUBA_".concat(variabiliInterventoPerUba.get(suffisso))), input));
			result.put("ACZVAL".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZVAL_".concat(suffisso)), input));
			result.put("AGRATT".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("AGRATT"), input));
			result.put("AZCMPBOV".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("AZCMPBOV"), input));
			result.put("AZCMPOVI".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("AZCMPOVI"), input));
			result.put("PERCRIT".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("PERCRIT"), input));
			String percritistr = getValoreVariabile(TipoVariabile.valueOf("PERCRITISTR"), input);
			if (percritistr != null) {
				result.put("PERCRITISTR".concat(INPUT_SUFFIX), percritistr);
			}
			result.put("PERCSANZDET".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("PERCSANZDET_".concat(suffisso)), input));
			result.put("PERCSANZ".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("PERCSANZ_".concat(suffisso)), input));
			result.put("ACZCAPIDEB".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZCAPIDEB_".concat(suffisso)), input));
			if (suffisso.equals("320")) {
				result.put("ACZOVIADULTI".concat(INPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZOVIADULTI_".concat(suffisso)), input));
			}
			// Dati output
			result.put("ACZIMPRICNET".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPRICNET_".concat(suffisso)), output));
			result.put("ACZIMPRID".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPRID_".concat(suffisso)), output));
			result.put("ACZIMPACC".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPACC_".concat(suffisso)), output));
			result.put("ACZIMPRIDSANZ".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPRIDSANZ_".concat(suffisso)), output));
			result.put("ACZIMPRIDRIT".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPRIDRIT_".concat(suffisso)), output));
			result.put("ACZIMPCALC".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPCALC_".concat(suffisso)), output));
			result.put("ACZIMPDEB".concat(OUTPUT_SUFFIX), getValoreVariabile(TipoVariabile.valueOf("ACZIMPDEB_".concat(suffisso)), output));
			return result;
		} else {
			return Collections.emptyMap();
		}
	}
}
