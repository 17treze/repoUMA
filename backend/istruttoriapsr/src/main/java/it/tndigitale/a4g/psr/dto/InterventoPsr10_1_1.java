package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class InterventoPsr10_1_1 {
  private String title = "";
  private BigDecimal caricoBestiame = BigDecimal.ZERO;
  private BigDecimal superficieRichiesta = BigDecimal.ZERO;
  private BigDecimal superficieAmmissibile = BigDecimal.ZERO;
  private BigDecimal percentualeScostamento = BigDecimal.ZERO;
  private BigDecimal aliquotaSostegno = BigDecimal.ZERO;
  private BigDecimal importoAmmissibile = BigDecimal.ZERO;
  private BigDecimal importoSanzioneSovradichiarazione = BigDecimal.ZERO;
  private BigDecimal importoCalcolatoIntervento = BigDecimal.ZERO;
  private BigDecimal caricoBestiameAccertatoControllo;

  public InterventoPsr10_1_1(String title, BigDecimal caricoBestiame,
                             BigDecimal superficieRichiesta,
                             BigDecimal superficieAmmissibile,
                             BigDecimal percentualeScostamento,
                             BigDecimal aliquotaSostegno,
                             BigDecimal importoAmmissibile,
                             BigDecimal importoSanzioneSovradichiarazione,
                             BigDecimal importoCalcolatoIntervento) {
    this.title = title;
    this.caricoBestiame = caricoBestiame;
    this.superficieRichiesta = superficieRichiesta;
    this.superficieAmmissibile = superficieAmmissibile;
    this.percentualeScostamento = percentualeScostamento;
    this.aliquotaSostegno = aliquotaSostegno;
    this.importoAmmissibile = importoAmmissibile;
    this.importoSanzioneSovradichiarazione = importoSanzioneSovradichiarazione;
    this.importoCalcolatoIntervento = importoCalcolatoIntervento;
  }

  public InterventoPsr10_1_1(){

  }

  public BigDecimal getCaricoBestiame() {
    return caricoBestiame;
  }

  public BigDecimal getSuperficieRichiesta() {
    return superficieRichiesta;
  }

  public BigDecimal getSuperficieAmmissibile() {
    return superficieAmmissibile;
  }

  public BigDecimal getPercentualeScostamento() {
    return percentualeScostamento;
  }

  public BigDecimal getAliquotaSostegno() {
    return aliquotaSostegno;
  }

  public BigDecimal getImportoAmmissibile() {
    return importoAmmissibile;
  }

  public BigDecimal getImportoSanzioneSovradichiarazione() {
    return importoSanzioneSovradichiarazione;
  }

  public BigDecimal getImportoCalcolatoIntervento() {
    return importoCalcolatoIntervento;
  }

  public String getTitle() {
    return title;
  }

  public void setCaricoBestiameAccertatoControllo(BigDecimal caricoBestiameAccertatoControllo) {
    this.caricoBestiameAccertatoControllo = caricoBestiameAccertatoControllo;
  }

  public BigDecimal getCaricoBestiameAccertatoControllo() {
    return caricoBestiameAccertatoControllo;
  }
}
