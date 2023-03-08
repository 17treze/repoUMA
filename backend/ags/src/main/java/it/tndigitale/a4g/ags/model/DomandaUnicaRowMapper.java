package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;

public class DomandaUnicaRowMapper implements RowMapper<DomandaUnica>{

	@Override
	public DomandaUnica mapRow(ResultSet rs, int rowNum) throws SQLException {
		DomandaUnica du = new DomandaUnica();
		InfoGeneraliDomanda igd= new InfoGeneraliDomanda();
		igd.setNumeroDomanda(rs.getLong("ID_DOMANDA"));
		igd.setCampagna(rs.getBigDecimal("CAMPAGNA"));
		igd.setCuaaIntestatario(rs.getString("CUAA"));
		igd.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
		igd.setStato(StatoDomanda.fromCodStato(rs.getString("SCO_STATO")));
		igd.setDataPresentazione(rs.getDate("DATA_PRESENTAZIONE"));
		igd.setDataProtocollazione(rs.getDate("DATA_PROTOCOLLAZIONE"));
		du.setInfoGeneraliDomanda(igd);
		return du;
	}

}
