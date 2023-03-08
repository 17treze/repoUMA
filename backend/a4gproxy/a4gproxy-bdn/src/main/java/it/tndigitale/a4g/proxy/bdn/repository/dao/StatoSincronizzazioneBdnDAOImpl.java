package it.tndigitale.a4g.proxy.bdn.repository.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;
import it.tndigitale.a4g.proxy.bdn.repository.model.StatoSincronizzazioneRowMapper;

@Repository
public class StatoSincronizzazioneBdnDAOImpl extends JdbcDaoSupport implements StatoSincronizzazioneBdnDAO {

	private Logger logger = LoggerFactory.getLogger(StatoSincronizzazioneBdnDAOImpl.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public List<StatoSincronizzazioneDO> getListaCuaaDaSincronizzare(Integer annoCampagna) {
		try {
			// Seleziona i record che sono in stato diverso da Success o Fail e che l ultima
			// esecuzione non sia sta fatta nel giorno precedente
			String query = "SELECT ANNO, CUAA, STATO_ESECUZIONE, DATA_ESECUZIONE FROM A4GT_BDN_SYNC_DATI WHERE anno = ? AND (STATO_ESECUZIONE NOT IN ('SUCCESS', 'FAIL') OR TRUNC(SYSDATE) > TRUNC(DATA_ESECUZIONE)) ORDER BY DATA_ESECUZIONE ASC";
			logger.debug("chiamata getListaCuaaDaSincronizzare");
			List<Object> params = new ArrayList<Object>();
			params.add(annoCampagna);

			List<StatoSincronizzazioneDO> resultList = getJdbcTemplate().query(query, params.toArray(), new StatoSincronizzazioneRowMapper());
			logger.debug("getListaCuaaDaSincronizzare size: " + resultList.size());
			return resultList;

		} catch (EmptyResultDataAccessException e) {
			logger.error("Record non trovato sul DB: " + e.getMessage());
		}
		return null;
	}

	@Override
	public void aggiornaStatoSincronizzazione(Integer annoCampagna, String cuaa, String statoEsecuzione) {
		String sql = "UPDATE A4GT_BDN_SYNC_DATI SET DATA_ESECUZIONE = SYSDATE, STATO_ESECUZIONE = ? WHERE CUAA = ? AND ANNO = ?";
		logger.info("aggiornamentoStatoSincronizzazione " + cuaa + "  " + statoEsecuzione);
		getJdbcTemplate().update(sql, new Object[] { statoEsecuzione, cuaa, annoCampagna });

	}

	@Override
	public List<String> getListaCuaaTabella(Integer annoCampagna) {
		try {
			String query = "SELECT DISTINCT CUAA FROM A4GT_BDN_SYNC_DATI WHERE ANNO = ?";
			logger.debug("chiamata getListaCuaaTabella");
			List<Object> params = new ArrayList<Object>();
			params.add(annoCampagna);

			List<String> resultList = getJdbcTemplate().queryForList(query, params.toArray(), String.class);
			logger.debug("getListaCuaaTabella size: " + resultList.size());
			return resultList;

		} catch (EmptyResultDataAccessException e) {
			logger.error("Record non trovato sul DB: " + e.getMessage());
		}
		return null;
	}

	@Override
	public void insertCuaa(Integer annoCampagna, String cuaa) {
		String sql = "INSERT INTO A4GT_BDN_SYNC_DATI (ID, ANNO, CUAA, STATO_ESECUZIONE, DATA_ESECUZIONE) VALUES (NXTNBR.NEXTVAL, ?, ?, 'FAIL', SYSDATE-1)";

		getJdbcTemplate().update(sql, new Object[] { annoCampagna, cuaa });

	}

	@Override
	public List<StatoSincronizzazioneDO> findAllPerCampagna(Integer annoCampagna) {
		try {
			// Seleziona i record che sono in stato diverso da Success e che l ultima
			// esecuzione non sia sta fatta nel giorno precedente
			String query = "SELECT ANNO, CUAA, STATO_ESECUZIONE, DATA_ESECUZIONE FROM A4GT_BDN_SYNC_DATI WHERE ANNO = ? AND (STATO_ESECUZIONE NOT IN ('SUCCESS') OR TRUNC(SYSDATE) > TRUNC(DATA_ESECUZIONE)) ";
			logger.debug("chiamata getListaCuaaDaSincronizzare");
			List<Object> params = new ArrayList<Object>();
			params.add(annoCampagna);

			List<StatoSincronizzazioneDO> resultList = getJdbcTemplate().query(query, params.toArray(), new StatoSincronizzazioneRowMapper());
			logger.debug("findAllPerCampagna size: " + resultList.size());
			return resultList;

		} catch (EmptyResultDataAccessException e) {
			logger.error("Record non trovato sul DB: " + e.getMessage());
		}
		return null;
	}

}
