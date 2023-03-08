package it.tndigitale.a4g.ags.model;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaFascicoloData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnagraficaAziendaFascicoloDataRowMapper implements RowMapper<AnagraficaAziendaFascicoloData> {
    @Override
    public AnagraficaAziendaFascicoloData mapRow(ResultSet rs, int i) throws SQLException {
        return new AnagraficaAziendaFascicoloData(rs.getTimestamp("DT_MOVIMENTO"));
    }
}
