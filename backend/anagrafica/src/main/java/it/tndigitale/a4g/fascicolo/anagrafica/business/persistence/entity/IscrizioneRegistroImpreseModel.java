package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CCIAA = Camera di Commercio, Industria, Agricoltura  e Artigianato (una per Provincia)
 * RI = Registro imprese: ufficio della Camera di commercio.
 * REA = Repertorio delle notizie Economiche e Amministrative costituito presso l'Ufficio del Registro Imprese
 */
@Embeddable
public class IscrizioneRegistroImpreseModel {
    @Column(name = "DATA_ISCRIZIONE")
    private LocalDate dataIscrizione;

    @Column(name = "PROVINCIA_CCIAA")
    private String provinciaCameraCommercio;

    @Column(name = "NUMERO_REPERTORIO")
    private Long numeroRepertorioEconomicoAmministrativo;

    private Boolean cessata;

    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    public IscrizioneRegistroImpreseModel setDataIscrizione(LocalDate dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
        return this;
    }

    public String getProvinciaCameraCommercio() {
        return provinciaCameraCommercio;
    }

    public IscrizioneRegistroImpreseModel setProvinciaCameraCommercio(String provinciaCameraCommercio) {
        this.provinciaCameraCommercio = provinciaCameraCommercio;
        return this;
    }

    public Long getNumeroRepertorioEconomicoAmministrativo() {
        return numeroRepertorioEconomicoAmministrativo;
    }

    public IscrizioneRegistroImpreseModel setNumeroRepertorioEconomicoAmministrativo(Long numeroRepertorioEconomicoAmministrativo) {
        this.numeroRepertorioEconomicoAmministrativo = numeroRepertorioEconomicoAmministrativo;
        return this;
    }

    public Boolean getCessata() {
        return cessata;
    }

    public IscrizioneRegistroImpreseModel setCessata(Boolean cessata) {
        this.cessata = cessata;
        return this;
    }
}
