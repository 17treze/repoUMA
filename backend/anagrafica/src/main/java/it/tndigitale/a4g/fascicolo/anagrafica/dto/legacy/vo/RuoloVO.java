package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
public class RuoloVO implements SQLData {

	private String codiceFiscale;
	private String codRuolo;
	private String scoRuolo;
	private Date dataInizioVal;
	private Date dataFineVal;

	private String sql_type = "AGS_RUOLO";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		codRuolo = stream.readString();
		scoRuolo = stream.readString();
		dataInizioVal = stream.readDate();
		dataFineVal = stream.readDate();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeString(codRuolo);
		stream.writeString(scoRuolo);
		stream.writeDate(dataInizioVal);
		stream.writeDate(dataFineVal);
	}

	public static RuoloVO mapper(CaricaModel carica, String cuaa) {
		if(carica == null) {
			return null;
		}

		RuoloVO dto = new RuoloVO();

//		il codice fiscale del ruolo si riferisce al cuaa del fascicolo
		dto.codiceFiscale = cuaa;
		dto.codRuolo = carica.getIdentificativo();
		dto.scoRuolo = carica.getDescrizione();
		if (carica.getDataInizio() != null) {
			dto.dataInizioVal = Date.valueOf(carica.getDataInizio());
		}

		return dto;
	}

}
