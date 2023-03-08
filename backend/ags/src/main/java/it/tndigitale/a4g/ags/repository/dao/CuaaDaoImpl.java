package it.tndigitale.a4g.ags.repository.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.dto.Cuaa;
import it.tndigitale.a4g.ags.dto.InfoDomandaDU;
import it.tndigitale.a4g.ags.model.CuaaRowMapper;
import it.tndigitale.a4g.ags.model.InfoDomandaDURowMapper;

@Repository
public class CuaaDaoImpl extends JdbcDaoSupport implements CuaaDao {

	private static final Logger logger = LoggerFactory.getLogger(CuaaDaoImpl.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final String GET_CUAA = "select distinct csv.cuaa as codice_fiscale, csv.cognome, csv.nome, csv.sesso, csv.d_nascita as data_nascita, "
			+ "sis_nas.istatp||sis_nas.istatc as codice_istat_nascita, sis_nas.sigla_prov as sigla_prov_nascita, sis_nas.nome as comune_nascita, "
			+ "csv.indirizzo_est as indirizzo_recapito, sis_rec.istatp||sis_rec.istatc as codice_istat_recapito, sis_rec.sigla_prov as sigla_prov_recapito, "
			+ "sis_rec.nome as comune_recapito, csv.cap as cap "
			+ "from cons_sogg_viw csv "
			+ "left join siticomu_istat sis_nas on sis_nas.codi_fisc_luna = csv.fkcod_comu_nasc "
			+ "left join siticomu_istat sis_rec on sis_rec.codi_fisc_luna = csv.fkcod_comu_resi "
			+ "where csv.cuaa = ?";
	
	/**
	 * Serve per il catasto, gli anni > 2017 non deve andare su AGS ma su a4g.istruttoria
	 */
	private static final String GET_INFO_DOMANDA_DU = "SELECT modu.anno AS anno_riferimento, "
		    + "(SELECT ds_decodifica FROM fascicolo.tdecodifica_language WHERE locale = 'it' AND codice = modu.cod_settore AND sotto_codice = modu.sco_settore) AS pac, "
		    + "dom.id_domanda AS numero_domanda, "
		    + "(SELECT ds_decodifica FROM fascicolo.tdecodifica_language WHERE locale = 'it' AND codice = modu.cod_tipo_domanda AND sotto_codice = modu.sco_tipo_domanda) AS descrizione_domanda, "
		    + "(SELECT export_pck_2018.get_data_presentazione(dom.id_domanda) FROM dual) AS data_presentazione, "
		    + "(SELECT cuaa FROM fascicolo.cons_sogg_viw WHERE pk_cuaa = dom.id_soggetto) AS cuaa, "
		    + "(SELECT des_ente FROM siti.sitiente WHERE cod_ente = (SELECT id_ente_compilatore FROM tqdo_enti WHERE SYSDATE BETWEEN dt_insert AND dt_delete AND id_domanda = dom.id_domanda)) AS ente_compilatore, "
		    + "(SELECT ragi_soci FROM fascicolo.cons_sogg_viw WHERE pk_cuaa = dom.id_soggetto) AS ragione_sociale "
		    + "FROM fascicolo.tdom_domanda dom "
		    + "INNER JOIN fascicolo.tdom_modulo_deco modu "
		    + "ON dom.id_modulo = modu.id_modulo "
		    + "WHERE SYSDATE BETWEEN dom.dt_insert AND dom.dt_delete "
		    + "AND ((modu.anno = 2014 AND modu.sco_settore = '000003') OR (modu.anno BETWEEN 2015 AND 2017 AND modu.sco_settore = 'PI2014')) "
		    + "AND dom.sco_stato >= '000050' "
		    + "AND dom.id_soggetto = (SELECT pk_cuaa FROM fascicolo.cons_sogg_viw WHERE cuaa = :cuaa) "
		    + "AND modu.anno = :anno";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Cuaa getInfoCuaa(String cuaa) {
		List<Object> params = new ArrayList<>();
		params.add(cuaa);
		Cuaa result = new Cuaa();
		try {
			result = getJdbcTemplate().queryForObject(GET_CUAA, params.toArray(), new CuaaRowMapper());
			return result;
		} catch (NoResultException e) {
			logger.error("getInfoCuaa: ", e);
			return null;
		}
	}
	
	@Override
	public InfoDomandaDU getInfoDomandaDU(String cuaa, Integer anno) {

		List<Object> params = new ArrayList<>();
		params.add(cuaa);
		params.add(anno);
		InfoDomandaDU result = new InfoDomandaDU();
		try {
			result = getJdbcTemplate().queryForObject(GET_INFO_DOMANDA_DU,params.toArray(), new InfoDomandaDURowMapper());
			return result;
		} catch (NoResultException e) {
			logger.error("getInfoDomandaDU: ", e);
			return null;
		}
	}

}
