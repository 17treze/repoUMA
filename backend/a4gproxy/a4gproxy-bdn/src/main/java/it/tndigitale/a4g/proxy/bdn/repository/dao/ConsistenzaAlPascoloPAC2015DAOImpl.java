package it.tndigitale.a4g.proxy.bdn.repository.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DO;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.ConsistenzaPascolo2015Dto;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAlPascoloPAC2015DAO;
import it.tndigitale.a4g.proxy.bdn.repository.model.ConsistenzaPascolo2015DORowMapper;
import it.tndigitale.a4g.proxy.bdn.repository.model.ConsistenzaPascolo2015RowMapper;

@Repository
public class ConsistenzaAlPascoloPAC2015DAOImpl extends JdbcDaoSupport implements ConsistenzaAlPascoloPAC2015DAO {

	private Logger log = LoggerFactory.getLogger(ConsistenzaAlPascoloPAC2015DAOImpl.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public List<ConsistenzaAlPascoloPAC2015DO> getConsistenzaAlPascoloPAC2015(int annoCampagna, ConsistenzaAlPascoloPAC2015DO bdnData) {
		String sql = "SELECT ID_CPWS, CODI_FISC_SOGG, NUME_CAMP, CODI_PASC, FASC_ETAA, NUME_CAPI, NUME_CAPI_MEDI, GIOR_PASC, "
				+ " CODI_GRUP_SPEC, CODI_SPEC, DESC_SPEC, COOR_LATI, COOR_LONG, NUME_FOGL, NUME_PART, "
				+ " CODI_SEZI, CODI_SUBA, CODI_SIGL_PROV, CODI_PROV, CODI_COMU, DESC_LOCA, CODI_FISC_RESP, DECO_STAT, " + " DATA_INIZ, DATA_FINE, DATA_AGGI, USER_NAME "
				+ " FROM A4GT_BDN_CONSIST_PASCOLO WHERE " + " NUME_CAMP = ? AND CODI_PASC = ? AND FASC_ETAA = ? AND CODI_SPEC = ? AND"
				+ " TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";
		try {
			log.debug("Anno campagna - {}", annoCampagna);
			log.debug("Bdn data getCodiFiscSogg - {}", bdnData.getCodiFiscSogg());
			log.debug("Bdn data getCodiPasc - {}", bdnData.getCodiPasc());
			log.debug("Bdn data getFascEtaa - {}", bdnData.getFascEtaa());
			log.debug("Bdn data getCodiSpec - {}", bdnData.getCodiSpec());
			List<ConsistenzaAlPascoloPAC2015DO> query = null;
			if (bdnData.getCodiFiscSogg() == null) {
				sql = sql.concat(" AND CODI_FISC_SOGG IS NULL ");
				query = getJdbcTemplate().query(sql, new Object[] { annoCampagna, bdnData.getCodiPasc(), bdnData.getFascEtaa(), bdnData.getCodiSpec() }, new ConsistenzaPascolo2015DORowMapper());
			} else {
				sql = sql.concat(" AND CODI_FISC_SOGG = ? ");
				query = getJdbcTemplate().query(sql, new Object[] { annoCampagna, bdnData.getCodiPasc(), bdnData.getFascEtaa(), bdnData.getCodiSpec(), bdnData.getCodiFiscSogg() }, new ConsistenzaPascolo2015DORowMapper());
			}
			return query;
		} catch (EmptyResultDataAccessException e) {
			log.debug("Record non trovato sul DB per: anno campagna " + annoCampagna + " CUAA " + bdnData.getCodiFiscSogg() + " codicePascolo " + bdnData.getCodiPasc() + " fascia età "
					+ bdnData.getFascEtaa() + " codice specie " + bdnData.getCodiSpec());
		}
		return null;
	}

	@Override
	public void closeRecordById(ConsistenzaAlPascoloPAC2015DO dataObject) {
		String sql = "UPDATE A4GT_BDN_CONSIST_PASCOLO set DATA_FINE = SYSDATE, DATA_AGGI = SYSDATE WHERE ID_CPWS = ?";

		getJdbcTemplate().update(sql, new Object[] { dataObject.getIdCpws() });

	}

	// TODO completare l'associazione dei codiciIstatProvincia
	private String getCodiceIstatProvincia(String siglaProvincia) {
		// String sql = "select ISTATP from SITI.CATA_PROV where SIGLA_PROV = ?";
		//
		// Map<String, Object> row = getJdbcTemplate().queryForMap(sql, new Object[] {
		// siglaProvincia });
		// String result = (String) row.get("ISTATP");
		// return result;
		return "022";
	}

	@Override
	public void insert(ConsistenzaAlPascoloPAC2015DO consistenza, int annoCampagna) {
		if (consistenza == null) {
			log.info("insert per consistenza null" + " e anno: " + annoCampagna);
			return;
		}

		log.debug("insert per responsabile " + consistenza.getCodiFiscResp() + " e anno: " + annoCampagna);
		String sql = "INSERT INTO A4GT_BDN_CONSIST_PASCOLO "
				+ "(ID_CPWS, CODI_FISC_SOGG, NUME_CAMP, CODI_PASC, FASC_ETAA, NUME_CAPI, NUME_CAPI_MEDI, GIOR_PASC, "
				+ " CODI_GRUP_SPEC, CODI_SPEC, DESC_SPEC, COOR_LATI, COOR_LONG, NUME_FOGL, NUME_PART, "
				+ " CODI_SEZI, CODI_SUBA, CODI_SIGL_PROV, CODI_PROV, CODI_COMU, DESC_LOCA, CODI_FISC_RESP, DECO_STAT, "
				+ " DATA_INIZ, DATA_FINE, DATA_AGGI, USER_NAME ) "
				+ "VALUES (NXTNBR.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, to_date('31129999', 'ddmmyyyy'), SYSDATE, ?)";

		String codiceIstatProvincia = getCodiceIstatProvincia(consistenza.getCodiSiglProv());
		log.debug("insert codiceIstatProvincia " + codiceIstatProvincia);
		log.debug("insert sql " + sql);
		int rows = getJdbcTemplate().update(sql, new Object[] {
				// ID_CAWS
				consistenza.getCodiFiscSogg(), // CODI_FISC_SOGG
				annoCampagna, // CODI_ASLL
				consistenza.getCodiPasc(), // CODI_PASC
				consistenza.getFascEtaa(), // FASC_ETAA
				consistenza.getNumeCapi(), // NUME_CAPI
				consistenza.getNumeCapiMedi(), // NUME_CAPI_MEDI
				consistenza.getGiorPasc(), // GIOR_PASC
				consistenza.getCodiGrupSpec(), // CODI_GRUP_SPEC
				consistenza.getCodiSpec(), // CODI_SPEC
				consistenza.getDescSpec(), // DESC_SPEC
				consistenza.getCoorLati(), // COOR_LATI
				consistenza.getCoorLong(), // COOR_LONG
				consistenza.getNumeFogl(), // NUME_FOGL
				consistenza.getNumePart(), // NUME_PART
				consistenza.getCodiSezi(), // CODI_SEZI
				consistenza.getCodiSuba(), // CODI_SUBA
				consistenza.getCodiSiglProv(), // CODI_SIGL_PROV
				codiceIstatProvincia, // CODI_PROV
				consistenza.getCodiComu(), // CODI_COMU
				consistenza.getDescLoca(), // DESC_LOCA
				consistenza.getCodiFiscResp(), // CODI_FISC_RESP
				consistenza.getDecoStat(), // DECO_STAT
				consistenza.getUserName() // USER_NAME
		});
		log.info("Aggiornate #righe: " + rows);
	}

	@Override
	public void closeRecordByCodicePascolo(String codicePascolo, int annoCampagna) {
		String sql = "UPDATE A4GT_BDN_CONSIST_PASCOLO SET DATA_FINE = SYSDATE, DATA_AGGI = SYSDATE WHERE CODI_PASC = ? AND NUME_CAMP = ? AND TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		getJdbcTemplate().update(sql, new Object[] { codicePascolo, annoCampagna });

	}

	@Override
	public List<ConsistenzaPascolo2015Dto> getConsistenzaPascoloPerIstruttoria(BigDecimal annoCampagna,
			String codicePascolo) {

		String sql = "SELECT CODI_FISC_SOGG, NUME_CAMP, CODI_PASC, FASC_ETAA, NUME_CAPI, NUME_CAPI_MEDI, GIOR_PASC, DESC_SPEC, CODI_SIGL_PROV, CODI_FISC_RESP"
				+ " FROM A4GT_BDN_CONSIST_PASCOLO WHERE NUME_CAMP = ? AND CODI_PASC = ? AND DATA_FINE > SYSDATE";
		try {
			List<ConsistenzaPascolo2015Dto> query = getJdbcTemplate().query(sql,
					new Object[] { annoCampagna, codicePascolo }, new ConsistenzaPascolo2015RowMapper());
			return query;
		} catch (EmptyResultDataAccessException e) {
			log.debug("getConsistenzaPascoloPerIstruttoria - Record non trovato per: anno campagna " + annoCampagna
					+ " codicePascolo: " + codicePascolo);
		}
		return null;
	}

}
