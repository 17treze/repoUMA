package it.tndigitale.a4g.soc.business.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoGroupBy;
import it.tndigitale.a4g.soc.business.dto.TipoDomanda;

public class LiquidazioneHandler {
	
	private LiquidazioneHandler() {}
	
	private static final String IMPORTO_LIQUIDATO_NETTO_QUERY =
			"SELECT CUAA AS cuaa, NUMERO_DOMANDA AS numeroDomanda, NUMERO_ELENCO as numeroElenco, " +
			"TIPO_BILANCIO as tipoBilancio, ANNO_ESERCIZIO as annoEsercizio, PROGRESSIVO_CREDITO as progressivoCredito, " +
			"TIPO_PAGAMENTO as tipoPagamento, INCASSATO_NETTO as incassatoNetto, " +
			"CAPITOLO as capitolo, CODICE_PRODOTTO as codProdotto, DATA_ESECUZIONE as dataEsecuzione, " +
			"NUMERO_AUTORIZZAZIONE as numeroAutorizzazione, DATA_AUTORIZZAZIONE as dataAutorizzazione, PROGRESSIVO_PAGAMENTO as progressivoPagamento " +
			"FROM A4G_IMPORTO_LIQUIDATO " +
			"WHERE CUAA like (:cuaa) " +
			"AND REGEXP_LIKE(NUMERO_DOMANDA, :numeroDomanda)";

    private static final String WHERE_NUMEROELENCO_EQUALS = " AND NUMERO_ELENCO = :idElencoLiquidazione";
	private static final String WHERE_TIPOPAGAMENTO_EQUALS = " AND REGEXP_LIKE(NUMERO_DOMANDA, :startNumeroDomanda)";
	
	
	public static void handleDebitiElencoLiquidazione(Long idElencoLiquidazione, Integer size, Supplier<Void> handler) {
		if (idElencoLiquidazione != null && size == 1)
			handler.get();
	}
	
	public static void handleDebitiElencoLiquidazione(Long idElencoLiquidazione, Supplier<Void> handler) {
		if (idElencoLiquidazione != null)
			handler.get();
	}
	
	public static String getQuery(
			TipoDomanda tipoDomanda, final boolean isIdElencoLiquidazionePresent,
			final boolean isAnnoPresent,
			final boolean isTipoPagamentoPresent) {
		String query = IMPORTO_LIQUIDATO_NETTO_QUERY;
		if (isIdElencoLiquidazionePresent) query += WHERE_NUMEROELENCO_EQUALS;
		if (isTipoPagamentoPresent) query += WHERE_TIPOPAGAMENTO_EQUALS;
		query += " ORDER BY PROGRESSIVO_PAGAMENTO ASC";
		return query;
	}
	
	public static Map<ImportoLiquidatoGroupBy, List<ImportoLiquidato>> raggruppa(List<ImportoLiquidato> importiLiquidato) {
		//criterio che anno esercizio, bilancio e progressivo coincidano
		return importiLiquidato.stream()
				  .collect(groupingBy(imp -> new ImportoLiquidatoGroupBy(imp.getTipoBilancio(), imp.getAnno(), imp.getProgressivo())));
	}
	
	public static BiConsumer<? super ImportoLiquidatoGroupBy, ? super List<ImportoLiquidato>> sommaIncassi(List<ImportoLiquidato> importiLiquidato){
		return (importoLiquidatoGroupBy, importiRaggruppati) -> {
				ImportoLiquidato importo = importiRaggruppati.get(0);//prendo il primo essendo record duplicati in cui cambia solo il capitolo
				//sommo gli incassi in base al raggruppamento
				//( SELECT SUM (vocespesaImpNetto.IMPORTO)
				//		FROM GAGREA.TVOCESPESA vocespesaImpNetto
				//		WHERE vocespesaImpNetto.FLAG_ARECUPERO = 'N'
				//		AND vocespesaImpNetto.TIPO_BILANCIO = richiesta_pagam.TIPO_BILANCIO
				//		AND vocespesaImpNetto.ANNO_ES = richiesta_pagam.ANNO_ES
				//		AND vocespesaImpNetto.PROGR_CREDITO = richiesta_pagam.PROGR_CREDITO
				//		) AS INCASSATO_NETTO
				Double sumIncassatoNetto = importiRaggruppati.stream()
							.map(ImportoLiquidato::getIncassatoNetto)
							.collect(summingDouble(BigDecimal::doubleValue));
				importo.setIncassatoNetto(BigDecimal.valueOf(sumIncassatoNetto));
				importiLiquidato.add(importo);
		};
	}

	
}
