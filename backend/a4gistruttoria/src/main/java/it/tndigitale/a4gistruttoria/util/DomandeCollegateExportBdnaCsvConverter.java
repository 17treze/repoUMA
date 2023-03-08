package it.tndigitale.a4gistruttoria.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExportBdna;
import it.tndigitale.a4gistruttoria.repository.model.DomandeCollegateExportBdnaCsv;

public class DomandeCollegateExportBdnaCsvConverter {

	public static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public static String from(Date date) {
		return (date!=null)? df.format(date):null;
	}

	public static List<DomandeCollegateExportBdnaCsv> from(DomandeCollegateExportBdna source) {
		df.format(new Date());
		List<DomandeCollegateExportBdnaCsv> result = new ArrayList<>();
		// Se non esistono domande collegate riempio l'oggetto target con i dati della sola dichiarazione
		// Altrimenti genero un item per ciascuna domanda collegata con i dati della dichiarazione 
		if (source.getDomandeCollegate().isEmpty()) {
			DomandeCollegateExportBdnaCsv res = getDomanda(source);
			result.add(res);
		} else {
			source.getDomandeCollegate().forEach(item -> {
				DomandeCollegateExportBdnaCsv res = getDomanda(source);
				// dati presi da A4gtDomandeCollegate
				res.setCampagna(item.getCampagna());
				res.setDtBdna(from(item.getDtBdna()));
				res.setDtBdnaOp(from(item.getDtBdnaOp()));
				res.setDtFineEsitoNegativo(from(item.getDtFineEsitoNegativo()));
				res.setDtFineSilenzioAssenso(from(item.getDtFineSilenzioAssenso()));
				res.setDtInizioEsitoNegativo(from(item.getDtInizioEsitoNegativo()));
				res.setDtInizioSilenzioAssenso(from(item.getDtInizioSilenzioAssenso()));
				res.setImportoRichiesto(item.getImportoRichiesto());
				res.setProtocollo(item.getProtocollo());
				res.setStatoBdna(item.getStatoBdna().getStatoDomandaCollegata());
				res.setTipoDomanda(item.getTipoDomanda().name());
				result.add(res);
			});
		}
		return result;
	}

	public static CsvSchema getDomandeCollegateExportSchema() {
		return CsvSchema.builder()
						.addColumn("CUAA", CsvSchema.ColumnType.STRING)
						.addColumn("STATO_AMF", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_INIZIO_COMPILAZIONE_AMF", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_PROTOCOLLAZIONE_AMF", CsvSchema.ColumnType.STRING)
						.addColumn("PROTOCOLLO_AMF", CsvSchema.ColumnType.STRING)
						.addColumn("TIPO_DOMANDA_COLLEGATA", CsvSchema.ColumnType.STRING)
						.addColumn("CAMPAGNA", CsvSchema.ColumnType.NUMBER)
						.addColumn("IMPORTO_RICHIESTO", CsvSchema.ColumnType.NUMBER)
						.addColumn("DATA_CARICAMENTO_BDNA", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_ESITO_BDNA", CsvSchema.ColumnType.STRING)
						.addColumn("PROTOCOLLO_BDNA", CsvSchema.ColumnType.STRING)
						.addColumn("STATO_BDNA", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_INIZIO_SILENZIO_ASSENSO", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_FINE_SILENZIO_ASSENSO", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_INIZIO_ESITO_NEGATIVO", CsvSchema.ColumnType.STRING)
						.addColumn("DATA_FINE_ESITO_NEGATIVO", CsvSchema.ColumnType.STRING)
						.build()
						.withColumnSeparator(';')
						.withoutQuoteChar()
						.withHeader();
	}

	private static DomandeCollegateExportBdnaCsv getDomanda(DomandeCollegateExportBdna source) {
		// dati presi da DichiarazioneAntimafia
		DomandeCollegateExportBdnaCsv res = new DomandeCollegateExportBdnaCsv();
		res.setCuaa(source.getDichiarazioneAntimafia().getAzienda().getCuaa());
		res.setDtInizioCompilazione(from(source.getDichiarazioneAntimafia().getDtInizioCompilazione()));
		res.setDtProtocollazione(from(source.getDichiarazioneAntimafia().getDtProtocollazione()));
		res.setIdProtocollo(source.getDichiarazioneAntimafia().getIdProtocollo());
		res.setStato(adjustStatoDichiarazione(source.getDichiarazioneAntimafia().getStato().getIdentificativo()));
		return res;
	}

	private static String adjustStatoDichiarazione(String statoDichiarazioneOld) {
		StatoDichiarazioneEnum value = StatoDichiarazioneEnum.findByIdentificativo(statoDichiarazioneOld);
		switch (value) {
			case CONTROLLATA:
				return "IN_ISTRUTTORIA";
			case BOZZA:
				return "IN_COMPILAZIONE";
			default:
				return statoDichiarazioneOld;
		}
	}

}
