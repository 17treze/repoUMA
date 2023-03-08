package it.tndigitale.a4g.psr.business.persistence.repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DettaglioPagamentoCampioneDomandaPsr implements RowMapper<Boolean> {
    @Override
    public Boolean mapRow(ResultSet rs, int i) throws SQLException {
        return rs.getBoolean("DOMANDA_CAMPIONE");
    }
}