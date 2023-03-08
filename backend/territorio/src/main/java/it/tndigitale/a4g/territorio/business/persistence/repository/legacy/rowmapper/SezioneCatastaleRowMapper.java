package it.tndigitale.a4g.territorio.business.persistence.repository.legacy.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.ComuneAmministrativoDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.ProvinciaDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.SezioneCatastaleDto;

public class SezioneCatastaleRowMapper implements RowMapper<SezioneCatastaleDto> {

	@Override
	public SezioneCatastaleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new SezioneCatastaleDto()
				.setCodice(rs.getString("codice"))
				.setDenominazione(rs.getString("denominazione"))
				.setComune(new ComuneAmministrativoDto()
						.setCodiceFiscale(rs.getString("codiceFiscaleCA"))
						.setCodiceIstat(rs.getString("codiceIstatCA"))
						.setDenominazione(rs.getString("denominazioneCA"))
						.setProvincia(new ProvinciaDto()
								.setCodiceIstat(rs.getString("codiceIstatProv"))
								.setSigla(rs.getString("siglaProv"))
								.setDenominazione(rs.getString("denominazioneProv"))));
	}

}
