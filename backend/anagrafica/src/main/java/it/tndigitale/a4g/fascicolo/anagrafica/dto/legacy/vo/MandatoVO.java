package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.time.LocalDate;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;

public class MandatoVO implements SQLData {

	private String codiceFiscale;
	private String codEnte;
	private Date dataInizioVal;
	private Date dataFineVal;
	private Date dataSottoscrizione;

	private String sql_type = "AGS_MANDATO";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type) throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		codEnte = stream.readString();
		dataInizioVal = stream.readDate();
		dataFineVal = stream.readDate();
		dataSottoscrizione = stream.readDate();
	}

	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeString(codEnte);
		stream.writeDate(dataInizioVal);
		stream.writeDate(dataFineVal);
		stream.writeDate(dataSottoscrizione);
	}

	public static MandatoVO mapper(FascicoloModel input, String cuaa) {
		if (input == null) {
			return null;
		}
		MandatoVO dto = new MandatoVO();

		dto.codiceFiscale = cuaa;
		for (DetenzioneModel detenzione : input.getDetenzioni()) {
			if (detenzione.getDataFine() == null || detenzione.getDataFine().isAfter(LocalDate.now())) {
				dto.dataInizioVal = Date.valueOf(detenzione.getDataInizio());
				if (detenzione.getDataFine() != null) {
					dto.dataFineVal = Date.valueOf(detenzione.getDataFine());
				}

				if (detenzione instanceof MandatoModel) {
					MandatoModel mandatoCorrente = (MandatoModel) detenzione;
					dto.codEnte = "" + mandatoCorrente.getSportello().getIdentificativo();
					if (mandatoCorrente.getDataSottoscrizione() != null) {
						dto.dataSottoscrizione = Date.valueOf(mandatoCorrente.getDataSottoscrizione());
					}
				}
			}
		}

		return dto;
	}
}
