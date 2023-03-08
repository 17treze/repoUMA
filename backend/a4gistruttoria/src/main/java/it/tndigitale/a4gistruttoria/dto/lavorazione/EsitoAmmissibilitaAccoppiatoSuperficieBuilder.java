package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;

import it.tndigitale.a4gistruttoria.action.InitVariabiliAgricoltoreAttivoConsumer;
import it.tndigitale.a4gistruttoria.action.acs.InitVariabileCampioneSuperficiConsumer;
import it.tndigitale.a4gistruttoria.action.acs.InitVariabileOlioNazionaleConsumer;
import it.tndigitale.a4gistruttoria.action.acs.InitVariabileOlioDOPConsumer;
import it.tndigitale.a4gistruttoria.action.acs.InitVariabileSigecoConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.UnitaMisura;

public class EsitoAmmissibilitaAccoppiatoSuperficieBuilder {
	
	private EsitoAmmissibilitaAccoppiatoSuperficieBuilder() {
	}

	public static EsitoAmmissibilitaAccoppiatoSuperficie buildEsitoAmmissibilitaAccoppiatoSuperficie(CalcoloAccoppiatoHandler handler) throws Exception {
		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		return new EsitoAmmissibilitaAccoppiatoSuperficie()
				.withInfoAgricoltoreAttivo(getCheck(inputListaVariabiliCalcolo, InitVariabiliAgricoltoreAttivoConsumer.INFOAGRATT))
				.withAgricoltoreAttivo(getCheck(inputListaVariabiliCalcolo, InitVariabiliAgricoltoreAttivoConsumer.AGRATT))
				.withCampione(getCheck(inputListaVariabiliCalcolo, InitVariabileCampioneSuperficiConsumer.ISCAMP))
				.withSigeco(calcolaSigeco(inputListaVariabiliCalcolo))
				.withSuperficieMinima(calcolaSuperficieMinima(handler))
				.withOlivo75(briduacs090_olivo75(handler))
				.withOlivoNazionale(briduacs091_olioNaz(handler))
				.withOlivoQualita(briduacs092_olioQual(handler));
	}
	
	protected static Boolean calcolaSuperficieMinima(CalcoloAccoppiatoHandler handler) {
		boolean result = false;
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		VariabileCalcolo vc = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPAMMTOT);
		if (vc != null) {
			BigDecimal supAmmessa = vc.getValNumber(); // MQ
			if (UnitaMisura.ETTARI.equals(TipoVariabile.ACSSUPAMMTOT.getUnitaMisura())) {
				supAmmessa = ConversioniCalcoli.convertiEttariInMetriQuadri(supAmmessa); // quando ETTARI devo calcolare MQ
			}
			result = supAmmessa.compareTo(new BigDecimal("5000")) > 0;
		}
		return result;
	}

	protected static Boolean briduacs092_olioQual(CalcoloAccoppiatoHandler handler) {
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		VariabileCalcolo vc = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPRIC_M17);
		if (vc != null) {
			BigDecimal supRichiestaOlivo = vc.getValNumber();
			// se richiesto olio nazionale allora controllo dato anagrafica
			if (supRichiestaOlivo != null && supRichiestaOlivo.compareTo(BigDecimal.ZERO) > 0) {
				return getCheck(handler.getVariabiliInput(), InitVariabileOlioDOPConsumer.OLIOQUAL);
			}
		}
		return null;
	}

	protected static Boolean briduacs091_olioNaz(CalcoloAccoppiatoHandler handler) {
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		VariabileCalcolo vcM15 = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPRIC_M15);
		VariabileCalcolo vcM16 = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPRIC_M16);
		VariabileCalcolo vcM17 = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPRIC_M17);
		if ((vcM15 != null) || (vcM17 != null) || (vcM16 != null)) {
			BigDecimal supRichiestaM15 = vcM15 != null ? vcM15.getValNumber() : BigDecimal.ZERO;
			BigDecimal supRichiestaM16 = vcM16 != null ? vcM16.getValNumber() : BigDecimal.ZERO;
			BigDecimal supRichiestaM17 = vcM17 != null ? vcM17.getValNumber() : BigDecimal.ZERO;
			// se richiesto olio nazionale allora controllo dato anagrafica
			if ((supRichiestaM15 != null && supRichiestaM15.compareTo(BigDecimal.ZERO) > 0) ||
					(supRichiestaM16 != null && supRichiestaM16.compareTo(BigDecimal.ZERO) > 0) ||
					(supRichiestaM17 != null && supRichiestaM17.compareTo(BigDecimal.ZERO) > 0)) {
				return getCheck(handler.getVariabiliInput(), InitVariabileOlioNazionaleConsumer.OLIONAZ);
			}
		}
		return null;
	}
	
	protected static Boolean briduacs090_olivo75(CalcoloAccoppiatoHandler handler) {
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		VariabileCalcolo vc = outputListaVariabiliCalcolo.get(TipoVariabile.ACSSUPRIC_M16);
		if (vc != null) {
			BigDecimal supRichiestaOlivo = vc.getValNumber();
			// se richiesto olivo pend 75 allora controllo la superficie determinata
			if (supRichiestaOlivo != null && supRichiestaOlivo.compareTo(BigDecimal.ZERO) > 0) {
				return handler.getVariabiliInput().get(TipoVariabile.ACSSUPDETIST_M16) != null;
			}
		}
		return null;
	}
	
	protected static Boolean calcolaSigeco(MapVariabili inputListaVariabiliCalcolo) {
		if (!Boolean.TRUE.equals(getCheck(inputListaVariabiliCalcolo, InitVariabileCampioneSuperficiConsumer.ISCAMP))) {
			return null;
		}
		return getCheck(inputListaVariabiliCalcolo, InitVariabileSigecoConsumer.DOMSIGECOCHIUSA);
	}
	
	protected static Boolean getCheck(MapVariabili inputListaVariabiliCalcolo, TipoVariabile tipo) {
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(tipo);
		if (vc == null) return null;
		return vc.getValBoolean();
	}
}
