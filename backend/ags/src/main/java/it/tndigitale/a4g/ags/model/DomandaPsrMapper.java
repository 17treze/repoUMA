package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.DomandaPsr;

public class DomandaPsrMapper implements RowMapper{


	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DomandaPsr(rs.getString("COD_MODULO"), 
				rs.getInt("ANNO"), 
				rs.getString("CUAA"), 
				rs.getString("RAGI_SOCI"), 
				rs.getLong("ID_DOMANDA"), 
				rs.getDate("DATA_DOMANDA"), 
				rs.getString("SCO_SETTORE"), 
				rs.getString("STATO_DOMANDA"), 
				rs.getBigDecimal("IMPORTO_RICHIESTO"));
	}

}
