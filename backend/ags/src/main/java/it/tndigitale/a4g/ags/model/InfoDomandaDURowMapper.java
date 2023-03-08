package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.InfoDomandaDU;
import it.tndigitale.a4g.ags.dto.InfoParticella;

public class InfoDomandaDURowMapper implements RowMapper<InfoDomandaDU> {
	
	public InfoDomandaDURowMapper() {
		super();
	}

	@Override
	public InfoDomandaDU mapRow(ResultSet rs, int rowNum) throws SQLException {

		InfoDomandaDU infoDomandaDU = new InfoDomandaDU();
		
		infoDomandaDU.setAnnoRiferimento(rs.getInt("ANNO_RIFERIMENTO"));
		infoDomandaDU.setPac(rs.getString("PAC"));
		infoDomandaDU.setNumeroDomanda(rs.getLong("NUMERO_DOMANDA"));
		infoDomandaDU.setDescrizioneDomanda(rs.getString("DESCRIZIONE_DOMANDA"));
		infoDomandaDU.setDataPresentazione(rs.getDate("DATA_PRESENTAZIONE"));
		infoDomandaDU.setCuaa(rs.getString("CUAA"));
		infoDomandaDU.setEnteCompilatore(rs.getString("ENTE_COMPILATORE"));
		infoDomandaDU.setRagioneSociale(rs.getString("RAGIONE_SOCIALE"));
		
		return infoDomandaDU;
	}

}
