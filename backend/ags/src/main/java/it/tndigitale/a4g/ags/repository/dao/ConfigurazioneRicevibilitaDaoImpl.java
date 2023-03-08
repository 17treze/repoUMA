package it.tndigitale.a4g.ags.repository.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.dto.ScadenziarioRicevibilitaDto;
import it.tndigitale.a4g.framework.config.DataSourceConfig;

@Repository
public class ConfigurazioneRicevibilitaDaoImpl extends JdbcDaoSupport implements ConfigurazioneRicevibilitaDao {

	private static final String SCADENZIARIO_RICEVIBILITA =
			"SELECT SCADENZA_DOM_INIZIALE_RITARDO, SCADENZA_DOMANDA_RITIROPARZIAL, SCADENZA_DOM_MODIFICA_RITARDO " +
					"FROM a4gt_conf_ricevibilita WHERE anno_campagna = ?";


	@Autowired
	private DataSource dataSource;

	@Autowired
	private DataSourceConfig dataSourceConfig;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}


	@Override
	public ScadenziarioRicevibilitaDto getScandenziario(Integer campagna) throws NoResultException {
		List<Object> criteri = new ArrayList<>();
		criteri.add(campagna);

		return getJdbcTemplate().queryForObject(SCADENZIARIO_RICEVIBILITA, criteri.toArray(), new ScadenziarioRowMapper());
	}


	private class ScadenziarioRowMapper implements RowMapper<ScadenziarioRicevibilitaDto> {
		@Override
		public ScadenziarioRicevibilitaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new ScadenziarioRicevibilitaDto()
					.setScadenzaDomandaInizialeInRitardo(rs.getDate("SCADENZA_DOM_INIZIALE_RITARDO"))
					.setScadenzaDomandaRitiroParziale(rs.getDate("SCADENZA_DOMANDA_RITIROPARZIAL"))
					.setScadenzaDomandaModificaInRitardo(rs.getDate("SCADENZA_DOM_MODIFICA_RITARDO"));
		}
	}

}
