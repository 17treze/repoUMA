package it.tndigitale.a4gutente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import it.tndigitale.a4gutente.service.builder.ProfiloBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApiModel("Rappresenta una istruttoria per lo storico")
public class IstruttoriaPerStorico {

    @ApiModelProperty(value = "Id dell'istruttoria")
    private Long id;
    @ApiModelProperty(value = "Id della domanda a cui è riferita l'istruttoria")
    private Long idDomanda;
    @ApiModelProperty(value = "Variazione richiesta")
    private String variazioneRichiesta;
    @ApiModelProperty(value = "Testo della comunicazione")
    private String testoComunicazione;
    @ApiModelProperty(value = "Eventuale motivazione rifiuto")
    private String motivazioneRifiuto;
    @ApiModelProperty(value = "Lista dei profili (attivati/disattivati)")
    private List<Profilo> profili;
    @ApiParam(value = "Lista delle sedi associate all'sitrtturoia")
    private List<EnteSede> sedi;
    @ApiParam(value = "Lista dei distributori associati all'istruttoria")
    private List<Distributore> distributori;
    @ApiParam(value = "Lista delle aziende associate all'istruttoria")
    private List<Azienda> aziende;
    @ApiModelProperty(value = "Testo della mail inviata")
    @JsonProperty("testoMail")
    private String testoMailInviata;
    @ApiModelProperty(value = "Note dell'istruttoria")
    private String note;
    @ApiModelProperty(value = "Istruttore che ha eseguito la modifica")
    private Utente istruttore;
    @ApiModelProperty(value = "Data di validazione dell’istruttoria")
    private LocalDateTime dataTermineIstruttoria;
    @ApiModelProperty(value = "Eventuale motivo della disattivazione")
    private MotivazioneDisattivazione motivazioneDisattivazione;

    public Long getId() {
        return id;
    }

    public IstruttoriaPerStorico setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getIdDomanda() {
        return idDomanda;
    }

    public IstruttoriaPerStorico setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
        return this;
    }

    public String getVariazioneRichiesta() {
        return variazioneRichiesta;
    }

    public IstruttoriaPerStorico setVariazioneRichiesta(String variazioneRichiesta) {
        this.variazioneRichiesta = variazioneRichiesta;
        return this;
    }

    public String getTestoComunicazione() {
        return testoComunicazione;
    }

    public IstruttoriaPerStorico setTestoComunicazione(String testoComunicazione) {
        this.testoComunicazione = testoComunicazione;
        return this;
    }

    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public IstruttoriaPerStorico setMotivazioneRifiuto(String motivazioneRifiuto) {
        this.motivazioneRifiuto = motivazioneRifiuto;
        return this;
    }

    public List<Profilo> getProfili() {
        return profili;
    }

    public IstruttoriaPerStorico setProfili(List<Profilo> profili) {
        this.profili = profili;
        return this;
    }

    public List<EnteSede> getSedi() {
        return sedi;
    }

    public IstruttoriaPerStorico setSedi(List<EnteSede> sedi) {
        this.sedi = sedi;
        return this;
    }

    public List<Distributore> getDistributori() {
        return distributori;
    }

    public IstruttoriaPerStorico setDistributori(List<Distributore> distributori) {
        this.distributori = distributori;
        return this;
    }

    public String getTestoMailInviata() {
        return testoMailInviata;
    }

    public IstruttoriaPerStorico setTestoMailInviata(String testoMailInviata) {
        this.testoMailInviata = testoMailInviata;
        return this;
    }

    public String getNote() {
        return note;
    }

    public IstruttoriaPerStorico setNote(String note) {
        this.note = note;
        return this;
    }

    public Utente getIstruttore() {
        return istruttore;
    }

    public IstruttoriaPerStorico setIstruttore(Utente istruttore) {
        this.istruttore = istruttore;
        return this;
    }

    public LocalDateTime getDataTermineIstruttoria() {
        return dataTermineIstruttoria;
    }

    public IstruttoriaPerStorico setDataTermineIstruttoria(LocalDateTime dataTermineIstruttoria) {
        this.dataTermineIstruttoria = dataTermineIstruttoria;
        return this;
    }

    public MotivazioneDisattivazione getMotivazioneDisattivazione() {
        return motivazioneDisattivazione;
    }

    public IstruttoriaPerStorico setMotivazioneDisattivazione(MotivazioneDisattivazione motivazioneDisattivazione) {
        this.motivazioneDisattivazione = motivazioneDisattivazione;
        return this;
    }

    public List<Azienda> getAziende() {
        return aziende;
    }

    public IstruttoriaPerStorico setAziende(List<Azienda> aziende) {
        this.aziende = aziende;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IstruttoriaPerStorico that = (IstruttoriaPerStorico) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idDomanda, that.idDomanda) &&
                Objects.equals(variazioneRichiesta, that.variazioneRichiesta) &&
                Objects.equals(testoComunicazione, that.testoComunicazione) &&
                Objects.equals(motivazioneRifiuto, that.motivazioneRifiuto) &&
                Objects.equals(profili, that.profili) &&
                Objects.equals(sedi, that.sedi) &&
                Objects.equals(distributori, that.distributori) &&
                Objects.equals(aziende, that.aziende) &&
                Objects.equals(testoMailInviata, that.testoMailInviata) &&
                Objects.equals(note, that.note) &&
                Objects.equals(istruttore, that.istruttore) &&
                Objects.equals(dataTermineIstruttoria, that.dataTermineIstruttoria) &&
                motivazioneDisattivazione == that.motivazioneDisattivazione;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idDomanda, variazioneRichiesta,
                testoComunicazione, motivazioneRifiuto, profili, sedi,
                distributori, aziende, testoMailInviata, note, istruttore,
                dataTermineIstruttoria, motivazioneDisattivazione);
    }

    public IstruttoriaPerStorico addProfiliDisattivi(List<A4gtProfilo> profiliDisattivati) {
        List<Profilo> profili = ProfiloBuilder.creaProfiliDisattivi(profiliDisattivati);
        this.profili.addAll(profili);
        return this;
    }
}
