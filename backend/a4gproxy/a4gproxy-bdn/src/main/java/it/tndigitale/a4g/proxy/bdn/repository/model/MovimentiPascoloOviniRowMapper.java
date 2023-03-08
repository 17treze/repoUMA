package it.tndigitale.a4g.proxy.bdn.repository.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.MovimentazionePascoloOviniDto;
import org.springframework.jdbc.core.RowMapper;

public class MovimentiPascoloOviniRowMapper implements RowMapper<MovimentazionePascoloOviniDto> {

	public MovimentiPascoloOviniRowMapper() {
		super();
	}

	public MovimentazionePascoloOviniDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		MovimentazionePascoloOviniDto movimentazionePascoloOviniDto = new MovimentazionePascoloOviniDto();
		movimentazionePascoloOviniDto.setIdAllevamento(rs.getBigDecimal("ID_ALLE"));
		movimentazionePascoloOviniDto.setAnnoCampagna(rs.getBigDecimal("NUME_CAMP"));
		movimentazionePascoloOviniDto.setNumeroCapi(rs.getBigDecimal("NUM_CAPI"));
		movimentazionePascoloOviniDto.setCodiceFiscaleDetentore(rs.getString("CODI_FISC_DETE"));
		movimentazionePascoloOviniDto.setCodPascolo(rs.getString("CODI_PASC"));
		movimentazionePascoloOviniDto.setComunePascolo(rs.getString("DESC_COMU_PASC"));
		movimentazionePascoloOviniDto.setIdPascolo(rs.getBigDecimal("ID_PASC"));
		movimentazionePascoloOviniDto.setDataIngresso(rs.getDate("DATA_INGRESSO"));
		movimentazionePascoloOviniDto.setDataUscita(rs.getDate("DATA_USCITA"));
		movimentazionePascoloOviniDto.setGiorniPascolamento(rs.getLong("GG_PASC"));

		return movimentazionePascoloOviniDto;
	}

}
