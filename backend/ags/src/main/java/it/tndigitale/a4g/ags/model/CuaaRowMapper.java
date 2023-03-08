package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.Cuaa;

public class CuaaRowMapper implements RowMapper<Cuaa> {

	public CuaaRowMapper() {
		super();
	}

	@Override
	public Cuaa mapRow(ResultSet rs, int rowNum) throws SQLException {
		Cuaa cuaa = new Cuaa();
		cuaa.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
		cuaa.setCognome(rs.getString("COGNOME"));
		cuaa.setNome(rs.getString("NOME"));
		cuaa.setSesso(rs.getString("SESSO"));
		cuaa.setDataNascita(rs.getDate("DATA_NASCITA"));
		cuaa.setCodiceIstatNascita(rs.getString("CODICE_ISTAT_NASCITA"));
		cuaa.setSiglaProvNacita(rs.getString("SIGLA_PROV_NASCITA"));
		cuaa.setComuneNascita(rs.getString("COMUNE_NASCITA"));
		cuaa.setIndirizzoRecapito(rs.getString("INDIRIZZO_RECAPITO"));
		cuaa.setCodiceIstatRecapito(rs.getBigDecimal("CODICE_ISTAT_RECAPITO"));
		cuaa.setSiglaProvRecapito(rs.getString("SIGLA_PROV_RECAPITO"));
		cuaa.setComuneRecapito(rs.getString("COMUNE_RECAPITO"));
		cuaa.setCap(rs.getBigDecimal("CAP"));
		return cuaa;
	}

}
