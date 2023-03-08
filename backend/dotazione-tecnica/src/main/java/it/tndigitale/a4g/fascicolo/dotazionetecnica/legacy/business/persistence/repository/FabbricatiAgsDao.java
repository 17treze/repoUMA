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

		// Filtro Province
		if (!CollectionUtils.isEmpty(province)) {
			sql = sql.concat("AND comu.SIGLA_PROV IN ( " + createfilterProvince(province)+ " ) ");
		} else {
			sql = sql.concat("AND comu.SIGLA_PROV IN ( 'TN','BZ' ) ");
		}

		// Filtro Titoli Conduzione
		if (!CollectionUtils.isEmpty(titoloConduzioneAgs)) {
			sql = sql.concat("AND t.SCO_TIPO_POSSESSO IN ( " + createfilterTitoliConduzione(titoloConduzioneAgs) + " )");
		}

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
		return "SELECT t.ID_FABBRICATO, " + 
				"t.VOLUME, " +
			    "t.SUPERFICIE, " +
			    "NVL(TO_CHAR(cattn.COD_CC), t.COD_NAZIONALE) as COMUNE_CATASTALE, " +
			    "comu.sezione_censuaria as SEZIONE, " +
			    "t.FOGLIO, " +
			    "t.NOTE as DESCRIZIONE, " +
				"t.SCO_TIPO_POSSESSO AS CODICE_TITOLO_CONDUZIONE," +
				"t.SCO_TIPO_FABBRICATO AS CODICE_FABBRICATO, " + 
				"t.PARTICELLA, " + 
				"t.SUB AS SUBALTERNO," +
				"comu.NOME AS COMUNE, " + 
				"comu.SIGLA_PROV AS SIGLA_PROVINCIA, " + 
				"comu.DENO_PROV AS PROVINCIA, " + 
				"(SELECT DS_DECODIFICA FROM TDECODIFICA WHERE CODICE = t.COD_TIPO_FABBRICATO AND SOTTO_CODICE = t.SCO_TIPO_FABBRICATO) AS TIPO_FABBRICATO, " + 
				"(SELECT DS_DECODIFICA FROM TDECODIFICA WHERE CODICE = t.COD_TIPO_POSSESSO AND SOTTO_CODICE = t.SCO_TIPO_POSSESSO) AS TITOLO_CONDUZIONE, " + 
				"t.NOTE " +
				"FROM TFABBRICATO t " + 
				"INNER JOIN SITI.CONS_SOGG_VIW csv ON t.ID_SOGGETTO = csv.PK_CUAA " + 
				"INNER JOIN SITI.SITICOMU comu ON t.COD_NAZIONALE = comu.COD_NAZIONALE " + 
				"INNER JOIN CATA_SEZIONI catasez ON t.COD_NAZIONALE = catasez.COD_SEZIONE " + 
				"LEFT JOIN SITI.AMM_CAT_TN cattn ON t.cod_nazionale = cattn.cod_nazionale " + 
				"WHERE :data between csv.data_inizio_val and csv.data_fine_val " + 
				"AND :data BETWEEN t.DT_INIZIO AND t.DT_FINE " + 
				"AND :data BETWEEN t.DT_INSERT AND t.DT_DELETE " + 
				"AND :data BETWEEN catasez.INIZIO AND catasez.FINE " + 
				"AND csv.CUAA = :cuaa ";
	}
}
