package it.tndigitale.a4g.ags.repository.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.dto.Fascicolo;
import it.tndigitale.a4g.ags.dto.FascicoloFilter;

/**
 * @author S.DeLuca
 */
@Repository
public class FascicoloDao extends NamedParameterJdbcDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(FascicoloDao.class);
	
	//	private final String sqlRicercaFascicolo = "select distinct f.id_fascicolo as id_fascicolo, csv.ragi_soci as denominazione, csv.cuaa as cuaa, deco.ds_decodifica as stato,"
	//			+ " ente.cod_ente as caacodice, ente.des_ente as caa, csv.pk_cuaa as idsoggetto, cuaa_ente.tipo_associazione as tipo_detenzione" 
	//			+ " from tfascicolo f"
	//			+ " inner join cons_sogg_viw csv on csv.pk_cuaa = f.id_soggetto "
	//			+ " inner join tdecodifica_language deco on f.cod_stato = deco.codice and f.sco_stato = deco.sotto_codice "
	//			+ " left outer join siti.cons_cuaa_ente cuaa_ente on cuaa_ente.pk_cuaa = csv.pk_cuaa and cuaa_ente.tipo_associazione = 'MAN' and sysdate between cuaa_ente.data_inizio and cuaa_ente.data_fine" 
	//			+ " left outer join siti.sitiente ente on ente.cod_ente = cuaa_ente.cod_ente and sysdate between ente.data_inizio and ente.data_fine"
	//			+ " where sysdate between f.dt_inizio and f.dt_fine and sysdate between csv.data_inizio_val and csv.data_fine_val"
	//			+ " and cuaa_ente.tipo_associazione is not null and ente.des_ente is not null";
	
	private final String sqlRicercaFascicolo = "select distinct f.id as id_fascicolo, \r\n"
			+ "    f.denominazione as denominazione, \r\n" + "    f.cuaa as cuaa, \r\n" + "    f.stato as stato, \r\n"
			+ "    s.id_caa as caacodice, \r\n" + "    s.denominazione as caa, \r\n"
			+ "    f.id_persona as idsoggetto, \r\n"
			+ "    case when m.identificativo_sportello is null then 'DET' else 'MAN' end as tipo_detenzione \r\n"
			+ "from a4gt_fascicolo f \r\n"
			+ "    left outer join a4gt_detenzione d on d.id_fascicolo = f.id and d.fascicolo_id_validazione = f.id_validazione \r\n"
			+ "        and d.versione = f.versione\r\n" + "    left outer join a4gt_mandato m on m.id = d.id \r\n"
			+ "    left outer join a4gt_sportello s on s.identificativo = m.identificativo_sportello \r\n"
			+ "where 1 = 1 ";
	
	private final String sqlRicercaEsistenzaFascicolo = "select count(distinct f.id_fascicolo) from tfascicolo f inner join cons_sogg_viw csv on csv.pk_cuaa = f.id_soggetto "
			+ " where sysdate between f.dt_inizio and f.dt_fine and sysdate between csv.data_inizio_val and csv.data_fine_val ";
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	@PersistenceContext
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Fascicolo> getFascicoli(FascicoloFilter fascicolo) {
		
		logger.debug("chiamata getFascicoli {}, {}, {}", fascicolo.getCuaa(), fascicolo.getDenominazione(),
				fascicolo.getCaacodici());
		
		String filter = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (fascicolo.getCuaa() != null && !fascicolo.getCuaa().isEmpty()) {
			filter = filter.concat(" and UPPER(f.cuaa) like :cuaa");
			params.put("cuaa", "%".concat(fascicolo.getCuaa().toUpperCase()).concat("%"));
		}
		
		if (fascicolo.getDenominazione() != null && !fascicolo.getDenominazione().isEmpty()) {
			filter = filter.concat(" and UPPER(f.denominazione) like :denominazione");
			params.put("denominazione", "%".concat(fascicolo.getDenominazione().toUpperCase()).concat("%"));
		}
		
//		if (fascicolo.getCaacodici() != null && !fascicolo.getCaacodici().isEmpty()) {
//			StringBuilder sb = new StringBuilder(" and exists (");
//			sb.append("select 1 from siti.cons_cuaa_ente enti_abilitati");
//			sb.append(" where enti_abilitati.pk_cuaa = csv.pk_cuaa ");
//			sb.append("and sysdate between enti_abilitati.data_inizio and enti_abilitati.data_fine ");
//			sb.append("and enti_abilitati.tipo_associazione in ('MAN', 'DEL') ");
//			sb.append("and enti_abilitati.cod_ente in (:caacodici)");
//			sb.append(")");
//			params.put("caacodici", fascicolo.getCaacodici());
//			filter = filter.concat(sb.toString());
//		}
		
		if (filter.length() == 0)
			return Collections.emptyList();
		
		try {
			String sql = this.sqlRicercaFascicolo.concat(filter);
			logger.debug("getFascicoli: ricerco per select {}", sql);
			List<Fascicolo> fascicoloDTOList = getNamedParameterJdbcTemplate().<Fascicolo> query(sql, params,
					new BeanPropertyRowMapper<Fascicolo>(Fascicolo.class));
			return fascicoloDTOList;
		}
		catch (NoResultException e) {
			logger.warn("getFascicoli: nessun risultato trovato per il filtro " + filter);
			return Collections.emptyList();
		}
	}
	
	public Fascicolo getFascicolo(Long idFascicolo) throws Exception {
		
		logger.debug("chiamata getFascicolo {}", idFascicolo);
		
		String filter = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (idFascicolo != null) {
			filter = filter.concat(" and f.id = :idFascicolo");
			params.put("idFascicolo", idFascicolo);
		}
		
		if (filter.length() == 0)
			return null;
		
		String sql = this.sqlRicercaFascicolo.concat(filter);
		
		List<Fascicolo> fascicoloDTOList = getNamedParameterJdbcTemplate().<Fascicolo> query(sql, params,
				new BeanPropertyRowMapper<Fascicolo>(Fascicolo.class));
		
		if (fascicoloDTOList.size() > 0) {
			return fascicoloDTOList.get(0);
		}
		logger.warn("getFascicolo: nessun risultato trovato per id = {}", idFascicolo);
		throw new NoResultException();
	}
	
	public boolean checkFascicoloValido(String cuaa) {
		logger.debug("chiamata checkFascicoloValido {}", cuaa);
		String filter = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (cuaa != null && !cuaa.isEmpty()) {
			filter = filter.concat(" and UPPER(csv.cuaa) = :cuaa");
			params.put("cuaa", cuaa);
		}
		
		if (filter.length() == 0)
			return false;
		
		String sql = this.sqlRicercaEsistenzaFascicolo.concat(filter)
				.concat(" AND SCO_STATO IN ('AGGIOR', 'ANOMAL','VALIDO')");
		
		int count = getNamedParameterJdbcTemplate().queryForObject(sql, params, Integer.class);
		
		if (count > 0)
			return true;
		else
			return false;
	}
}