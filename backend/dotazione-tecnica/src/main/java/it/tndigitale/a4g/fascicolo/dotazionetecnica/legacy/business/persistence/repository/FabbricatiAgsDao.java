package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.persistence.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.TitoloConduzioneAgs;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

@Repository
public class FabbricatiAgsDao extends NamedParameterJdbcDaoSupport {

	@Autowired
	private DataSourceConfig dataSourceConfig;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
	}


	public List<FabbricatoAgsDto> getFabbricati(String cuaa, LocalDateTime data, List<String> province, List<TitoloConduzioneAgs> titoloConduzioneAgs) {
		String sql = getFabbricatiSql();
		MapSqlParameterSource params = new MapSqlParameterSource("cuaa", cuaa)
				.addValue("data", LocalDateConverter.to(data));

//		// Filtro Province
//		if (!CollectionUtils.isEmpty(province)) {
//			sql = sql.concat("AND comu.SIGLA_PROV IN ( " + createfilterProvince(province)+ " ) ");
//		} else {
//			sql = sql.concat("AND comu.SIGLA_PROV IN ( 'TN','BZ' ) ");
//		}
//
//		// Filtro Titoli Conduzione
//		if (!CollectionUtils.isEmpty(titoloConduzioneAgs)) {
//			sql = sql.concat("AND t.SCO_TIPO_POSSESSO IN ( " + createfilterTitoliConduzione(titoloConduzioneAgs) + " )");
//		}

		logger.debug("[FabbricatiAgsDao] - get fabbricati Ags: ".concat(sql));

		try {
			return getNamedParameterJdbcTemplate().<FabbricatoAgsDto>query(sql, params, new FabbricatiRowMapper());
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("[FabbricatiAgsDao] - getFabbricati: ", e);
			return new ArrayList<>();
		}
	}

	private String createfilterTitoliConduzione(List<TitoloConduzioneAgs> titoliConduzione) {
		StringJoiner joiner = new StringJoiner(",");
		titoliConduzione.forEach(titoloConduzione -> {
			if (titoloConduzione.equals(TitoloConduzioneAgs.AFFITTO)) {
				joiner.add("'000002'");
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.ALTRE_FORME)) {
				joiner.add("'000004'");
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.COMODATO)) {
				joiner.add("'000010'"); 
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.MEZZADRIA)) {
				joiner.add("'000003'");
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.NON_CONDOTTA)) {
				joiner.add("'000999'");
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.NON_DEFINITO)) {
				joiner.add("'000000'");
				joiner.add("'000005'");
				joiner.add("'000006'");
				joiner.add("'000007'");
				joiner.add("'000008'");
				joiner.add("'000009'");
			}
			if (titoloConduzione.equals(TitoloConduzioneAgs.PROPRIETA)) {
				joiner.add("'000001'");
			}
		});
		return joiner.toString();
	}

	private String createfilterProvince(List<String> sigleProvincia) {
		StringJoiner joiner = new StringJoiner(",");
		sigleProvincia.forEach(sigla -> joiner.add("'"+ sigla +"'"));
		return joiner.toString();
	}

	private String getFabbricatiSql() {
		return "SELECT t.ID as id_fabbricato, \r\n"
				+ "    t.VOLUME, \r\n"
				+ "    t.SUPERFICIE, \r\n"
				+ "    null as COMUNE_CATASTALE,\r\n"
				+ "    NULL as SEZIONE, \r\n"
				+ "    9999 as FOGLIO, \r\n"
				+ "    t.DESCRIZIONE, \r\n"
				+ "    null AS CODICE_TITOLO_CONDUZIONE, \r\n"
				+ "    null AS CODICE_FABBRICATO, \r\n"
				+ "    null as PARTICELLA, \r\n"
				+ "    null AS SUBALTERNO, \r\n"
				+ "    t.COMUNE, \r\n"
				+ "    null AS SIGLA_PROVINCIA, \r\n"
				+ "    null AS PROVINCIA, \r\n"
				+ "    null AS TIPO_FABBRICATO, \r\n"
				+ "    null AS TITOLO_CONDUZIONE, \r\n"
				+ "    t.denominazione as NOTE \r\n"
				+ "FROM a4gt_FABBRICATO t \r\n"
				+ "    INNER JOIN a4gt_fascicolo f ON t.ID_fascicolo = f.id and t.id_validazione_fascicolo = f.id_validazione \r\n"
				+ "    WHERE f.CUAA = :cuaa ";
	}
}
