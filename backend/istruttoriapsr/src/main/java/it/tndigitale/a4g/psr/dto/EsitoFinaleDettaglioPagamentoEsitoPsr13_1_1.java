package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1 {
    private final BigDecimal importoRiduzionePerRitardataPresentazione;
    private final BigDecimal coefficienteRiduzionePerSuperamentoBudget;
    private final BigDecimal coefficienteRiduzionePerSuperamentoLimiteEttaro;
    private final BigDecimal importoCalcolato;
    private final BigDecimal importoLiquidatoPrecedentemente;
    private final BigDecimal importoTotaleGiaLiquidato;
    private final BigDecimal premioantliq;
    private final BigDecimal premiototliquidato;

    public EsitoFinaleDettaglioPagamentoEsitoPsr13_1_1(BigDecimal importoRiduzionePerRitardataPresentazione,
                                                       BigDecimal coefficienteRiduzionePerSuperamentoBudget,
                                                       BigDecimal coefficienteRiduzionePerSuperamentoLimiteEttaro,
                                                       BigDecimal importoCalcolato,
                                                       BigDecimal importoLiquidatoPrecedentemente,
                                                       BigDecimal importoTotaleGiaLiquidato,
                                                       BigDecimal premioantliq,
                                                       BigDecimal premiototliquidato) {
        this.importoRiduzionePerRitardataPresentazione = importoRiduzionePerRitardataPresentazione;
        this.coefficienteRiduzionePerSuperamentoBudget = coefficienteRiduzionePerSuperamentoBudget;
        this.coefficienteRiduzionePerSuperamentoLimiteEttaro = coefficienteRiduzionePerSuperamentoLimiteEttaro;
        this.importoCalcolato = importoCalcolato;
        this.importoLiquidatoPrecedentemente = importoLiquidatoPrecedentemente;
        this.importoTotaleGiaLiquidato = importoTotaleGiaLiquidato;
        this.premioantliq = premioantliq;
        this.premiototliquidato = premiototliquidato;
    }

    public BigDecimal getImportoRiduzionePerRitardataPresentazione() {
        return importoRiduzionePerRitardataPresentazione;
    }

    public BigDecimal getCoefficienteRiduzionePerSuperamentoBudget() {
        return coefficienteRiduzionePerSuperamentoBudget;
    }

    public BigDecimal getCoefficienteRiduzionePerSuperamentoLimiteEttaro() {
        return coefficienteRiduzionePerSuperamentoLimiteEttaro;
    }

    public BigDecimal getImportoCalcolato() {
        return importoCalcolato;
    }

    public BigDecimal getImportoLiquidatoPrecedentemente() {
        return importoLiquidatoPrecedentemente;
    }

    public BigDecimal getImportoTotaleGiaLiquidato() {
        return importoTotaleGiaLiquidato;
    }

    public BigDecimal getPremioantliq() {
        return premioantliq;
    }

    public BigDecimal getPremiototliquidato() {
        return premiototliquidato;
    }
}
