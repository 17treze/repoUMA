package it.tndigitale.a4gistruttoria.dto;

public class VariabileStampa {

    private String codiceVariabile;
    private String descrizioneVariabile;
    private String valore;
    private Integer ordine;

    public String getCodiceVariabile() {
        return codiceVariabile;
    }

    public void setCodiceVariabile(String codiceVariabile) {
        this.codiceVariabile = codiceVariabile;
    }

    public String getDescrizioneVariabile() {
        return descrizioneVariabile;
    }

    public void setDescrizioneVariabile(String descrizioneVariabile) {
        this.descrizioneVariabile = descrizioneVariabile;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public Integer getOrdine() {
        return ordine;
    }

    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }
}
