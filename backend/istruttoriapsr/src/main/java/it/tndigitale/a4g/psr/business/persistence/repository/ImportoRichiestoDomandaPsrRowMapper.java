package it.tndigitale.a4g.psr.business.persistence.repository;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportoRichiestoDomandaPsrRowMapper implements RowMapper<BigDecimal> {
    @Override
    public BigDecimal mapRow(ResultSet rs, int i) throws SQLException {
        return rs.getBigDecimal("IMPORTO_RICHIESTO");
    }
}