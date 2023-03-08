/**
 * 
 */
package it.tndigitale.a4g.ags.repository.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.ags.dto.EsitoAntimafia;

/**
 * @author S.DeLuca
 *
 */
@Repository
public class AntimafiaDaoImpl extends JdbcDaoSupport implements AntimafiaDao {
	
	private String sqlCancellaEsitoPerCUAA = "delete from TPAGA_ESITI_ANTIMAFIA where CUAA = ?";
	private String sqlCancellaEsitoPerChiave = "delete from TPAGA_ESITI_ANTIMAFIA where CUAA = ? AND ID_DOMANDA = ? AND TIPO_DOMANDA = ?";
	private String sqlInserisciEsitoPerChiave = "insert into TPAGA_ESITI_ANTIMAFIA (CUAA,ID_DOMANDA,TIPO_DOMANDA,DATA_INIZIO_SILENZIO_ASSENZO,DATA_FINE_SILENZIO_ASSENZO,DATA_INIZIO_ESITO_NEGATIVO,DATA_FINE_ESITO_NEGATIVO) values (?,?,?,?,?,?,?)";
	private String sqlRecupereEsitoPerCuaa = "select * from TPAGA_ESITI_ANTIMAFIA where CUAA = ?";
	private String sqlCancellaCertificazioni = "truncate table TPAGA_CERTIFICAZIONI_ANTIMAFIA";
	private String sqlInserisciCertificazioniCuaa  = "INSERT INTO TPAGA_CERTIFICAZIONI_ANTIMAFIA (CUAA,DATA_INSERIMENTO) VALUES (?,?)";
	
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@PersistenceContext
	EntityManager entityManager;

	@Override
	@Transactional
	public void salva(List<EsitoAntimafia> esitiAntimafia) throws Exception {
		esitiAntimafia.forEach(esitoAntimafia -> {
			if (esitoAntimafia.getIdDomanda() == null && esitoAntimafia.getTipoDomanda() == null) {
				getJdbcTemplate().update(sqlCancellaEsitoPerCUAA, esitoAntimafia.getCuaa());
			} else {
				getJdbcTemplate().update(sqlCancellaEsitoPerChiave, esitoAntimafia.getCuaa(), esitoAntimafia.getIdDomanda(), esitoAntimafia.getTipoDomanda());
			}
		});

		esitiAntimafia.forEach(esitoAntimafia -> {
			if (esitoAntimafia.getIdDomanda() != null && esitoAntimafia.getTipoDomanda() != null) {
				getJdbcTemplate().update(sqlInserisciEsitoPerChiave, esitoAntimafia.getCuaa(), esitoAntimafia.getIdDomanda(), esitoAntimafia.getTipoDomanda(), esitoAntimafia.getDtInizioSilenzioAssenso(),
						esitoAntimafia.getDtFineSilenzioAssenso(), esitoAntimafia.getDtInizioEsitoNegativo(), esitoAntimafia.getDtFineEsitoNegativo());
			}
		});
	}

	@Override
	public void cancella(String cuaa) throws Exception {
		if (!StringUtils.isEmpty(cuaa)) 
			getJdbcTemplate().update(sqlCancellaEsitoPerCUAA, cuaa);
	}
 
	@Override
	public List<EsitoAntimafia> recuperaEsiti(EsitoAntimafia esitoAntimafia) throws Exception {
		List<Object> params = new ArrayList<>();
        params.add(esitoAntimafia.getCuaa());
        return getJdbcTemplate().<EsitoAntimafia>query(sqlRecupereEsitoPerCuaa, params.toArray(), new BeanPropertyRowMapper<EsitoAntimafia>(EsitoAntimafia.class));
	}

	@Override
	public void sincronizzaCert(List<String> cuaaList) throws Exception {
		getJdbcTemplate().update(sqlCancellaCertificazioni);
		cuaaList.forEach(cuaa -> {
			getJdbcTemplate().update(sqlInserisciCertificazioniCuaa, cuaa, new Date());
		});
	}

}
