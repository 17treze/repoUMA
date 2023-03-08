package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class GeneraStatisticaCs22 extends GeneraStatisticaBase {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs22.class);

	@Override
	@Transactional
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
		try {
			StatisticheInputData input = caricaDatiInput(istruttoria, annoCampagna);
			Map<TipoVariabile, VariabileCalcolo> variabili = input.getVariabiliCalcolo();
			//c400
			String obblighiInverdimento = getObblighiInverdimento(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREAZIBIO, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREESESEM, Boolean.class, false),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPPP, BigDecimal.class)
					);
			String diversificazioneColture = getDiversificazioneColture(
					obblighiInverdimento, 
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREESEDIV, Boolean.class,false), 
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVPERC1COL, BigDecimal.class), 
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVPERC2COL, BigDecimal.class, false)
					);
			Float c552 = getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPRIC, Float.class);
			Float c554 = getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPRIC, Float.class);
			Float c558 = getQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.BPSSUPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					);
			Float c559 = getImportoQuantitaDeterminata(
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPAMM, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPRIDRIT, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPRIC, Float.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.ISCAMP, Boolean.class),
					getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
					);
			String c600 = getControlloInLoco(input.getCampioneSuperficiEntity(), input.getFlagConv());
			StatisticaDu item = StatisticaDu.empty()
					.withIdDomanda(input.getNumeroDomanda())
					.withStato(istruttoria.getStato())
					.withImpAmm(getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPAMM, Float.class))
					.withC110(annoCampagna.intValue())
					.withC109(getSigla())
					.withC109a(null)
					.withF200(istruttoria.getDomandaUnicaModel().getCuaaIntestatario())
					.withF201(istruttoria.getDomandaUnicaModel().getRagioneSociale())
					.withF202a(input.getInfoCuaa() != null ? input.getInfoCuaa().getIndirizzoRecapito() : null)
					.withF202b(input.getInfoCuaa() != null && input.getInfoCuaa().getCap() != null ? input.getInfoCuaa().getCap().intValue() : null)
					.withF202c(input.getInfoCuaa() != null ? input.getInfoCuaa().getComuneRecapito() : null)
					.withF207(input.getNutsEntity().getCodice3())
					.withF300(getNumeroDomandaFormattato(annoCampagna, input.getNumeroDomanda()))
					.withC300a(getRitardoPresentazioneDomanda(input.getDataProtocollazione(),input.getConfIstruttorieDto(),input.getConfRicevibilita()))
					.withF300b(input.getDataProtocollazione())
					.withC400(obblighiInverdimento)
					.withC401(getInverdimentoPraticheEquivalenti(obblighiInverdimento))
					.withC402(getInverdimentoAttuazioneCollettiva(obblighiInverdimento))
					.withC403(getInverdimentoRiconversionePratoPermanente(obblighiInverdimento))
					.withC403a(getInverdimentoMantenimentoPorzionePratoPermanente(obblighiInverdimento))
					.withC404("N")
					.withC405(diversificazioneColture)
					.withC406(getInverdimentoRichiestaEfa(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPSEM, BigDecimal.class), obblighiInverdimento))
					.withC407(null)
					.withC551(null)
					.withC552(c552)
					.withC554(c554)
					.withC554a(null)
					.withC557(null)
					.withC558(c558)
					.withC558a(getQuantitaDeterminataColturaPrincipale(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVSUP1COL, Float.class,false),//testato se null all'interno del metodo
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPSEM, Float.class),
							obblighiInverdimento))
					.withC558b(getQuantitaDeterminataSecondaColtura(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPSEM, BigDecimal.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVSUP1COL, BigDecimal.class,false),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVSUP2COL, BigDecimal.class, false),
							obblighiInverdimento))
					.withC558c(getQuantitaDeterminataAreaInteresseEcologico(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPAZOTODET, Float.class), obblighiInverdimento))
					.withC558d(getQuantitaDeterminataPratiPermanenti(
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPPP, Float.class), obblighiInverdimento))
					.withC558e(null) // TODO: in attesa di speficiche!
					.withC558f(getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPSEM, Float.class))
					.withC559(c559)
					.withC560(getQuantitaNonPagataASeguitoDiControlli(c552, c558,getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPRID, BigDecimal.class)))
					.withC560a(getInverdimentoNonConforme(obblighiInverdimento, diversificazioneColture,
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVSUP1COL, BigDecimal.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVSUP2COL, BigDecimal.class),
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPSEM, BigDecimal.class), 
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVPERC1COL, BigDecimal.class), 
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREDIVPERC2COL, BigDecimal.class, false), 
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GREEFAPERCAZOTO, BigDecimal.class, false), 
							getValoreVariabilePrecaricata(variabili, TipoVariabile.GRESUPAZOTOPOND, BigDecimal.class)))
					.withC561()
					.withC561a(null)
					.withC561b(null)
					.withC600(c600)
					.withC605(null)
					.withC611(getMetodoSelezioneControlliInLoco(input.getCampioneSuperficiEntity()))
					.withC620(getDomandaIrricevibileNonAmmissibile(c552, c554, c559, c600))
					.withC621()
					//.withC633 sta in empty --> withC633("N")
					.withC640(toNegativeValue(getValoreVariabilePrecaricata(variabili, TipoVariabile.GREIMPSANZ, Float.class)))
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
		return TipologiaStatistica.CS22;
	}
	
	@Override
	protected String getSigla() {
		return "CS-22";
	}

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.DISACCOPPIATO;
	}
	
	private Float getQuantitaNonPagataASeguitoDiControlli(Float c552, Float c558, BigDecimal gresuprid) {
		if (c552 == null || c558 == null) {
			return null;
		}
		BigDecimal var1 = BigDecimal.valueOf(c552);
		BigDecimal var2 = BigDecimal.valueOf(c558);
		return var1.subtract(var2).add(gresuprid).setScale(4, RoundingMode.HALF_UP).floatValue();
	}

	private String getDiversificazioneColture(String obblighiInverdimento, Boolean greEseDiv, BigDecimal greDivPerc1col, BigDecimal greDivPerc2col) {
		if (!"1".equals(obblighiInverdimento)) {
			return null;
		} else {
			if (greEseDiv == null || greEseDiv == true) {
				return "4";
			} else {
				if (greDivPerc1col.compareTo(BigDecimal.ONE) == 0) { //IF GREDIVPERC1COL = 100%
					return "1";
				} else {
					if (greDivPerc2col == null || greDivPerc2col.compareTo(BigDecimal.ONE) == 0) {
						return "2";
					}
					return "3";
				}
			}
		}
	}
	
	private Float getInverdimentoNonConforme(String obblighiInverdimento, String diversificazioneColture, BigDecimal greDivSup1col, BigDecimal greDivSup2col, BigDecimal greSupSem, BigDecimal greDivPerc1col, 
			BigDecimal greDivPerc2col, BigDecimal greEfaPercAzoto, BigDecimal greSupAzotoPond) {
		if (!"1".equals(obblighiInverdimento)) {
			return null;
		} else if ("4".equals(diversificazioneColture)) {
			return BigDecimal.ZERO.floatValue();
		} else {
			BigDecimal perct1 = BigDecimal.valueOf(0.75d);
			BigDecimal perct2 = BigDecimal.valueOf(0.95d);
			BigDecimal perct3 = BigDecimal.valueOf(0.05d);
			BigDecimal primoAddendo = (greDivPerc1col.compareTo(perct1) > 0) 
					? greDivSup1col.subtract(greSupSem.multiply(perct1)).setScale(4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
			BigDecimal secondoAddendo = (greDivPerc2col != null && greDivPerc2col.compareTo(perct2) > 0) 
					? (greDivSup1col.add(greDivSup2col)).subtract(greSupSem.multiply(perct2)).setScale(4, RoundingMode.HALF_UP): BigDecimal.ZERO;
			BigDecimal terzoAddendo = (greEfaPercAzoto != null && greEfaPercAzoto.compareTo(perct3) < 0) 
					? greSupSem.multiply(perct3).subtract(greSupAzotoPond).setScale(4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
			return primoAddendo.add(secondoAddendo).add(terzoAddendo).floatValue();
		}
	}
	
	private Float getQuantitaDeterminataSecondaColtura(BigDecimal greSupSem, BigDecimal greDivPerc1col, BigDecimal greDivPerc2col, String c400) {
		if (!c400.equals("1")) 
			return null;
		
		return greDivPerc2col != null ? greDivPerc2col.floatValue() :  (greDivPerc1col != null ? greSupSem.subtract(greDivPerc1col).floatValue() : 0);
		
	}
	
	private Float getInverdimentoRichiestaEfa(BigDecimal greSupSem, String c400) {
		if (c400 != null && !"1".equals(c400))
			return null;
		if (greSupSem.compareTo(BigDecimal.valueOf(15.00d)) > 0) {
			return greSupSem.multiply(BigDecimal.valueOf(0.05d)).setScale(4, RoundingMode.HALF_UP).floatValue();
		}
		return BigDecimal.ZERO.floatValue();
	}
	
	private Float getQuantitaDeterminataColturaPrincipale(Float greDivSup1Col,Float greSupSem, String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		return greDivSup1Col != null ? greDivSup1Col : greSupSem;
	}
	
	private String getInverdimentoPraticheEquivalenti(String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return "N";
	}
	
	private Float getInverdimentoAttuazioneCollettiva(String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return 0.00f;
	}
	
	private String getInverdimentoRiconversionePratoPermanente(String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return "N";
	}
	
	private Float getInverdimentoMantenimentoPorzionePratoPermanente(String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return 0.00f;
	}
	
	private Float getQuantitaDeterminataAreaInteresseEcologico(Float greSupAzotoDet, String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return greSupAzotoDet;
	}
	
	private Float getQuantitaDeterminataPratiPermanenti(Float greSupp, String c400) {
		if (c400 != null && !c400.equals("1"))
			return null;
		else return greSupp;
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
			result.put(TipoVariabile.GREDIVPERC1COL, getVariabile(TipoVariabile.GREDIVPERC1COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREDIVPERC2COL, getVariabile(TipoVariabile.GREDIVPERC2COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GRESUPSEM, getVariabile(TipoVariabile.GRESUPSEM, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREDIVSUP1COL, getVariabile(TipoVariabile.GREDIVSUP1COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREDIVSUP2COL, getVariabile(TipoVariabile.GREDIVSUP2COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GRESUPAZOTODET, getVariabile(TipoVariabile.GRESUPAZOTODET, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GRESUPPP, getVariabile(TipoVariabile.GRESUPPP, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREIMPAMM, getVariabile(TipoVariabile.GREIMPAMM, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREIMPRIDRIT, getVariabile(TipoVariabile.GREIMPRIDRIT, passiBps, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));
			result.put(TipoVariabile.GRESUPRID, getVariabile(TipoVariabile.GRESUPRID, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREDIVPERC1COL, getVariabile(TipoVariabile.GREDIVPERC1COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREDIVPERC2COL, getVariabile(TipoVariabile.GREDIVPERC2COL, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREEFAPERCAZOTO, getVariabile(TipoVariabile.GREEFAPERCAZOTO, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GRESUPAZOTOPOND, getVariabile(TipoVariabile.GRESUPAZOTOPOND, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREIMPSANZ, getVariabile(TipoVariabile.GREIMPSANZ, passiBps, TipologiaPassoTransizione.GREENING).orElse(null));
			result.put(TipoVariabile.GREPERC, getVariabile(TipoVariabile.GREPERC, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			result.put(TipoVariabile.GREIMPRIC, getVariabile(TipoVariabile.GREIMPRIC, passiBps, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
			
		} catch (Exception e) {
			logger.debug("Errore in recupero variabili", e);
		}
		return result;
	}
	
	
}
