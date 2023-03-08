package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.AmbitoVariabile;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public class IstruttoriaDisaccoppiatoHelper {
	
	
	public static final  List<StatoIstruttoria> STATI_INIZALI=Arrays.asList(
			StatoIstruttoria.RICHIESTO,
			StatoIstruttoria.CONTROLLI_CALCOLO_KO,
			StatoIstruttoria.CONTROLLI_CALCOLO_OK,
			StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
			StatoIstruttoria.INTEGRATO
		);
	
	public static final  List<StatoIstruttoria> STATI_FINALI=Arrays.asList(
			StatoIstruttoria.CONTROLLI_CALCOLO_KO,
			StatoIstruttoria.CONTROLLI_CALCOLO_OK
		);	
	
	public static final  List<StatoIstruttoria> STATI_INIZALI_LIQ=Arrays.asList(
			StatoIstruttoria.LIQUIDABILE,
			StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO,
			StatoIstruttoria.CONTROLLI_CALCOLO_OK,
			StatoIstruttoria.DEBITI
		);
	public static final  List<StatoIstruttoria> STATI_FINALI_LIQ=Arrays.asList(
			StatoIstruttoria.LIQUIDABILE,
			StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO,
			StatoIstruttoria.DEBITI
		);	
	
	public static final  List<StatoIstruttoria> STATI_INIZALI_INTER=Arrays.asList(
			StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
			StatoIstruttoria.LIQUIDABILE,
			StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK
		);
	
	public static final  List<StatoIstruttoria> STATI_FINALI_INTER=Arrays.asList(
			StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
			//StatoIstruttoria.PAGAMENTO_AUTORIZZATO, 18/03/2020 Con questo nuovo giro ho una transizione in più: da controllo_intersostegno_ok a pagamento_autorizzato che è senza passi
			StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
			StatoIstruttoria.NON_LIQUIDABILE
		);		
	
	public static Predicate<? super TransizioneIstruttoriaModel> predicatoFiltroPerStati(List<StatoIstruttoria> statiIniziali, List<StatoIstruttoria> statiFinali) {
		return (trans -> 
			 statiIniziali.contains(StatoIstruttoria.valueOfByIdentificativo(trans.getA4gdStatoLavSostegno2().getIdentificativo())) &&
					statiFinali.contains(StatoIstruttoria.valueOfByIdentificativo(trans.getA4gdStatoLavSostegno1().getIdentificativo()))
		);
	}
	
	public static List<ControlloFrontend> getControlliSostegnoFromPassi(List<PassoTransizioneModel> infoCalcoli) throws Exception {

		List<ControlloFrontend> controlli = new ArrayList<>();

		for (PassoTransizioneModel infoCalcolo : infoCalcoli) {
			TipologiaPassoTransizione passo = infoCalcolo.getCodicePasso();

			String datiSintesi = infoCalcolo.getDatiSintesiLavorazione();
			if (datiSintesi != null && !datiSintesi.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				DatiSintesi in = mapper.readValue(datiSintesi, DatiSintesi.class);
				if (in != null && in.getEsitiControlli() != null) {
					for (EsitoControllo esito : in.getEsitiControlli()) {
						ControlloFrontend contr = ControlloFrontend.newControlloFrontend(passo, esito);
						if (contr != null) {
							controlli.add(contr);
						}
					}
				}
			}
		}
		Collections.sort(controlli);
		return controlli;
	}	
	
	
	public static List<ControlloFrontend> getDatiDomandaFromPassi(List<PassoTransizioneModel> infoCalcoli) throws Exception {
		List<ControlloFrontend> controlli = new ArrayList<>();

		for (PassoTransizioneModel infoCalcolo : infoCalcoli) {
			TipologiaPassoTransizione passo = infoCalcolo.getCodicePasso();
			addVariabiliCalcolo(infoCalcolo.getDatiInput(), DatiInput.class, controlli, passo, "Input", "DATI IN INGRESSO", 1);
			addVariabiliCalcolo(infoCalcolo.getDatiOutput(), DatiOutput.class, controlli, passo, "Output", "DATI IN USCITA", 2);
		}

		// dati di sintesi
		int i = 0;

		BigDecimal val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPCALCFIN, AmbitoVariabile.OUTPUT);

		controlli.add(new ControlloFrontend(TipoVariabile.IMPCALCFIN.name(), TipoVariabile.IMPCALCFIN.name(), "Importo Calcolato finale",
				VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.IMPCALCFIN, val), ++i));

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT))
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(new ControlloFrontend(TipoVariabile.RIDTOT.name(), TipoVariabile.RIDTOT.name(), "Importo Riduzioni",
					VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.RIDTOT, val), ++i));
		}

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Premio base", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Greening", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPRID, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Giovane", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDRIT, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Per ritardo", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.CONTROLLI_FINALI, TipoVariabile.IMPRIDCAP, AmbitoVariabile.OUTPUT, TipoVariabile.RIDTOT.name(), "Capping", ++i);

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.SANZTOT, AmbitoVariabile.OUTPUT, TipoVariabile.SANZTOT.name(), "Importo Sanzioni",
				++i);

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZREC, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(new ControlloFrontend(TipoVariabile.SANZTOT.name(), "SANZIONI_BASE", "Premio base", VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.BPSIMPSANZ, val),
					++i));
		}

		addVariabileSintesiDatiDomanda(infoCalcoli, controlli, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPSANZ, AmbitoVariabile.OUTPUT, TipoVariabile.SANZTOT.name(), "Greening", ++i);

		val = getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZ, AmbitoVariabile.OUTPUT)
				.add(getValoreVariabile(infoCalcoli, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZREC, AmbitoVariabile.OUTPUT));

		if (val.compareTo(new BigDecimal(0)) > 0) {
			controlli.add(
					new ControlloFrontend(TipoVariabile.SANZTOT.name(), "SANZIONI_GIOVANE", "Giovane", VariabileCalcolo.recuperaValoreStringNumeric(false, false, TipoVariabile.GIOIMPSANZ, val), ++i));
		}

		Collections.sort(controlli);
		return controlli;
	}	
	
	public static BigDecimal getValoreVariabile(List<PassoTransizioneModel> infoCalcoli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile, AmbitoVariabile ambitoVariabile) throws Exception {
		VariabileCalcolo variabileCalcolo = recuperaVariabile(infoCalcoli, passo, tipoVariabile, ambitoVariabile);
		BigDecimal val = new BigDecimal(0);

		if (variabileCalcolo != null && variabileCalcolo.recuperaValoreString(true) != null) {
			val = variabileCalcolo.getValNumber();
		}
		return val;
	}
	
	public static void addVariabileSintesiDatiDomanda(List<PassoTransizioneModel> infoCalcoli, List<ControlloFrontend> controlli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile,
                                                      AmbitoVariabile ambitoVariabile, String pcodice2, String descrizioneControllo, Integer ordineControllo) throws Exception {
		VariabileCalcolo variabileCalcolo = recuperaVariabile(infoCalcoli, passo, tipoVariabile, ambitoVariabile);
		if (variabileCalcolo != null && (variabileCalcolo.recuperaValoreString(true) != null)) {
			controlli.add(new ControlloFrontend(pcodice2, variabileCalcolo.getTipoVariabile().name(), descrizioneControllo, variabileCalcolo.recuperaValoreString(), ordineControllo));
		}
	}
	
	public static VariabileCalcolo recuperaVariabile(List<PassoTransizioneModel> infoCalcoli, TipologiaPassoTransizione passo, TipoVariabile tipoVariabile, AmbitoVariabile ambitoVariabile) throws Exception {
		Optional<PassoTransizioneModel> passOp = infoCalcoli.stream().filter(x -> x.getCodicePasso().equals(passo)).findFirst();
		if (passOp.isPresent()) {
			PassoTransizioneModel passoVariabile = passOp.get();
			switch (ambitoVariabile) {
			case INPUT:
				return mapVal(passoVariabile.getDatiInput(), DatiInput.class, tipoVariabile);
			case OUTPUT:
				return mapVal(passoVariabile.getDatiOutput(), DatiInput.class, tipoVariabile);
			case SINTESI:
				return mapVal(passoVariabile.getDatiSintesiLavorazione(), DatiInput.class, tipoVariabile);
			default:
			}
		}
		return null;
	}
	
	
	public static <T extends DatiCalcoli> VariabileCalcolo mapVal(String content, Class<T> valueType, TipoVariabile tipoVariabile) throws Exception {
		if (content != null && !content.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			T d = mapper.readValue(content, valueType);
			Optional<VariabileCalcolo> variabileOp = d.getVariabiliCalcolo().stream().filter(x -> x.getTipoVariabile().equals(tipoVariabile)).findFirst();
			if (variabileOp.isPresent()) {
				return variabileOp.get();
			}
		}
		return null;
	}
	
	public static <T extends DatiCalcoli> void addVariabiliCalcolo(String content, Class<T> valueType, List<ControlloFrontend> controlli, TipologiaPassoTransizione passo, String pCodice2, String pdescrizione2,
                                                                   Integer order) throws IOException {
		if (content != null && !content.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			T d = mapper.readValue(content, valueType);
			if (d != null && d.getVariabiliCalcolo() != null) {
				for (VariabileCalcolo variabileCalcolo : d.getVariabiliCalcoloDaStampare()) {
					ControlloFrontend contr = new ControlloFrontend(passo, pCodice2, pdescrizione2, order, variabileCalcolo);
					controlli.add(contr);
				}
			}
		}
	}	
}
