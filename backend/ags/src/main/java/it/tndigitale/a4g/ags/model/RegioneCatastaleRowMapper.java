package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.RegioneCatastale;

public class RegioneCatastaleRowMapper implements RowMapper<RegioneCatastale> {

	public RegioneCatastaleRowMapper() {
		super();
	}

	@Override
	public RegioneCatastale mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return new RegioneCatastale()
				.withIdRegione(rs.getInt("ID_REGI"))
				.withDenominazione(rs.getString("DENO_REGI"))
				.withCodiceIstat(rs.getString("ISTATR"));
	}

}
