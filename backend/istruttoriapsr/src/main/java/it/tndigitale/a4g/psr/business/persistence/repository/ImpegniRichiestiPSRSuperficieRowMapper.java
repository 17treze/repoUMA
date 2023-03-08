package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.psr.dto.ImpegnoRichiestoPSRSuperficie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImpegniRichiestiPSRSuperficieRowMapper  implements RowMapper<ImpegnoRichiestoPSRSuperficie> {
    @Override
    public ImpegnoRichiestoPSRSuperficie mapRow(ResultSet rs, int i) throws SQLException {
        ImpegnoRichiestoPSRSuperficie impegnoRichiestoPSRSuperficie = new ImpegnoRichiestoPSRSuperficie();
        impegnoRichiestoPSRSuperficie.setIdDomanda(rs.getLong("ID_DOMANDA"));
        impegnoRichiestoPSRSuperficie.setIdModulo(rs.getLong("ID_MODULO"));
        impegnoRichiestoPSRSuperficie.setDataPres(LocalDateConverter.fromDate(rs.getDate("DATAPRES")));
        impegnoRichiestoPSRSuperficie.setGrafica(rs.getLong("GRAFICA"));
        impegnoRichiestoPSRSuperficie.setCodDestinazione(rs.getString("COD_DESTINAZIONE"));
        impegnoRichiestoPSRSuperficie.setDsDestinazione(rs.getString("DS_DESTINAZIONE"));
        impegnoRichiestoPSRSuperficie.setSupImpegno(rs.getLong("SUP_IMPEGNO"));
        impegnoRichiestoPSRSuperficie.setSupImpegnoNetta(rs.getLong("SUP_IMPEGNO_NETTA"));
        return impegnoRichiestoPSRSuperficie;
    }
}
