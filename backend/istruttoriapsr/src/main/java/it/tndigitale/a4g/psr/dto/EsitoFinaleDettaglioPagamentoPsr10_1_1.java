package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class EsitoFinaleDettaglioPagamentoPsr10_1_1 {
  private BigDecimal importoRiduzioneMancatoRispettoImpegni = BigDecimal.ZERO;
  private BigDecimal importoRiduzioneRitardataPresentazione = BigDecimal.ZERO;
  private BigDecimal coefficienteRiduzioneSuperamentoBudget = BigDecimal.ZERO;
  private BigDecimal importoCalcolato = BigDecimal.ZERO;
  private BigDecimal importoLiquidatoPrecedentemente;
  private BigDecimal importoCalcolatoLiquidato;
  private BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici;
  private BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline;

  public EsitoFinaleDettaglioPagamentoPsr10_1_1(BigDecimal importoRiduzioneMancatoRispettoImpegni, BigDecimal importoRiduzioneRitardataPresentazione, BigDecimal coefficienteRiduzioneSuperamentoBudget, BigDecimal importoCalcolato, BigDecimal importoLiquidatoPrecedentemente, BigDecimal importoCalcolatoLiquidato, BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici, BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline) {

    this.importoRiduzioneMancatoRispettoImpegni = importoRiduzioneMancatoRispettoImpegni;
    this.importoRiduzioneRitardataPresentazione = importoRiduzioneRitardataPresentazione;
    this.coefficienteRiduzioneSuperamentoBudget = coefficienteRiduzioneSuperamentoBudget;
    this.importoCalcolato = importoCalcolato;
    this.importoLiquidatoPrecedentemente = importoLiquidatoPrecedentemente;
    this.importoCalcolatoLiquidato = importoCalcolatoLiquidato;
    this.percentualeRiduzioneMancatoRispettoImpegniSpecifici = percentualeRiduzioneMancatoRispettoImpegniSpecifici;
    this.percentualeRiduzioneMancatoRispettoImpegniBaseline = percentualeRiduzioneMancatoRispettoImpegniBaseline;
  }

  public EsitoFinaleDettaglioPagamentoPsr10_1_1() {

  }

  public BigDecimal getImportoRiduzioneMancatoRispettoImpegni() {
    return importoRiduzioneMancatoRispettoImpegni;
  }

  public BigDecimal getImportoRiduzioneRitardataPresentazione() {
    return importoRiduzioneRitardataPresentazione;
  }

  public BigDecimal getCoefficienteRiduzioneSuperamentoBudget() {
    return coefficienteRiduzioneSuperamentoBudget;
  }

  public BigDecimal getImportoCalcolato() {
    return importoCalcolato;
  }

  public BigDecimal getImportoLiquidatoPrecedentemente() {
    return importoLiquidatoPrecedentemente;
  }

  public BigDecimal getImportoCalcolatoLiquidato() {
    return importoCalcolatoLiquidato;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniSpecifici() {
    return percentualeRiduzioneMancatoRispettoImpegniSpecifici;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniBaseline() {
    return percentualeRiduzioneMancatoRispettoImpegniBaseline;
  }
}
