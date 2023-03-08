package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * The persistent class for the A4GT_PRODUZIONE_LATTE database table.
 */
@Entity
@Table(name = "A4GT_PRODUZIONE_LATTE")
public class RegistroProduzioneLatteModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
    private static final long serialVersionUID = -6825614548055289933L;

    @Column
    private String cuaa;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CONS_VEND")
    private Date dtConsVend;

    @Column(name = "ANNO_CAMPAGNA", nullable = false)
    private Integer campagna;

    public RegistroProduzioneLatteModel() {
    }

    public String getCuaa() {
        return cuaa;
    }

    public RegistroProduzioneLatteModel setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public Date getDtConsVend() {
        return dtConsVend;
    }

    public RegistroProduzioneLatteModel setDtConsVend(Date dtConsVend) {
        this.dtConsVend = dtConsVend;
        return this;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public RegistroProduzioneLatteModel setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroProduzioneLatteModel that = (RegistroProduzioneLatteModel) o;
        return Objects.equals(cuaa, that.cuaa) &&
                Objects.equals(dtConsVend, that.dtConsVend) &&
                Objects.equals(campagna, that.campagna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuaa, dtConsVend, campagna);
    }
}