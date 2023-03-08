package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;

class ResponsabilitaRowMapper implements RowMapper<CaricaAgsDto> {
	@Override
	public CaricaAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		CaricaAgsDto carica = new CaricaAgsDto();

		carica.setCodiceFiscale(rs.getString("PERSONA"))
		.setCuaa(rs.getString("CUAA_AZIENDA"))
		.setCarica(CaricheLegacyMap.RUOLI_RAPPRESENTANTI_LOCALI.get(rs.getString("SCO_RUOLO")))
		.setDenominazione(rs.getString("RAGIONE_SOCIALE_AZIENDA"))
		.setNome(rs.getString("NOME"))
		.setCognome(rs.getString("COGNOME"));

		return carica;
	}
}
