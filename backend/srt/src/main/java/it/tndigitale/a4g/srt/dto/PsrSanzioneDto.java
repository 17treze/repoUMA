package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;

public class PsrSanzioneDto {
    private String descrizioneSanzione;
    private BigDecimal ammontareSanzione;

    public String getDescrizioneSanzione() {
        return descrizioneSanzione;
    }
    public void setDescrizioneSanzione(String descrizioneSanzione) {
        this.descrizioneSanzione = descrizioneSanzione;
    }
    public BigDecimal getAmmontareSanzione() {
        return ammontareSanzione;
    }
    public void setAmmontareSanzione(BigDecimal ammontareSanzione) {
        this.ammontareSanzione = ammontareSanzione;
    }
}
