package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;
import java.util.List;

public class InformazioniImmobileDettaglioDto {
    private BigInteger comuneCatastale;
    private String particella;
    private TipologiaParticellaCatastale tipologia;
    private String subalterno;
    private List<PorzioneMaterialeDto> porzioniMateriali;

    public BigInteger getComuneCatastale() {
        return comuneCatastale;
    }

    public void setComuneCatastale(BigInteger comuneCatastale) {
        this.comuneCatastale = comuneCatastale;
    }

    public String getParticella() {
        return particella;
    }

    public void setParticella(String particella) {
        this.particella = particella;
    }

    public TipologiaParticellaCatastale getTipologia() {
        return tipologia;
    }

    public void setTipologia(TipologiaParticellaCatastale tipologia) {
        this.tipologia = tipologia;
    }

    public String getSubalterno() {
        return subalterno;
    }

    public void setSubalterno(String subalterno) {
        this.subalterno = subalterno;
    }

    public List<PorzioneMaterialeDto> getPorzioniMateriali() {
        return porzioniMateriali;
    }

    public void setPorzioniMateriali(List<PorzioneMaterialeDto> porzioniMateriali) {
        this.porzioniMateriali = porzioniMateriali;
    }
}
