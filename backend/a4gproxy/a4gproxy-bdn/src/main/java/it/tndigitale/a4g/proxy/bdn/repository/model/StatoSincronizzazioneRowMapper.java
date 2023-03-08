package it.tndigitale.a4g.proxy.bdn.repository.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;
import org.springframework.jdbc.core.RowMapper;

public class StatoSincronizzazioneRowMapper implements RowMapper<StatoSincronizzazioneDO> {

	public StatoSincronizzazioneRowMapper() {
		super();
	}

	@Override
	public StatoSincronizzazioneDO mapRow(ResultSet rs, int rowNum) throws SQLException {

		StatoSincronizzazioneDO statoSincronizzazione = new StatoSincronizzazioneDO();

		statoSincronizzazione.setAnnoCampagna(rs.getInt("ANNO"));
		statoSincronizzazione.setCuaa(rs.getString("CUAA"));
		statoSincronizzazione.setStatoEsecuzione(rs.getString("STATO_ESECUZIONE"));
		statoSincronizzazione.setDataEsecuzione(rs.getDate("DATA_ESECUZIONE"));

		return statoSincronizzazione;
	}

}
