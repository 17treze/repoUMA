package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;

public class InformazioniQualitaColturaDto {
    protected BigInteger superficie;
    protected TipologiaQualitaColtura qualita;
    protected BigInteger classe;

    public BigInteger getSuperficie() {
        return superficie;
    }

    public void setSuperficie(BigInteger superficie) {
        this.superficie = superficie;
    }

    public TipologiaQualitaColtura getQualita() {
        return qualita;
    }

    public void setQualita(TipologiaQualitaColtura qualita) {
        this.qualita = qualita;
    }

    public BigInteger getClasse() {
        return classe;
    }

    public void setClasse(BigInteger classe) {
        this.classe = classe;
    }
}
