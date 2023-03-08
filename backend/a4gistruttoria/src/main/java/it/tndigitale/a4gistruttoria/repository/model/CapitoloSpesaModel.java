package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * The persistent class for the A4GD_CAPITOLO_SPESA database table.
 */
@Entity
@Table(name = "A4GD_CAPITOLO_SPESA")
@NamedQuery(name = "CapitoloSpesaModel.findAll", query = "SELECT a FROM CapitoloSpesaModel a")
public class CapitoloSpesaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = -2720479177998546305L;

    @Column(nullable = false, length = 50)
    private String capitolo;

    @Column(name = "CODICE_PRODOTTO", nullable = false, precision = 20)
    private BigDecimal codiceProdotto;

    @Column(name = "DESCRIZIONE_CAPITOLO", length = 500)
    private String descrizioneCapitolo;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    //bi-directional many-to-one association to InterventoModel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERVENTO", nullable = false)
    private InterventoModel intervento;

    public CapitoloSpesaModel() {
    }

    public String getCapitolo() {
        return capitolo;
    }

    public CapitoloSpesaModel setCapitolo(String capitolo) {
        this.capitolo = capitolo;
        return this;
    }

    public BigDecimal getCodiceProdotto() {
        return codiceProdotto;
    }

    public CapitoloSpesaModel setCodiceProdotto(BigDecimal codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
        return this;
    }

    public String getDescrizioneCapitolo() {
        return descrizioneCapitolo;
    }

    public CapitoloSpesaModel setDescrizioneCapitolo(String descrizioneCapitolo) {
        this.descrizioneCapitolo = descrizioneCapitolo;
        return this;
    }

    public String getTipo() {
        return tipo;
    }

    public CapitoloSpesaModel setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public CapitoloSpesaModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    public InterventoModel getIntervento() {
        return intervento;
    }

    public CapitoloSpesaModel setIntervento(InterventoModel intervento) {
        this.intervento = intervento;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapitoloSpesaModel that = (CapitoloSpesaModel) o;
        return Objects.equals(capitolo, that.capitolo) &&
                Objects.equals(codiceProdotto, that.codiceProdotto) &&
                Objects.equals(descrizioneCapitolo, that.descrizioneCapitolo) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(campagna, that.campagna) &&
                Objects.equals(intervento, that.intervento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capitolo, codiceProdotto, descrizioneCapitolo, tipo, campagna, intervento);
    }
}