package it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
public class DomandaPsrDao extends JdbcDaoSupport {
	
    @Autowired
    private DataSource dataSource;
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    
	private final static String SQL_CUAA_PRESENTATA_DOMANDA_PSR = "SELECT csw.cuaa AS cuaa " +
			"FROM fascicolo.cons_sogg_viw csw " +
			"WHERE EXISTS (SELECT 1 FROM tfascicolo fas WHERE fas.id_soggetto = csw.pk_cuaa) AND " +
			"EXISTS ( SELECT 1 FROM tdom_domanda dom, tdom_modulo tdm " +
			"WHERE tdm.anno = :campagna AND tdm.sco_settore IN ('P22014') AND dom.id_modulo = tdm.id_modulo AND SYSDATE BETWEEN dom.dt_insert AND dom.dt_delete " +
			"AND SYSDATE BETWEEN dom.dt_inizio AND dom.dt_fine AND dom.id_soggetto = csw.pk_cuaa)";

	public List<String> getDomandePresentatoPsrInCampagna(Integer campagna) {
		List<Integer> criteria = new ArrayList<>();
		criteria.add(campagna);

		return getJdbcTemplate().queryForList(SQL_CUAA_PRESENTATA_DOMANDA_PSR, criteria.toArray(), String.class);
	}
}
