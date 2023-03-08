package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "A4SR_JOB_FME_LAVORAZIONE")
public class JobFmeLavorazioneModel extends EntitaDominio implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4702270860277035599L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LAVORAZIONE", referencedColumnName = "ID")
    private LavorazioneSuoloModel relLavorazioneSuolo;

    @Column(name = "ID_JOB_FME")
    private Long idJobFme;

    @Column(name = "TIPO_JOB_FME")
    @Enumerated(EnumType.STRING)
    private TipoJobFME tipoJobFme;

    @Column(name = "DATA_INIZIO_ESECUZIONE")
    private LocalDateTime dataInizioEsecuzione;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public LavorazioneSuoloModel getRelLavorazioneSuolo() {
        return relLavorazioneSuolo;
    }

    public void setRelLavorazioneSuolo(LavorazioneSuoloModel relLavorazioneSuolo) {
        this.relLavorazioneSuolo = relLavorazioneSuolo;
    }

    public Long getIdJobFme() {
        return idJobFme;
    }

    public void setIdJobFme(Long idJobFme) {
        this.idJobFme = idJobFme;
    }

    public TipoJobFME getTipoJobFme() {
        return tipoJobFme;
    }

    public void setTipoJobFme(TipoJobFME tipoJobFme) {
        this.tipoJobFme = tipoJobFme;
    }

    public LocalDateTime getDataInizioEsecuzione() {
        return dataInizioEsecuzione;
    }

    public void setDataInizioEsecuzione(LocalDateTime dataInizioEsecuzione) {
        this.dataInizioEsecuzione = dataInizioEsecuzione;
    }
}
