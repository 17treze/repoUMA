package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.CalcoloSostegnoAgs;

public class CalcoloSostegnoRowMapper implements RowMapper {

	public CalcoloSostegnoRowMapper() {
		super();
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		CalcoloSostegnoAgs calcolo = new CalcoloSostegnoAgs();
		calcolo.setIdCalcolo(rs.getLong("ID_CALCOLO"));
		calcolo.setIdDefCalcolo(rs.getLong("ID_DEF_CALCOLO"));
		calcolo.setCodCalcolo(rs.getString("COD_CALCOLO"));

		return calcolo;
	}

}
