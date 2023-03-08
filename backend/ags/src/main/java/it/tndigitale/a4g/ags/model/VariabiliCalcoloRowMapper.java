package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.VariabileSostegnoAgs;

public class VariabiliCalcoloRowMapper implements RowMapper {

	public VariabiliCalcoloRowMapper() {
		super();
	}

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		VariabileSostegnoAgs variabile = new VariabileSostegnoAgs();
		variabile.setVariabile(rs.getString("VARIABILE"));
		variabile.setDescVariabile(rs.getString("DS_VARIABILE"));
		variabile.setCodGruppo(rs.getString("COD_GRUPPO"));
		variabile.setValore(rs.getString("VALORE"));
		variabile.setOrdine(rs.getBigDecimal("ORDINE"));

		return variabile;
	}

}
