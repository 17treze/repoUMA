package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.domandaunica.DatiPascolo;

public class DatiPascoloRowMapper implements RowMapper {

	public DatiPascoloRowMapper() {
		super();
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		DatiPascolo impegnoPascolo = new DatiPascolo();

		impegnoPascolo.setCodPascolo(rs.getString("COD_PASCOLO"));
		impegnoPascolo.setDescPascolo(rs.getString("DENOMINAZIONE"));
		impegnoPascolo.setUba(rs.getDouble("UBA"));

		return impegnoPascolo;
	}

}
