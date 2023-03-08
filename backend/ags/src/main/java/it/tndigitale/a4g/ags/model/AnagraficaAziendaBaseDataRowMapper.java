package it.tndigitale.a4g.ags.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaBaseData;


public class AnagraficaAziendaBaseDataRowMapper implements RowMapper<AnagraficaAziendaBaseData> {
    @Override
    public AnagraficaAziendaBaseData mapRow(ResultSet rs, int i) throws SQLException {
        return new AnagraficaAziendaBaseData(rs.getString("NATURA_GIURIDICA"),
                                             rs.getString("NOME"),
                                             rs.getString("COGNOME"),
                                             rs.getString("SESSO"),
                                             rs.getTimestamp("D_NASCITA"),
                                             rs.getString("COMUNE_NASCITA"),
                                             rs.getString("DENOMINAZIONE"),
                                             rs.getString("COMUNE_RESIDENZA"),
                                             rs.getString("PROVINCIA_RESIDENZA"),
                                             rs.getString("CAP_RESIDENZA"),
                                             rs.getString("INDIRIZZO_RESIDENZA"),
                                             rs.getString("COMUNE_RECAPITO"),
                                             rs.getString("PROVINCIA_RECAPITO"),
                                             rs.getString("CAP_RECAPITO"),
                                             rs.getString("INDIRIZZO_RECAPITO"),
                                             rs.getString("PART_IVA"),
                                             rs.getString("NUMERO_REA_CCIAA"),
                                             rs.getString("SCO_OP"),
                                             rs.getTimestamp("FASC_DT_INIZIO"),
                                             rs.getTimestamp("FASC_DT_FINE"),
                                             rs.getString("EMAIL_PEC"),
                                             rs.getString("sco_attivita_1"));
    }
}
