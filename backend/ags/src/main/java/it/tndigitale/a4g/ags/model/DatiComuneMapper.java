package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.DatiComune;

public class DatiComuneMapper implements RowMapper<DatiComune> {

	@Override
	public DatiComune mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DatiComune(rs.getString("DENO_REGI"), rs.getString("SIGLA_PROV"), rs.getString("DENO_PROV"), rs.getString("CODICE_ISTAT"));
	}

}
