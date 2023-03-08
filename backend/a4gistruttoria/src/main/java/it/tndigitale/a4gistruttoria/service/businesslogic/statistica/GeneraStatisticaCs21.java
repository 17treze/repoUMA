package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class GeneraStatisticaCs21 extends GeneraStatisticaBase {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs21.class);

	@Override
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
		try {
			StatisticheInputData input = caricaDatiInput(istruttoria, annoCampagna);
			
			Map<TipoVariabile, VariabileCalcolo> variabili = input.getVariabiliCalcolo();
			
			Float c552 = getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPRIC, Float.class);
			Float c554 = getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPRIC, Float.class);
			Float c558 = getQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					);
			Float c559 = (getImportoQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPRIDRIT, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					));
			String c600 = getControlloInLoco(input.getCampioneSuperficiEntity(), input.getFlagConv());
			StatisticaDu item = StatisticaDu.empty()
					.withIdDomanda(input.getNumeroDomanda())
					.withStato(istruttoria.getStato())
					.withImpAmm(getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPAMM, Float.class))
					.withC110(annoCampagna)
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
					.withC400(getObblighiInverdimento(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREAZIBIO, Boolean.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREESESEM, Boolean.class, false),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPPP, BigDecimal.class)
							))
					.withC401(null)
					.withC402(null)
					.withC403(null)
					.withC403a(null)
					.withC404(null)
					.withC405(null)
					.withC406(null)
					.withC407("N")
					.withC551(getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPIMP, Float.class))
					.withC552(c552)
					.withC554(c554)
					.withC554a(null)
					.withC557(getQuantitaMisurata(
							input.getNumeroDomanda(),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPIMP, Float.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPSIGECO, Float.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPDETIST, Float.class,false),//impostato a false pe ril controllo interno a getQuantitaMisurata
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPELE, Float.class)
							))
					.withC558(c558)
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
					.withC600(getControlloInLoco(input.getCampioneSuperficiEntity(), input.getFlagConv()))
					.withC605(null)
					.withC611(getMetodoSelezioneControlliInLoco(input.getCampioneSuperficiEntity()))
					.withC620(getDomandaIrricevibileNonAmmissibile(c552, c554, c559, c600))
					.withC621()
					.withC640(getSanzioneIrrogata(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPSANZ, Float.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSIMPSANZREC, Float.class)
							))
					.withC640a(null)
					;
			
			statisticheService.salvaStatistica(item, getTipoDatoAnnuale());
		} catch (Exception e) {
			logger.error("Impossibile generare le informazioni " + 
				getTipoDatoAnnuale().name() + 
				" per l'istruttoria con id ".concat(istruttoria.getId().toString()), e);
		}
	}
	
	@Override
	public TipologiaStatistica getTipoDatoAnnuale() {
		return TipologiaStatistica.CS21;
	}
	
	@Override
	protected String getSigla() {
		return "CS-21";
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
}
