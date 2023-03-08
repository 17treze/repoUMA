package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GR_PROFILO_RUOLO")
public class A4grProfiloRuolo extends EntitaDominio implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_PROFILO")
    private A4gtProfilo profilo;

    public A4gtProfilo getProfilo() {
        return profilo;
    }

    public A4grProfiloRuolo setProfilo(A4gtProfilo profilo) {
        this.profilo = profilo;
        return this;
    }

}
