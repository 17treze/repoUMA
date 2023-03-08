package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;

public class PorzioneMaterialeDto extends ParticellaCatastaleDto {
    protected BigInteger numeroPorzione;

    public BigInteger getNumeroPorzione() {
        return numeroPorzione;
    }

    public void setNumeroPorzione(BigInteger numero) {
        this.numeroPorzione = numero;
    }
}
