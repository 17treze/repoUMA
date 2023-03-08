package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class RiduzioniControlloInLoco {
    private BigDecimal importoRiduzioneMancatoRispettoImpegniUba;
    private BigDecimal percentualeRiduzioneMancatoRispettoImpegniZootecnico;
    private BigDecimal importoRiduzioneMancatoRispettoImpegniZootecnico;

    public void setImportoRiduzioneMancatoRispettoImpegniUba(BigDecimal importoRiduzioneMancatoRispettoImpegniUba) {
        this.importoRiduzioneMancatoRispettoImpegniUba = importoRiduzioneMancatoRispettoImpegniUba;
    }

    public void setPercentualeRiduzioneMancatoRispettoImpegniZootecnico(BigDecimal percentualeRiduzioneMancatoRispettoImpegniZootecnico) {
        this.percentualeRiduzioneMancatoRispettoImpegniZootecnico = percentualeRiduzioneMancatoRispettoImpegniZootecnico;
    }

    public void setImportoRiduzioneMancatoRispettoImpegniZootecnico(BigDecimal importoRiduzioneMancatoRispettoImpegniZootecnico) {
        this.importoRiduzioneMancatoRispettoImpegniZootecnico = importoRiduzioneMancatoRispettoImpegniZootecnico;
    }

    public BigDecimal getImportoRiduzioneMancatoRispettoImpegniUba() {
        return importoRiduzioneMancatoRispettoImpegniUba;
    }

    public BigDecimal getPercentualeRiduzioneMancatoRispettoImpegniZootecnico() {
        return percentualeRiduzioneMancatoRispettoImpegniZootecnico;
    }

    public BigDecimal getImportoRiduzioneMancatoRispettoImpegniZootecnico() {
        return importoRiduzioneMancatoRispettoImpegniZootecnico;
    }
}
