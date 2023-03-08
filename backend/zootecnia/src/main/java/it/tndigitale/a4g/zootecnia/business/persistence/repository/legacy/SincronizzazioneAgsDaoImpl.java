package it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.AllevamentoModel;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.AllevamentoDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.zootecnia.dto.legacy.vo.AllevamentoVO;

@Repository
public class SincronizzazioneAgsDaoImpl extends JdbcDaoSupport implements SincronizzazioneAgsDao {

	static final Logger log = LoggerFactory.getLogger(SincronizzazioneAgsDaoImpl.class);

	@Autowired
	private DataSourceConfig dataSourceConfig;

	@Autowired
	private FascicoloDao fascicoloDao;

	@Autowired
	private AllevamentoDao allevamentoDao;

	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.jdbcTemplate = new JdbcTemplate(dataSourceConfig.secondaryDataSource());
	}

	public void sincronizzaFascicoloAgs(String cuaa, Integer idValidazione) throws SincronizzazioneAgsException
	{
		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();

			/* Verifico esistenza fascicolo in Zootecnia */
			Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
			if (!fascicoloModelOpt.isPresent()) {
				var err = String.format("Fascicolo CUAA %s non trovato in Zootecnia", cuaa);
				log.error(err);
				// non lancio eccezione perché la mancata presenza del fascicolo in Zootecnia non è un errore, significa che non ha allevamenti
				return;
			}

			List<AllevamentoModel> allevamenti = allevamentoDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, idValidazione);
			if (allevamenti == null || allevamenti.isEmpty()) {	
				var err = String.format("Non sono stati trovati allevamenti in Zootecnia per il fascicolo CUAA %s", cuaa);
				log.error(err);
				return;
			}
			List<AllevamentoVO> allevamentiVO = AllevamentoVO.mapper(allevamenti);

			if (connection == null || allevamentiVO == null || allevamentiVO.isEmpty()) {	
				return;
	        }
			var allevamentiArray = connection.unwrap(oracle.jdbc.OracleConnection.class).createOracleArray("AGS_ALLEVAMENTI", allevamentiVO.toArray());
				
			log.info("Chiamata alla Stored Function Zootecnia...");

			agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_ALLEVAMENTI( ?, ? ) }");
			agsStoredFunc.registerOutParameter(1, Types.NUMERIC);
			agsStoredFunc.setString(2, cuaa);
			agsStoredFunc.setArray(3, allevamentiArray);
			agsStoredFunc.execute();

			var codRit = agsStoredFunc.getBigDecimal(1);
			var info = String.format("Esito restituito Zootecnia: %d", codRit.intValue());
			log.info(info);
		} catch (Exception ex) {
			throw new SincronizzazioneAgsException(ex.getCause().getMessage());
		} finally {
			try {
				if (agsStoredFunc != null) {
					agsStoredFunc.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni Zootecnia", e);
			}
		}
	}

}