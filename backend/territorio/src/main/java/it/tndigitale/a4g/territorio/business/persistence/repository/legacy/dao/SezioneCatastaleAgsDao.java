package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.SezioneCatastaleDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.rowmapper.SezioneCatastaleRowMapper;
import it.tndigitale.a4g.territorio.dto.SezioneCatastaleFilter;

@Repository
public class SezioneCatastaleAgsDao extends NamedParameterJdbcDaoSupport {

	private static final String BASE_SELECT = "select sezione.cod_nazionale codice, sezione.nome denominazione, "
			+ "sezione.CODI_FISC_LUNA codiceFiscaleCA, sezione.istatc codiceIstatCA, sezione.NOME_COMUNE denominazioneCA, "
			+ "sezione.sigla_prov siglaProv, sezione.istatp codiceIstatProv, sezione.deno_prov denominazioneProv" + " from siticomu sezione";

	private static final String DEFAULT_ORDERBY = "codiceIstatProv asc, codiceIstatCA asc, denominazione asc";

	private static final String AND = " and ";
	private static final String OR = " or ";
	private static final String TIPOCOMUNE_AMM = "AMM";
	private static final String TIPOCOMUNE_CAT = "CAT";

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

	public RisultatiPaginati<SezioneCatastaleDto> ricerca(SezioneCatastaleFilter criteri) {
		logger.debug("ricerca - inizio ");
		String sql = BASE_SELECT;
		final MapSqlParameterSource params = new MapSqlParameterSource();
		sql = sql.concat(getWhere(criteri, params));
		sql = sql.concat(" order by " + DEFAULT_ORDERBY);
		List<SezioneCatastaleDto> lista = namedParameterJdbcTemplate.query(sql, params, new SezioneCatastaleRowMapper());
		RisultatiPaginati<SezioneCatastaleDto> result = new RisultatiPaginati<SezioneCatastaleDto>();
		result.setCount(Optional.ofNullable(lista).map(l -> Long.valueOf("" + l.size())).orElse(0L));
		result.setRisultati(lista);
		return result;
	}

	protected String getWhere(SezioneCatastaleFilter criteri, MapSqlParameterSource params) {
		String where = "";
		if (criteri != null) {
			boolean filtro = false;
			if (!StringUtils.isEmpty(criteri.getQ())) {
				where = where.concat(condizioneWhereOrAdd(filtro, OR));
				where = where.concat("(");
				where = where.concat("sezione.nome like :denominazioneSezione1");
				params.addValue("denominazioneSezione1", "%" + criteri.getQ().toUpperCase() + "%");
				filtro = true;
				where = where.concat(condizioneWhereOrAdd(filtro, OR));
				where = where.concat("sezione.cod_nazionale = :codiceSezione1");
				params.addValue("codiceSezione1", criteri.getQ());
				where = where.concat(")");
			}
			if (!StringUtils.isEmpty(criteri.getDenominazioneSezione())) {
				where = where.concat(condizioneWhereOrAdd(filtro, AND));
				where = where.concat("sezione.nome like :denominazioneSezione");
				params.addValue("denominazioneSezione", "%" + criteri.getDenominazioneSezione().toUpperCase() + "%");
				filtro = true;
			}
			if (!StringUtils.isEmpty(criteri.getCodiceSezione())) {
				where = where.concat(condizioneWhereOrAdd(filtro, AND));
				where = where.concat("sezione.cod_nazionale = :codiceSezione");
				params.addValue("codiceSezione", criteri.getCodiceSezione());
				filtro = true;
			}
			if (!StringUtils.isEmpty(criteri.getSiglaProvincia())) {
				where = where.concat(condizioneWhereOrAdd(filtro, AND));
				where = where.concat("sezione.sigla_prov = :siglaProvincia");
				params.addValue("siglaProvincia", criteri.getSiglaProvincia());
				filtro = true;
			}
			if (!StringUtils.isEmpty(criteri.getIstatProvincia())) {
				where = where.concat(condizioneWhereOrAdd(filtro, AND));
				where = where.concat("sezione.istatp = :istatProvincia");
				params.addValue("istatProvincia", criteri.getIstatProvincia());
				filtro = true;
			}
			if (!StringUtils.isEmpty(criteri.getTipoComune())) {
				if (criteri.getTipoComune().equals(TIPOCOMUNE_CAT)) {
					where = where.concat(condizioneWhereOrAdd(filtro, AND));
					where = where.concat("(sezione.cod_nazionale <> sezione.codi_fisc_luna or (sezione.cod_nazionale = sezione.codi_fisc_luna "
							+ "  and not exists (select 1 from siticomu a where a.codi_fisc_luna = sezione.codi_fisc_luna and a.cod_nazionale <> sezione.codi_fisc_luna)))");
					filtro = true;
				}
				if (criteri.getTipoComune().equals(TIPOCOMUNE_AMM)) {
					where = where.concat(condizioneWhereOrAdd(filtro, AND));
					where = where.concat("sezione.cod_nazionale = sezione.codi_fisc_luna");
					filtro = true;
				}
			}
		}
		return where;
	}

	protected String condizioneWhereOrAdd(boolean filtro, String andOr) {
		return Optional.of(filtro).filter(Boolean::booleanValue).map(f -> andOr).orElseGet(() -> " where ");
	}
}
