package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component
public class GeneraStatisticaCs27Zootecnia extends GeneraStatisticaCs27 {
    private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaCs27Zootecnia.class);

    private static Map<String, String> SUFFISSO_VARIABILE_MISURA;
    static {
        SUFFISSO_VARIABILE_MISURA = new HashMap<>();
        SUFFISSO_VARIABILE_MISURA.put("310", "M01");
        SUFFISSO_VARIABILE_MISURA.put("311", "M02");
        SUFFISSO_VARIABILE_MISURA.put("313", "M04");
        SUFFISSO_VARIABILE_MISURA.put("322", "M20");
        SUFFISSO_VARIABILE_MISURA.put("315", "M05");
        SUFFISSO_VARIABILE_MISURA.put("316", "M19");
        SUFFISSO_VARIABILE_MISURA.put("318", "M19");
        SUFFISSO_VARIABILE_MISURA.put("320", "M06");
        SUFFISSO_VARIABILE_MISURA.put("321", "M07");
    }

    private static List<String> MISURA_SOSTEGNO;
    static {
        MISURA_SOSTEGNO = new ArrayList<String>();
        MISURA_SOSTEGNO.add("M01");
        MISURA_SOSTEGNO.add("M02");
        MISURA_SOSTEGNO.add("M04");
        MISURA_SOSTEGNO.add("M20");
        MISURA_SOSTEGNO.add("M05");
        MISURA_SOSTEGNO.add("M19");
        MISURA_SOSTEGNO.add("M06");
        MISURA_SOSTEGNO.add("M07");
    }
    
    @Override
    public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna) {
        try {
        	logger.debug("Generazione statistica Cs27 Zootecnia per l'istruttoria con id {}",istruttoria.getId());
            StatisticheInputData input = caricaDatiInput(istruttoria, annoCampagna);

            Map<TipoVariabile, VariabileCalcolo> variabili = input.getVariabiliCalcolo();

            MISURA_SOSTEGNO.forEach(misura -> {
                // Verifico che almeno una delle potenzialmente multiple variabili associate alla misura
                // sia di un intervento richiesto in domanda. In caso negativo, esco e proseguo con la misura successiva
                if (keys(getSuffissoVariabileMisura(), misura)
                        .noneMatch(suffissoVariabile -> isInterventoRichiesto(suffissoVariabile, input.getPassiLavorazioneEntities())))
                    return;

                StatisticaDu item = StatisticaDu.empty(); // Inizializzo oggetto statistica

                Float c552 = getValoreVariabileMisuraOrDefault(variabili, "ACZCAPIRICNET", misura);
                Float c554 = getValoreVariabileMisuraOrDefault(variabili, "ACZIMPRICNET", misura);
                String c600 = getControlliInLocoAccoppiati(input.getCampioneZootecniaEntity());
                Float c558 = getQuantitaDeterminata(
                        getValoreVariabileMisuraOrDefault(variabili, "ACZCAPIACC", misura),
                        getValoreVariabileMisuraOrDefault(variabili, "ACZCAPIRICNET", misura),
                        "F".equals(c600),
                        !getValoreVariabilePrecaricata(variabili, TipoVariabile.ACZCONTROLLILOCO, Boolean.class)//utilizza metodo generico getQuantitaDeterminata e va messo il negato perchè in altri casi prende domSigecoChiusa 
                );
                Float c559 = getImportoQuantitaDeterminata(
                        getValoreVariabileMisuraOrDefault(variabili, "ACZIMPACC", misura),
                        getValoreVariabileMisuraOrDefault(variabili, "ACZIMPRIDRIT", misura),
                        c554,
                        "F".equals(c600),
                        !getValoreVariabilePrecaricata(variabili, TipoVariabile.ACZCONTROLLILOCO, Boolean.class)
                );
                item.withIdDomanda(input.getNumeroDomanda())
                        .withStato(istruttoria.getStato())
                        .withImpAmm(getValoreVariabileMisuraOrDefault(variabili, "ACZIMPACC", misura))
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
                        .withC611(getMetodoSelezioneControlliInLocoAccoppiati(input.getCampioneZootecniaEntity()))
                        .withC620(getDomandaIrricevibileNonAmmissibile(c552, c554, c559, c600))
                        .withC621()
                        .withC633("N")
                        //.withC640(toNegativeValue(getValoreVariabileMisuraOrDefault(variabili, "ACZIMPRIDSANZ", misura)))
                        .withC640(getImportoSanzioneIrrogate(
                        		getValoreVariabileMisuraOrDefault(variabili, "ACZIMPRIDSANZ", misura),
                        		getValoreVariabileMisuraOrDefault(variabili, "ACZIMPDEB", misura),
		                        "F".equals(c600),
		                        getValoreVariabilePrecaricata(variabili, TipoVariabile.ACZCONTROLLILOCO, Boolean.class)
                        		))
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

    private Float getImportoSanzioneIrrogate(Float impSanz, Float impDeb, boolean c600, Boolean controlliInLoco) {
    	if (c600 && controlliInLoco.booleanValue()) {
    		return 0.0f;
    	} else {
    		return -1 * Float.sum(impSanz, impDeb);
    	}
	}
    

	@Override
    protected Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passi) {
        EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
        try {
            getSuffissoVariabileMisura().forEach((k, v) -> {
                try {
                    if (isInterventoRichiesto(k, passi)) {
                        getVariabile(getTipo("ACZCAPIRICNET", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZCAPIRICNET", k), item));
                        getVariabile(getTipo("ACZIMPRICNET", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZIMPRICNET", k), item));
                        getVariabile(getTipo("ACZCAPIACC", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZCAPIACC", k), item));
                        getVariabile(getTipo("ACZIMPACC", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZIMPACC", k), item));
                        getVariabile(getTipo("ACZIMPRIDRIT", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZIMPRIDRIT", k), item));
                        getVariabile(getTipo("ACZIMPRIDSANZ", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                                .ifPresent(item -> result.put(getTipo("ACZIMPRIDSANZ", k), item));
                        getVariabile(getTipo("ACZIMPDEB", k), passi, TipologiaPassoTransizione.CALCOLO_ACZ)
                        	.ifPresent(item -> result.put(getTipo("ACZIMPDEB", k), item));                        
                        result.put(TipoVariabile.ISCAMP, getVariabile(TipoVariabile.ISCAMP, passi, TipologiaPassoTransizione.CALCOLO_ACZ).orElse(null));
                        result.put(TipoVariabile.ACZCONTROLLILOCO, getVariabile(TipoVariabile.ISCAMP, passi, TipologiaPassoTransizione.CALCOLO_ACZ).orElse(null));
                    }
                } catch (Exception e) {
                    logger.debug("Errore in recupero variabili", e);
                }
            });
        } catch (Exception e) {
            logger.debug("Errore in recupero variabili", e);
        }
        return result;
    }

    private Boolean isInterventoRichiesto(String intervento, List<PassoTransizioneModel> passi) {
        Boolean result = false;
        try {
            result = getVariabile(getTipo("ACZCAPIRICNET", intervento), passi, TipologiaPassoTransizione.CALCOLO_ACZ).isPresent();
        } catch (Exception e) {
            logger.debug("Intervento {} non richiesto", intervento);
        }
        return result;
    }

    private String getControlliInLocoAccoppiati(CampioneModel campione) {
        if (campione != null && campione.getAmbitoCampione() != null) {
            if (campione.getAmbitoCampione().equals(AmbitoCampione.ZOOTECNIA))
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
        return Sostegno.ZOOTECNIA;
    }

    @Override
    protected TipologiaPassoTransizione getCodicePassoByIdentificativoSostegno() {
        return TipologiaPassoTransizione.CALCOLO_ACZ;
    }

    @Override
    protected Map<String, String> getSuffissoVariabileMisura() {
        return SUFFISSO_VARIABILE_MISURA;
    }

	@Override
	protected List<String> getMisure() {
		return MISURA_SOSTEGNO;
	}
}
