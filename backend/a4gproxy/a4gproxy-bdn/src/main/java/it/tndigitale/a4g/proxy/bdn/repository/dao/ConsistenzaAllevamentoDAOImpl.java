package it.tndigitale.a4g.proxy.bdn.repository.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAllevamentoDAO;
import it.tndigitale.a4g.proxy.bdn.repository.model.ConsistenzaAllevamentoRowMapper;

@Repository
public class ConsistenzaAllevamentoDAOImpl extends JdbcDaoSupport implements ConsistenzaAllevamentoDAO {

	private Logger log = LoggerFactory.getLogger(ConsistenzaAllevamentoDAOImpl.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public void insert(ConsistenzaAllevamentoDO consistenza, String cuaa, int annoCampagna) {

		if (consistenza == null) {
			return;
		}

		String sql = "INSERT INTO A4GT_BDN_CONS_ALLEVAMENTO " + "(ID_CAWS, CODI_FISC_SOGG, NUME_CAMP, ID_ALLE_BDN, CODI_SPEC, CODI_ASLL, CODI_FISC_PROP, CODI_FISC_DETE, "
				+ " CONS_CAPI_0_6, CONS_CAPI_6_24, CONS_CAPI_OVER_24, CONS_TOTA, CONS_VACC_OVER_20, DATA_INIZ_DETE, DATA_FINE_DETE, " + " DECO_STAT, DATA_INIZ, DATA_FINE, DATA_AGGI, USER_NAME )"
				+ "VALUES (NXTNBR.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, to_date('31129999', 'ddmmyyyy'), SYSDATE, ?)";

		getJdbcTemplate().update(sql, new Object[] {
				// ID_CAWS
				cuaa, // CODI_FISC_SOGG
				annoCampagna, // NUME_CAMP
				consistenza.getIdAlleBdn(), // ID_ALLEV_BDN
				consistenza.getCodiSpec(), // CODI_SPEC
				consistenza.getCodiAsll(), // CODI_ASLL
				consistenza.getCodiFiscProp(), // CODI_FISC_PROP
				consistenza.getCodiFiscDete(), // CODI_FISC_DETE
				consistenza.getConsCapi06(), // CONS_CAPI_0_6
				consistenza.getConsCapi624(), // CONS_CAPI_6_24
				consistenza.getConsCapiOver24(), // CONS_CAPI_OVER_24
				consistenza.getConsTota(), // CONS_TOTA
				consistenza.getConsVaccOver20(), // CONS_VACC_OVER_20
				consistenza.getDataInizDete(), // DATA_INIZ_DETE
				consistenza.getDataFineDete(), // DATA_FINE_DETE
				85, // DECO_STAT
				"PROXY-BDN" // USERNAME
		});
	}

	@Override
	public ConsistenzaAllevamentoDO getConsistenzaAllevamento(String cuaa, ConsistenzaAllevamentoDO bdnData) {

		String sql = "SELECT ID_CAWS, CODI_FISC_SOGG, NUME_CAMP, ID_ALLE_BDN, CODI_SPEC, CODI_ASLL, CODI_FISC_PROP, CODI_FISC_DETE, "
				+ " CONS_CAPI_0_6, CONS_CAPI_6_24, CONS_CAPI_OVER_24, CONS_TOTA, CONS_VACC_OVER_20, DATA_INIZ_DETE, DATA_FINE_DETE, " + " DECO_STAT, DATA_INIZ, DATA_FINE, DATA_AGGI, USER_NAME "
				+ " FROM A4GT_BDN_CONS_ALLEVAMENTO WHERE " + " NUME_CAMP = ? AND CODI_FISC_SOGG = ? AND ID_ALLE_BDN = ? AND CODI_ASLL = ? AND "
				+ " CODI_FISC_PROP = ? AND CODI_FISC_DETE = ? AND CODI_SPEC = ? AND TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		try {
			Map<String, Object> row = getJdbcTemplate().queryForMap(sql, new Object[] { bdnData.getNumeCamp(), bdnData.getCodiFiscSogg(), bdnData.getIdAlleBdn(), bdnData.getCodiAsll(),
					bdnData.getCodiFiscProp(), bdnData.getCodiFiscDete(), bdnData.getCodiSpec() });

			ConsistenzaAllevamentoDO foundOnDB = new ConsistenzaAllevamentoDO();

			foundOnDB.setIdCaws(row.get("ID_CAWS") == null ? 0 : ((BigDecimal) row.get("ID_CAWS")).longValue());
			foundOnDB.setCodiFiscSogg((String) (row.get("CODI_FISC_SOGG")));
			foundOnDB.setNumeCamp(row.get("NUME_CAMP") == null ? 0 : ((BigDecimal) row.get("NUME_CAMP")).intValue());
			foundOnDB.setIdAlleBdn(row.get("ID_ALLE_BDN") == null ? 0 : ((BigDecimal) row.get("ID_ALLE_BDN")).longValue());
			foundOnDB.setCodiSpec((String) row.get("CODI_SPEC"));
			foundOnDB.setCodiAsll((String) row.get("CODI_ASLL"));
			foundOnDB.setCodiFiscProp((String) row.get("CODI_FISC_PROP"));
			foundOnDB.setCodiFiscDete((String) row.get("CODI_FISC_DETE"));
			foundOnDB.setConsCapi06(((BigDecimal) row.get("CONS_CAPI_0_6")).doubleValue());
			foundOnDB.setConsCapi624(((BigDecimal) row.get("CONS_CAPI_6_24")).doubleValue());
			foundOnDB.setConsCapiOver24(((BigDecimal) row.get("CONS_CAPI_OVER_24")).doubleValue());
			foundOnDB.setConsTota(((BigDecimal) row.get("CONS_TOTA")).doubleValue());
			foundOnDB.setConsVaccOver20(((BigDecimal) row.get("CONS_VACC_OVER_20")).doubleValue());
			foundOnDB.setDataInizDete((Timestamp) row.get("DATA_INIZ_DETE"));
			foundOnDB.setDataFineDete((Timestamp) row.get("DATA_FINE_DETE"));
			foundOnDB.setDecoStat((row.get("DECO_STAT") == null ? 0 : ((BigDecimal) row.get("DECO_STAT")).longValue()));
			foundOnDB.setDataIniz((Timestamp) row.get("DATA_INIZ"));
			foundOnDB.setDataFine((Timestamp) row.get("DATA_FINE"));
			foundOnDB.setDataAggi((Timestamp) row.get("DATA_AGGI"));
			foundOnDB.setUserName((String) row.get("USER_NAME"));

			return foundOnDB;
		} catch (EmptyResultDataAccessException e) {
			log.debug("Record non trovato sul DB per cuaa : " + cuaa);
		}
		return null;
	}

	@Override
	public void closeRecordById(ConsistenzaAllevamentoDO dataObject) {

		String sql = "UPDATE A4GT_BDN_CONS_ALLEVAMENTO set DATA_FINE = SYSDATE, DATA_AGGI = SYSDATE WHERE ID_CAWS = ?";

		getJdbcTemplate().update(sql, new Object[] {
				// new Timestamp(System.currentTimeMillis()),
				// new Timestamp(System.currentTimeMillis()),
				dataObject.getIdCaws() });
	}

	@Override
	public void closeRecordByCuaa(String cuaa, int annoCampagna) {

		String sql = "UPDATE A4GT_BDN_CONS_ALLEVAMENTO set DATA_FINE = SYSDATE, DATA_AGGI = SYSDATE WHERE CODI_FISC_SOGG = ? AND NUME_CAMP = ? AND TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		getJdbcTemplate().update(sql, new Object[] { cuaa, annoCampagna });
	}

	@Override
	public List<ConsistenzaAllevamentoDO> getConsAllevamento(String codiceFiscale, BigDecimal annoCampagna) {
		String sql = "SELECT ca.ID_CAWS, ca.CODI_FISC_SOGG, ca.NUME_CAMP, ca.ID_ALLE_BDN, ca.CODI_SPEC, ca.CODI_ASLL, ca.CODI_FISC_PROP, ca.CODI_FISC_DETE, "
				+ "ca.CONS_CAPI_0_6, ca.CONS_CAPI_6_24, ca.CONS_CAPI_OVER_24, CONS_TOTA, ca.CONS_VACC_OVER_20, ca.DATA_INIZ_DETE, ca.DATA_FINE_DETE, ca.DECO_STAT, ca.DATA_INIZ, ca.DATA_FINE, ca.DATA_AGGI, ca.USER_NAME, "
				+ " nvl(a.CODICE_COMUNE, SUBSTR(codi_asll, 1, 3)) as CODICE_COMUNE "
				+ "FROM A4GT_BDN_CONS_ALLEVAMENTO ca LEFT OUTER JOIN A4GT_BDN_AZIENDA a on a.codice_azienda = ca.CODI_ASLL "
				+ "WHERE ca.CODI_FISC_DETE = ? AND ca.NUME_CAMP = ? and sysdate between ca.DATA_INIZ and ca.DATA_FINE";
		try {
			List<ConsistenzaAllevamentoDO> query = getJdbcTemplate().query(sql, new Object[] { codiceFiscale, annoCampagna }, new ConsistenzaAllevamentoRowMapper());
			return query;
		} catch (EmptyResultDataAccessException e) {
			log.debug("getConsAllevamento - Record non trovato per: anno campagna " + annoCampagna + " codiceFiscale: " + codiceFiscale);
		}
		return null;
	}
	
	@Override
	public boolean esisteAziendaInCache(String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT 1 as ESISTE FROM A4GT_BDN_AZIENDA a WHERE a.codice_azienda = ?");
		try {
			Boolean result = getJdbcTemplate().queryForObject(sb.toString(), new Object[] { codiceAzienda }, Boolean.class);
			return Boolean.TRUE.equals(result);
		} catch (EmptyResultDataAccessException e) {
			log.debug("Azienda non trovata {}", codiceAzienda);
		}
		return false;
		
	}
	
	@Override
	public boolean esisteAziendaCollegataInCache(long idCaws) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT 1 as ESISTE FROM A4GT_BDN_AZIENDA a ");
		sb.append("join A4GT_BDN_CONS_ALLEVAMENTO ca on a.codice_azienda = ca.CODI_ASLL ");
		sb.append("WHERE ca.ID_CAWS = ?");
		try {
			Boolean result = getJdbcTemplate().queryForObject(sb.toString(), new Object[] { idCaws }, Boolean.class);
			return Boolean.TRUE.equals(result);
		} catch (EmptyResultDataAccessException e) {
			log.debug("Azienda non trovata per consistenza allevamento {}", idCaws);
		}
		return false;
		
	}
	
	@Override
	public void insertAziendaNuova(String codiceAzienda, String codiceComune) {

		String sql = "INSERT INTO A4GT_BDN_AZIENDA " + "(ID, CODICE_AZIENDA, CODICE_COMUNE )"
				+ "VALUES (NXTNBR.NEXTVAL, ?, ?)";

		getJdbcTemplate().update(sql, new Object[] {
				codiceAzienda, 
				codiceComune, 
		});
	}

	@Override
	public void deleteAzienda(String codiceAzienda) {
		String sql = "DELETE FROM A4GT_BDN_AZIENDA " + "WHERE CODICE_AZIENDA = ?";

		getJdbcTemplate().update(sql, new Object[] {
				codiceAzienda
		});
	}

}
