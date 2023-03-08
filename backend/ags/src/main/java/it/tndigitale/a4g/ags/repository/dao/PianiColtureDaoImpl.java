package it.tndigitale.a4g.ags.repository.dao;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import it.tndigitale.a4g.ags.dto.CatastoPianoColture;
import it.tndigitale.a4g.ags.dto.DatiAggiuntiviParticella;
import it.tndigitale.a4g.ags.dto.InfoParticella;
import it.tndigitale.a4g.ags.model.InfoParticellaRowMapper;

@Repository
public class PianiColtureDaoImpl extends JdbcDaoSupport implements PianiColtureDao {

	private final Logger logger = LoggerFactory.getLogger(PianiColtureDao.class);
	
	private static final String ANNO_PLACEHOLDER = "{ANNO}";

	private static final String SQL_GET_PARTICELLE_FROM_PIANI_COLTURE = "SELECT CAT_COMUNE_CO, PARTICELLA, SUB FROM TCAT_PIANI_COLTURE_" + ANNO_PLACEHOLDER + " "
			+ "WHERE (NRO_ANOMALIE IS NULL OR NRO_ANOMALIE_GIS IS NULL) "
			+ "GROUP BY CAT_COMUNE_CO, PARTICELLA, SUB ORDER BY 1, 2, 3";
	private static final String SQL_GET_PIANI_COLTURE_BY_INFO_PARTICELLA = "SELECT * FROM TCAT_PIANI_COLTURE_" + ANNO_PLACEHOLDER + " "
			+ "WHERE CAT_COMUNE_CO = ? AND PARTICELLA = ? "
			+ "AND (NRO_ANOMALIE IS NULL OR NRO_ANOMALIE_GIS IS NULL)";
	private static final String SQL_GET_PARTICELLE_DISTINCT_COUNT = "SELECT COUNT(*) FROM TCAT_PIANI_COLTURE_" + ANNO_PLACEHOLDER + " "
			+ "WHERE (NRO_ANOMALIE IS NULL OR NRO_ANOMALIE_GIS IS NULL) "
			+ "GROUP BY CAT_COMUNE_CO, PARTICELLA, SUB ORDER BY 1, 2, 3";
	
	private static final String SQL_GET_ORIENTAMENTO_MEDIO = "SELECT FASCICOLO.SITI_INTERFACCIA_PCK.GET_ORIENTAMENTO_MEDIO(?,'9999',?,?,SYSDATE) FROM DUAL";
	private static final String SQL_GET_PENDENZA_MEDIA = "SELECT FASCICOLO.SITI_INTERFACCIA_PCK.GET_PENDENZA_MEDIA(?,'9999',?,?,SYSDATE) FROM DUAL";
	private static final String SQL_GET_ALTITUDINE_MEDIA = "SELECT FASCICOLO.SITI_INTERFACCIA_PCK.GET_ALTITUDINE_MEDIA(?,'9999',?,?,SYSDATE) FROM DUAL";
	private static final String SQL_GET_IRRIGABILITA = "SELECT COUNT(*) FROM FASCICOLO.TPARTICELLA_DATI_AGG P WHERE P.FOGLIO = '9999' AND P.COD_NAZIONALE = ? AND P.PARTICELLA = ? AND SUB = ? AND SCO_IRRIGUO = '000001' AND SYSDATE BETWEEN DT_INSERT AND DT_DELETE AND SYSDATE BETWEEN DT_INIZIO AND DT_FINE";
	
	@Autowired
	private DataSource dataSource;

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	@Transactional
	public boolean eseguiCaricaPiani(Date dateFrom) {

		boolean result = false;
		try {
			// Elimino i dati precedentemente caricati
			entityManager.createNativeQuery("TRUNCATE TABLE TCAT_PIANI_COLTURE").executeUpdate();
			// Chiamo la procedura e ricarico tutti i dati
			getJdbcTemplate().update("CALL CARICA_PIANI(?)", dateFrom);
			result = true;
		} catch (Exception e) {
			logger.error("Errore in eseguiCaricaPiani", e);
			throw e;
		}
		return result;
	}

	@Override
	public List<CatastoPianoColture> getPianiColtureByInfoParticella(InfoParticella info, Integer anno) {

		logger.trace("PianiColtureDaoImpl getPianiColtureByInfoParticella comune catastale: {} particella: {} sub: {}",
				info.getCodiceComuneCatastale(), info.getParticella(),
				Strings.isNullOrEmpty(info.getSub()) ? "" : info.getSub());

		String sql = SQL_GET_PIANI_COLTURE_BY_INFO_PARTICELLA.replace(ANNO_PLACEHOLDER, anno.toString());
		ArrayList<String> params = new ArrayList<>();
		if (info.getCodiceComuneCatastale() == null)
			throw new InvalidParameterException("CodiceComuneCatastale cannot be null");
		params.add(Integer.toString(info.getCodiceComuneCatastale()));
		if (Strings.isNullOrEmpty(info.getParticella()))
			throw new InvalidParameterException("Particella cannot be null");
		params.add(info.getParticella());
		try {
			if (!Strings.isNullOrEmpty(info.getSub())) {
				sql += " AND SUB = ?";
				params.add(info.getSub());
			}
			return getJdbcTemplate().<CatastoPianoColture>query(sql, params.toArray(),
					new BeanPropertyRowMapper<CatastoPianoColture>(CatastoPianoColture.class));
		} catch (NoResultException e) {
			logger.error("getPianiColtureByInfoParticella: ", e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<InfoParticella> getParticelleFromPianiColture(Integer anno) {

		logger.trace("PianiColtureDaoImpl getParticelleFromPianiColture");

		try {
			String sql = SQL_GET_PARTICELLE_FROM_PIANI_COLTURE.replace(ANNO_PLACEHOLDER, anno.toString());
			return getJdbcTemplate().query(sql, new InfoParticellaRowMapper());
		} catch (NoResultException e) {
			logger.error("getParticelleFromPianiColture: ", e);
			return Collections.emptyList();
		}
	}

	@Override
	public Integer getParticelleDistinctCount(Integer anno) {

		logger.trace("PianiColtureDaoImpl getParticelleDistinctCount");
		String sql = SQL_GET_PARTICELLE_DISTINCT_COUNT.replace(ANNO_PLACEHOLDER, anno.toString());
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	@Override
	public DatiAggiuntiviParticella getDatiAggiuntiviParticella(InfoParticella info, Integer anno) {
		
		List<CatastoPianoColture> pianiColture = getPianiColtureByInfoParticella(info, anno);
		if (pianiColture.isEmpty()) {
			logger.error("Impossibile recuperare il codice nazionale per il comune catastale.");
			return null;
		}
		String sub = Strings.isNullOrEmpty(info.getSub()) ? " " : info.getSub();
		String codiceNazionale = pianiColture.get(0).getCodNazionale();
		
		DatiAggiuntiviParticella result = new DatiAggiuntiviParticella();
		
		ArrayList<Object> params = new ArrayList<>();
		params.add(codiceNazionale);
		params.add(info.getParticella());
		params.add(sub);
		
		try {
			BigDecimal orientamentoMedio = getJdbcTemplate().queryForObject(SQL_GET_ORIENTAMENTO_MEDIO, params.toArray(), BigDecimal.class);
			result.setOrientamentoMedio(orientamentoMedio != null ? orientamentoMedio.longValue() : 0);
			BigDecimal pendenzaMedia = getJdbcTemplate().queryForObject(SQL_GET_PENDENZA_MEDIA, params.toArray(), BigDecimal.class);
			result.setPendenzaMedia(pendenzaMedia != null ? pendenzaMedia.longValue() : 0);
			BigDecimal altitudineMedia = getJdbcTemplate().queryForObject(SQL_GET_ALTITUDINE_MEDIA, params.toArray(), BigDecimal.class);
			result.setAltitudineMedia(altitudineMedia != null ? altitudineMedia.longValue() : 0);
			result.setIrrigabilita(getJdbcTemplate().queryForObject(SQL_GET_IRRIGABILITA, params.toArray(), long.class) == 1);
		} catch (Exception e) {
			logger.error("getDatiAggiuntiviParticella: ", e);
			return null;
		}
		return result;
	}

}
