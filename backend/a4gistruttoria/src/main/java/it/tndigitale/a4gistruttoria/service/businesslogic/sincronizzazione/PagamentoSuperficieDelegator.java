package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
class PagamentoSuperficieDelegator extends PagamentoDelegator {
    private static final Logger logger = LoggerFactory.getLogger(PagamentoSuperficieDelegator.class);

    private static List<String> INTERVENTI;
    static {
        INTERVENTI = new ArrayList<>();
        INTERVENTI.add("M8");
        INTERVENTI.add("M9");
        INTERVENTI.add("M10");
        INTERVENTI.add("M11");
        INTERVENTI.add("M14");
        INTERVENTI.add("M15");
        INTERVENTI.add("M16");
        INTERVENTI.add("M17");
    }

    @Override
    public Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passi) {
        EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
        try {
            String prefissoImpoDete = "ACSIMPAMM";
            String prefissoImpoLiqui = "ACSIMPCALC";
            String prefissoImpoRichiesto = "ACSIMPRIC";
            String prefissoInterventoRichiesto = "ACSSUPRIC";

            INTERVENTI.forEach(intervento -> {
                try {
                    TipoVariabile tipoImpoDete = getTipo(prefissoImpoDete, intervento);
                    TipoVariabile tipoImpoLiqui = getTipo(prefissoImpoLiqui, intervento);
                    TipoVariabile tipoImpoRichiesto = getTipo(prefissoImpoRichiesto, intervento);
                    TipoVariabile tipoInterventoRichiesto = getTipo(prefissoInterventoRichiesto, intervento);
                    result.put(tipoImpoDete, translator.getVariabile(tipoImpoDete, passi, getTipologiaPassoTransizione()).orElse(null));
                    result.put(tipoImpoLiqui, translator.getVariabile(tipoImpoLiqui, passi, getTipologiaPassoTransizione()).orElse(null));
                    result.put(tipoImpoRichiesto, translator.getVariabile(tipoImpoRichiesto, passi, getTipologiaPassoTransizione()).orElse(null));
                    result.put(tipoInterventoRichiesto, translator.getVariabile(tipoInterventoRichiesto, passi, getTipologiaPassoTransizione()).orElse(null));
                } catch (Exception e) {
                    logger.debug("Errore in recupero variabili", e);
                }
            });
        } catch (Exception e) {
            logger.debug("Errore in recupero variabili", e);
        }
        return result;
    }

    @Override
    public Boolean isInterventoRichiesto(String intervento, PagamentiData pagamentiData) {

        try {
            if (isIstruttoriaValida(pagamentiData.getIstruttoria())) {
//                StatoIstruttoria sls = StatoIstruttoria.valueOfByIdentificativo(
//                        pagamentiData.getIstruttoria().getA4gdStatoLavSostegno().getIdentificativo());

                return translator.getVariabile(getTipo("ACSSUPRIC", intervento),
                        pagamentiData.getPassiLavorazioneTransizione(),
                        getTipologiaPassoTransizione()).isPresent();
            }
        } catch (Exception e) {
            logger.debug("Intervento {} non richiesto", intervento);
        }
        return false;
    }

    @Override
    public List<String> getInterventiSostegno() {
        return INTERVENTI;
    }

    @Override
    protected Sostegno getIdentificativoSostegno() {
        return Sostegno.SUPERFICIE;
    }

    @Override
    public TipologiaPassoTransizione getTipologiaPassoTransizione() {
        return TipologiaPassoTransizione.CALCOLO_ACS;
    }
}
