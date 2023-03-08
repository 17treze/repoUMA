package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.psr.dto.UbaAlpeggiatePsr;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UbaAlpeggiatePSRSuperficieRowMapper implements RowMapper<UbaAlpeggiatePsr> {
    @Override
    public UbaAlpeggiatePsr mapRow(ResultSet rs, int i) throws SQLException {
        UbaAlpeggiatePsr ubaAlpeggiatePSR = new UbaAlpeggiatePsr();
        ubaAlpeggiatePSR.setIdDomanda(rs.getLong("ID_DOMANDA"));
        ubaAlpeggiatePSR.setIdModulo(rs.getLong("ID_MODULO"));
        ubaAlpeggiatePSR.setDataPres(LocalDateConverter.fromDate(rs.getDate("DATAPRES")));
        ubaAlpeggiatePSR.setCodDestinazione(rs.getString("COD_DESTINAZIONE"));
        ubaAlpeggiatePSR.setDsDestinazione(rs.getString("DS_DESTINAZIONE"));
        ubaAlpeggiatePSR.setUbaAlpeggiate(rs.getLong("UBA_ALPEGGIATE"));
        ubaAlpeggiatePSR.setSupUbaAlpeggiate(rs.getLong("SUPERFICIE_UBA_ALPEGGIATE"));
        return ubaAlpeggiatePSR;
    }
}
