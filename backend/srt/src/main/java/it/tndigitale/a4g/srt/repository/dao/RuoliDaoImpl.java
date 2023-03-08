package it.tndigitale.a4g.srt.repository.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class RuoliDaoImpl extends JdbcDaoSupport implements RuoliDao {

	private final String sqlGetRuoliPerUtente = "SELECT PROFILO FROM vUTENTI WHERE ATTIVO = 1 AND CF_UTENTE = ?";

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public List<String> getRuoliPerUtente(String codiceFiscale) {
		logger.trace(String.format("RuoliDaoImpl getRuoliPerUtente codiceFiscale: %s", codiceFiscale));

		List<Object> params = new ArrayList<>();
		params.add(codiceFiscale);
		return getJdbcTemplate().query(sqlGetRuoliPerUtente, new String[] { codiceFiscale }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}

}
