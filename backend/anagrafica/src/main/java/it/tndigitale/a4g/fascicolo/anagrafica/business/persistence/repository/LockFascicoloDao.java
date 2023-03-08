package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.config.DataSourceConfig;

@Repository
public class LockFascicoloDao extends NamedParameterJdbcDaoSupport {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private DataSourceConfig dataSourceConfig;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}

	private static final String SQL_INSERT_TCUAA_LOCK = "INSERT INTO TCUAA_LOCK" + "(CUAA, UTENTE, TIMESTAMP, TIPO_LOCK)" + " VALUES (?, ?, ?, ?)";

	public void lockFascicolo(String cuaa) throws Exception {
		Connection connection = null;
		PreparedStatement agsQuery = null;

		connection = this.dataSourceConfig.secondaryDataSource().getConnection();
		agsQuery = connection.prepareStatement(this.SQL_INSERT_TCUAA_LOCK);
		agsQuery.setString(1, cuaa);
		agsQuery.setString(2, "A4G");
		agsQuery.setDate(3, Date.valueOf(LocalDate.now()));
		agsQuery.setInt(4, 0);
		agsQuery.execute();
	}
}
