package it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.zootecnia.dto.ReportValidazioneConsistenzaZootecnicaDto;
import it.tndigitale.a4g.framework.config.DataSourceConfig;

@Repository
public class FascicoloAgsDao extends NamedParameterJdbcDaoSupport {

	static final Logger log = LoggerFactory.getLogger(FascicoloAgsDao.class);
	
	@Autowired private DataSource dataSource;

	@Autowired
	private DataSourceConfig dataSourceConfig;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
	   setDataSource(dataSourceConfig.secondaryDataSource());
	   this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}	
		
	public List<ReportValidazioneConsistenzaZootecnicaDto> findConsistenzaZootecnica(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneConsistenzaZootecnicaDto> consZootList = new ArrayList<ReportValidazioneConsistenzaZootecnicaDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("SELECT \r\n"
					+ "    tcd.ds_tipo_capo as gruppo,\r\n"
					+ "    tc.conteggio1 as numero_capi,\r\n"
					+ "    tcd.uba as coefficiente_uba,\r\n"
					+ "    (tc.conteggio1 * tcd.uba) AS conteggio_uba\r\n"
					+ "FROM FASCICOLO.anag_soggetti s,\r\n"
					+ "    FASCICOLO.tcapo_conteggio tc,\r\n"
					+ "    FASCICOLO.tcapo_conteggio_def tcd,\r\n"
					+ "    FASCICOLO.tdecodifica td\r\n"
					+ "WHERE s.cod_fiscale = ? \r\n"
					+ "    and tc.id_soggetto = s.cod_soggetto\r\n"
					+ "    and sysdate between s.data_inizio_val and s.data_fine_val\r\n"
					+ "    AND tcd.id_tipo_capo = tc.id_tipo_capo\r\n"
					+ "    AND NVL (NULL, SYSDATE) BETWEEN tc.dt_insert AND tc.dt_delete\r\n"
					+ "    AND td.codice = tcd.cod_gruppo_capo\r\n"
					+ "    AND td.sotto_codice = tcd.sco_gruppo_capo\r\n"
					+ "ORDER BY td.ordine, tcd.ordine");
			agsQuery.setString(1, cuaa);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Consistenza zootecnica: {}, {}", rs.getString(2), rs.getString(3));
				consZootList.add(
					new ReportValidazioneConsistenzaZootecnicaDto(
						rs.getString(1),
						rs.getInt(2),
						rs.getFloat(3),
						rs.getFloat(4)));
		    }
		    return consZootList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}
}
