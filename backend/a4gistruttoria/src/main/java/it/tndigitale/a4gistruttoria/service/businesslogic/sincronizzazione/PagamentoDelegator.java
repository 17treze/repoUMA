package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.strategy.PagamentiDataItem;
import it.tndigitale.a4gistruttoria.util.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

abstract class PagamentoDelegator {
    @Autowired
    protected JsonTranslator translator;

    protected abstract Map<TipoVariabile, VariabileCalcolo> recuperaValoriVariabili(List<PassoTransizioneModel> passi);

    /**
     * Se non esiste nessun idLavorazioneSostegno associato al sostegno dell'intervento, ritorno false;
     * Per gli interventi BPS GREENING e GIOVANE è sempre true (escluso se in stato NON AMMISSIBILE e NON LIQUIDABILE);
     * Per gli interventi del sostegno SUPERFICIE verifico se è presente la variabile ACSSUPRIC_{intervento}, escludo
     * sempre le domande in stato NON AMMISSIBILE e NON LIQUIDABILE;
     * Per gli interventi del sostegno ZOOTECNIA verifico se è presente la variabile ACZCAPIRICNET_{intervento}, escludo
     * sempre le domande in stato NON AMMISSIBILE, NON LIQUIDABILE e INTEGRATO.
     * @return
     */
    protected abstract Boolean isInterventoRichiesto(String intervento, PagamentiData pagamentiData);

    protected abstract TipologiaPassoTransizione getTipologiaPassoTransizione();

    protected abstract List<String> getInterventiSostegno();

    protected abstract Sostegno getIdentificativoSostegno();

    protected Boolean isIstruttoriaValida(IstruttoriaModel istruttoria) {
        StatoIstruttoria sls = StatoIstruttoria.valueOfByIdentificativo(
                istruttoria.getA4gdStatoLavSostegno().getIdentificativo());

        if (sls.equals(StatoIstruttoria.NON_AMMISSIBILE) || sls.equals(StatoIstruttoria.NON_LIQUIDABILE)) {
            return false;
        }

        return true;
    }

    protected TipoVariabile getTipo(String prefisso, String suffisso) {
        return TipoVariabile.valueOf(prefisso.concat("_").concat(suffisso));
    }


    protected void caricaPagamenti(PagamentiData result) {
        for (String intervento : getInterventiSostegno()) {
            if (isInterventoRichiesto(intervento, result)) {
                PagamentiDataItem item = new PagamentiDataItem();
                item.setPagamentoAutorizzato(result.isStatoPagamentoAutorizzato());
                item.setImportoDeterminato(roundAndScale(recuperaImportoDeterminato(result, intervento)));
                item.setImportoLiquidato(roundAndScale(recuperaImportoLiquidato(result, intervento)));
                item.setImportoRichiesto(roundAndScale(recuperaImportoRichiesto(result, intervento)));

                result.addDataItem(intervento, item);
            }
        }
    }

    private Double roundAndScale(Float value) {
        if (value == null)
            return Double.valueOf(0);
        return new BigDecimal(String.valueOf(value)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private Float recuperaImportoDeterminato(PagamentiData inputData, String intervento) {
        if (inputData.isStatoPagamentoAutorizzato()) {
            String prefisso = getPrefissoByIntervento(intervento);
            String suffisso = getSuffissoByIntervento(intervento);
            String nomeTipoVar = getIdentificativoSostegno().equals(Sostegno.ZOOTECNIA)? "IMPACC":"IMPAMM";
            return translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(),
                    TipoVariabile.valueOf(prefisso.concat(nomeTipoVar).concat(suffisso)), Float.class);
        } else {
            return Float.valueOf(0);
        }
    }

    private Float recuperaImportoLiquidato(PagamentiData inputData, String intervento) {
        if (inputData.isStatoPagamentoAutorizzato()) {
            String prefisso = getPrefissoByIntervento(intervento);
            String suffisso = getSuffissoByIntervento(intervento);
            String nomeTipoVar = "IMPCALC";
            if (getIdentificativoSostegno().equals(Sostegno.DISACCOPPIATO))
                nomeTipoVar = nomeTipoVar.concat("FINLORDO");
            return translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(),
                    TipoVariabile.valueOf(prefisso.concat(nomeTipoVar).concat(suffisso)), Float.class);
        } else {
            return Float.valueOf(0);
        }
    }

    private Float recuperaImportoRichiesto(PagamentiData inputData, String intervento) {
    	String prefisso = getPrefissoByIntervento(intervento);
    	String suffisso = getSuffissoByIntervento(intervento);
    	String nomeTipoVar = "IMPRIC";
    	if (getIdentificativoSostegno().equals(Sostegno.ZOOTECNIA))
    		nomeTipoVar = nomeTipoVar.concat("NET");
    	return translator.getValoreVariabilePrecaricata(inputData.getVariabiliCalcolo(),
    			TipoVariabile.valueOf(prefisso.concat(nomeTipoVar).concat(suffisso)), Float.class);
    }

    private String getPrefissoByIntervento(String intervento) {
        if (Arrays.asList(CodiceInterventoAgs.BPS.name(), CodiceInterventoAgs.GREE.name(), CodiceInterventoAgs.GIOV.name()).contains(intervento)) {
            return intervento.substring(0, 3);
        } else {
            return getIdentificativoSostegno().equals(Sostegno.SUPERFICIE) ? "ACS" : "ACZ";
        }
    }

    private String getSuffissoByIntervento(String intervento) {
        if (Arrays.asList(CodiceInterventoAgs.BPS.name(), CodiceInterventoAgs.GREE.name(), CodiceInterventoAgs.GIOV.name()).contains(intervento)) {
            return "";
        } else {
            return "_".concat(intervento);
        }
    }
}
