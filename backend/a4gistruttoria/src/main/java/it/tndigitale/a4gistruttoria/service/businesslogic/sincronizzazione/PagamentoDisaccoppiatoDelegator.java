package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.InterventoDisaccoppiato;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component
class PagamentoDisaccoppiatoDelegator extends PagamentoDelegator {
    private static final Logger logger = LoggerFactory.getLogger(PagamentoDisaccoppiatoDelegator.class);

    private static List<String> INTERVENTI;
    static {
        INTERVENTI = new ArrayList<String>();
        INTERVENTI.add(CodiceInterventoAgs.BPS.name());
        INTERVENTI.add(CodiceInterventoAgs.GREE.name());
        INTERVENTI.add(CodiceInterventoAgs.GIOV.name());
    }

    @Override
    public Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passi) {
        EnumMap<TipoVariabile, VariabileCalcolo> result = new EnumMap<>(TipoVariabile.class);
        try {
            INTERVENTI.forEach(intervento -> {
                try {
                    if (intervento.equals(CodiceInterventoAgs.BPS.name())) {
                        result.put(TipoVariabile.BPSIMPAMM, translator.getVariabile(TipoVariabile.BPSIMPAMM, passi, TipologiaPassoTransizione.RIDUZIONI_BPS).orElse(null));
                        result.put(TipoVariabile.BPSIMPCALCFINLORDO, translator.getVariabile(TipoVariabile.BPSIMPCALCFINLORDO, passi, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));
                        result.put(TipoVariabile.BPSIMPRIC, translator.getVariabile(TipoVariabile.BPSIMPRIC, passi, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
                    } else if (intervento.equals(CodiceInterventoAgs.GREE.name())) {
                        result.put(TipoVariabile.GREIMPAMM, translator.getVariabile(TipoVariabile.GREIMPAMM, passi, TipologiaPassoTransizione.GREENING).orElse(null));
                        result.put(TipoVariabile.GREIMPCALCFINLORDO, translator.getVariabile(TipoVariabile.GREIMPCALCFINLORDO, passi, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));
                        result.put(TipoVariabile.GREIMPRIC, translator.getVariabile(TipoVariabile.GREIMPRIC, passi, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
                    } else if (intervento.equals(CodiceInterventoAgs.GIOV.name())) {
                        result.put(TipoVariabile.GIOIMPAMM, translator.getVariabile(TipoVariabile.GIOIMPAMM, passi, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).orElse(null));
                        result.put(TipoVariabile.GIOIMPCALCFINLORDO, translator.getVariabile(TipoVariabile.GIOIMPCALCFINLORDO, passi, TipologiaPassoTransizione.CONTROLLI_FINALI).orElse(null));
                        result.put(TipoVariabile.GIOIMPRIC, translator.getVariabile(TipoVariabile.GIOIMPRIC, passi, TipologiaPassoTransizione.AMMISSIBILITA).orElse(null));
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

    @Override
    public Boolean isInterventoRichiesto(String intervento, PagamentiData pagamentiData) {
        try {
            if (isIstruttoriaValida(pagamentiData.getIstruttoria()).booleanValue()) {
            	TipoVariabile var = null;    
            	
            	if (InterventoDisaccoppiato.BPS.getCodiceAgs().name().equals(intervento)) {
            		return true;
            	} else if (InterventoDisaccoppiato.GREENING.getCodiceAgs().name().equals(intervento)) {
         			var = TipoVariabile.GRERIC;
            	} else if (InterventoDisaccoppiato.GIOVANE.getCodiceAgs().name().equals(intervento)) {
        			var = TipoVariabile.GIORIC;
            	} else {
            		logger.warn("L'intervento {} richiesto per l'istruttoria {} e' sbagliato", intervento , pagamentiData.getIstruttoria().getId());
            		return false;
            	}
            	
            	Optional<VariabileCalcolo> variabileOpt = translator.getVariabile(var,pagamentiData.getPassiLavorazioneTransizione(),TipologiaPassoTransizione.AMMISSIBILITA);
            	return variabileOpt.isPresent() ? variabileOpt.get().getValBoolean(): Boolean.FALSE;
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
        return Sostegno.DISACCOPPIATO;
    }

    @Override
    public TipologiaPassoTransizione getTipologiaPassoTransizione() {
        return null;
    }
}
