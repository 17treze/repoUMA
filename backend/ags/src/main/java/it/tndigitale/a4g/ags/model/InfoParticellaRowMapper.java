package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.InfoParticella;

public class InfoParticellaRowMapper implements RowMapper<InfoParticella> {
	
	public InfoParticellaRowMapper() {
		super();
	}

	@Override
	public InfoParticella mapRow(ResultSet rs, int rowNum) throws SQLException {

		InfoParticella infoParticella = new InfoParticella();
		
		infoParticella.setCodiceComuneCatastale(rs.getInt("CAT_COMUNE_CO"));
		infoParticella.setParticella(rs.getString("PARTICELLA"));
		infoParticella.setSub(rs.getString("SUB"));
		
		return infoParticella;
	}

}
