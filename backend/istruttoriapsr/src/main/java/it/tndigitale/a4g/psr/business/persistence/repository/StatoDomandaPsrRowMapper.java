package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.psr.dto.StatoDomandaPsr;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatoDomandaPsrRowMapper implements RowMapper<StatoDomandaPsr> {
    @Override
    public StatoDomandaPsr mapRow(ResultSet rs, int i) throws SQLException {
        StatoDomandaPsr statoDomandaPsr = new StatoDomandaPsr();
        statoDomandaPsr.setIdDomanda(rs.getLong("ID_DOMANDA"));
        statoDomandaPsr.setCodOperazione(rs.getString("COD_OPERAZIONE"));
        statoDomandaPsr.setInterventoPat(rs.getString("INTERVENTO_PAT"));
        statoDomandaPsr.setDataUltimoMovimento((LocalDateConverter.fromDate(rs.getDate("DATA_ULTIMO_MOVIMENTO"))));
        statoDomandaPsr.setTipoPagamento(rs.getString("TIPO_PAGAMENTO"));
        statoDomandaPsr.setStato(rs.getString("STATO"));
        statoDomandaPsr.setCampione(rs.getBoolean("DOMANDA_CAMPIONE"));
        return statoDomandaPsr;
    }
}