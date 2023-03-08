package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.MovimentoValidazioneFascicoloAgsDto;

public class MovimentoValidazioneFascicoloAgsRowMapper implements RowMapper<MovimentoValidazioneFascicoloAgsDto> {

	@Override
	public MovimentoValidazioneFascicoloAgsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocalDateTime dataValidazione = null;
		if (rs.getTimestamp("data_ultima_validazione_succ") != null) {
			dataValidazione = rs.getTimestamp("data_ultima_validazione_succ").toLocalDateTime();
		}
		return new MovimentoValidazioneFascicoloAgsDto(
				rs.getLong("id_fascicolo"),
				rs.getString("cuaa"),
				rs.getString("denominazione"),
				rs.getInt("validazioni_effettuate_succ"),
				dataValidazione
				);
	}

}
