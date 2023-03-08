package it.tndigitale.a4g.ags.model;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaEnteData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnagraficaAziendaEnteDataRowMapper implements RowMapper<AnagraficaAziendaEnteData> {
    @Override
    public AnagraficaAziendaEnteData mapRow(ResultSet rs, int i) throws SQLException {
        return new AnagraficaAziendaEnteData(rs.getString("DES_ENTE"),
                                             rs.getString("CODI_TRAM"),
                                             rs.getTimestamp("DATA_RICHIESTA"));
    }
}
