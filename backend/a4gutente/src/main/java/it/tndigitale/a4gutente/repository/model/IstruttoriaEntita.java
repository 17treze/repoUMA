package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4gutente.dto.MotivazioneDisattivazione;

@Entity
@Table(name="A4GT_ISTRUTTORIA")
public class IstruttoriaEntita extends EntitaDominio implements Serializable {

    @Column(name = "VARIAZIONE_RICHIESTA")
    private String variazioneRichiesta;
    @Column(name = "TESTO_COMUNICAZIONE")
    private String testoComunicazione;
    @Column(name = "MOTIVAZIONE_RIFIUTO")
    private String motivazioneRifiuto;
    private String note;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOMANDA", referencedColumnName = "ID")
    private DomandaRegistrazioneUtente domanda;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UTENTE", referencedColumnName = "ID")
    private A4gtUtente utente;
    @ManyToMany
    @JoinTable(name = "A4GR_PROFILO_ISTRUTTORIA",
               joinColumns = @JoinColumn(name = "ID_ISTRUTTORIA"),
               inverseJoinColumns = @JoinColumn(name = "ID_PROFILO"))
    private List<A4gtProfilo> profili;
    @ManyToMany
    @JoinTable(name = "A4GR_PROFILO_DIS_ISTRUTTORIA",
            joinColumns = @JoinColumn(name = "ID_ISTRUTTORIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_PROFILO"))
    private List<A4gtProfilo> profiliDisabilitati;
    @ManyToMany
    @JoinTable(name = "A4GR_ENTE_ISTRUTTORIA",
               joinColumns = @JoinColumn(name = "ID_ISTRUTTORIA"),
               inverseJoinColumns = @JoinColumn(name = "ID_ENTE"))
    private List<A4gtEnte> enti;
    @Column(name = "TESTO_MAIL_INVIATA")
    private String testoMailInviata;
    @Column(name = "DATA_TERMINE_ISTRUTTORIA")
    private LocalDateTime dataTermineIstruttoria;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ISTRUTTORE", referencedColumnName = "ID")
    private A4gtUtente istruttore;
    @Column(name = "MOTIVO_DISATTIVAZIONE")
    @Enumerated(EnumType.STRING)
    private MotivazioneDisattivazione motivazioneDisattivazione;


    public String getVariazioneRichiesta() {
        return variazioneRichiesta;
    }

    public IstruttoriaEntita setVariazioneRichiesta(String variazioneRichiesta) {
        this.variazioneRichiesta = variazioneRichiesta;
        return this;
    }

    public String getTestoComunicazione() {
        return testoComunicazione;
    }

    public IstruttoriaEntita setTestoComunicazione(String testoComunicazione) {
        this.testoComunicazione = testoComunicazione;
        return this;
    }

    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public IstruttoriaEntita setMotivazioneRifiuto(String motivazioneRifiuto) {
        this.motivazioneRifiuto = motivazioneRifiuto;
        return this;
    }

    public DomandaRegistrazioneUtente getDomanda() {
        return domanda;
    }

    public IstruttoriaEntita setDomanda(DomandaRegistrazioneUtente domanda) {
        this.domanda = domanda;
        return this;
    }

    public List<A4gtProfilo> getProfili() {
        return profili;
    }

    public IstruttoriaEntita setProfili(List<A4gtProfilo> profili) {
        this.profili = profili;
        return this;
    }

    public List<A4gtEnte> getEnti() {
        return enti;
    }

    public IstruttoriaEntita setEnti(List<A4gtEnte> enti) {
        this.enti = enti;
        return this;
    }

    public String getNote() {
        return note;
    }

    public IstruttoriaEntita setNote(String note) {
        this.note = note;
        return this;
    }

    public String getTestoMailInviata() {
        return testoMailInviata;
    }

    public IstruttoriaEntita setTestoMailInviata(String testoMailInviata) {
        this.testoMailInviata = testoMailInviata;
        return this;
    }

    public A4gtUtente getUtente() {
        return utente;
    }

    public IstruttoriaEntita setUtente(A4gtUtente utente) {
        this.utente = utente;
        return this;
    }

    public LocalDateTime getDataTermineIstruttoria() {
        return dataTermineIstruttoria;
    }

    public IstruttoriaEntita setDataTermineIstruttoria(LocalDateTime dataTermineIstruttoria) {
        this.dataTermineIstruttoria = dataTermineIstruttoria;
        return this;
    }

    public A4gtUtente getIstruttore() {
        return istruttore;
    }

    public IstruttoriaEntita setIstruttore(A4gtUtente istruttore) {
        this.istruttore = istruttore;
        return this;
    }

    public List<A4gtProfilo> getProfiliDisabilitati() {
        return profiliDisabilitati;
    }

    public IstruttoriaEntita setProfiliDisabilitati(List<A4gtProfilo> profiliDisabilitati) {
        this.profiliDisabilitati = profiliDisabilitati;
        return this;
    }

    public MotivazioneDisattivazione getMotivazioneDisattivazione() {
        return motivazioneDisattivazione;
    }

    public IstruttoriaEntita setMotivazioneDisattivazione(MotivazioneDisattivazione motivazioneDisattivazione) {
        this.motivazioneDisattivazione = motivazioneDisattivazione;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IstruttoriaEntita that = (IstruttoriaEntita) o;
        return Objects.equals(variazioneRichiesta, that.variazioneRichiesta) &&
                Objects.equals(testoComunicazione, that.testoComunicazione) &&
                Objects.equals(motivazioneRifiuto, that.motivazioneRifiuto) &&
                Objects.equals(note, that.note) &&
                Objects.equals(domanda, that.domanda) &&
                Objects.equals(utente, that.utente) &&
                Objects.equals(profili, that.profili) &&
                Objects.equals(profiliDisabilitati, that.profiliDisabilitati) &&
                Objects.equals(enti, that.enti) &&
                Objects.equals(testoMailInviata, that.testoMailInviata) &&
                Objects.equals(dataTermineIstruttoria, that.dataTermineIstruttoria) &&
                Objects.equals(istruttore, that.istruttore) &&
                motivazioneDisattivazione == that.motivazioneDisattivazione;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variazioneRichiesta, testoComunicazione,
                motivazioneRifiuto, note, domanda, utente,
                profili, profiliDisabilitati, enti,
                testoMailInviata, dataTermineIstruttoria,
                istruttore, motivazioneDisattivazione);
    }

    public void preparaPerCambioStato(LocalDateTime dataCambioStato, A4gtUtente utente) {
        this.dataTermineIstruttoria = dataCambioStato;
        this.istruttore = utente;
    }
}
