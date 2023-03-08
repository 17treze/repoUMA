package it.tndigitale.a4g.proxy.bdn.repository.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import it.tndigitale.a4g.proxy.bdn.dto.MovimentazionePascoloDO;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.MovimentazionePascoloOviniDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.bdn.repository.interfaces.MovimentazionePascoloDAO;
import it.tndigitale.a4g.proxy.bdn.repository.model.MovimentiPascoloOviniRowMapper;

@Repository
public class MovimentazionePascoloDAOImpl extends JdbcDaoSupport implements MovimentazionePascoloDAO {

	private Logger log = LoggerFactory.getLogger(MovimentazionePascoloDAOImpl.class);

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	@Override
	public MovimentazionePascoloDO getMovimentazionePascoloIngresso(String cuaa, int annoCampagna, MovimentazionePascoloDO bdnData) {
		String sql = "SELECT ID_MOVI, ID_ALLE, CODI_ASLL, CODI_FISC_ALLE, CODI_SPEC, CODI_FISC_DETE, CODI_ASL_AZIE, ID_PASC, "
				+ " CODI_PASC, DESC_COMU_PASC, ASL_PASC, DATA_INGR_PASC, DATA_USCI_PASC, NUME_OVIN, DATA_COMU_AUTO, "
				+ " CODI_DOCU, DATA_DOCU, TIPO_MOVI, NUME_CAMP, FLAG_CONT, DATA_INSE, DATA_REVI, DATA_FINE " + " FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE "
				+ " ID_ALLE = ? AND CODI_ASLL = ? AND CODI_FISC_ALLE= ? AND CODI_SPEC = ? AND " + " CODI_FISC_DETE = ? AND CODI_ASL_AZIE = ? AND ID_PASC = ? AND CODI_PASC = ? AND "
				+ " ASL_PASC = ? AND DATA_INGR_PASC = ? AND NUME_CAMP = ? AND CODI_DOCU = ? AND " + " TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		try {
			Map<String, Object> row = getJdbcTemplate().queryForMap(sql,
					new Object[] { bdnData.getIdAlle(), bdnData.getCodiAsll(), bdnData.getCodiFiscAlle(), bdnData.getCodiSpec(), bdnData.getCodiFiscDete(), bdnData.getCodiAslAzie(),
							bdnData.getIdPasc(), bdnData.getCodiPasc(), bdnData.getAslPasc(), bdnData.getDataIngrPasc(), annoCampagna, bdnData.getCodiDocu() });

			MovimentazionePascoloDO foundOnDB = new MovimentazionePascoloDO();

			foundOnDB.setIdMovi(row.get("ID_MOVI") == null ? 0 : ((BigDecimal) row.get("ID_MOVI")).longValue());
			foundOnDB.setIdAlle(row.get("ID_ALLE") == null ? 0 : ((BigDecimal) row.get("ID_ALLE")).longValue());
			foundOnDB.setCodiAsll((String) row.get("CODI_ASLL"));
			foundOnDB.setCodiFiscAlle((String) row.get("CODI_FISC_ALLE"));
			foundOnDB.setCodiSpec((String) row.get("CODI_SPEC"));
			foundOnDB.setCodiFiscDete((String) row.get("CODI_FISC_DETE"));
			foundOnDB.setCodiAslAzie((String) row.get("CODI_ASL_AZIE"));
			foundOnDB.setIdPasc(row.get("ID_PASC") == null ? 0 : ((BigDecimal) row.get("ID_PASC")).longValue());
			foundOnDB.setCodiPasc((String) row.get("CODI_PASC"));
			foundOnDB.setDescComuPasc((String) row.get("DESC_COMU_PASC"));
			foundOnDB.setAslPasc((String) row.get("ASL_PASC"));
			foundOnDB.setDataIngrPasc((Date) row.get("DATA_INGR_PASC"));
			foundOnDB.setDataUsciPasc((Date) row.get("DATA_USCI_PASC"));
			foundOnDB.setNumeOvin(row.get("NUME_OVIN") == null ? 0 : ((BigDecimal) row.get("NUME_OVIN")).longValue());
			foundOnDB.setDataComuAuto((Date) row.get("DATA_COMU_AUTO"));
			foundOnDB.setCodiDocu((String) row.get("CODI_DOCU"));
			foundOnDB.setDataDocu((Date) row.get("DATA_DOCU"));
			foundOnDB.setTipoMovi((String) row.get("TIPO_MOVI"));
			foundOnDB.setNumeCamp(row.get("NUME_CAMP") == null ? 0 : ((BigDecimal) row.get("NUME_CAMP")).longValue());
			foundOnDB.setFlagCont(row.get("FLAG_CONT") == null ? 0 : ((BigDecimal) row.get("FLAG_CONT")).intValue());
			foundOnDB.setDataInse((Date) row.get("DATA_INSE"));
			foundOnDB.setDataRevi((Date) row.get("DATA_REVI"));
			foundOnDB.setDataFine((Date) row.get("DATA_FINE"));

			return foundOnDB;
		} catch (EmptyResultDataAccessException e) {
			log.error("Record non trovato sul DB per cuaa : " + cuaa);
		}
		return null;
	}

	@Override
	public void insert(MovimentazionePascoloDO movimentazione, String cuaa, int annoCampagna) {

		if (movimentazione == null) {

			return;
		}

		// FLAG_CONT e DATA_REVI non sono mai valorizzati, li escludo dall'insert
		String sql = "INSERT INTO A4GT_BDN_MOVIMENTI_PASCOLO " + "(ID_MOVI, ID_ALLE, CODI_ASLL, CODI_FISC_ALLE, CODI_SPEC, CODI_FISC_DETE, CODI_ASL_AZIE, ID_PASC, CODI_PASC, "
				+ " DESC_COMU_PASC, ASL_PASC, NUME_OVIN, DATA_COMU_AUTO, CODI_DOCU, DATA_DOCU, TIPO_MOVI, NUME_CAMP, DATA_INSE, DATA_FINE, DATA_INGR_PASC, DATA_USCI_PASC)"
				+ "VALUES (NXTNBR.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, to_date('31129999', 'ddmmyyyy'), ?, ?)";

		getJdbcTemplate().update(sql, new Object[] {
				// ID_CAWS
				movimentazione.getIdAlle(), // ID_ALLE
				movimentazione.getCodiAsll(), // CODI_ASLL
				movimentazione.getCodiFiscAlle(), // CODI_FISC_ALLE
				movimentazione.getCodiSpec(), // CODI_SPEC
				movimentazione.getCodiFiscDete(), // CODI_FISC_DETE
				movimentazione.getCodiAslAzie(), // CODI_ASL_AZIE
				movimentazione.getIdPasc(), // ID_PASC
				movimentazione.getCodiPasc(), // CODI_PASC
				movimentazione.getDescComuPasc(), // DESC_COMU_PASC
				movimentazione.getAslPasc(), // ASL_PASC
				// movimentazione.getDataIngrPasc(), // DATA_INGR_PASC
				// movimentazione.getDataUsciPasc(), // DATA_USCI_PASC
				movimentazione.getNumeOvin(), // NUME_OVIN
				movimentazione.getDataComuAuto(), // DATA_COMU_AUTO
				movimentazione.getCodiDocu(), // CODI_DOCU
				movimentazione.getDataDocu(), // DATA_DOCU
				movimentazione.getTipoMovi(), // TIPO_MOVI
				annoCampagna, movimentazione.getDataIngrPasc(), movimentazione.getDataUsciPasc() });

	}

	@Override
	public void closeRecordById(MovimentazionePascoloDO dataObject) {
		String sql = "UPDATE A4GT_BDN_MOVIMENTI_PASCOLO set DATA_FINE = SYSDATE WHERE ID_MOVI = ?";

		getJdbcTemplate().update(sql, new Object[] { dataObject.getIdMovi() });
	}

	@Override
	public void closeRecordByCuaa(String cuaa, String tipoMovimento, int annoCampagna) {

		String sql = "UPDATE A4GT_BDN_MOVIMENTI_PASCOLO set DATA_FINE = SYSDATE WHERE CODI_FISC_DETE = ? AND TIPO_MOVI = ? AND  NUME_CAMP = ? AND TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		getJdbcTemplate().update(sql, new Object[] { cuaa, tipoMovimento, annoCampagna });
	}

	@Override
	public MovimentazionePascoloDO getMovimentazionePascoloUscita(String cuaa, int annoCampagna, MovimentazionePascoloDO bdnData) {
		String sql = "SELECT ID_MOVI, ID_ALLE, CODI_ASLL, CODI_FISC_ALLE, CODI_SPEC, CODI_FISC_DETE, CODI_ASL_AZIE, ID_PASC, "
				+ " CODI_PASC, DESC_COMU_PASC, ASL_PASC, DATA_INGR_PASC, DATA_USCI_PASC, NUME_OVIN, DATA_COMU_AUTO, "
				+ " CODI_DOCU, DATA_DOCU, TIPO_MOVI, NUME_CAMP, FLAG_CONT, DATA_INSE, DATA_REVI, DATA_FINE " + " FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE "
				+ " ID_ALLE = ? AND CODI_ASLL = ? AND CODI_FISC_ALLE= ? AND CODI_SPEC = ? AND " + " CODI_FISC_DETE = ? AND CODI_ASL_AZIE = ? AND ID_PASC = ? AND CODI_PASC = ? AND "
				+ " ASL_PASC = ? AND NUME_CAMP = ? AND TIPO_MOVI = ? AND DATA_USCI_PASC = ? AND NUME_OVIN = ? AND " + " TRUNC(DATA_FINE) = TRUNC(to_date('31129999', 'ddmmyyyy'))";

		try {
			Map<String, Object> row = getJdbcTemplate().queryForMap(sql,
					new Object[] { bdnData.getIdAlle(), bdnData.getCodiAsll(), bdnData.getCodiFiscAlle(), bdnData.getCodiSpec(), bdnData.getCodiFiscDete(), bdnData.getCodiAslAzie(),
							bdnData.getIdPasc(), bdnData.getCodiPasc(), bdnData.getAslPasc(), annoCampagna, bdnData.getTipoMovi(), bdnData.getDataUsciPasc(), bdnData.getNumeOvin() });

			MovimentazionePascoloDO foundOnDB = new MovimentazionePascoloDO();

			foundOnDB.setIdMovi(row.get("ID_MOVI") == null ? 0 : ((BigDecimal) row.get("ID_MOVI")).longValue());
			foundOnDB.setIdAlle(row.get("ID_ALLE") == null ? 0 : ((BigDecimal) row.get("ID_ALLE")).longValue());
			foundOnDB.setCodiAsll((String) row.get("CODI_ASLL"));
			foundOnDB.setCodiFiscAlle((String) row.get("CODI_FISC_ALLE"));
			foundOnDB.setCodiSpec((String) row.get("CODI_SPEC"));
			foundOnDB.setCodiFiscDete((String) row.get("CODI_FISC_DETE"));
			foundOnDB.setCodiAslAzie((String) row.get("CODI_ASL_AZIE"));
			foundOnDB.setIdPasc(row.get("ID_PASC") == null ? 0 : ((BigDecimal) row.get("ID_PASC")).longValue());
			foundOnDB.setCodiPasc((String) row.get("CODI_PASC"));
			foundOnDB.setDescComuPasc((String) row.get("DESC_COMU_PASC"));
			foundOnDB.setAslPasc((String) row.get("ASL_PASC"));
			foundOnDB.setDataIngrPasc((Date) row.get("DATA_INGR_PASC"));
			foundOnDB.setDataUsciPasc((Date) row.get("DATA_USCI_PASC"));
			foundOnDB.setNumeOvin(row.get("NUME_OVIN") == null ? 0 : ((BigDecimal) row.get("NUME_OVIN")).longValue());
			foundOnDB.setDataComuAuto((Date) row.get("DATA_COMU_AUTO"));
			foundOnDB.setCodiDocu((String) row.get("CODI_DOCU"));
			foundOnDB.setDataDocu((Date) row.get("DATA_DOCU"));
			foundOnDB.setTipoMovi((String) row.get("TIPO_MOVI"));
			foundOnDB.setNumeCamp(row.get("NUME_CAMP") == null ? 0 : ((BigDecimal) row.get("NUME_CAMP")).longValue());
			foundOnDB.setFlagCont(row.get("FLAG_CONT") == null ? 0 : ((BigDecimal) row.get("FLAG_CONT")).intValue());
			foundOnDB.setDataInse((Date) row.get("DATA_INSE"));
			foundOnDB.setDataRevi((Date) row.get("DATA_REVI"));
			foundOnDB.setDataFine((Date) row.get("DATA_FINE"));

			return foundOnDB;
		} catch (EmptyResultDataAccessException e) {
			log.error("Record non trovato sul DB per cuaa : " + cuaa);
		}
		return null;
	}

	@Override
	public List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOviniPerIstruttoria(BigDecimal annoCampagna, String codicePascolo, String codiceFiscale) {
//		String sql = "SELECT CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP, NUME_OVIN, MAX(DATA_INGR_PASC) AS DATA_INGRESSO, MAX(DATA_USCI_PASC) AS DATA_USCITA, "
//				+ "(MAX(DATA_USCI_PASC) - MAX(DATA_INGR_PASC)) AS GG_PASC, MIN(NUME_OVIN) AS NUM_CAPI "
//				+ "FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE NUME_CAMP = ? AND CODI_PASC = ? AND CODI_FISC_DETE = ? AND DATA_FINE > SYSDATE GROUP BY CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP, NUME_OVIN";
		String sql = "SELECT ID_ALLE, CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP,NUME_OVIN, DATA_INGR_PASC AS DATA_INGRESSO, DATA_USCI_PASC AS DATA_USCITA, "
				+ "0 AS GG_PASC, NUME_OVIN AS NUM_CAPI "
				+ "FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE NUME_CAMP = ? AND CODI_PASC = ? AND CODI_FISC_DETE = ? AND SYSDATE between DATA_INSE AND DATA_FINE";
		try {
			List<MovimentazionePascoloOviniDto> query = getJdbcTemplate().query(sql, new Object[] { annoCampagna, codicePascolo, codiceFiscale }, new MovimentiPascoloOviniRowMapper());
			return query;
		} catch (EmptyResultDataAccessException e) {
			log.error("getMovimentazionePascoloOviniPerIstruttoria - Record non trovato per: anno campagna " + annoCampagna + " codicePascolo: " + codicePascolo + " codiceFiscale: " + codiceFiscale);
		}
		return null;
	}

	@Override
	public List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOviniPerIstruttoriaPerCodPascolo(BigDecimal annoCampagna, String codicePascolo) {
//		String sql = "SELECT CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP,NUME_OVIN, MAX(DATA_INGR_PASC) AS DATA_INGRESSO, MAX(DATA_USCI_PASC) AS DATA_USCITA, "
//				+ "(MAX(DATA_USCI_PASC) - MAX(DATA_INGR_PASC)) AS GG_PASC, MIN(NUME_OVIN) AS NUM_CAPI "
//				+ "FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE NUME_CAMP = ? AND CODI_PASC = ? AND DATA_FINE > SYSDATE GROUP BY CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP, NUME_OVIN";
		String sql = "SELECT ID_ALLE, CODI_FISC_DETE, ID_PASC, CODI_PASC, DESC_COMU_PASC, NUME_CAMP,NUME_OVIN, DATA_INGR_PASC AS DATA_INGRESSO, DATA_USCI_PASC AS DATA_USCITA, "
				+ "0 AS GG_PASC, NUME_OVIN AS NUM_CAPI "
				+ "FROM A4GT_BDN_MOVIMENTI_PASCOLO WHERE NUME_CAMP = ? AND CODI_PASC = ? AND SYSDATE between DATA_INSE AND DATA_FINE";
		try {
			List<MovimentazionePascoloOviniDto> query = getJdbcTemplate().query(sql, new Object[] { annoCampagna, codicePascolo }, new MovimentiPascoloOviniRowMapper());
			return query;
		} catch (EmptyResultDataAccessException e) {
			log.error("getMovimentazionePascoloOviniPerIstruttoria - Record non trovato per: anno campagna " + annoCampagna + " codicePascolo: " + codicePascolo);
		}
		return null;
	}

}
