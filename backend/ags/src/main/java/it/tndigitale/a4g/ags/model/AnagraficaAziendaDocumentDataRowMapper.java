package it.tndigitale.a4g.ags.model;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaDocumentData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnagraficaAziendaDocumentDataRowMapper implements RowMapper<AnagraficaAziendaDocumentData> {

	@Override
	public AnagraficaAziendaDocumentData mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new AnagraficaAziendaDocumentData(rs.getInt("tipodocumento"),
												 rs.getString("numerodocumento"),
												 rs.getTimestamp("datadocumento"),
												 rs.getTimestamp("datascaddocumento"));
	}

}
