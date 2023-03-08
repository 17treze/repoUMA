package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class DatiAziendali {

    private final BigDecimal caricoDiBestiame;
    private final BigDecimal superficieForaggera;
    private final BigDecimal animaliInAlpeggio;
    private final String aziendaTransumante;
    private final BigDecimal coefficientePendenzaAltitudine;
    private final String presenzaSanzioniAnniPrecendenti;
    private BigDecimal caricoBestiameAccertatoControllo;
    private BigDecimal superficieForaggeraAccertataControllo;

    public DatiAziendali(BigDecimal caricoDiBestiame,
                         BigDecimal superficieForaggera,
                         BigDecimal animaliInAlpeggio,
                         String aziendaTransumante,
                         BigDecimal coefficientePendenzaAltitudine,
                         String presenzaSanzioniAnniPrecendenti) {


        this.caricoDiBestiame = caricoDiBestiame;
        this.superficieForaggera = superficieForaggera;
        this.animaliInAlpeggio = animaliInAlpeggio;
        this.aziendaTransumante = aziendaTransumante;
        this.coefficientePendenzaAltitudine = coefficientePendenzaAltitudine;
        this.presenzaSanzioniAnniPrecendenti = presenzaSanzioniAnniPrecendenti;
    }

    public BigDecimal getCaricoDiBestiame() {
        return caricoDiBestiame;
    }

    public BigDecimal getSuperficieForaggera() {
        return superficieForaggera;
    }

    public BigDecimal getAnimaliInAlpeggio() {
        return animaliInAlpeggio;
    }

    public String getAziendaTransumante() {
        return aziendaTransumante;
    }

    public BigDecimal getCoefficientePendenzaAltitudine() {
        return coefficientePendenzaAltitudine;
    }

    public String getPresenzaSanzioniAnniPrecendenti() {
        return presenzaSanzioniAnniPrecendenti;
    }

    public void setCaricoBestiameAccertatoControllo(BigDecimal caricoBestiameAccertatoControllo) {
        this.caricoBestiameAccertatoControllo = caricoBestiameAccertatoControllo;
    }

    public void setSuperficieForaggeraAccertataControllo(BigDecimal superficieForaggeraAccertataControllo) {
        this.superficieForaggeraAccertataControllo = superficieForaggeraAccertataControllo;
    }

    public BigDecimal getCaricoBestiameAccertatoControllo() {
        return caricoBestiameAccertatoControllo;
    }

    public BigDecimal getSuperficieForaggeraAccertataControllo() {
        return superficieForaggeraAccertataControllo;
    }
}
