package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class DettaglioSostegnoMantenimentoPsr {

    private BigDecimal superficieRichiesta;
    private BigDecimal superficieAmmissibile;
    private BigDecimal percentualeScostamento;
    private BigDecimal aliquotaSostegno;
    private BigDecimal importoAmmissibile;
    private BigDecimal importoSanzioneSovradichiarazione;
    private BigDecimal calcolatoIntervento;
    private String nomeSostegno;

    public DettaglioSostegnoMantenimentoPsr() {
    }

    public BigDecimal getSuperficieRichiesta() {
        return superficieRichiesta;
    }

    public void setSuperficieRichiesta(BigDecimal superficieRichiesta) {
        this.superficieRichiesta = superficieRichiesta;
    }

    public BigDecimal getSuperficieAmmissibile() {
        return superficieAmmissibile;
    }

    public void setSuperficieAmmissibile(BigDecimal superficieAmmissibile) {
        this.superficieAmmissibile = superficieAmmissibile;
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

    public BigDecimal getCalcolatoIntervento() {
        return calcolatoIntervento;
    }

    public void setCalcolatoIntervento(BigDecimal calcolatoIntervento) {
        this.calcolatoIntervento = calcolatoIntervento;
    }

    public String getNomeSostegno() {
        return nomeSostegno;
    }

    public void setNomeSostegno(String nomeSostegno) {
        this.nomeSostegno = nomeSostegno;
    }
}
