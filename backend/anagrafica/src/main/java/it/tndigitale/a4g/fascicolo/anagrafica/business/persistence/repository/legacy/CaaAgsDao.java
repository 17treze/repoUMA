package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

@Repository
public class CaaAgsDao extends JdbcDaoSupport {

	@Autowired
	private DataSourceConfig dataSourceConfig;
	@Autowired
	private Clock clock;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}

	public List<SportelloFascicoloDto> getFascicoliCaa(List<String> sportelliAbilitati, TipoDetenzioneAgs tipoDetenzione) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
//		parameters.addValue("data", LocalDateConverter.to(clock.now()));

		String sportelliAbilitatiString = sportelliAbilitati.stream().collect(Collectors.joining(","));
		List<SportelloFascicoloRow> result = new ArrayList<>();
		String sql = SQL_FASCICOLI_SPORTELLI;
//		String sql = SQL_FASCICOLI_SPORTELLI.concat("AND ente.COD_ENTE IN (").concat(sportelliAbilitatiString).concat(") ");
//		if (tipoDetenzione != null) {
//			parameters.addValue("tipo", tipoDetenzione.getNome());
//			sql = sql.concat("AND cuaa_ente.tipo_associazione = :tipo ");
//		}
		
		try {
			result.addAll(namedParameterJdbcTemplate.<SportelloFascicoloRow>query(sql,parameters, new FascicoliCaaRowMapper()));
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("Errore Recupero Sportelli da fascicolo AGS: ", e);
		}

		return result
				.stream()
				.collect(Collectors.groupingBy(SportelloFascicoloRow::getIdentificativoSportello, Collectors.mapping(SportelloFascicoloRow::getCuaa, Collectors.toList())))
				.entrySet()
				.stream()
				.map(entrySet -> new SportelloFascicoloDto().setIdentificativoSportello(entrySet.getKey()).setCuaaList(entrySet.getValue()))
				.collect(Collectors.toList());
	}

	// Rimuove sportelli uap, appag e caa fuori provincia - 70	CAA C.A.N.A.P.A.
	private static final String SQL_FASCICOLI_SPORTELLI = "select f.cuaa as CUAA, sp.identificativo AS IDENTIFICATIVO_SPORTELLO "
			+ "from a4gt_fascicolo f "
			+ "LEFT OUTER JOIN a4gt_detenzione d on d.id_fascicolo = f.id and d.fascicolo_id_validazione = f.id_validazione "
			+ "LEFT OUTER JOIN a4gt_mandato m on m.id = d.id "
			+ "LEFT OUTER JOIN a4gt_sportello sp on sp.identificativo = m.identificativo_sportello ";
//	private static final String SQL_FASCICOLI_SPORTELLI = "select csv.cuaa as CUAA, ente.COD_ENTE AS IDENTIFICATIVO_SPORTELLO  "
//			+"from cons_sogg_viw csv "
//			+"LEFT OUTER JOIN siti.cons_cuaa_ente cuaa_ente on cuaa_ente.pk_cuaa = csv.pk_cuaa and cuaa_ente.tipo_associazione in ('MAN', 'DEL') and :data between cuaa_ente.data_inizio and cuaa_ente.data_fine AND cuaa_ente.flag_gestione='S' AND cuaa_ente.COD_ENTE not in (1, 23,24,25,26,27,28,29,30,31,32,33, 106, 70) "
//			+"LEFT OUTER JOIN siti.sitiente ente on ente.cod_ente = cuaa_ente.cod_ente and :data between ente.data_inizio and ente.data_fine "
//			+"where sysdate between csv.data_inizio_val and csv.data_fine_val ";


	private class FascicoliCaaRowMapper implements RowMapper<SportelloFascicoloRow> {

		@Override
		public SportelloFascicoloRow mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new SportelloFascicoloRow()
					.setCuaa(rs.getString("CUAA"))
					.setIdentificativoSportello(rs.getInt("IDENTIFICATIVO_SPORTELLO"));
		}

	}

	private class SportelloFascicoloRow {

		private String cuaa;
		private Integer identificativoSportello;

		public String getCuaa() {
			return cuaa;
		}
		public SportelloFascicoloRow setCuaa(String cuaa) {
			this.cuaa = cuaa;
			return this;
		}
		public Integer getIdentificativoSportello() {
			return identificativoSportello;
		}
		public SportelloFascicoloRow setIdentificativoSportello(Integer identificativoSportello) {
			this.identificativoSportello = identificativoSportello;
			return this;
		}
	}


}
