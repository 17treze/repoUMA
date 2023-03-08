package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;

public class SoggettiRowMapper implements RowMapper<CaricaAgsDto> {

	@Override
	public CaricaAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		Map<String, Carica> map = new HashMap<>();
		map.putAll(CaricheLegacyMap.RUOLI_RAPPRESENTANTI_LOCALI);
		map.putAll(CaricheLegacyMap.RUOLI_EREDI);

		return new CaricaAgsDto()
				.setCodiceFiscale(rs.getString("CODICE_FISCALE"))
				.setCarica(map.get(rs.getString("RUOLO")))
				.setCuaa(rs.getString("CUAA"))
				.setDenominazione(rs.getString("COGNOME") + " " + rs.getString("NOME"))
				.setNome(rs.getString("NOME"))
				.setCognome(rs.getString("COGNOME"));
	}

}
