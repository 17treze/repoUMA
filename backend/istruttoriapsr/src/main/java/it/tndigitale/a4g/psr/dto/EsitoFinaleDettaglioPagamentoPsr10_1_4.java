package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class EsitoFinaleDettaglioPagamentoPsr10_1_4 implements DettaglioPagamentoPsr{
  private BigDecimal superficieRichiesta = BigDecimal.ZERO;
  private BigDecimal superficieAmmissibile = BigDecimal.ZERO;
  private BigDecimal percentualeDiScostamento = BigDecimal.ZERO;
  private BigDecimal aliquotaSostegno = BigDecimal.ZERO;
  private BigDecimal importoAmmissibile = BigDecimal.ZERO;
  private BigDecimal importoSanzione = BigDecimal.ZERO;
  private BigDecimal importoRiduzionePerRitardataPresentazione = BigDecimal.ZERO;
  private BigDecimal coefficienteDiRiduzionePerSuperamentoBudget = BigDecimal.ZERO;
  private BigDecimal importoCalcolato = BigDecimal.ZERO;
  private BigDecimal importoLiquidatoPrecedentemente;
  private BigDecimal importoCalcolatoLiquidato;
  private BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici;
  private BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline;
  private BigDecimal importoRiduzioneMancatoRispettoImpegni;

  public EsitoFinaleDettaglioPagamentoPsr10_1_4(BigDecimal superficieRichiesta,
                                                BigDecimal superficieAmmissibile,
                                                BigDecimal percentualeDiScostamento,
                                                BigDecimal aliquotaSostegno,
                                                BigDecimal importoAmmissibile,
                                                BigDecimal importoSanzione,
                                                BigDecimal importoRiduzionePerRitardataPresentazione,
                                                BigDecimal coefficienteDiRiduzionePerSuperamentoBudget,
                                                BigDecimal importoCalcolato,
                                                BigDecimal importoLiquidatoPrecedentemente,
                                                BigDecimal importoCalcolatoLiquidato,
                                                BigDecimal percentualeRiduzioneMancatoRispettoImpegniSpecifici,
                                                BigDecimal percentualeRiduzioneMancatoRispettoImpegniBaseline,
                                                BigDecimal importoRiduzioneMancatoRispettoImpegni) {
    this.superficieRichiesta = superficieRichiesta;
    this.superficieAmmissibile = superficieAmmissibile;
    this.percentualeDiScostamento = percentualeDiScostamento;
    this.aliquotaSostegno = aliquotaSostegno;
    this.importoAmmissibile = importoAmmissibile;
    this.importoSanzione = importoSanzione;
    this.importoRiduzionePerRitardataPresentazione = importoRiduzionePerRitardataPresentazione;
    this.coefficienteDiRiduzionePerSuperamentoBudget = coefficienteDiRiduzionePerSuperamentoBudget;
    this.importoCalcolato = importoCalcolato;
    this.importoLiquidatoPrecedentemente = importoLiquidatoPrecedentemente;
    this.importoCalcolatoLiquidato = importoCalcolatoLiquidato;
    this.percentualeRiduzioneMancatoRispettoImpegniSpecifici = percentualeRiduzioneMancatoRispettoImpegniSpecifici;
    this.percentualeRiduzioneMancatoRispettoImpegniBaseline = percentualeRiduzioneMancatoRispettoImpegniBaseline;
    this.importoRiduzioneMancatoRispettoImpegni = importoRiduzioneMancatoRispettoImpegni;
  }

  public EsitoFinaleDettaglioPagamentoPsr10_1_4() {

  }

  public BigDecimal getSuperficieRichiesta() {
    return superficieRichiesta;
  }

  public BigDecimal getSuperficieAmmissibile() {
    return superficieAmmissibile;
  }

  public BigDecimal getPercentualeDiScostamento() {
    return percentualeDiScostamento;
  }

  public BigDecimal getAliquotaSostegno() {
    return aliquotaSostegno;
  }

  public BigDecimal getImportoAmmissibile() {
    return importoAmmissibile;
  }

  public BigDecimal getImportoSanzione() {
    return importoSanzione;
  }

  public BigDecimal getImportoRiduzionePerRitardataPresentazione() {
    return importoRiduzionePerRitardataPresentazione;
  }

  public BigDecimal getCoefficienteDiRiduzionePerSuperamentoBudget() {
    return coefficienteDiRiduzionePerSuperamentoBudget;
  }

  public BigDecimal getImportoCalcolato() {
    return importoCalcolato;
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
