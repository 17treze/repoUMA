package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.domandaunica.Particella;

public class ParticelleRowMapper implements RowMapper<Particella> {

	public ParticelleRowMapper() {
		super();
	}

	@Override
	public Particella mapRow(ResultSet rs, int rowNum) throws SQLException {
		Particella particella = new Particella();

		particella.setIdParticella(rs.getLong("ID_PARTICELLA"));
		particella.setComune(rs.getString("COMUNE"));
		particella.setCodNazionale(rs.getString("COD_NAZIONALE"));
		particella.setFoglio(rs.getLong("FOGLIO"));
		particella.setParticella(rs.getString("PARTICELLA"));
		particella.setSub(rs.getString("SUB"));
		return particella;
	}

}
