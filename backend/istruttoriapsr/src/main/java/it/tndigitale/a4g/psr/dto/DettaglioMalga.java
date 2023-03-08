package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class DettaglioMalga {

  private String codiceAlpeggio;
  private BigDecimal caricoDiBestiameDellaMalga;
  private BigDecimal superficieRichiesta;
  private BigDecimal superficieAmmissibile;
  private BigDecimal percentualeDiScostamento;
  private BigDecimal aliquotaDiSostegno;
  private BigDecimal importoAmmissibile;
  private BigDecimal importoDellaSanzionePerSovradichiarazione;
  private BigDecimal importoCalcolatoMalga;
  private final BigDecimal percentualeRiduzioneMancatoRispettoImpegniMalga;
  private final BigDecimal importoRiduzioneMancatoRispettoImpegniMalga;

  public DettaglioMalga(String codiceAlpeggio,
                        BigDecimal caricoDiBestiameDellaMalga,
                        BigDecimal superficieRichiesta,
                        BigDecimal superficieAmmissibile,
                        BigDecimal percentualeDiScostamento,
                        BigDecimal aliquotaDiSostegno,
                        BigDecimal importoAmmissibile,
                        BigDecimal importoDellaSanzionePerSovradichiarazione,
                        BigDecimal importoCalcolatoMalga,
                        BigDecimal percentualeRiduzioneMancatoRispettoImpegniMalga,
                        BigDecimal importoRiduzioneMancatoRispettoImpegniMalga) {
    this.codiceAlpeggio = codiceAlpeggio;
    this.caricoDiBestiameDellaMalga = caricoDiBestiameDellaMalga;
    this.superficieRichiesta = superficieRichiesta;
    this.superficieAmmissibile = superficieAmmissibile;
    this.percentualeDiScostamento = percentualeDiScostamento;
    this.aliquotaDiSostegno = aliquotaDiSostegno;
    this.importoAmmissibile = importoAmmissibile;
    this.importoDellaSanzionePerSovradichiarazione = importoDellaSanzionePerSovradichiarazione;
    this.importoCalcolatoMalga = importoCalcolatoMalga;
    this.percentualeRiduzioneMancatoRispettoImpegniMalga = percentualeRiduzioneMancatoRispettoImpegniMalga;
    this.importoRiduzioneMancatoRispettoImpegniMalga = importoRiduzioneMancatoRispettoImpegniMalga;
  }

  public String getCodiceAlpeggio() {
    return codiceAlpeggio;
  }

  public BigDecimal getCaricoDiBestiameDellaMalga() {
    return caricoDiBestiameDellaMalga;
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

  public BigDecimal getAliquotaDiSostegno() {
    return aliquotaDiSostegno;
  }

  public BigDecimal getImportoAmmissibile() {
    return importoAmmissibile;
  }

  public BigDecimal getImportoDellaSanzionePerSovradichiarazione() {
    return importoDellaSanzionePerSovradichiarazione;
  }

  public BigDecimal getImportoCalcolatoMalga() {
    return importoCalcolatoMalga;
  }

  public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniMalga() {
    return percentualeRiduzioneMancatoRispettoImpegniMalga;
  }

  public BigDecimal getImportoRiduzioneMancatoRispettoImpegniMalga() {
    return importoRiduzioneMancatoRispettoImpegniMalga;
  }
}
