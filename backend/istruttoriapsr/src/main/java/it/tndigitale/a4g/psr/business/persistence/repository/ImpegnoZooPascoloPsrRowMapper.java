package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.psr.dto.ImpegnoZooPascoloPsr;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImpegnoZooPascoloPsrRowMapper implements RowMapper<ImpegnoZooPascoloPsr> {
    @Override
    public ImpegnoZooPascoloPsr mapRow(ResultSet rs, int i) throws SQLException {
        ImpegnoZooPascoloPsr pascoloPsr = new ImpegnoZooPascoloPsr();
        pascoloPsr.setIdDomanda(rs.getLong("ID_DOMANDA"));
        pascoloPsr.setIdModulo(rs.getLong("ID_MODULO"));
        pascoloPsr.setDataPresentazione((LocalDateConverter.fromDate(rs.getDate("DATAPRES"))));
        pascoloPsr.setCodAzione(rs.getString("COD_AZIONE"));
        pascoloPsr.setDeAzione(rs.getString("DE_AZIONE"));
        pascoloPsr.setQuantitaImp(rs.getLong("QUANTITA_IMP"));
        return pascoloPsr;
    }
}
