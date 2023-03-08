package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class EsitoFinaleDettaglioPagamentoPsr10_1_3 {
  private BigDecimal importoRiduzionePerRitardataPresentazione;
  private BigDecimal coefficienteRiduzionePerSuperamentoBudget;
  private BigDecimal importoCalcolato;
  private final BigDecimal importoLiquidatoPrecedentemente;
  private final BigDecimal importoCalcolatoLiquidato;
  private final BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici;
  private final BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline;
  private final BigDecimal importoRiduzioneMancatoRispettoImpegni;

  public EsitoFinaleDettaglioPagamentoPsr10_1_3(BigDecimal importoRiduzionePerRitardataPresentazione, BigDecimal coefficienteRiduzionePerSuperamentoBudget, BigDecimal importoCalcolato, BigDecimal importoLiquidatoPrecedentemente, BigDecimal importoCalcolatoLiquidato, BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici, BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline, BigDecimal importoRiduzioneMancatoRispettoImpegni) {
    this.importoRiduzionePerRitardataPresentazione = importoRiduzionePerRitardataPresentazione;
    this.coefficienteRiduzionePerSuperamentoBudget = coefficienteRiduzionePerSuperamentoBudget;
    this.importoCalcolato = importoCalcolato;
    this.importoLiquidatoPrecedentemente = importoLiquidatoPrecedentemente;
    this.importoCalcolatoLiquidato = importoCalcolatoLiquidato;
    this.percentualeRiduzioneMancatoRispettoImpegniSpecifici = percentualeRiduzioneMancatoRispettoImpegniSpecifici;
    this.percentualeRiduzioneMancatoRispettoImpegniBaseline = percentualeRiduzioneMancatoRispettoImpegniBaseline;
    this.importoRiduzioneMancatoRispettoImpegni = importoRiduzioneMancatoRispettoImpegni;
  }

  public BigDecimal getImportoRiduzionePerRitardataPresentazione() {
    return importoRiduzionePerRitardataPresentazione;
  }

  public void setImportoRiduzionePerRitardataPresentazione(BigDecimal importoRiduzionePerRitardataPresentazione) {
    this.importoRiduzionePerRitardataPresentazione = importoRiduzionePerRitardataPresentazione;
  }

  public BigDecimal getCoefficienteRiduzionePerSuperamentoBudget() {
    return coefficienteRiduzionePerSuperamentoBudget;
  }

  public void setCoefficienteRiduzionePerSuperamentoBudget(BigDecimal coefficienteRiduzionePerSuperamentoBudget) {
    this.coefficienteRiduzionePerSuperamentoBudget = coefficienteRiduzionePerSuperamentoBudget;
  }

  public BigDecimal getImportoCalcolato() {
    return importoCalcolato;
  }

  public void setImportoCalcolato(BigDecimal importoCalcolato) {
    this.importoCalcolato = importoCalcolato;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniSpecifici() {
    return percentualeRiduzioneMancatoRispettoImpegniSpecifici;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniBaseline() {
    return percentualeRiduzioneMancatoRispettoImpegniBaseline;
  }

  public BigDecimal getImportoRiduzioneMancatoRispettoImpegni() {
    return importoRiduzioneMancatoRispettoImpegni;
  }

  public BigDecimal getImportoLiquidatoPrecedentemente() {
    return importoLiquidatoPrecedentemente;
  }

  public BigDecimal getImportoCalcolatoLiquidato() {
    return importoCalcolatoLiquidato;
  }
}
