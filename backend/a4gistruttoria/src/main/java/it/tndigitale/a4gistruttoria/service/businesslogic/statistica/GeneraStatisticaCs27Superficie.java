package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class GeneraStatisticaCs27Superficie extends GeneraStatisticaCs27 {
    private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs27Superficie.class);

    private static Map<String, String> SUFFISSO_VARIABILE_MISURA;
    static {
        SUFFISSO_VARIABILE_MISURA = new HashMap<>();
        SUFFISSO_VARIABILE_MISURA.put("M8", "M08");
        SUFFISSO_VARIABILE_MISURA.put("M9", "M09");
        SUFFISSO_VARIABILE_MISURA.put("M10", "M10");
        SUFFISSO_VARIABILE_MISURA.put("M11", "M11");
        SUFFISSO_VARIABILE_MISURA.put("M14", "M14");
        SUFFISSO_VARIABILE_MISURA.put("M15", "M15");
        SUFFISSO_VARIABILE_MISURA.put("M16", "M16");
        SUFFISSO_VARIABILE_MISURA.put("M17", "M17");
    }

    private static List<String> MISURA;
    static {
        MISURA = new ArrayList<String>();
        MISURA.add("M08");
        MISURA.add("M09");
        MISURA.add("M10");
        MISURA.add("M11");
        MISURA.add("M14");
        MISURA.add("M15");
        MISURA.add("M16");
        MISURA.add("M17");
    }

    @Override
    public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
        try {
        	logger.debug("Generazione statistica Cs27 Superficie per l'istruttoria con id {}",istruttoria.getId());
            StatisticheInputData input = caricaDatiInput(istruttoria, annoCampagna);

            Map<TipoVariabile, VariabileCalcolo> variabili = input.getVariabiliCalcolo();
            if (skipIstruttoria(variabili)) return;

            MISURA.forEach(misura -> {
                // Verifico che almeno una delle potenzialmente multiple variabili associate alla misura
                // sia di un intervento richiesto in domanda. In caso negativo, esco e proseguo con la misura successiva
                if (keys(getSuffissoVariabileMisura(), misura)
                        .noneMatch(suffissoVariabile -> isInterventoRichiesto(suffissoVariabile, input.getPassiLavorazioneEntities())))
                    return;

                StatisticaDu item = StatisticaDu.empty(); // Inizializzo oggetto statistica

                Float c552 = getValoreVariabileMisuraOrDefault(variabili, "ACSSUPRIC", misura);
                Float c554 = getValoreVariabileMisuraOrDefault(variabili, "ACSIMPRIC", misura);
                String c600 = getControlliInLocoAccoppiati(input.getCampioneSuperficiEntity());
                Float c558 = getQuantitaDeterminata(
                        getValoreVariabileMisuraOrDefault(variabili, "ACSSUPAMM", misura),
                        getValoreVariabileMisuraOrDefault(variabili, "ACSSUPRIC", misura),
                        "F".equals(c600),
                        getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
                );
                Float c559 = getImportoQuantitaDeterminata(
                        getValoreVariabileMisuraOrDefault(variabili, "ACSIMPAMM", misura),
                        getValoreVariabileMisuraOrDefault(variabili, "ACSIMPRIDRIT", misura),
                        c554,
                        "F".equals(c600),
                        getValoreVariabilePrecaricata(variabili, TipoVariabile.DOMSIGECOCHIUSA, Boolean.class)
                );
                item.withIdDomanda(input.getNumeroDomanda())
                        .withStato(istruttoria.getStato())
                        .withImpAmm(getValoreVariabileMisuraOrDefault(variabili, "ACSIMPAMM", misura))
                        .withC110(annoCampagna.intValue())
                        .withC109(getSigla())
                        .withC109a(misura)
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
                        .withC551(c552) // Stessa variabile di c552
                        .withC552(c552)
                        .withC554(c554)
                        .withC554a(null)
                        .withC557(null)
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
                        .withC600(c600)
                        .withC605(null)
                        .withC611(getMetodoSelezioneControlliInLocoAccoppiati(input.getCampioneSuperficiEntity()))
                        .withC620(getDomandaIrricevibileNonAmmissibile(c552, c554, c559, c600))
                        .withC621()
                        .withC633("N")
                        .withC640(BigDecimal.ZERO.floatValue())
                        .withC640a(null)
                ;
                statisticheService.salvaStatistica(item, getTipoDatoAnnuale());
            });

        } catch (Exception e) {
            logger.error("Impossibile generare le informazioni " +
                    getTipoDatoAnnuale().name() +
                    " per l'istruttoria con id ".concat(istruttoria.getId().toString()), e);
        }
    }

    @Override
    protected Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passi) {
        EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
        try {
            getSuffissoVariabileMisura().forEach((k, v) -> {
                try {
                    if (isInterventoRichiesto(k, passi)) {
                        getVariabile(getTipo("ACSSUPRIC", k), passi, TipologiaPassoTransizione.CALCOLO_ACS)
                                .ifPresent(item -> result.put(getTipo("ACSSUPRIC", k), item));
                        getVariabile(getTipo("ACSIMPRIC", k), passi, TipologiaPassoTransizione.CALCOLO_ACS)
                                .ifPresent(item -> result.put(getTipo("ACSIMPRIC", k), item));
                        getVariabile(getTipo("ACSSUPAMM", k), passi, TipologiaPassoTransizione.CALCOLO_ACS)
                                .ifPresent(item -> result.put(getTipo("ACSSUPAMM", k), item));
                        getVariabile(getTipo("ACSIMPAMM", k), passi, TipologiaPassoTransizione.CALCOLO_ACS)
                                .ifPresent(item -> result.put(getTipo("ACSIMPAMM", k), item));
                        getVariabile(getTipo("ACSIMPRIDRIT", k), passi, TipologiaPassoTransizione.CALCOLO_ACS)
                                .ifPresent(item -> result.put(getTipo("ACSIMPRIDRIT", k), item));
                        result.put(TipoVariabile.ISCAMP, getVariabile(TipoVariabile.ISCAMP, passi, TipologiaPassoTransizione.CALCOLO_ACS).orElse(null));
                        result.put(TipoVariabile.DOMSIGECOCHIUSA, getVariabile(TipoVariabile.ISCAMP, passi, TipologiaPassoTransizione.CALCOLO_ACS).orElse(null));
                    }
                } catch (Exception e) {
                    logger.debug("Errore in recupero variabili", e);
                }
            });
            result.put(TipoVariabile.ACSSUPRICTOT, getVariabile(TipoVariabile.ACSSUPRICTOT, passi, TipologiaPassoTransizione.CALCOLO_ACS).orElse(null));
        } catch (Exception e) {
            logger.debug("Errore in recupero variabili", e);
        }
        return result;
    }

    private Boolean isInterventoRichiesto(String intervento, List<PassoTransizioneModel> passi) {
        Boolean result = false;
        try {
            result = getVariabile(getTipo("ACSSUPRIC", intervento), passi, TipologiaPassoTransizione.CALCOLO_ACS).isPresent();
        } catch (Exception e) {
            logger.debug("Intervento {} non richiesto", intervento);
        }
        return result;
    }

    private String getControlliInLocoAccoppiati(CampioneModel campione) {
        if (campione != null && campione.getAmbitoCampione() != null) {
            if (campione.getAmbitoCampione().equals(AmbitoCampione.SUPERFICIE))
                return "F";
        }
        return "N";
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
			if (c600.equals("N"))
				return "2";
			else
				return "3";
		else if(c559.compareTo(0f) == 0)
			if (c600.equals("N"))
				return "2";
			else
				return "3";
		else 
			return "4";
	}

    @Override
    protected Sostegno getSostegno() {
        return Sostegno.SUPERFICIE;
    }

    @Override
    protected TipologiaPassoTransizione getCodicePassoByIdentificativoSostegno() {
        return TipologiaPassoTransizione.CALCOLO_ACS;
    }

    @Override
    protected Map<String, String> getSuffissoVariabileMisura() {
        return SUFFISSO_VARIABILE_MISURA;
    }

	@Override
	protected List<String> getMisure() {
		return MISURA;
	}
	
	/**
	 * Metodo usato per recupare solo le istruttorie che rispettano alcune condizioni, come ad esempio
	 * - CS25: VANNO CONSIDERATE SOLTANTO LE DOMANDE CHE HANNO RICHIESTO L'INTERVENTO GIOVANE
	 * - CS27: PER QUANTO RIGUARDA L'ACCOPPAITO SUPERFICIE VANNO INSERITI RECORD SOLTANTO DI DOMANDE CHE HANNO UNA SUPERIFCIE RICHIESTA COMPLESSIVA SU TTUTI GLI INTERVENTI > 5000 mq
	 * @param Istruttoria
	 * @return
	 */
	private boolean skipIstruttoria(Map<TipoVariabile, VariabileCalcolo> variabili) {
		BigDecimal superficieComplessiva = getValoreVariabilePrecaricata(variabili, TipoVariabile.ACSSUPRICTOT, BigDecimal.class);
		return superficieComplessiva == null
				? Boolean.FALSE
				: superficieComplessiva.multiply(BigDecimal.valueOf(10000d)).compareTo(BigDecimal.valueOf(5000)) < 0 ;
	}
}
