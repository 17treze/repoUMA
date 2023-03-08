package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "A4GT_CONF_RICEVIBILITA")
public class ConfigurazioneRicevibilitaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = 3201040217543984449L;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    @Column(name = "DATA_RICEVIBILITA", nullable = false)
    private LocalDate dataRicevibilita;

    @Column(name = "SCADENZA_DOM_INIZIALE_RITARDO", nullable = false)
    private LocalDate dataScadenzaDomandaInizialeInRitardo;

    @Column(name = "SCADENZA_DOMANDA_RITIROPARZIAL", nullable = false)
    private LocalDate dataScadenzaDomandaRitiroParziale;
    
    @Column(name = "SCADENZA_DOM_MODIFICA_RITARDO", nullable = false)
    private LocalDate dataScadenzaDomandaModificaInRitardo;

    public Integer getCampagna() {
        return campagna;
    }

    public ConfigurazioneRicevibilitaModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    public LocalDate getDataRicevibilita() {
        return dataRicevibilita;
    }

    public ConfigurazioneRicevibilitaModel setDataRicevibilita(LocalDate dataRicevibilita) {
        this.dataRicevibilita = dataRicevibilita;
        return this;
    }

    public LocalDate getDataScadenzaDomandaInizialeInRitardo() {
        return dataScadenzaDomandaInizialeInRitardo;
    }

    public ConfigurazioneRicevibilitaModel setDataScadenzaDomandaInizialeInRitardo(LocalDate dataScadenzaDomandaInizialeInRitardo) {
        this.dataScadenzaDomandaInizialeInRitardo = dataScadenzaDomandaInizialeInRitardo;
        return this;
    }

    public LocalDate getDataScadenzaDomandaRitiroParziale() {
        return dataScadenzaDomandaRitiroParziale;
    }

    public ConfigurazioneRicevibilitaModel setDataScadenzaDomandaRitiroParziale(LocalDate dataScadenzaDomandaRitiroParziale) {
        this.dataScadenzaDomandaRitiroParziale = dataScadenzaDomandaRitiroParziale;
        return this;
    }
    
    public LocalDate getDataScadenzaDomandaModificaInRitardo() {
        return dataScadenzaDomandaModificaInRitardo;
    }

    public ConfigurazioneRicevibilitaModel setDataScadenzaDomandaModificaInRitardo(LocalDate dataScadenzaDomandaModificaInRitardo) {
        this.dataScadenzaDomandaModificaInRitardo = dataScadenzaDomandaModificaInRitardo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurazioneRicevibilitaModel that = (ConfigurazioneRicevibilitaModel) o;
        return Objects.equals(campagna, that.campagna) &&
                Objects.equals(dataRicevibilita, that.dataRicevibilita) &&
                Objects.equals(dataScadenzaDomandaInizialeInRitardo, that.dataScadenzaDomandaInizialeInRitardo) &&
                Objects.equals(dataScadenzaDomandaRitiroParziale, that.dataScadenzaDomandaRitiroParziale) &&
                Objects.equals(dataScadenzaDomandaModificaInRitardo, that.dataScadenzaDomandaModificaInRitardo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(campagna, dataRicevibilita, dataScadenzaDomandaInizialeInRitardo, dataScadenzaDomandaRitiroParziale, dataScadenzaDomandaModificaInRitardo);
    }
}
