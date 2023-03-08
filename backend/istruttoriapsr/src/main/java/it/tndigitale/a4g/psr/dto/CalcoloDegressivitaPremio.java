package it.tndigitale.a4g.psr.dto;

import java.math.BigDecimal;

public class CalcoloDegressivitaPremio {
    private final BigDecimal importoCalcolatoAziendale;
    private final BigDecimal superficieAziendaleOggettoDiContributo;
    private final BigDecimal importoCalcolatoAziendaleDopoDegressivita;

    public CalcoloDegressivitaPremio(BigDecimal importoCalcolatoAziendale, BigDecimal superficieAziendaleOggettoDiContributo, BigDecimal importoCalcolatoAziendaleDopoDegressivita) {
        this.importoCalcolatoAziendale = importoCalcolatoAziendale;
        this.superficieAziendaleOggettoDiContributo = superficieAziendaleOggettoDiContributo;
        this.importoCalcolatoAziendaleDopoDegressivita = importoCalcolatoAziendaleDopoDegressivita;
    }

    public BigDecimal getImportoCalcolatoAziendale() {
        return importoCalcolatoAziendale;
    }

    public BigDecimal getSuperficieAziendaleOggettoDiContributo() {
        return superficieAziendaleOggettoDiContributo;
    }

    public BigDecimal getImportoCalcolatoAziendaleDopoDegressivita() {
        return importoCalcolatoAziendaleDopoDegressivita;
    }
}
