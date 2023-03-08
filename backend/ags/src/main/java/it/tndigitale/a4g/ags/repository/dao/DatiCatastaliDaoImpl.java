package it.tndigitale.a4g.ags.repository.dao;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.dto.RegioneCatastale;
import it.tndigitale.a4g.ags.model.RegioneCatastaleRowMapper;

@Repository
public class DatiCatastaliDaoImpl extends JdbcDaoSupport implements DatiCatastaliDao {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final String REGIONE_BY_CODNAZIONALE = "select r.* from siti.cata_sezioni s " + 
			"join siti.cata_comu c on s.id_comune = c.id_comune and :dataValidita between c.inizio and c.fine " + 
			"join siti.cata_prov p on c.id_prov = p.id_prov and :dataValidita between p.inizio and p.fine " + 
			"join siti.cata_regi r on r.id_regi = p.id_regi and :dataValidita between r.inizio and r.fine " + 
			"where :dataValidita between s.inizio and s.fine " + 
			"and s.cod_sezione = :codNazionale";

	@Override
	public RegioneCatastale getRegioneByCodNazionaleAndDataValidita(String codNazionale, Date dataValidita) {
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("codNazionale", codNazionale)
				.addValue("dataValidita", dataValidita);
		try {
			return namedParameterJdbcTemplate.queryForObject(REGIONE_BY_CODNAZIONALE, params, new RegioneCatastaleRowMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.error("getRegioneByCodNazionaleAndDataValidita: ", e);
			return null;
		}
	}

}
