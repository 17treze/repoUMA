package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/**
 * The persistent class for the A4GT_ANALISI_LATTE database table.
 */
@Entity
@Table(name = "A4GT_ANALISI_LATTE")
public class RegistroAnalisiLatteModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = 8795349399240563538L;

    @Column(name = "CARICA_BATTERICA")
    private BigDecimal caricaBatterica;

    @Column(name = "CELLULE_SOMATICHE")
    private BigDecimal celluleSomatiche;

    @Column(name = "CONTENUTO_PROTEINA")
    private BigDecimal contenutoProteina;

    @Column
    private String cuaa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ANALISI")
    private Date dtAnalisi;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    public RegistroAnalisiLatteModel() {
    }

    public BigDecimal getCaricaBatterica() {
        return caricaBatterica;
    }

    public RegistroAnalisiLatteModel setCaricaBatterica(BigDecimal caricaBatterica) {
        this.caricaBatterica = caricaBatterica;
        return this;
    }

    public BigDecimal getCelluleSomatiche() {
        return celluleSomatiche;
    }

    public RegistroAnalisiLatteModel setCelluleSomatiche(BigDecimal celluleSomatiche) {
        this.celluleSomatiche = celluleSomatiche;
        return this;
    }

    public BigDecimal getContenutoProteina() {
        return contenutoProteina;
    }

    public RegistroAnalisiLatteModel setContenutoProteina(BigDecimal contenutoProteina) {
        this.contenutoProteina = contenutoProteina;
        return this;
    }

    public String getCuaa() {
        return cuaa;
    }

    public RegistroAnalisiLatteModel setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public Date getDtAnalisi() {
        return dtAnalisi;
    }

    public RegistroAnalisiLatteModel setDtAnalisi(Date dtAnalisi) {
        this.dtAnalisi = dtAnalisi;
        return this;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public RegistroAnalisiLatteModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroAnalisiLatteModel that = (RegistroAnalisiLatteModel) o;
        return Objects.equals(caricaBatterica, that.caricaBatterica) &&
                Objects.equals(celluleSomatiche, that.celluleSomatiche) &&
                Objects.equals(contenutoProteina, that.contenutoProteina) &&
                Objects.equals(cuaa, that.cuaa) &&
                Objects.equals(dtAnalisi, that.dtAnalisi) &&
                Objects.equals(campagna, that.campagna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caricaBatterica, celluleSomatiche, contenutoProteina, cuaa, dtAnalisi, campagna);
    }
}