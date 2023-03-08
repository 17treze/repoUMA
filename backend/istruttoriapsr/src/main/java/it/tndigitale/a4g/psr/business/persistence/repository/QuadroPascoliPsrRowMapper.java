package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.psr.dto.PascoloPsr;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuadroPascoliPsrRowMapper implements RowMapper<PascoloPsr> {
    @Override
    public PascoloPsr mapRow(ResultSet rs, int i) throws SQLException {
        PascoloPsr pascoloPsr = new PascoloPsr();
        pascoloPsr.setIdDomanda(rs.getLong("ID_DOMANDA"));
        pascoloPsr.setCuaa(rs.getString("CUAA"));
        pascoloPsr.setRagioneSociale(rs.getString("RAGI_SOCI"));
        pascoloPsr.setCodPascolo(rs.getString("COD_PASCOLO"));
        pascoloPsr.setDenominazione(rs.getString("DENOMINAZIONE"));
        pascoloPsr.setUba(rs.getLong("UBA"));
        return pascoloPsr;
    }
}
