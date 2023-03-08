package it.tndigitale.a4g.psr.business.persistence.repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import it.tndigitale.a4g.psr.dto.StatoDomanda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.psr.dto.DomandaPsr;
import it.tndigitale.a4g.psr.dto.StatoOperazione;


class DomandaPsrRowMapper implements RowMapper<DomandaPsr>{
	
	private static final Logger logger = LoggerFactory.getLogger(DomandaPsrRowMapper.class);

	private final static HashMap<String, StatoDomanda> MAPPING_STATO_DOMANDA = populateStatoDomanda();

	@Override
	public DomandaPsr mapRow(ResultSet rs, int rowNum) throws SQLException {
		DomandaPsr domandaPsr=new DomandaPsr();
		domandaPsr.setCampagna(rs.getInt("campagna"));
		domandaPsr.setCuaa(rs.getString("cuaa"));
		domandaPsr.setDataPresentazione(LocalDateConverter.fromDate(rs.getDate("dataPresentazione")));
		domandaPsr.setNumeroDomanda(rs.getLong("numeroDomanda"));
		domandaPsr.setStato(MAPPING_STATO_DOMANDA.get(rs.getString("sco_stato")));
		try {
			domandaPsr.setSottoStato(StatoOperazione.valueOf(rs.getString("stato")));
		} catch (IllegalArgumentException e) {
			logger.error("Errore conversione StatoOperazione: {}",rs.getString("stato"),e);
		}
		return domandaPsr;
	}

	/**
	 * Lista degli stati da recuperare e relativo mapping
	 * STATO CRUSCOTTO MOBILE		STATO ISTRUTTORIA AGS E
	 * 								SOTTO STATO CRUSCOTTO MOBILE
	 * PROTOCOLLATA					PROTOCOLLATA
	 * RICEVIBILE					RICEVIBILE
	 * NON RICEVIBILE				NON RICEVIBILE
	 * IN ASSEGNAZIONE				ASSEGNABILE,ASSEGNATA,NON ASSEGNATA
	 * ISTRUTTORIA CONCLUSA			ISTRUTTORIA CONCLUSA
	 * IN ISTRUTTORIA				TUTTI GLI ALTRI STATI
	 */
	static HashMap<String, StatoDomanda> populateStatoDomanda() {
		HashMap<String, StatoDomanda> map = new HashMap<>();
		map.put("000063", StatoDomanda.IN_ISTRUTTORIA);//000063	AMMISSIBILE
		map.put("000060", StatoDomanda.IN_ASSEGNAZIONE);//000060	ASSEGNABILE
		map.put("000061", StatoDomanda.IN_ASSEGNAZIONE);//000061	ASSEGNATA
		map.put("000066", StatoDomanda.IN_ISTRUTTORIA);//000066	IN ISTRUTTORIA
		map.put("000090", StatoDomanda.IN_ISTRUTTORIA);//000090	IN LIQUIDAZIONE
		map.put("000095", StatoDomanda.IN_ISTRUTTORIA);//000095	IN SOSPENSIONE
		map.put("000095", StatoDomanda.IN_ISTRUTTORIA);//INIIST	INIZIO ISTRUTTORIA
		map.put("000C66", StatoDomanda.IN_ISTRUTTORIA);//000C66	INIZIO ISTRUTTORIA DOMANDE A CAMPIONE
		map.put("000170", StatoDomanda.IN_ISTRUTTORIA);//000170	INTEGRAZIONE
		map.put("000079", StatoDomanda.ISTRUTTORIA_CONCLUSA);//000079	ISTRUTTORIA CONCLUSA
		map.put("000067", StatoDomanda.IN_ISTRUTTORIA);//000067	ISTRUTTORIA PARZIALE
		map.put("000C67", StatoDomanda.IN_ISTRUTTORIA);//000C67	ISTRUTTORIA PARZIALE DOMANDE A CAMPIONE
		map.put("000068", StatoDomanda.IN_ISTRUTTORIA);//000068	LIQUIDABILE
		map.put("000064", StatoDomanda.IN_ISTRUTTORIA);//000064	NON AMMISSIBILE
		map.put("000062", StatoDomanda.IN_ASSEGNAZIONE);//000062	NON ASSEGNATA
		map.put("000069", StatoDomanda.IN_ISTRUTTORIA);//000069	NON LIQUIDABILE
		map.put("000055", StatoDomanda.NON_RICEVIBILE);//000055	NON RICEVIBILE
		map.put("000087", StatoDomanda.IN_ISTRUTTORIA);//000087	PROPOSTA LIQUIDAZIONE
		map.put("000015", StatoDomanda.PROTOCOLLATA);//000015	PROTOCOLLATA
		map.put("REVISI", StatoDomanda.IN_ISTRUTTORIA);//REVISI	REVISIONE
		map.put("000050", StatoDomanda.RICEVIBILE);//000050	RICEVIBILE
		return map;
	}

}
