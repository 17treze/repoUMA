package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class DettaglioPagametoPsrRow {
  private String variabile;
  private String valore;

  public String getVariabile() {
    return variabile;
  }

  public void setVariabile(String variabile) {
    this.variabile = variabile;
  }

  public BigDecimal getValoreAsBigDecimal() {
    return valore == null ? null : new BigDecimal(valore);
  }

  public String getValoreAString() {
    return valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }
}
