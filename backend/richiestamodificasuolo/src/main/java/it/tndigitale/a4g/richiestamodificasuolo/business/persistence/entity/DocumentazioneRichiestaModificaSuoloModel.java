package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import javax.persistence.Lob;

@Entity
@Table(name = "A4ST_DOCUMENTAZIONE_RICHIESTA")
public class DocumentazioneRichiestaModificaSuoloModel extends EntitaDominio implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RICHIESTA")
    private RichiestaModificaSuoloModel richiestaModificaSuolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_POLIGONO_DICHIARATO")
    private SuoloDichiaratoModel suoloDichiarato;
    
    @Column(name="NOME_FILE")
    private String nomeFile;
    
    private String descrizione;

    private Long dimensione;
    
    private String utente;
    
    @Enumerated(EnumType.STRING)
    private ProfiloUtente profiloUtente;
    
    @Column(name = "DATA_INSERIMENTO")
    private LocalDateTime dataInserimento;
    
    @Lob
    @Column(name = "DOC_CONTENT")
    private byte[] docContent;

    public RichiestaModificaSuoloModel getRichiestaModificaSuolo() {
        return richiestaModificaSuolo;
    }

    public void setRichiestaModificaSuolo(RichiestaModificaSuoloModel richiestaModificaSuolo) {
        this.richiestaModificaSuolo = richiestaModificaSuolo;
    }

    public SuoloDichiaratoModel getSuoloDichiarato() {
        return suoloDichiarato;
    }

    public void setSuoloDichiarato(SuoloDichiaratoModel suoloDichiarato) {
        this.suoloDichiarato = suoloDichiarato;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getDimensione() {
        return dimensione;
    }

    public void setDimensione(Long dimensione) {
        this.dimensione = dimensione;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public ProfiloUtente getProfiloUtente() {
        return profiloUtente;
    }

    public void setProfiloUtente(ProfiloUtente profiloUtente) {
        this.profiloUtente = profiloUtente;
    }

    public LocalDateTime getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(LocalDateTime dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public byte[] getDocContent() {
        return docContent;
    }

    public void setDocContent(byte[] docContent) {
        this.docContent = docContent;
    }
}
