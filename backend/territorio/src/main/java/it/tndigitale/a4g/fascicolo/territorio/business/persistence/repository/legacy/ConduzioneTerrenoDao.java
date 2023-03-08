package it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.ConduzioneSianDto;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import oracle.sql.CLOB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class ConduzioneTerrenoDao extends NamedParameterJdbcDaoSupport {

	@Autowired private DataSource dataSource;
	@Autowired private ObjectMapper objectMapper;
	
	@Autowired
	private DataSourceConfig dataSourceConfig;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
	   setDataSource(dataSourceConfig.secondaryDataSource());
	   this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}	
	
	public Short salvaConduzioneTerreno(final String cuaa, List<ConduzioneSianDto> conduzioneDtoList) throws SQLException, JsonProcessingException {
		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
//			connection = this.dataSource.getConnection();
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_CONSISTENZA_SIAN(?, ?) }");
			agsStoredFunc.registerOutParameter(1, Types.NUMERIC);
			agsStoredFunc.setString(2, cuaa);
			Clob clobValue = connection.createClob();
			clobValue.setString(1, objectMapper.writeValueAsString(conduzioneDtoList));
			agsStoredFunc.setClob(3, clobValue);
			agsStoredFunc.execute();
			return agsStoredFunc.getShort(1);
		} finally {
			try {
				if (agsStoredFunc != null) {
					agsStoredFunc.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Errore in chiusura connessioni", e);
			}
		}
	}
}