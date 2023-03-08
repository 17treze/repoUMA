package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="A4GT_ESITO_MAN_PASCOLO")
public class EsitoMantenimentoPascolo extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ISTRUTTORIA")
    private IstruttoriaModel istruttoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PASCOLO_PARTICELLA")
    private A4gtPascoloParticella pascoloParticella;

    @Column(name = "ESITO_MAN")
    private String esitoMan;

    @Lob
    @Column(name = "DATI_INPUT")
    private String datiInput;

    @Lob
    @Column(name = "DATI_OUTPUT")
    private String datiOutput;

    public IstruttoriaModel getIstruttoria() {
        return istruttoria;
    }

    public EsitoMantenimentoPascolo setIstruttoria(IstruttoriaModel istruttoria) {
        this.istruttoria = istruttoria;
        return this;
    }

    public A4gtPascoloParticella getPascoloParticella() {
        return pascoloParticella;
    }

    public EsitoMantenimentoPascolo setPascoloParticella(A4gtPascoloParticella pascoloParticella) {
        this.pascoloParticella = pascoloParticella;
        return this;
    }

    public String getEsitoMan() {
        return esitoMan;
    }

    public EsitoMantenimentoPascolo setEsitoMan(String esitoMan) {
        this.esitoMan = esitoMan;
        return this;
    }

    public String getDatiInput() {
        return datiInput;
    }

    public EsitoMantenimentoPascolo setDatiInput(String datiInput) {
        this.datiInput = datiInput;
        return this;
    }

    public String getDatiOutput() {
        return datiOutput;
    }

    public EsitoMantenimentoPascolo setDatiOutput(String datiOutput) {
        this.datiOutput = datiOutput;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsitoMantenimentoPascolo that = (EsitoMantenimentoPascolo) o;
        return Objects.equals(istruttoria, that.istruttoria) &&
                Objects.equals(pascoloParticella, that.pascoloParticella) &&
                Objects.equals(esitoMan, that.esitoMan) &&
                Objects.equals(datiInput, that.datiInput) &&
                Objects.equals(datiOutput, that.datiOutput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(istruttoria, pascoloParticella, esitoMan, datiInput, datiOutput);
    }

}
