package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class GeneraStatisticaCs25 extends GeneraStatisticaBase {

	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs25.class);
	
	@Autowired
	DisaccoppiatoService disaccoppiatoService;

	@Override
	@Transactional
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
		try {
			if (skipIstruttoria(istruttoria)) return;
			StatisticheInputData input = caricaDatiInput(istruttoria, annoCampagna);
			input.setVariabiliCalcolo(recuperaValoriVariabili(input.getPassiLavorazioneEntities()));

			Map<TipoVariabile, VariabileCalcolo> variabili = input.getVariabiliCalcolo();
			Float c552 = getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOSUPRIC, Float.class);
			Float c554 = getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPRIC, Float.class);
			Float c558 = getQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOSUPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOSUPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					);
			Float c559 = getImportoQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPRIDRIT, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					);
			String c600 = getControlloInLoco(input.getCampioneSuperficiEntity(), input.getFlagConv());
			StatisticaDu item = StatisticaDu.empty()
					.withIdDomanda(input.getNumeroDomanda())
					.withStato(istruttoria.getStato())
					.withImpAmm(getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPAMM, Float.class))
					.withC110(annoCampagna.intValue())
					.withC109(getSigla())
					.withC109a(null)
					.withF200(istruttoria.getDomandaUnicaModel().getCuaaIntestatario())
					.withF201(istruttoria.getDomandaUnicaModel().getRagioneSociale())
					.withF202a(input.getInfoCuaa() != null ? input.getInfoCuaa().getIndirizzoRecapito() : null)
					.withF202b(input.getInfoCuaa() != null && input.getInfoCuaa().getCap() != null ? input.getInfoCuaa().getCap().intValue() : null)
					.withF202c(input.getInfoCuaa() != null ? input.getInfoCuaa().getComuneRecapito() : null)
					.withF207(input.getNutsEntity() != null ? input.getNutsEntity().getCodice3() : null)
					.withF300(getNumeroDomandaFormattato(annoCampagna, input.getNumeroDomanda()))
					.withC300a(getRitardoPresentazioneDomanda(input.getDataProtocollazione(),input.getConfIstruttorieDto(),input.getConfRicevibilita()))
					.withF300b(input.getDataProtocollazione())
					.withC400(null)
					.withC401(null)
					.withC402(null)
					.withC403(null)
					.withC403a(null)
					.withC404(null)
					.withC405(null)
					.withC406(null)
					.withC407(null)
					.withC551(getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPIMP, Float.class))
					.withC552(getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOSUPRIC, Float.class))
					.withC554(getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPRIC, Float.class))
					.withC554a(null)
					.withC557(null)
					.withC558(getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOSUPAMM, Float.class))
					.withC558a(null)
					.withC558b(null)
					.withC558c(null)
					.withC558d(null)
					.withC558e(null)
					.withC558f(null)
					.withC559(c559)
					.withC560(getQuantitaNonPagataASeguitoDiControlli(c552, c558))
					.withC560a(null)
					.withC561()
					.withC561a(null)
					.withC561b(null)
					.withC600(c600)
					.withC605(null)
					.withC611(getMetodoSelezioneControlliInLoco(input.getCampioneSuperficiEntity()))
					.withC620(getDomandaIrricevibileNonAmmissibile(c552, c554, c559, c600))
					.withC621()
					.withC640(getSanzioneIrrogata(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPSANZ, Float.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GIOIMPSANZREC, Float.class)))
					.withC640a(null);

			statisticheService.salvaStatistica(item, getTipoDatoAnnuale());

		} catch (Exception e) {
			logger.error("Impossibile generare le informazioni " + 
					getTipoDatoAnnuale().name() + 
					" per l'istruttoria con id ".concat(istruttoria.getId().toString()), e);
		}
	}
	
	@Override
	public TipologiaStatistica getTipoDatoAnnuale() {
		return TipologiaStatistica.CS25;
	}
	
	@Override
	protected String getSigla() {
		return "CS-25";
	}

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.DISACCOPPIATO;
	}

	private Float getQuantitaNonPagataASeguitoDiControlli(Float c552, Float c558) {
		if (c552 == null || c558 == null) {
			return null;
		}
		BigDecimal var1 = BigDecimal.valueOf(c552);
		BigDecimal var2 = BigDecimal.valueOf(c558);
		return var1.subtract(var2).setScale(4, RoundingMode.HALF_UP).floatValue();
	}

	private String getDomandaIrricevibileNonAmmissibile(Float c552, Float c554, Float c559, String c600) {
		if (c552 == null || c554 == null || c559 == null || c600 == null) {
			return null;
		}

		if (c552.compareTo(0f) == 0)
			if (c600.equals("N"))
				return "1";
			else
				return "3";
		else if (c554.compareTo(0f) == 0)
			return "2";
		else if(c559.compareTo(0f) == 0)
			if (c600.equals("N"))
				return "2";
			else
				return "3";
		else 
			return "4";
	}
	
	@Override
	protected Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passiBps) {

		Map<TipoVariabile, VariabileCalcolo> result = super.recuperaValoriVariabili(passiBps);
		try {
			result.put(TipoVariabile.GIOSUPRIC, getVariabile(TipoVariabile.GIOSUPRIC, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
			result.put(TipoVariabile.GIOSUPAMM, getVariabile(TipoVariabile.GIOSUPAMM, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
			result.put(TipoVariabile.GIOIMPRIC, getVariabile(TipoVariabile.GIOIMPRIC, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
			result.put(TipoVariabile.GIOIMPAMM, getVariabile(TipoVariabile.GIOIMPAMM, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
			result.put(TipoVariabile.GIOIMPRIDRIT, getVariabile(TipoVariabile.GIOIMPRIDRIT, passiBps, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));
			result.put(TipoVariabile.GIOIMPSANZ, getVariabile(TipoVariabile.GIOIMPSANZ, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
			result.put(TipoVariabile.GIOIMPSANZREC, getVariabile(TipoVariabile.GIOIMPSANZREC, passiBps, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
		} catch (Exception e) {
			logger.debug("Errore in recupero variabili", e);
		}
		return result;
	}
	
	/**
	 * Metodo usato per recupare solo le istruttorie che rispettano alcune condizioni, come ad esempio
	 * - CS25: VANNO CONSIDERATE SOLTANTO LE DOMANDE CHE HANNO RICHIESTO L'INTERVENTO GIOVANE
	 * - CS27: PER QUANTO RIGUARDA L'ACCOPPAITO SUPERFICIE VANNO INSERITI RECORD SOLTANTO DI DOMANDE CHE HANNO UNA SUPERIFCIE RICHIESTA COMPLESSIVA SU TTUTI GLI INTERVENTI > 5000 mq
	 * @param Istruttoria
	 * @return
	 */
	private boolean skipIstruttoria(IstruttoriaModel ultimaIstruttoria) {
		return !disaccoppiatoService.isGiovane(ultimaIstruttoria.getDomandaUnicaModel());
	}
	
}
