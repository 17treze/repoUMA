package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo.FascicoloVO;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo.IbanVO;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo.SoggettoVO;
import it.tndigitale.a4g.framework.config.DataSourceConfig;

@Repository
public class SincronizzazioneAgsDao extends JdbcDaoSupport {

	static final Logger log = LoggerFactory.getLogger(SincronizzazioneAgsDao.class);

	@Autowired
	private DataSourceConfig dataSourceConfig;

	@Autowired
	private FascicoloDao fascicoloDao;

	@Autowired
	private PersonaFisicaDao personaFisicaDao;

	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;

	@Autowired
	private CaricaDao caricaDao;

	@Autowired
	private PersonaFisicaConCaricaDao personaFisicaConCaricaDao;

	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.jdbcTemplate = new JdbcTemplate(dataSourceConfig.secondaryDataSource());
	}

	public void sincronizzaFascicoloAgs(String cuaa, Integer idValidazione) throws SincronizzazioneAgsException, NoSuchElementException {
		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
			connection = this.jdbcTemplate.getDataSource().getConnection();

			/* FascicoloVO (contiene MandatoVO) */
			Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
			if (!fascicoloModelOpt.isPresent()) {
				var err = String.format("Fascicolo CUAA %s non trovato", cuaa);
				log.error(err);
				throw new SincronizzazioneAgsException(err);
			}
			var fascicoloModel = fascicoloModelOpt.get();
			var fascicoloVO = FascicoloVO.mapper(fascicoloModel, cuaa);
			List<IbanVO> ibanVO = IbanVO.mapper(fascicoloModel, cuaa);

			/* SoggettoVO */
			SoggettoVO soggettoVO;
			var soggettoVOSenzaFascicoloList = new ArrayList<SoggettoVO>();
			if (cuaa.length() == 16) {
				PersonaFisicaModel personaFisica = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione).orElseThrow();
				List<CaricaModel> cariche = caricaDao.findByPersonaFisicaModel(personaFisica);
				soggettoVO = SoggettoVO.mapper(fascicoloModel, personaFisica, cariche, cuaa, connection);
				if (cariche != null && !cariche.isEmpty()) {
					for (CaricaModel carica : cariche) {
						if (!carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equalsIgnoreCase(cuaa)) {
							soggettoVOSenzaFascicoloList.add(SoggettoVO.mapperSenzaFascicolo(cuaa, carica.getPersonaFisicaConCaricaModel(), cariche, connection));
						}
					}
				}
			} else {
				Optional<PersonaGiuridicaModel> personaGiuridica = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione);
				if (!personaGiuridica.isPresent()) {
					var err = String.format("Persona giuridica %s non trovata", cuaa);
					log.error(err);
					throw new SincronizzazioneAgsException(err);
				}

				var personaGiuridicaModel = personaGiuridica.get();
				soggettoVO = SoggettoVO.mapper(fascicoloModel, personaGiuridicaModel, cuaa);
				List<CaricaModel> cariche = caricaDao.findByPersonaGiuridicaModel(personaGiuridicaModel);
				if (cariche != null && !cariche.isEmpty()) {
					for (CaricaModel carica : cariche) {
						if (!carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equalsIgnoreCase(cuaa)) {
							soggettoVOSenzaFascicoloList.add(SoggettoVO.mapperSenzaFascicolo(cuaa, carica.getPersonaFisicaConCaricaModel(), cariche, connection));
						}
					}
				}
			}

			log.info("Chiamata alla Stored Function...");

			if (ibanVO != null && !ibanVO.isEmpty()) {
				var ibanArray = connection.unwrap(oracle.jdbc.OracleConnection.class).createOracleArray("AGS_IBAN_ARR", ibanVO.toArray());
				agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_MAIN_TEST( ?, ?, ?, ?, ?, ?, ?) }");
				agsStoredFunc.registerOutParameter(1, Types.NUMERIC);
				agsStoredFunc.setString(2, cuaa);
				agsStoredFunc.setObject(3, soggettoVO);
				agsStoredFunc.setObject(4, fascicoloVO);
				agsStoredFunc.setString(5, fascicoloModel.getUtenteValidazione());
				agsStoredFunc.setLong(6, fascicoloModel.getIdSchedaValidazione());
				agsStoredFunc.setDate(7, Date.valueOf(fascicoloModel.getDataValidazione()));
				agsStoredFunc.setArray(8, ibanArray);

			} else {
				agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_MAIN_TEST( ?, ?, ?, ?, ?, ?) }");
				agsStoredFunc.registerOutParameter(1, Types.NUMERIC);
				agsStoredFunc.setString(2, cuaa);
				agsStoredFunc.setObject(3, soggettoVO);
				agsStoredFunc.setObject(4, fascicoloVO);
				agsStoredFunc.setString(5, fascicoloModel.getUtenteValidazione());
				agsStoredFunc.setLong(6, fascicoloModel.getIdSchedaValidazione());
				agsStoredFunc.setDate(7, Date.valueOf(fascicoloModel.getDataValidazione()));
			}
			agsStoredFunc.execute();
			var codRit = agsStoredFunc.getBigDecimal(1);
			var info = String.format("Esito restituito: %d", codRit.intValue());
			log.info(info);

			if (!soggettoVOSenzaFascicoloList.isEmpty()) {
				for (SoggettoVO soggettoSenzaFascicolo : soggettoVOSenzaFascicoloList) {
					CallableStatement agsStoredFuncSenzaFascicolo = null;
					try {
						agsStoredFuncSenzaFascicolo = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_SOGGETTO( ? ) }");
						agsStoredFuncSenzaFascicolo.registerOutParameter(1, Types.NUMERIC);
						agsStoredFuncSenzaFascicolo.setObject(2, soggettoSenzaFascicolo);

						agsStoredFuncSenzaFascicolo.execute();
						var codRitCarica = agsStoredFuncSenzaFascicolo.getBigDecimal(1);
						var infoCarica = String.format("Esito restituito per persistenza carica: %d", codRitCarica.intValue());
						log.info(infoCarica);
					} finally {
						try {
							if (agsStoredFuncSenzaFascicolo != null) {
								agsStoredFuncSenzaFascicolo.close();
							}
						} catch (SQLException e) {
							log.error("Errore in chiusura connessioni", e);
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new SincronizzazioneAgsException(ex);
		} finally {
			try {
				if (agsStoredFunc != null) {
					agsStoredFunc.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

}
