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
				.append(" UPPER(f.CUAA) = :cuaa ").append("OR").append(" UPPER(f.DENOMINAZIONE) LIKE :denominazione ")
				.append(") ");
		
		String proprieta = ordinamento.getProprieta();
		if (proprieta != null) {
			sb.append(" ORDER BY ");
			switch (proprieta.toUpperCase()) {
				case "CUAA":
					sb.append(" f.CUAA ");
					break;
				case "DENOMINAZIONE":
					sb.append(" f.DENOMINAZIONE ");
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
		List<Object> params = new ArrayList<>();
		params.add(codiceFiscale.toUpperCase());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("codiceFiscale", codiceFiscale);
		String sql = NativeQueryString.GET_CARICHE;
		if (cuaa != null) {
			String cuaaCondition = " AND f.cuaa = :cuaa ";
			sql = sql.concat(cuaaCondition);
			parameters.addValue("cuaa", cuaa);
			params.add(cuaa.toUpperCase());
		}
		
		try {
			JdbcTemplate jdbcTemplate = getJdbcTemplate();
			if (jdbcTemplate == null) {
				return Collections.emptyList();
			}
			return jdbcTemplate.query(sql, params.toArray(), new ResponsabilitaRowMapper());
		}
		catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getListaDomandePerStato: ", e);
			return Collections.emptyList();
		}
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
		String filter = "";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (id != null) {
			filter = filter.concat("AND f.ID = :id ");
			parameters.addValue("id", id);
		}
		//		if (data != null) {
		//			parameters.addValue("data", LocalDateConverter.to(data));
		//		}
		if (cuaa != null) {
			filter = filter.concat("AND f.CUAA = :cuaa ");
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
	
	private interface NativeQueryString {
		// Attualmente ci sono i codici dei soli ruoli censiti sul DB. A fronte dell'aggiornamento che ne verrà fatto, dovranno essere aggiornati.
		String GET_CARICHE = "SELECT p.codice_fiscale AS persona \r\n" + " , c.descrizione AS ruolo_persona\r\n"
				+ " , 'Agenzia dele Entrate' AS fonte_dati\r\n" + " , f.cuaa AS cuaa_azienda\r\n"
				+ " , pf.nome AS nome \r\n" + " , pf.cognome AS cognome\r\n"
				+ " , f.denominazione AS ragione_sociale_azienda ,\r\n" + " '000001' as sco_ruolo\r\n"
				+ "FROM a4gt_persona p\r\n" + "    inner join a4gt_fascicolo f on f.id_persona = p.id\r\n"
				+ "    left outer join a4gt_persona_fisica pf on pf.id = p.id\r\n"
				+ "    left outer join a4gt_carica c on c.persona_fisica_id = pf.id and c.persona_fisica_id_validazione = pf.id_validazione "
				+ "WHERE p.codice_fiscale = :codiceFiscale";
		
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
		
		String SQL_GET_FASCICOLO = "select COUNT(*) OVER () as TOTALE, \r\n" + "    f.ID,\r\n"
				+ "    f.cuaa, f.DENOMINAZIONE, f.STATO, \r\n" + "    f.ORGANISMO_PAGATORE,\r\n"
				+ "    c.denominazione AS CAA, \r\n" + "    s.denominazione AS SPORTELLO, \r\n"
				+ "    m.IDENTIFICATIVO_SPORTELLO,\r\n" + "    f.data_apertura AS DATA_COSTITUZIONE, \r\n"
				+ "    nvl(f.data_modifica, f.data_apertura) AS DATA_AGGIORNAMENTO,\r\n"
				+ "    nvl(f.data_validazione, f.data_apertura) AS DATA_VALIDAZIONE, \r\n" + "    NULL as SEZIONE,\r\n"
				+ "    NULL as UMA_NO_SEZIONE, \r\n" + "    p.PEC\r\n" + "from a4gt_fascicolo f\r\n"
				+ "INNER JOIN a4gt_persona_fisica p on p.id = f.id_persona\r\n"
				+ "inner join a4gt_detenzione d on d.id_fascicolo = f.id and d.fascicolo_id_validazione = f.id_validazione\r\n"
				+ "left outer join a4gt_mandato m on m.id = d.id and m.id_validazione = d.id_validazione\r\n"
				+ "left outer join a4gt_sportello s on m.identificativo_sportello = s.identificativo\r\n"
				+ "left outer join a4gt_caa c on s.id_caa = c.id ";
		
		String SQL_GET_FASCICOLO_CON_DELEGHE = "select COUNT(*) OVER () as TOTALE, f.ID,\r\n"
				+ "    f.CUAA, f.DENOMINAZIONE, p.DATA_MORTE,\r\n" + "    f.STATO, f.ORGANISMO_PAGATORE,\r\n"
				+ "    c.denominazione AS CAA,\r\n" + "    'MAN' as TIPO_ASSOCIAZIONE, \r\n"
				+ "    m.DATA_SOTTOSCRIZIONE AS DATA_INIZIO_DETENZIONE,\r\n" + "    NULL AS DATA_FINE_DETENZIONE, \r\n"
				+ "    s.denominazione AS SPORTELLO,\r\n" + "    m.IDENTIFICATIVO_SPORTELLO, \r\n"
				+ "    f.data_apertura AS DATA_COSTITUZIONE,\r\n"
				+ "    nvl(f.data_modifica, f.data_apertura) AS DATA_AGGIORNAMENTO,\r\n"
				+ "    nvl(f.data_validazione, f.data_apertura) AS DATA_VALIDAZIONE, \r\n" + "    NULL as SEZIONE, \r\n"
				+ "    NULL as UMA_NO_SEZIONE,\r\n" + "    p.PEC \r\n" + "from a4gt_fascicolo f\r\n"
				+ "INNER JOIN a4gt_persona_fisica p on p.id = f.id_persona\r\n"
				+ "inner join a4gt_detenzione d on d.id_fascicolo = f.id and d.fascicolo_id_validazione = f.id_validazione\r\n"
				+ "left outer join a4gt_mandato m on m.id = d.id and m.id_validazione = d.id_validazione\r\n"
				+ "left outer join a4gt_sportello s on m.identificativo_sportello = s.identificativo\r\n"
				+ "left outer join a4gt_caa c on s.id_caa = c.id\r\n" + "where 1 = 1 ";
		
		String SQL_MOVIMENTI_VALIDAZIONE_FASCICOLO = "SELECT f.id,\r\n" + "    f.cuaa,\r\n" + "    f.DENOMINAZIONE,\r\n"
				+ "    f.id_validazione AS validazioni_effettuate_succ,\r\n"
				+ "    f.data_validazione AS data_ultima_validazione_succ\r\n"
				+ "FROM a4gt_fascicolo f WHERE f.ID = :id";
		
		static String getSoggetti(Map<String, Carica> mappaCariche) {
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
