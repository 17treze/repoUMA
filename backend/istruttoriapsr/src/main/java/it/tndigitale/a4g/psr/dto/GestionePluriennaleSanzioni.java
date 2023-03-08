package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class GestionePluriennaleSanzioni {


    private final String presenzaSanzAnniPrec;
    private final String presenzaSanzioniAnnoCorrente;
    private final String presenzaRecidiva;
    private final BigDecimal sanzioneScontataPrimaInfrazione;
    private final BigDecimal sanzioneRecidiva;
    private final BigDecimal sanzioneComplessiva;

    public GestionePluriennaleSanzioni(String presenzaSanzAnniPrec,
                                       String presenzaSanzioniAnnoCorrente,
                                       String presenzaRecidiva,
                                       BigDecimal sanzioneScontataPrimaInfrazione,
                                       BigDecimal sanzioneRecidiva,
                                       BigDecimal sanzioneComplessiva) {
        this.presenzaSanzAnniPrec = presenzaSanzAnniPrec;
        this.presenzaSanzioniAnnoCorrente = presenzaSanzioniAnnoCorrente;
        this.presenzaRecidiva = presenzaRecidiva;
        this.sanzioneScontataPrimaInfrazione = sanzioneScontataPrimaInfrazione;
        this.sanzioneRecidiva = sanzioneRecidiva;
        this.sanzioneComplessiva = sanzioneComplessiva;
    }

    public String getPresenzaSanzAnniPrec() {
        return presenzaSanzAnniPrec;
    }

    public String getPresenzaSanzioniAnnoCorrente() {
        return presenzaSanzioniAnnoCorrente;
    }

    public String getPresenzaRecidiva() {
        return presenzaRecidiva;
    }

    public BigDecimal getSanzioneScontataPrimaInfrazione() {
        return sanzioneScontataPrimaInfrazione;
    }

    public BigDecimal getSanzioneRecidiva() {
        return sanzioneRecidiva;
    }

    public BigDecimal getSanzioneComplessiva() {
        return sanzioneComplessiva;
    }
}
