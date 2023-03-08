package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class SistemaAgricolo {

    private final String tableName;
    private final BigDecimal superficieRichiesta;
    private final BigDecimal superficieAmmissibile;
    private final BigDecimal percentualeScostamento;
    private final BigDecimal aliquotaSostegno;
    private final BigDecimal importoAmmissibile;
    private final BigDecimal importoSanzioneSovradichiarazione;
    private final BigDecimal importoCalcolatoIntervento;

    public SistemaAgricolo(String tableName, BigDecimal superficieRichiesta, BigDecimal superficieAmmissibile, BigDecimal percentualeScostamento, BigDecimal aliquotaSostegno, BigDecimal importoAmmissibile, BigDecimal importoSanzioneSovradichiarazione, BigDecimal importoCalcolatoIntervento) {

        this.tableName = tableName;
        this.superficieRichiesta = superficieRichiesta;
        this.superficieAmmissibile = superficieAmmissibile;
        this.percentualeScostamento = percentualeScostamento;
        this.aliquotaSostegno = aliquotaSostegno;
        this.importoAmmissibile = importoAmmissibile;
        this.importoSanzioneSovradichiarazione = importoSanzioneSovradichiarazione;
        this.importoCalcolatoIntervento = importoCalcolatoIntervento;
    }

    public String getTableName() {
        return tableName;
    }

    public BigDecimal getSuperficieRichiesta() {
        return superficieRichiesta;
    }

    public BigDecimal getSuperficieAmmissibile() {
        return superficieAmmissibile;
    }

    public BigDecimal getPercentualeScostamento() {
        return percentualeScostamento;
    }

    public BigDecimal getAliquotaSostegno() {
        return aliquotaSostegno;
    }

    public BigDecimal getImportoAmmissibile() {
        return importoAmmissibile;
    }

    public BigDecimal getImportoSanzioneSovradichiarazione() {
        return importoSanzioneSovradichiarazione;
    }

    public BigDecimal getImportoCalcolatoIntervento() {
        return importoCalcolatoIntervento;
    }
}
