package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class EsitoFinaleDettaglioPagamentoPsr10_1_2 {
  private BigDecimal importoRiduzionePerRitardataPresentazione;
  private BigDecimal coefficienteRiduzionePerSuperamentoBudget;
  private BigDecimal importoCalcolato;
  private BigDecimal importoLiquidatoPrecedentemente;
  private BigDecimal importoCalcolatoLiquidato;
  private final BigDecimal premioAntiLiq;
  private final BigDecimal premioTotLiquidato;
  private final BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline;
  private final BigDecimal importoRiduzioneMancatoRispettoImpegniBaseline;

  public EsitoFinaleDettaglioPagamentoPsr10_1_2(BigDecimal importoRiduzionePerRitardataPresentazione, BigDecimal coefficienteRiduzionePerSuperamentoBudget, BigDecimal importoCalcolato, BigDecimal importoLiquidatoPrecedentemente, BigDecimal importoCalcolatoLiquidato, BigDecimal premioAntiLiq, BigDecimal premioTotLiquidato, BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline, BigDecimal importoRiduzioneMancatoRispettoImpegniBaseline) {
    this.importoRiduzionePerRitardataPresentazione = importoRiduzionePerRitardataPresentazione;
    this.coefficienteRiduzionePerSuperamentoBudget = coefficienteRiduzionePerSuperamentoBudget;
    this.importoCalcolato = importoCalcolato;
    this.importoLiquidatoPrecedentemente = importoLiquidatoPrecedentemente;
    this.importoCalcolatoLiquidato = importoCalcolatoLiquidato;
    this.premioAntiLiq = premioAntiLiq;
    this.premioTotLiquidato = premioTotLiquidato;
    this.percentualeRiduzioneMancatoRispettoImpegniBaseline = percentualeRiduzioneMancatoRispettoImpegniBaseline;
    this.importoRiduzioneMancatoRispettoImpegniBaseline = importoRiduzioneMancatoRispettoImpegniBaseline;
  }

  public BigDecimal getImportoRiduzionePerRitardataPresentazione() {
    return importoRiduzionePerRitardataPresentazione;
  }

  public BigDecimal getCoefficienteRiduzionePerSuperamentoBudget() {
    return coefficienteRiduzionePerSuperamentoBudget;
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

  public BigDecimal getPremioAntiLiq() {
    return premioAntiLiq;
  }

  public BigDecimal getPremioTotLiquidato() {
    return premioTotLiquidato;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniBaseline() {
    return percentualeRiduzioneMancatoRispettoImpegniBaseline;
  }

  public BigDecimal getImportoRiduzioneMancatoRispettoImpegniBaseline() {
    return importoRiduzioneMancatoRispettoImpegniBaseline;
  }
}
