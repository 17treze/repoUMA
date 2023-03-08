package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;
import it.tndigitale.a4g.fascicolo.territorio.dto.*;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.component.dto.SegnalazioneDto;
import it.tndigitale.a4g.framework.component.dto.TipoSegnalazioneEnum;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.territorio.dto.FascicoloDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FascicoloAgsDao extends NamedParameterJdbcDaoSupport {

	static final Logger log = LoggerFactory.getLogger(FascicoloAgsDao.class);
	
	@Autowired private DataSource dataSource;

	private ObjectMapper mapper = new ObjectMapper();
	static final String ESITO = "esito";
	static final String ID_CONTROLLO = "idControllo";
	static final String SEGNALAZIONI = "segnalazioni";

	@Autowired
	private DataSourceConfig dataSourceConfig;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
	   setDataSource(dataSourceConfig.secondaryDataSource());
	   this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}	
		
	public EsitoControlloDto getControlloCompletezzaFascicolo(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {

		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_CTRL_CONSISTENZA2(?) }");
			agsStoredFunc.registerOutParameter(1, Types.VARCHAR);
			agsStoredFunc.setString(2, cuaa);
			agsStoredFunc.execute();
			JsonNode obj = mapper.readTree(agsStoredFunc.getString(1));
			List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
			return new EsitoControlloDto(obj.get(ESITO).asInt(), obj.get(ID_CONTROLLO).asLong(), segnalazioni);
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

	public EsitoControlloDto getControlloPianoColturaleGrafico(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_CTRL_PCG2(?) }");
			agsStoredFunc.registerOutParameter(1, Types.VARCHAR);
			agsStoredFunc.setString(2, cuaa);
			agsStoredFunc.execute();
			JsonNode obj = mapper.readTree(agsStoredFunc.getString(1));
			List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
			return new EsitoControlloDto(obj.get(ESITO).asInt(), obj.get(ID_CONTROLLO).asLong(), segnalazioni);
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

	public EsitoControlloDto getControlloPianoColturaleAlfanumerico(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		CallableStatement agsStoredFunc = null;
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsStoredFunc = connection.prepareCall("{? = call AGS_SINCRO_FASCICOLO.SINC_CTRL_PIANOCOLT2(?) }");
			agsStoredFunc.registerOutParameter(1, Types.VARCHAR);
			agsStoredFunc.setString(2, cuaa);
			agsStoredFunc.execute();
			JsonNode obj = mapper.readTree(agsStoredFunc.getString(1));
			List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
			return new EsitoControlloDto(obj.get(ESITO).asInt(), obj.get(ID_CONTROLLO).asLong(), segnalazioni);
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
	
	public List<SegnalazioneDto> getSegnalazioni(final Long idControllo) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<SegnalazioneDto> segnalazioni = new ArrayList<SegnalazioneDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select \r\n"
					+ "        case when b.cod_effetto_anomalia = 'BLOCCA' then 'ERRORE' else 'AVVERTENZA' end as tipo, \r\n"
					+ "        de_anomalia as descrizione\r\n"
					+ "      from tcntr_rec_anomalia a left outer join tcnt_anomalia b on a.id_controllo = b.id_controllo \r\n"
					+ "      where a.id_controllo = ?");
			agsQuery.setLong(1, idControllo);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Segnalazione: {}, {}", rs.getString(1), rs.getString(2));
				if (rs.getString(1).equalsIgnoreCase(TipoSegnalazioneEnum.AVVERTENZA.name())) {
					segnalazioni.add(new SegnalazioneDto(rs.getString(2), TipoSegnalazioneEnum.AVVERTENZA));
				}
				else {
					segnalazioni.add(new SegnalazioneDto(rs.getString(2), TipoSegnalazioneEnum.ERRORE));
				}
		    }
		    return segnalazioni;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}
	
	// controllo in AGS per il momento
	public Optional<FascicoloDto> findByCuaaAndIdValidazione(String cuaa, Integer idValidazione) throws SQLException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select id_fascicolo from tfascicolo f\r\n"
					+ "where sysdate between f.dt_inizio and f.dt_fine\r\n"
					+ "and f.sco_stato = 'VALIDO'\r\n"
					+ "and f.id_soggetto = (\r\n"
					+ "    select cod_soggetto from anag_soggetti \r\n"
					+ "    where cod_fiscale = ? and sysdate between data_inizio_val and data_fine_val\r\n"
					+ ")");
			agsQuery.setString(1, cuaa);
		    ResultSet rs = agsQuery.executeQuery();
		    if (rs.next()) {
				FascicoloDto fm = new FascicoloDto();
		    	fm.setCuaa(cuaa);
		    	return Optional.of(fm);
		    }
		    return Optional.empty();
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	public List<ReportValidazioneFascicoloTitoloConduzioneDto> findTitoliConduzione(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloTitoloConduzioneDto> titoliList = new ArrayList<ReportValidazioneFascicoloTitoloConduzioneDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select distinct \r\n"
					+ "    u.tipo_titolo,\r\n"
					+ "	t.pkid_tipo_doc || ' - ' || t.descrizione, \r\n"
					+ "	e.protoappag,\r\n"
					+ "	to_char(e.ave_dt_documento, 'dd/mm/yyyy'),\r\n"
					+ "	to_char(d.data_scadenza, 'dd/mm/yyyy')\r\n"
					+ "from siti.cons_ufre_tab u \r\n"
					+ "    join fascicolo.anag_soggetti s on u.pk_cuaa = s.cod_soggetto and sysdate between s.data_inizio_val and s.data_fine_val\r\n"
					+ "    left outer join siti.sitifile_atti_doc a on a.fkid_atto = u.atto_ini and sysdate between a.data_inizio_val and a.data_fine_val\r\n"
					+ "        join siti.sitifile_documenti d on d.pk_cuaa = u.pk_cuaa and a.fkid_doc = d.pkid_doc and sysdate between d.data_inizio_val and d.data_fine_val\r\n"
					+ "        join siti.sitifile_tipidoc t on d.fkid_tipo_doc = t.pkid_tipo_doc\r\n"
					+ "        join siti.sitifile_documenti_campi_ext e on e.fkid_doc = d.pkid_doc\r\n"
					+ "where s.cod_fiscale = ?\r\n"
					+ "and sysdate between u.data_inizio and u.data_fine\r\n"
					+ "order by 3, 4");
			agsQuery.setString(1, cuaa);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Titolo: {}, {}", rs.getString(2), rs.getString(3));
				TipoConduzioneEnum tipologia = TipoConduzioneEnum.create(rs.getInt(1));
				titoliList.add(
					new ReportValidazioneFascicoloTitoloConduzioneDto(
						tipologia, 
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5)));
		    }
		    return titoliList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	public List<ReportValidazioneFascicoloIsolaAziendaleDto> findIsoleAziendali(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloIsolaAziendaleDto> isoleList = new ArrayList<ReportValidazioneFascicoloIsolaAziendaleDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select\r\n"
					+ "    i.codi_isol,\r\n"
					+ "    round(i.supe_isol) as supe_isol\r\n"
					+ "from domagraf.AABGFGRA_TAB f \r\n"
					+ "    join domagraf.AABGCONS_TAB c on c.id_fgra = f.id_fgra\r\n"
					+ "    join domagraf.AABGPCG_VERS_TAB pcg on pcg.id_cons_pcg = c.id_cons\r\n"
					+ "    join domagraf.AABGISOL_VERS_TAB i on i.id_pcg_vers = pcg.id_pcg_vers\r\n"
					+ "where f.cuaa = ?\r\n"
					+ "and sysdate between i.data_iniz_isol and i.data_fine_isol\r\n"
					+ "and to_date('15/05/' || extract(year from sysdate), 'dd/mm/yyyy') = c.data_rife_cons\r\n"
					+ "and pcg.flag_npr = 1\r\n"
					+ "order by 1");
			agsQuery.setString(1, cuaa);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Isola: {}, {}", rs.getString(1), rs.getInt(2));
			    // riempire parcelle, particelle e appezzamenti
				List<ReportValidazioneFascicoloParticellaCatastaleDto> particelleCondotteList = this.findParticelleCondotte(rs.getString(1));
				List<ReportValidazioneFascicoloParcelleRiferimentoDto> parcelleRiferimentoList = this.findParcelleRiferimento(rs.getString(1));
				List<ReportValidazioneFascicoloPianoColturaleDto> appezzamentiList = this.findPianoColturale(rs.getString(1));
				ReportValidazioneFascicoloIsolaAziendaleDto isola = new ReportValidazioneFascicoloIsolaAziendaleDto(
						rs.getString(1),
						rs.getInt(2));
				isola.setParticelleCondotteList(particelleCondotteList);
				isola.setParcelleRiferimentoList(parcelleRiferimentoList);
				isola.setAppezzamentiList(appezzamentiList);
				isoleList.add(isola);
		    }
		    return isoleList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	private List<ReportValidazioneFascicoloParticellaCatastaleDto> findParticelleCondotte(final String codiceIsola) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloParticellaCatastaleDto> particelleList = new ArrayList<ReportValidazioneFascicoloParticellaCatastaleDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select \r\n"
					+ "	c.nome_comune as comune_amministrativo,\r\n"
					+ "	c.cod_nazionale as codice_catastale,\r\n"
					+ "	case when nvl(c.sezione_censuaria, ' ' ) = ' ' then '' else c.nome end as sezione,\r\n"
					+ "	u.foglio,\r\n"
					+ "	u.particella,\r\n"
					+ "	u.sub as subalterno,\r\n"
					+ "	round(ip.supe_cons) as supe_cond,\r\n"
					+ "	round(u.perc_poss) as perc_cond,\r\n"
					+ "	e.protoappag as prot_docu_cond\r\n"
					+ "from siti.cons_ufre_tab u \r\n"
					+ "    join siti.siticomu c on u.cod_nazionale = c.cod_nazionale \r\n"
					+ "    join fascicolo.anag_soggetti s on u.pk_cuaa = s.cod_soggetto and sysdate between s.data_inizio_val and s.data_fine_val\r\n"
					+ "    join domagraf.AABGFGRA_TAB f on f.cuaa = s.cod_fiscale\r\n"
					+ "    join domagraf.AABGCONS_TAB c on c.id_fgra = f.id_fgra\r\n"
					+ "    join domagraf.AABGPCG_VERS_TAB pcg on pcg.id_cons_pcg = c.id_cons\r\n"
					+ "    join domagraf.AABGISOL_VERS_TAB i on i.id_pcg_vers = pcg.id_pcg_vers\r\n"
					+ "    join domagraf.AABGISOL_PART_VERS_TAB ip on ip.id_pcg_vers = pcg.id_pcg_vers and ip.id_isol = i.id_isol and ip.id_part = u.id_particella   \r\n"
					+ "    left outer join siti.sitifile_atti_doc a on a.fkid_atto = u.atto_ini and sysdate between a.data_inizio_val and a.data_fine_val\r\n"
					+ "        left outer join siti.sitifile_documenti d on d.pk_cuaa = u.pk_cuaa and a.fkid_doc = d.pkid_doc and sysdate between d.data_inizio_val and d.data_fine_val\r\n"
					+ "        left outer join siti.sitifile_documenti_campi_ext e on e.fkid_doc = d.pkid_doc\r\n"
					+ "where i.codi_isol = ?\r\n"
					+ "and sysdate between u.data_inizio and u.data_fine\r\n"
					+ "and sysdate between i.data_iniz_isol and i.data_fine_isol\r\n"
					+ "and to_date('15/05/' || extract(year from sysdate), 'dd/mm/yyyy') = c.data_rife_cons\r\n"
					+ "and pcg.flag_npr = 1\r\n"
					+ "and c.flag_stat = 1\r\n"
					+ "order by 1, 3, 4, 5, 6, 7");
			agsQuery.setString(1, codiceIsola);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Particella: {}, {}", rs.getString(1), rs.getString(7));
				particelleList.add(
					new ReportValidazioneFascicoloParticellaCatastaleDto(
						rs.getString(1), 
						rs.getString(2),
						rs.getString(3),
						rs.getInt(4),
						rs.getString(5),
						rs.getString(6),
						rs.getInt(7),
						rs.getInt(8),
						rs.getString(9)));
		    }
		    return particelleList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	private List<ReportValidazioneFascicoloParcelleRiferimentoDto> findParcelleRiferimento(final String codiceIsola) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloParcelleRiferimentoDto> parcelleList = new ArrayList<ReportValidazioneFascicoloParcelleRiferimentoDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select\r\n"
					+ "    pris.codi_prif,\r\n"
					+ "    pr.CODI_REFR ,\r\n"
					+ "    cat.descrizione,\r\n"
					+ "    round( pr.SUPE_PRIF ) as SUPE_PRIF\r\n"
					+ "from domagraf.AABGPCG_VERS_TAB pcg\r\n"
					+ "join domagraf.AABGISOL_VERS_TAB i using (id_pcg_vers)\r\n"
					+ "left outer join domagraf.AABGPRIF_ISOL_VERS_TAB pris using (id_pcg_vers, id_isol)\r\n"
					+ "join domagraf.AABGCONS_TAB c on c.id_cons = pcg.id_cons_pcg\r\n"
					+ "join domagraf.AABGFGRA_TAB f on f.id_fgra = c.id_fgra\r\n"
					+ "join domagraf.AABLPRIF_TAB pr ON pr.codi_prif = pris.codi_prif\r\n"
					+ "join siti.siticods_vari cat ON cat.cod_vari = pr.codi_refr\r\n"
					+ "where pcg.data_Fine_vali > sysdate\r\n"
					+ "and i.codi_isol = ?\r\n"
					+ "and sysdate between i.data_iniz_isol and i.data_fine_isol\r\n"
					+ "and to_date('15/05/' || extract(year from sysdate), 'dd/mm/yyyy') = c.data_rife_cons\r\n"
					+ "and pcg.flag_npr = 1\r\n"
					+ "and c.flag_stat = 1\r\n"
					+ "order by 1, 2, 3");
			agsQuery.setString(1, codiceIsola);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Parcella riferimento: {}, {}", rs.getString(1), rs.getString(2));
				parcelleList.add(
					new ReportValidazioneFascicoloParcelleRiferimentoDto(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getInt(4)));
		    }
		    return parcelleList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	private List<ReportValidazioneFascicoloPianoColturaleDto> findPianoColturale(final String codiceIsola) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloPianoColturaleDto> appezzamentiList = new ArrayList<ReportValidazioneFascicoloPianoColturaleDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("select\r\n"
					+ "    to_char(appe.id_appe) as codi_appe,\r\n"
					+ "    to_char(appe_dett.id_appe_dett) as codi_appe_dett,\r\n"
					+ "    round(appe_dett.supe_dett) as supe_dett,\r\n"
					+ "    appe_dett.codi_dest_util || '-' || appe_dett.codi_occu || '-' || \r\n"
					+ "        DECODE(appe_dett.codi_dest_uso, '000', '', appe_dett.codi_dest_uso) || '-' || \r\n"
					+ "        DECODE(appe_dett.codi_usoo, '000', '', appe_dett.codi_usoo) || '-' || \r\n"
					+ "        DECODE(appe_dett.codi_qual, '000', '', appe_dett.codi_qual) as codi_colt,\r\n"
					+ "    c.desc_prod,\r\n"
					+ "    case \r\n"
					+ "    when appe_dett.codi_prat_perm like 'PPR%' then (select ds_decodifica from fascicolo.tdecodifica where codice = 'PPR' and sotto_codice = substr(appe_dett.codi_prat_perm, 5)) \r\n"
					+ "    when appe_dett.codi_prat_altr like 'SSM%' then (select ds_decodifica from fascicolo.tdecodifica where codice = 'SSM' and sotto_codice = substr(appe_dett.codi_prat_altr, 5))\r\n"
					+ "    else NULL\r\n"
					+ "    end as crit_mant\r\n"
					+ "from domagraf.AABGPCG_VERS_TAB pcg\r\n"
					+ "join domagraf.AABGISOL_VERS_TAB i on i.id_pcg_vers = pcg.id_pcg_vers and sysdate between i.data_iniz_isol and i.data_fine_isol\r\n"
					+ "join domagraf.AABGAPPE_VERS_TAB appe on appe.id_isol = i.id_isol and appe.id_pcg_vers = i.id_pcg_vers and sysdate between appe.data_iniz_appe and appe.data_fine_appe\r\n"
					+ "left outer join domagraf.AABGAPPE_VERS_DETT_TAB appe_dett on appe_dett.id_appe = appe.id_appe and sysdate between appe_dett.data_iniz_dett and appe_dett.data_fine_dett\r\n"
					+ "join domagraf.AABACATA5_CATA3 c on appe_dett.codi_dest_util = c.codi_cgis and appe_dett.codi_occu = c.codi_prod \r\n"
					+ "    and appe_dett.codi_dest_uso = c.codi_dest_uso and appe_dett.codi_usoo = c.codi_uso and appe_dett.codi_qual = c.codi_qual\r\n"
					+ "    and sysdate between c.data_iniz_ammi and c.data_fine_ammi\r\n"
					+ "where pcg.data_Fine_vali > sysdate\r\n"
					+ "and i.codi_isol = ?\r\n"
					+ "and pcg.flag_npr = 1\r\n"
					+ "order by 1, 2");
			agsQuery.setString(1, codiceIsola);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("Appezzamento: {}, {}", rs.getString(1), rs.getString(2));
				appezzamentiList.add(
					new ReportValidazioneFascicoloPianoColturaleDto(
						rs.getString(1),
						rs.getString(2), 
						rs.getInt(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6)));
		    }
		    return appezzamentiList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.error("Errore in chiusura connessioni", e);
			}
		}
	}

	public List<ReportValidazioneFascicoloSchedarioFrutticoloDto> findSchedarioFrutticolo(final String cuaa) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection connection = null;
		PreparedStatement agsQuery = null;
		List<ReportValidazioneFascicoloSchedarioFrutticoloDto> ufList = new ArrayList<ReportValidazioneFascicoloSchedarioFrutticoloDto>();
		try {
			connection = this.dataSourceConfig.secondaryDataSource().getConnection();
			agsQuery = connection.prepareStatement("SELECT \r\n"
					+ "    x.sigla_prov,\r\n"
					+ "    x.nome,\r\n"
					+ "    s.foglio,\r\n"
					+ "    s.particella,\r\n"
					+ "    s.sub,\r\n"
					+ "    (select descrizione from sitiunar_deco where nome_campo = 'TIPO_UNAR' and codice = s.tipo_unar and tipo_unar = 0) as desc_tipo_unar,\r\n"
					+ "    s.unar,\r\n"
					+ "    s.area,\r\n"
					+ "    substr(c.de_coltura, instr(c.de_coltura, '- ', -1) + 2) as desc_varieta,\r\n"
					+ "    s.anno_impi,\r\n"
					+ "    s.sesto_su_fila || 'x' || s.sesto_tra_file as sesto,\r\n"
					+ "    (select descrizione from sitiunar_deco where nome_campo = 'FORMA_ALL' and codice = s.forma_all and tipo_unar = s.tipo_unar) as forma_allevamento,\r\n"
					+ "    s.num_ceppi,\r\n"
					+ "    nvl(s.copertura, 'N.A.') as copertura,\r\n"
					+ "    case when s.antibrina = 1 then 'SI' else 'NO' end as antibrina,\r\n"
					+ "    case when s.antigrandine = 1 then 'SI' else 'NO' end as antigrandine\r\n"
					+ "FROM anag_soggetti b \r\n"
					+ "    inner join tpiano_colture p on p.id_soggetto = b.cod_soggetto\r\n"
					+ "    inner join SITIUNAR S on s.cod_nazionale = p.cod_nazionale \r\n"
					+ "        and s.foglio = p.foglio\r\n"
					+ "        and s.particella = p.particella\r\n"
					+ "        and nvl(s.sub, ' ') = nvl(p.sub, ' ')\r\n"
					+ "        and s.area = p.sup_dichiarata\r\n"
					+ "    INNER JOIN tcompat_colture_unar VU ON  S.COD_VARIETA = VU.cod_varieta_unar \r\n"
					+ "        AND VU.TIPO_UNAR = s.TIPO_UNAR AND sysdate between VU.DT_INIZIO and VU.DT_FINE\r\n"
					+ "    inner join siticomu x on s.cod_nazionale = x.cod_nazionale\r\n"
					+ "    inner join vcoltura c on c.cod_utilizzo = vu.cod_utilizzo and c.cod_coltura = vu.cod_coltura \r\n"
					+ "        and c.cod_varieta = vu.cod_varieta_unar AND sysdate between c.DT_INIZIO and c.DT_FINE\r\n"
					+ "WHERE b.cod_FISCALE = ?\r\n"
					+ "    and sysdate between b.data_inizio_val and b.data_fine_val\r\n"
					+ "    and p.anno = extract (year from sysdate)\r\n"
					+ "    and sysdate between p.dt_inizio and p.dt_fine\r\n"
					+ "    and sysdate between p.dt_insert and p.dt_delete\r\n"
					+ "    and SYSDATE BETWEEN s.DATA_INIZIO_VAL AND s.DATA_FINE_VAL\r\n"
					+ "    AND S.TIPO_UNAR >= 11");
			agsQuery.setString(1, cuaa);
		    ResultSet rs = agsQuery.executeQuery();
		    while (rs.next()) {
				log.debug("UF: {}, {}", rs.getString(2), rs.getString(4));
				ufList.add(new ReportValidazioneFascicoloSchedarioFrutticoloDto(
						rs.getString(1),
						rs.getString(2),
						rs.getInt(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getInt(7),
						rs.getInt(8),
						rs.getString(9),
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13),
						rs.getString(14),
						rs.getString(15),
						rs.getString(16)
					));
		    }
		    return ufList;
		} finally {
			try {
				if (agsQuery != null) {
					agsQuery.close();
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
