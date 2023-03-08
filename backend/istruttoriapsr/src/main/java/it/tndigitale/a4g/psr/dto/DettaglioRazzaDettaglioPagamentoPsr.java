package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class DettaglioRazzaDettaglioPagamentoPsr {

  private String nome;
  private BigDecimal caricoBestiame;
  private final BigDecimal caricoBestiameAccertatoControlloInLoco;
  private BigDecimal animaliRichiesti;
  private BigDecimal animaliAmmissibili;
  private BigDecimal percentualeScostamento;
  private BigDecimal aliquotaSostegno;
  private BigDecimal importoAmmissibile;
  private BigDecimal importoSanzioneSovradichiarazione;
  private BigDecimal importoCalcolatoIntervento;

  public DettaglioRazzaDettaglioPagamentoPsr(String nome, BigDecimal caricoBestiame, BigDecimal caricoBestiameAccertatoControlloInLoco, BigDecimal animaliRichiesti,
                                             BigDecimal animaliAmmissibili, BigDecimal percentualeScostamento,
                                             BigDecimal aliquotaSostegno, BigDecimal importoAmmissibile,
                                             BigDecimal importoSanzioneSovradichiarazione, BigDecimal importoCalcolatoIntervento) {
    this.nome = nome;
    this.caricoBestiame = caricoBestiame;
    this.caricoBestiameAccertatoControlloInLoco = caricoBestiameAccertatoControlloInLoco;
    this.animaliRichiesti = animaliRichiesti;
    this.animaliAmmissibili = animaliAmmissibili;
    this.percentualeScostamento = percentualeScostamento;
    this.aliquotaSostegno = aliquotaSostegno;
    this.importoAmmissibile = importoAmmissibile;
    this.importoSanzioneSovradichiarazione = importoSanzioneSovradichiarazione;
    this.importoCalcolatoIntervento = importoCalcolatoIntervento;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public BigDecimal getCaricoBestiame() {
    return caricoBestiame;
  }

  public void setCaricoBestiame(BigDecimal caricoBestiame) {
    this.caricoBestiame = caricoBestiame;
  }

  public BigDecimal getAnimaliRichiesti() {
    return animaliRichiesti;
  }

  public void setAnimaliRichiesti(BigDecimal animaliRichiesti) {
    this.animaliRichiesti = animaliRichiesti;
  }

  public BigDecimal getAnimaliAmmissibili() {
    return animaliAmmissibili;
  }

  public void setAnimaliAmmissibili(BigDecimal animaliAmmissibili) {
    this.animaliAmmissibili = animaliAmmissibili;
  }

  public BigDecimal getPercentualeScostamento() {
    return percentualeScostamento;
  }

  public void setPercentualeScostamento(BigDecimal percentualeScostamento) {
    this.percentualeScostamento = percentualeScostamento;
  }

  public BigDecimal getAliquotaSostegno() {
    return aliquotaSostegno;
  }

  public void setAliquotaSostegno(BigDecimal aliquotaSostegno) {
    this.aliquotaSostegno = aliquotaSostegno;
  }

  public BigDecimal getImportoAmmissibile() {
    return importoAmmissibile;
  }

  public void setImportoAmmissibile(BigDecimal importoAmmissibile) {
    this.importoAmmissibile = importoAmmissibile;
  }

  public BigDecimal getImportoSanzioneSovradichiarazione() {
    return importoSanzioneSovradichiarazione;
  }

  public void setImportoSanzioneSovradichiarazione(BigDecimal importoSanzioneSovradichiarazione) {
    this.importoSanzioneSovradichiarazione = importoSanzioneSovradichiarazione;
  }

  public BigDecimal getImportoCalcolatoIntervento() {
    return importoCalcolatoIntervento;
  }

  public void setImportoCalcolatoIntervento(BigDecimal importoCalcolatoIntervento) {
    this.importoCalcolatoIntervento = importoCalcolatoIntervento;
  }

  public BigDecimal getCaricoBestiameAccertatoControlloInLoco() {
    return caricoBestiameAccertatoControlloInLoco;
  }
}
