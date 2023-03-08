package it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import it.tndigitale.a4g.framework.config.DataSourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.rowmapper.PianoColturaleRowMapper;
import it.tndigitale.a4g.fascicolo.territorio.dto.PianoColturaleFilter;
import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;
import it.tndigitale.a4g.fascicolo.territorio.dto.legacy.PianoColturaleAgsDto;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

@Repository
public class PianoColtureAgsDao extends NamedParameterJdbcDaoSupport{

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

	public List<PianoColturaleAgsDto> getColture(PianoColturaleFilter filter) {
		final MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("cuaa", filter.getCuaa())
				.addValue("data", LocalDateConverter.to(filter.getData()))
				.addValue("anno", filter.getData().getYear());
		String queryPianoColturaleSql = getQueryPianoColturaleSql();

		// Filtro Provincia
		if (!CollectionUtils.isEmpty(filter.getProvince())) {
			queryPianoColturaleSql = queryPianoColturaleSql.concat("AND comu.SIGLA_PROV IN ( " + createfilterProvincia(filter.getProvince())+ " ) ");
		} else {
			queryPianoColturaleSql = queryPianoColturaleSql.concat("AND comu.SIGLA_PROV IN ( 'TN','BZ' ) ");
		}

		// filtro sulla conduzione 
		Map<TitoloConduzione, Integer> mappaTitoloConduzione = getMappaTitoloConduzione();
		if (filter.getTitolo() != null) {
			params.addValue("titolo", mappaTitoloConduzione.get(filter.getTitolo()));
			queryPianoColturaleSql = queryPianoColturaleSql.concat("AND c.TIPO_TITOLO = :titolo ");

		}

		if (filter.getCodiceAtto() != null) {
			params.addValue("codice", filter.getCodiceAtto());
			queryPianoColturaleSql = queryPianoColturaleSql.concat("AND d.fkid_tipo_doc = :codice ");
		}

		try {
			return namedParameterJdbcTemplate.<PianoColturaleAgsDto>query(queryPianoColturaleSql, params, new PianoColturaleRowMapper());
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("[PianoColtureAgsDao] - getPianiColturali: ", e);
			return new ArrayList<>();
		}
	}

	private String getCodifica(String codice) {
		return String.format("(SELECT %s FROM TABLE(CODIFICA_3_5(pc.ID_COLTURA_1, NULL, :anno))) AS %s ", codice, codice);
	}

	private String getQueryPianoColturaleSql() {
		return "SELECT DISTINCT pc.ID_COLTURA_1 AS ID_COLTURA, pc.ID_PARTICELLA, pc.FOGLIO, pc.PARTICELLA, pc.SUB, pc.SUP_ACCERTATA, pc.SUP_DICHIARATA, pc.COD_NAZIONALE, c.TIPO_TITOLO, d.fkid_tipo_doc, t.descrizione, cod.DE_DECODIFICA AS CRIT_MANT, "
				+ getCodifica("CODI_PROD") + ", "
				+ getCodifica("CODI_DEST_USO") + ", "
				+ getCodifica("CODI_USO") + ", "
				+ getCodifica("CODI_QUAL") + ", "
				+ getCodifica("CODI_VARI")
				+ " FROM FASCICOLO.TPIANO_COLTURE pc "
				+ "INNER JOIN siti.SITICOND c ON c.ID_PARTICELLA  = pc.ID_PARTICELLA  "
				+ "LEFT OUTER JOIN siti.sitifile_atti a ON  a.pkid_atto = c.atto_inizio "
				+ "LEFT OUTER JOIN siti.sitifile_atti_doc ad ON ad.fkid_atto = a.pkid_atto "
				+ "LEFT OUTER JOIN siti.sitifile_documenti d on d.pkid_doc = ad.fkid_doc "
				+ "LEFT OUTER JOIN siti.sitifile_tipidoc t ON t.pkid_tipo_doc = d.fkid_tipo_doc "
				+ "LEFT OUTER JOIN siti.SITIFILE_DOCUMENTI_CAMPI_EXT r ON r.fkid_doc = d.pkid_doc  "
				+ "LEFT OUTER JOIN FASCICOLO.TDECODIFICA cod ON pc.COD_CRIT_MANTENIMENTO = cod.CODICE AND pc.SCO_CRIT_MANTENIMENTO = cod.SOTTO_CODICE "
				+ "INNER JOIN SITI.CONS_SOGG_VIW sogg ON pc.ID_SOGGETTO = sogg.PK_CUAA "
				+ "INNER JOIN TCOLTURA coltura ON coltura.ID_COLTURA = pc.ID_COLTURA_1 "
				+ "INNER JOIN SITI.SITICOMU comu ON pc.COD_NAZIONALE = comu.COD_NAZIONALE "
				+ "INNER JOIN CATA_SEZIONI catasez ON pc.COD_NAZIONALE = catasez.COD_SEZIONE "
				+ "WHERE sysdate between sogg.data_inizio_val AND sogg.data_fine_val "
				+ "AND :anno BETWEEN coltura.ANNO_INIZIO AND coltura.ANNO_FINE "
				+ "AND :data BETWEEN c.DATA_INIZIO AND c.DATA_FINE "
				+ "AND :data BETWEEN pc.DT_INIZIO AND pc.DT_FINE "
				+ "AND :data BETWEEN pc.DT_INSERT AND pc.DT_DELETE "
				+ "AND sogg.CUAA = :cuaa "
				+ "AND c.CUAA = :cuaa "
				+ "AND pc.ANNO = :anno "
				+ "AND :data BETWEEN catasez.INIZIO AND catasez.FINE "				
				+ "AND :data BETWEEN coltura.dt_inizio AND coltura.dt_fine "
				+ "AND ((:data BETWEEN d.DATA_INIZIO_VAL AND d.data_fine_val AND d.data_inizio_val is not null) or (d.data_inizio_val is null)) "
				+ "AND ((:data BETWEEN ad.data_inizio_val AND ad.data_fine_val AND ad.data_inizio_val is not null) or (ad.data_inizio_val is null)) "
				;
	}
	private String createfilterProvincia(List<String> sigleProvincia) {
		var joiner = new StringJoiner(",");
		sigleProvincia.forEach(sigla -> joiner.add("'"+ sigla +"'"));
		return joiner.toString();
	}

	public Map<TitoloConduzione, Integer> getMappaTitoloConduzione() {
		Map<TitoloConduzione, Integer> map = new EnumMap<>(TitoloConduzione.class);
		map.put(TitoloConduzione.PROPRIETA, 1);
		map.put(TitoloConduzione.AFFITTO, 2);
		map.put(TitoloConduzione.MEZZADRIA, 3);
		map.put(TitoloConduzione.ALTRO, 4);
		return map;
	}

}
