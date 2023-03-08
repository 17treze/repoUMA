package it.tndigitale.a4gutente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel("Rappresenta il modello relativo l'istruttoria di una domanda di richiesta utente")
public class Istruttoria {

    @ApiModelProperty(value = "Id dell'istruttoria")
    private Long id;
    @ApiModelProperty(value = "Versione")
    private Integer version;
    @ApiModelProperty(value = "Id della domanda a cui è riferita l'istruttoria", required = true)
    private Long idDomanda;
    @ApiModelProperty(value = "Variazione richiesta")
    private String variazioneRichiesta;
    @ApiModelProperty(value = "Testo della comunicazione")
    private String testoComunicazione;
    @ApiModelProperty(value = "Eventuale motivazione rifiuto")
    private String motivazioneRifiuto;
    @ApiModelProperty(value = "Lista degli identificativi degli enti (sedi) selezionati")
    private List<Long> sedi;
    @ApiModelProperty(value = "Lista dei profili selezionati")
    private List<Long> profili;
    @ApiModelProperty(value = "Testo della mail inviata")
    @JsonProperty("testoMail")
    private String testoMailInviata;
    @ApiModelProperty(value = "Note dell'istruttoria")
    private String note;
    @ApiModelProperty(value = "Istruttore che ha eseguito la modifica")
    private Utente istruttore;


    public Long getId() {
        return id;
    }

    public Istruttoria setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getIdDomanda() {
        return idDomanda;
    }

    public Istruttoria setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
        return this;
    }

    public String getVariazioneRichiesta() {
        return variazioneRichiesta;
    }

    public Istruttoria setVariazioneRichiesta(String variazioneRichiesta) {
        this.variazioneRichiesta = variazioneRichiesta;
        return this;
    }

    public String getTestoComunicazione() {
        return testoComunicazione;
    }

    public Istruttoria setTestoComunicazione(String testoComunicazione) {
        this.testoComunicazione = testoComunicazione;
        return this;
    }

    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public Istruttoria setMotivazioneRifiuto(String motivazioneRifiuto) {
        this.motivazioneRifiuto = motivazioneRifiuto;
        return this;
    }

    public List<Long> getSedi() {
        return sedi;
    }

    public Istruttoria setSedi(List<Long> sedi) {
        this.sedi = sedi;
        return this;
    }

    public List<Long> getProfili() {
        return profili;
    }

    public Istruttoria setProfili(List<Long> profili) {
        this.profili = profili;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public Istruttoria setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public String getTestoMailInviata() {
        return testoMailInviata;
    }

    public Istruttoria setTestoMailInviata(String testoMailInviata) {
        this.testoMailInviata = testoMailInviata;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Istruttoria setNote(String note) {
        this.note = note;
        return this;
    }

    public Utente getIstruttore() {
        return istruttore;
    }

    public Istruttoria setIstruttore(Utente istruttore) {
        this.istruttore = istruttore;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Istruttoria that = (Istruttoria) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(version, that.version) &&
                Objects.equals(idDomanda, that.idDomanda) &&
                Objects.equals(variazioneRichiesta, that.variazioneRichiesta) &&
                Objects.equals(testoComunicazione, that.testoComunicazione) &&
                Objects.equals(motivazioneRifiuto, that.motivazioneRifiuto) &&
                Objects.equals(sedi, that.sedi) &&
                Objects.equals(profili, that.profili) &&
                Objects.equals(testoMailInviata, that.testoMailInviata) &&
                Objects.equals(note, that.note) &&
                Objects.equals(istruttore, that.istruttore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, idDomanda, variazioneRichiesta, testoComunicazione, motivazioneRifiuto, sedi, profili, testoMailInviata, note, istruttore);
    }
}
