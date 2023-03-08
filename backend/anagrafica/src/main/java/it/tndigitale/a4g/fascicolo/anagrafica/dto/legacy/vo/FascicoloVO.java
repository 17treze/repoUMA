package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum;

public class FascicoloVO implements SQLData {

	private String codiceFiscale;
	private Date dtInizio;
	private Date dtFine;
	private String codOp;
	private String scoOp;
	private String codStato;
	private String scoStato;
	private MandatoVO mandatoVO;

	private String sql_type = "AGS_FASCICOLO";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type) throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		dtInizio = stream.readDate();
		dtFine = stream.readDate();
		codOp = stream.readString();
		scoOp = stream.readString();
		codStato = stream.readString();
		scoStato = stream.readString();
		mandatoVO = (MandatoVO) stream.readObject();
	}

	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeDate(dtInizio);
		stream.writeDate(dtFine);
		stream.writeString(codOp);
		stream.writeString(scoOp);
		stream.writeString(codStato);
		stream.writeString(scoStato);
		stream.writeObject(mandatoVO);
	}

	public static FascicoloVO mapper(FascicoloModel input, String cuaa) {
		if (input == null) {
			return null;
		}
		FascicoloVO dto = new FascicoloVO();

		dto.codiceFiscale = cuaa;
		if (input.getDataApertura() != null) {
			dto.dtInizio = Date.valueOf(input.getDataApertura());
		}
		dto.codOp = "ORGPAG";
		if (input.getOrganismoPagatore().equals(OrganismoPagatoreEnum.APPAG)) {
			dto.scoOp = "IT25";
		}
		dto.codStato = "STAFAS";
		dto.scoStato = "VALIDO";
		dto.mandatoVO = MandatoVO.mapper(input, cuaa);

		return dto;
	}

}
