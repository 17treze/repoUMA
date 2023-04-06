package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsRowMapper.FascicoloAgsRow;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDtoPaged;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.ModoPagamentoAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.StatoFascicoloLegacy;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;
import it.tndigitale.a4g.framework.config.DataSourceConfig;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

@Repository
public class FascicoloAgsDao extends JdbcDaoSupport {
	
	@Autowired
	private DataSourceConfig dataSourceConfig;
	
	private LocalDateTime clock;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSourceConfig.secondaryDataSource());
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSourceConfig.secondaryDataSource());
	}
	
	public FascicoloAgsDto getFascicolo(String cuaa, LocalDateTime data) {
		return getFascicolo(null, cuaa, data);
	}
	
	public FascicoloAgsDto getFascicolo(Long id, LocalDateTime data) {
		return getFascicolo(id, null, data);
	}
	
	public FascicoloAgsDto getFascicolo(Long id) {
		return getFascicolo(id, clock.now());
	}
	
	public List<FascicoloAgsDtoPaged> getFascicoli(FascicoloAgsFilter filter, Pageable pageable,
			Ordinamento ordinamento) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		StringBuilder sb = new StringBuilder().append(NativeQueryString.SQL_GET_FASCICOLO).append(" AND (")
				.append(" UPPER(f.CUAA) = :cuaa ").append("OR")
				.append(" UPPER(p.nome || p.cognome) LIKE :denominazione ").append(") ");
		
		String proprieta = ordinamento.getProprieta();
		if (proprieta != null) {
			sb.append(" ORDER BY ");
			switch (proprieta.toUpperCase()) {
				case "CUAA":
					sb.append(" f.CUAA ");
					break;
				case "DENOMINAZIONE":
					sb.append(" p.COGNOME ");
					break;
				default:
					sb.append(" f.CUAA ");
					break;
			}
			sb = Ordine.ASC.equals(ordinamento.getOrdine()) ? sb.append("ASC") : sb.append("DESC");
		}
		
		sb.append(" OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY ");
		
		parameters.addValue("cuaa", filter.getCuaa() == null ? "" : filter.getCuaa().toUpperCase());
		parameters.addValue("denominazione", filter.getDenominazione() == null ? ""
				: "%".concat(filter.getDenominazione().toUpperCase()).concat("%"));
		parameters.addValue("offset", pageable.getOffset());
		parameters.addValue("size", pageable.getPageSize());
		parameters.addValue("data", LocalDateConverter.to(clock.now()));
		
		List<FascicoloAgsPagedRowMapper.FascicoloAgsPagedRow> resultsRow = namedParameterJdbcTemplate.<FascicoloAgsPagedRowMapper.FascicoloAgsPagedRow> query(
				sb.toString(), parameters, new FascicoloAgsPagedRowMapper());
		List<FascicoloAgsDtoPaged> results = new ArrayList<>();
		resultsRow.stream().forEach(x -> {
			var f = new FascicoloAgsDtoPaged();
			BeanUtils.copyProperties(x, f);
			results.add(f);
		});
		return results;
	}
	
	public List<CaricaAgsDto> getCariche(String codiceFiscale, String cuaa) {
		/*
		 * List<Object> params = new ArrayList<>(); params.add(codiceFiscale.toUpperCase()); MapSqlParameterSource
		 * parameters = new MapSqlParameterSource(); parameters.addValue("codiceFiscale", codiceFiscale); String sql =
		 * NativeQueryString.GET_CARICHE; if (cuaa != null) { String cuaaCondition = " AND sogg.cuaa = :cuaa "; sql =
		 * sql.concat(cuaaCondition); parameters.addValue("cuaa", cuaa); params.add(cuaa.toUpperCase()); } try {
		 * JdbcTemplate jdbcTemplate = getJdbcTemplate(); if (jdbcTemplate == null) { return Collections.emptyList(); }
		 * return jdbcTemplate.query(sql, params.toArray(), new ResponsabilitaRowMapper()); } catch (NoResultException |
		 * EmptyResultDataAccessException e) { logger.warn("getListaDomandePerStato: ", e); return
		 * Collections.emptyList(); }
		 */
		List<CaricaAgsDto> cariche = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto carica = new CaricaAgsDto();
		carica.setCodiceFiscale("FLGKTA79S41L378T");
		carica.setCarica(Carica.PROPRIETARIO);
		carica.setCuaa("FLGKTA79S41L378T");
		carica.setDenominazione("KATIA FALAGIARDA");
		carica.setNome("KATIA");
		carica.setCognome("FALAGIARDA");
		cariche.add(carica);
		return cariche;
	}
	
	public List<CaricaAgsDto> getTitolariRappresentantiLegali(String cuaa) {
		return getSoggetti(cuaa, CaricheLegacyMap.RUOLI_RAPPRESENTANTI_LOCALI);
	}
	
	public List<CaricaAgsDto> getEredi(String cuaa) {
		return getSoggetti(cuaa, CaricheLegacyMap.RUOLI_EREDI);
	}
	
	public MovimentoValidazioneFascicoloAgsDto getMovimentiValidazioneFascicolo(Long id, Long campagna) {
		String filter = "";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (campagna != null) {
			parameters.addValue("campagna", campagna);
		}
		if (id != null) {
			parameters.addValue("id", id);
		}
		String sql = NativeQueryString.SQL_MOVIMENTI_VALIDAZIONE_FASCICOLO.concat(filter);
		
		return namedParameterJdbcTemplate.<MovimentoValidazioneFascicoloAgsDto> queryForObject(sql, parameters,
				new MovimentoValidazioneFascicoloAgsRowMapper());
	}
	
	public FascicoloAgsDto getFascicoloDaMigrare(String cuaa, LocalDateTime data) {
		String filter = "";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (data != null) {
			parameters.addValue("data", LocalDateConverter.to(data));
		}
		if (cuaa != null) {
			filter = filter.concat("AND csv.CUAA = :cuaa ");
			parameters.addValue("cuaa", cuaa);
		}
		String sql = NativeQueryString.SQL_GET_FASCICOLO_CON_DELEGHE.concat(filter);
		List<FascicoloAgsRowMapper.FascicoloAgsRow> rowResults = namedParameterJdbcTemplate.<FascicoloAgsRowMapper.FascicoloAgsRow> query(
				sql, parameters, new FascicoloAgsRowMapper());
		
		return rowResults.stream().collect(Collectors.groupingBy(FascicoloAgsRowMapper.FascicoloAgsRow::getCuaa))
				.entrySet().stream().map(entry -> {
					FascicoloAgsRow f = entry.getValue().stream().findFirst().get();
					FascicoloAgsDto fascicoloAgsDto = new FascicoloAgsDto().setIdAgs(f.getIdAgs()).setCuaa(f.getCuaa())
							.setDenominazione(f.getDenominazione()).setDataMorteTitolare(f.getDataMorte())
							.setStato(f.getStato()).setOrganismoPagatore(f.getOrganismoPagatore())
							.setDataCostituzione(f.getDataCostituzione()).setDataAggiornamento(f.getDataAggiornamento())
							.setDataValidazione(f.getDataValidazione())
							.setIscrittoSezioneSpecialeAgricola(f.isIscrittoSezioneSpecialeAgricola())
							.setNonIscrittoSezioneSpecialeAgricola(f.isNonIscrittoSezioneSpecialeAgricola())
							.setPec(f.getPec());
					
					entry.getValue().stream().forEach(detenzione -> fascicoloAgsDto.addDetenzione(new DetenzioneAgsDto()
							.setCaa(detenzione.getCaa())
							.setIdentificativoSportello(detenzione.getIdentificativoSportello())
							.setSportello(detenzione.getSportello()).setDataInizio(detenzione.getDataInizioDetenzione())
							.setDataFine(detenzione.getDataFineDetenzione())
							.setTipoDetenzione(detenzione.getTipoDetenzione())));
					return fascicoloAgsDto;
				}).collect(CustomCollectors.toSingleton());
		
	}
	
	public List<ModoPagamentoAgsDto> getModiPagamentoDaMigrare(String cuaa, LocalDateTime data) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (data != null) {
			parameters.addValue("data", LocalDateConverter.to(data));
		}
		if (cuaa != null) {
			parameters.addValue("cuaa", cuaa);
		}
		String sql = NativeQueryString.SQL_GET_MODI_PAGAMENTO;
		List<ModoPagamentoAgsRowMapper.ModoPagamentoAgsRow> rowResults = namedParameterJdbcTemplate.<ModoPagamentoAgsRowMapper.ModoPagamentoAgsRow> query(
				sql, parameters, new ModoPagamentoAgsRowMapper());
		List<ModoPagamentoAgsDto> listaModiPagamento = new ArrayList<>();
		for (ModoPagamentoAgsRowMapper.ModoPagamentoAgsRow f : rowResults) {
			listaModiPagamento.add(new ModoPagamentoAgsDto().setIdAgs(f.getIdAgs()).setIban(f.getIban())
					.setDenominazioneIstituto(f.getDenominazioneIstituto())
					.setDenominazioneFiliale(f.getDenominazioneFiliale()));
		}
		return listaModiPagamento;
	}
	
	private List<CaricaAgsDto> getSoggetti(String cuaa, Map<String, Carica> map) {
		List<Object> params = new ArrayList<>();
		params.add(cuaa.toUpperCase());
		try {
			JdbcTemplate jdbcTemplate = getJdbcTemplate();
			if (jdbcTemplate == null) {
				return Collections.emptyList();
			}
			return jdbcTemplate.query(NativeQueryString.getSoggetti(map), params.toArray(), new SoggettiRowMapper());
		}
		catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getSoggettiFascicoloAziendale: ", e);
			return Collections.emptyList();
		}
	}
	
	private FascicoloAgsDto getFascicolo(Long id, String cuaa, LocalDateTime data) {
		/*
		 * String filter = ""; MapSqlParameterSource parameters = new MapSqlParameterSource(); if (id != null) { filter
		 * = filter.concat("AND f.ID_FASCICOLO = :id "); parameters.addValue("id", id); } if (data != null) {
		 * parameters.addValue("data", LocalDateConverter.to(data)); } if (cuaa != null) { filter =
		 * filter.concat("AND csv.CUAA = :cuaa "); parameters.addValue("cuaa", cuaa); } String sql =
		 * NativeQueryString.SQL_GET_FASCICOLO_CON_DELEGHE.concat(filter); List<FascicoloAgsRowMapper.FascicoloAgsRow>
		 * rowResults = namedParameterJdbcTemplate.<FascicoloAgsRowMapper.FascicoloAgsRow> query( sql, parameters, new
		 * FascicoloAgsRowMapper()); return
		 * rowResults.stream().collect(Collectors.groupingBy(FascicoloAgsRowMapper.FascicoloAgsRow::getCuaa))
		 * .entrySet().stream().map(entry -> { FascicoloAgsRow f = entry.getValue().stream().findFirst().get();
		 * FascicoloAgsDto fascicoloAgsDto = new FascicoloAgsDto().setIdAgs(f.getIdAgs()).setCuaa(f.getCuaa())
		 * .setDenominazione(f.getDenominazione()).setDataMorteTitolare(f.getDataMorte())
		 * .setStato(f.getStato()).setOrganismoPagatore(f.getOrganismoPagatore())
		 * .setDataCostituzione(f.getDataCostituzione()).setDataAggiornamento(f.getDataAggiornamento())
		 * .setDataValidazione(f.getDataValidazione())
		 * .setIscrittoSezioneSpecialeAgricola(f.isIscrittoSezioneSpecialeAgricola())
		 * .setNonIscrittoSezioneSpecialeAgricola(f.isNonIscrittoSezioneSpecialeAgricola()) .setPec(f.getPec());
		 * entry.getValue().stream().forEach(detenzione -> fascicoloAgsDto.addDetenzione(new DetenzioneAgsDto()
		 * .setCaa(detenzione.getCaa()) .setIdentificativoSportello(detenzione.getIdentificativoSportello())
		 * .setSportello(detenzione.getSportello()).setDataInizio(detenzione.getDataInizioDetenzione())
		 * .setDataFine(detenzione.getDataFineDetenzione()) .setTipoDetenzione(detenzione.getTipoDetenzione()))); return
		 * fascicoloAgsDto; }).collect(CustomCollectors.toSingleton());
		 */
		FascicoloAgsDto fascicoloAgsDto = new FascicoloAgsDto();
		fascicoloAgsDto.setIdAgs(1L);
		fascicoloAgsDto.setCuaa("FLGKTA79S41L378T");
		fascicoloAgsDto.setStato(StatoFascicoloLegacy.IN_LAVORAZIONE);
		fascicoloAgsDto.setDenominazione("KATIA FALAGIARDA");
		fascicoloAgsDto.setPec("K.FALAGIARDA@GMAIL.COM");
		fascicoloAgsDto.setOrganismoPagatore("APPAG");
		DetenzioneAgsDto detenzioneAgsDto = new DetenzioneAgsDto();
		detenzioneAgsDto.setCaa("CAA COOPTRENTO SRL");
		detenzioneAgsDto.setTipoDetenzione(TipoDetenzioneAgs.MANDATO);
		detenzioneAgsDto.setSportello("TEST");
		detenzioneAgsDto.setIdentificativoSportello(1024L);
		detenzioneAgsDto.setDataInizio(LocalDateTime.now());
		fascicoloAgsDto.addDetenzione(detenzioneAgsDto);
		return fascicoloAgsDto;
	}
	
	private interface NativeQueryString {
		// Attualmente ci sono i codici dei soli ruoli censiti sul DB. A fronte dell'aggiornamento che ne verrà fatto, dovranno essere aggiornati.
		String GET_CARICHE = "SELECT pers.codice_fiscale AS persona "
				+ " ,(SELECT ds_decodifica FROM tdecodifica WHERE codice = pers.cod_ruolo AND sotto_codice = pers.sco_ruolo) AS ruolo_persona"
				+ " ,(SELECT ds_decodifica FROM tdecodifica WHERE codice = pers.cod_fonte_dati AND sotto_codice = pers.sco_fonte_dati) AS fonte_dati"
				+ " ,sogg.cuaa AS cuaa_azienda" + " ,pers.nome AS nome" + " ,pers.cognome AS cognome"
				+ " ,sogg.ragi_soci AS ragione_sociale_azienda" + " ,pers.sco_ruolo " + "FROM tpersona pers"
				+ "  JOIN cons_sogg_viw sogg" + "  ON pers.id_soggetto = sogg.pk_cuaa "
				+ "WHERE SYSDATE BETWEEN pers.dt_insert AND pers.dt_delete "
				+ "  AND SYSDATE BETWEEN sogg.data_inizio_val AND sogg.data_fine_val"
				+ "  AND SYSDATE BETWEEN pers.dt_insert AND pers.DT_FINE"
				+ "  AND pers.codice_fiscale = :codiceFiscale " + "  AND pers.sco_ruolo IN ("
				+ createScoRuoloFilter(CaricheLegacyMap.RUOLI_RAPPRESENTANTI_LOCALI) + ") ";
		
		String SQL_GET_MODI_PAGAMENTO = "select IBAN.PKID AS ID, COD_IBAN, de_banca, de_filiale, dt_inizio, dt_fine  "
				+ "from TMOD_PAGAMENTO_IBAN iban  "
				+ "    join ANAG_SOGGETTI sogg on iban.id_soggetto = sogg.cod_soggetto  "
				+ "        and sysdate between sogg.data_inizio_val and sogg.data_fine_val  "
				+ "WHERE :data between iban.dt_insert and iban.dt_delete  " + "and sogg.cod_fiscale = :cuaa "
				+ "and :data between iban.dt_inizio and iban.dt_fine ";
		
		final String SQL_DATA_VALIDAZIONE = "SELECT MAX (mov.dt_movimento) FROM tfascicolo fas, tmovimento mov "
				+ "WHERE fas.id_soggetto = csv.pk_cuaa " + "AND mov.id_fascicolo = fas.id_fascicolo "
				+ "AND mov.sco_movimento = 'VALIDA' " + "AND NOT EXISTS (SELECT 1 FROM tval_anomalia val, "
				+ "tdecodifica_relazione rel " + "WHERE val.id_movimento = mov.id_movimento "
				+ "AND rel.cod_relazione = 'DECREL' " + "AND rel.sco_relazione = 'TIPANO' "
				+ "AND rel.codice_a = val.cod_anomalia " + "AND rel.sotto_codice_a = val.sco_anomalia "
				+ "AND rel.codice_b = 'TIPANO' " + "AND rel.sotto_codice_b = 'BLOCCA') ";
		
		final String SQL_DATA_VALIDAZIONE_ANNO = SQL_DATA_VALIDAZIONE
				+ "AND EXTRACT (YEAR FROM mov.dt_movimento) = :campagna ";
		
		String SQL_GET_FASCICOLO = "select COUNT(*) OVER () as TOTALE, f.ID, \r\n"
				+ "    f.cuaa, p.nome || ' ' || p.cognome AS DENOMINAZIONE , \r\n" + "    f.STATO AS STATO, \r\n"
				+ "    f.ORGANISMO_PAGATORE, \r\n" + "    NULL AS CAA, \r\n" + "    NULL AS SPORTELLO, \r\n"
				+ "    1024 AS IDENTIFICATIVO_SPORTELLO, \r\n" + "    f.data_apertura AS DATA_COSTITUZIONE, \r\n"
				+ "    f.data_apertura AS DATA_AGGIORNAMENTO, \r\n" + "    f.data_apertura AS DATA_VALIDAZIONE, \r\n"
				+ "    NULL as SEZIONE, \r\n" + "    NULL as UMA_NO_SEZIONE, \r\n" + "    p.PEC \r\n"
				+ "from a4gt_fascicolo f \r\n" + "INNER JOIN a4gt_persona_fisica p on p.id = f.id_persona ";
		
		String SQL_GET_FASCICOLO_CON_DELEGHE = "select " + "COUNT(*) OVER () as TOTALE, " + "f.ID_FASCICOLO AS ID, "
				+ "csv.cuaa as CUAA, " + "csv.RAGI_SOCI AS DENOMINAZIONE, " + "csv.D_MORTE as DATA_MORTE, "
				+ "f.SCO_STATO AS STATO, " + "t.DS_DECODIFICA AS ORGANISMO_PAGATORE, "
				+ "(SELECT e.DES_ENTE FROM siti.sitiente e WHERE e.cod_ente = ente.COD_ENTE_SUP and SYSDATE between e.data_inizio and e.data_fine) AS CAA, "
				+ "cuaa_ente.TIPO_ASSOCIAZIONE, " + "cuaa_ente.DATA_INIZIO AS DATA_INIZIO_DETENZIONE, "
				+ "cuaa_ente.DATA_FINE AS DATA_FINE_DETENZIONE, " + "ente.DES_ENTE AS SPORTELLO, "
				+ "ente.COD_ENTE AS IDENTIFICATIVO_SPORTELLO, " + "f.DT_INIZIO AS DATA_COSTITUZIONE, "
				+ "f.DT_AGGIORNAMENTO AS DATA_AGGIORNAMENTO, " + "( " + SQL_DATA_VALIDAZIONE + ") "
				//questa è l'ultima data di validazione del fascicolo che non ha anomalie bloccanti
				+ " AS DATA_VALIDAZIONE, " + "inf.fg_sezione as SEZIONE, " + "inf.fg_uma_no_sezione as UMA_NO_SEZIONE, "
				+ "inf.EMAIL_PEC AS PEC " + "from tfascicolo f "
				+ "INNER JOIN TDECODIFICA t ON t.CODICE = f.COD_OP AND t.SOTTO_CODICE = f.SCO_OP "
				+ "INNER JOIN cons_sogg_viw csv on csv.pk_cuaa = f.id_soggetto "
				+ "LEFT OUTER JOIN tinfo_parix inf on inf.ID_SOGGETTO = f.id_soggetto and :data BETWEEN inf.dt_insert AND inf.dt_delete and csv.pk_cuaa = inf.id_soggetto "
				+ "INNER JOIN tdecodifica_language deco on f.cod_stato = deco.codice and f.sco_stato = deco.sotto_codice "
				// Rimuove sportelli uap, appag e caa fuori provincia - 70	CAA C.A.N.A.P.A.
				+ "LEFT OUTER JOIN siti.cons_cuaa_ente cuaa_ente on cuaa_ente.pk_cuaa = csv.pk_cuaa and cuaa_ente.tipo_associazione in ('MAN', 'DEL') and :data between cuaa_ente.data_inizio and cuaa_ente.data_fine AND cuaa_ente.flag_gestione='S' AND cuaa_ente.COD_ENTE not in (1, 23,24,25,26,27,28,29,30,31,32,33, 106, 70) "
				+ "LEFT OUTER JOIN siti.sitiente ente on ente.cod_ente = cuaa_ente.cod_ente and :data between ente.data_inizio and ente.data_fine "
				+ "where :data between f.dt_inizio and f.dt_fine "
				+ "and SYSDATE between csv.data_inizio_val and csv.data_fine_val " // cons_sogg_view va sempre con sysdate, anche se recupero lo storico
				+ "AND ente.DES_ENTE LIKE '%CAA%' ";
				
		String SQL_MOVIMENTI_VALIDAZIONE_FASCICOLO = "SELECT  " + "f.id_fascicolo, " + "csv.cuaa, "
				+ "csv.RAGI_SOCI AS DENOMINAZIONE, " + "(SELECT COUNT (*) " + "FROM tfascicolo fas, tmovimento mov "
				+ "WHERE fas.id_soggetto = csv.pk_cuaa " + "AND mov.id_fascicolo = fas.id_fascicolo "
				+ "AND mov.sco_movimento = 'VALIDA' " + "AND EXTRACT (YEAR FROM mov.dt_movimento) = :campagna "
				+ "AND NOT EXISTS " + "(SELECT 1 " + "FROM tval_anomalia val, " + "tdecodifica_relazione rel "
				+ "WHERE val.id_movimento = mov.id_movimento " + "AND rel.cod_relazione = 'DECREL' "
				+ "AND rel.sco_relazione = 'TIPANO' " + "AND rel.codice_a = val.cod_anomalia "
				+ "AND rel.sotto_codice_a = val.sco_anomalia " + "AND rel.codice_b = 'TIPANO' "
				+ "AND rel.sotto_codice_b = 'BLOCCA')) " + "AS validazioni_effettuate_succ, " + "( "
				+ SQL_DATA_VALIDAZIONE_ANNO + ") " + "AS data_ultima_validazione_succ " + "FROM cons_sogg_viw csv "
				+ "INNER JOIN TFASCICOLO f ON f.ID_SOGGETTO = csv.PK_CUAA " + "WHERE f.ID_FASCICOLO = :id";
		
		static String getSoggetti(Map<String, Carica> mappaCariche) {
			/*
			 * return
			 * "SELECT p.COGNOME AS cognome, p.NOME AS nome, p.CODICE_FISCALE AS codice_fiscale , p.SCO_RUOLO AS ruolo, c.CUAA AS cuaa "
			 * + "FROM fascicolo.tpersona p" + " JOIN fascicolo.cons_sogg_viw c" + " ON p.id_soggetto = c.pk_cuaa " +
			 * "WHERE SYSDATE BETWEEN p.dt_insert AND p.dt_delete" + " AND SYSDATE BETWEEN p.dt_inizio AND p.dt_fine" +
			 * " AND SYSDATE BETWEEN c.data_inizio_val AND c.data_fine_val" + " AND cod_ruolo = 'RUOPER'" +
			 * " AND sco_ruolo IN (" + createScoRuoloFilter(mappaCariche) + ")" + " AND c.cuaa = :cuaa ";
			 */
			return "SELECT pf.COGNOME AS cognome, pf.NOME AS nome, p.CODICE_FISCALE AS codice_fiscale , "
					+ "nvl(c.descrizione, '000001') AS ruolo, p.codice_fiscale AS cuaa "
					+ "FROM a4gt_persona p join a4gt_persona_fisica pf on p.id = pf.id "
					+ "left outer join a4gt_carica c on c.id_persona_fisica_con_carica = p.id "
					+ "WHERE p.CODICE_FISCALE = :cuaa ";
		}
		
		private static String createScoRuoloFilter(Map<String, Carica> map) {
			return map.keySet().stream().collect(Collectors.joining(","));
		}
	}
	
}
