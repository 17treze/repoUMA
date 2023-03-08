package it.tndigitale.a4g.psr.business.persistence.repository;

import it.tndigitale.a4g.psr.dto.DettaglioPagametoPsrRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DettaglioPagamentoPsrRowMapper implements RowMapper<DettaglioPagametoPsrRow>  {
  @Override
  public DettaglioPagametoPsrRow mapRow(ResultSet rs, int rowNum) throws SQLException {
    DettaglioPagametoPsrRow dettaglioPagametoPsrRow = new DettaglioPagametoPsrRow();
    dettaglioPagametoPsrRow.setVariabile(rs.getString("variabile"));
    dettaglioPagametoPsrRow.setValore(rs.getString("valore"));
    return dettaglioPagametoPsrRow;
  }
}
