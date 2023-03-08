package it.tndigitale.a4g.soc.business.persistence.builder;

import it.tndigitale.a4g.framework.exception.ValidationException;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.service.LiquidazioneHandler;

import java.util.HashMap;
import java.util.Map;

public class LiquidazioneBuilder {

    public static Map<String, Object> paramsOfCalcoloImportoLiquidato(ImportoLiquidatoFilter importoLiquidatoFilter) {
        Map<String, Object> params = new HashMap<>();
        final String numeroDomandaParam = mapNumeroDomandaParameter(importoLiquidatoFilter);
        params.put("numeroDomanda", numeroDomandaParam);
        params.put("cuaa", importoLiquidatoFilter.getCuaa());
        if (importoLiquidatoFilter.getAnno() != null) {
        	params.put("anno", importoLiquidatoFilter.getAnno());
        }
        if (importoLiquidatoFilter.getTipoPagamento() != null) {
        	String startNumeroDomanda = null;
        	switch (importoLiquidatoFilter.getTipoPagamento()) {
        	case ACCONTO:
        		startNumeroDomanda = "S[1-9]";
        		break;
        	case SALDO:
        		startNumeroDomanda = "SF";
        		break;
        	case ANTICIPO:
        		startNumeroDomanda = "A[1-9]";
        		break;
        	}
        	params.put("startNumeroDomanda", startNumeroDomanda);
        }
		LiquidazioneHandler.handleDebitiElencoLiquidazione(
				importoLiquidatoFilter.getIdElencoLiquidazione(), 
				() -> addIdElencoLiquidazione(params, importoLiquidatoFilter.getIdElencoLiquidazione()));
        return params;
    }

    private static Void addIdElencoLiquidazione(Map<String, Object> params, Long idElencoLiquidazione) {
    	params.put("idElencoLiquidazione", idElencoLiquidazione);
		return null;
    }
    
    public static Map<String, Object> paramsOfCalcoloDebitiXImportoLiquidato(ImportoLiquidato importoLiquidato) {
        Map<String, Object> params = new HashMap<>();
        params.put("annoEsercizio", importoLiquidato.getAnno());
        params.put("tipoBilancio", importoLiquidato.getTipoBilancio());
        params.put("progressivoCredito", importoLiquidato.getProgressivo());
        return params;
    }

    private static String mapNumeroDomandaParameter(ImportoLiquidatoFilter importoLiquidatoFilter) {
        if (importoLiquidatoFilter.getTipoDomanda() == null) {
            throw new ValidationException("Tipo domanda non settato");
        }
        switch (importoLiquidatoFilter.getTipoDomanda()) {
            case DOMANDA_UNICA:
                return String.format("009%d.*%d", importoLiquidatoFilter.getAnno(), importoLiquidatoFilter.getNumeroDomanda().toBigInteger());
            case DOMANDA_PSR_STRUTTURALE: {
                /*
                 * Quindi se sto cercando il numero domanda '13144' devo cercare:
                 * SF00013144
                 * S100013144, S200013144, ..., S900013144
                 * A100013144, A200013144, ..., A900013144
                 */
                String sPaddedNumeroDomanda = StringSupport.padLeftZeros(String.valueOf(importoLiquidatoFilter.getNumeroDomanda()), 8);
                return String.format("([A,S][1-9]|SF)%s", sPaddedNumeroDomanda);
            }
            case DOMANDA_PSR_SUPERFICIE: {
                String sPaddedNumeroDomanda = StringSupport.padLeftZeros(String.valueOf(importoLiquidatoFilter.getNumeroDomanda()), 8);
                return String.format("^\\d{2,3}%d%s$", importoLiquidatoFilter.getAnno(), sPaddedNumeroDomanda);
            }
            default:
                throw new ValidationException("Tipo domanda non gestito per effettuare la query");
        }
    }
}
