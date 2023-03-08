package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;

public class PartitaTavolareDto {
    protected BigInteger comuneCatastale;
    protected TipologiaSezione sezione;
    protected BigInteger numero;

    public BigInteger getComuneCatastale() {
        return comuneCatastale;
    }

    public PartitaTavolareDto setComuneCatastale(BigInteger comuneCatastale) {
        this.comuneCatastale = comuneCatastale;
        return this;
    }

    public TipologiaSezione getSezione() {
        return sezione;
    }

    public PartitaTavolareDto setSezione(TipologiaSezione sezione) {
        this.sezione = sezione;
        return this;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public PartitaTavolareDto setNumero(BigInteger numero) {
        this.numero = numero;
        return this;
    }
}
