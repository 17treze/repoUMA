package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;

public class DatiClassamentoDto {
    protected TipologiaCategoria categoria;
    protected BigInteger classe;
    protected Double consistenza;

    public TipologiaCategoria getCategoria() {
        return categoria;
    }

    public DatiClassamentoDto setCategoria(TipologiaCategoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public BigInteger getClasse() {
        return classe;
    }

    public DatiClassamentoDto setClasse(BigInteger classe) {
        this.classe = classe;
        return this;
    }

    public Double getConsistenza() {
        return consistenza;
    }

    public DatiClassamentoDto setConsistenza(Double consistenza) {
        this.consistenza = consistenza;
        return this;
    }
}
