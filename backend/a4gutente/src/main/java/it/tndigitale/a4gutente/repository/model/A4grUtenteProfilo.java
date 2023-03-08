package it.tndigitale.a4gutente.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "A4GR_UTENTE_PROFILO")
public class A4grUtenteProfilo extends EntitaDominio implements Serializable {

    private static final long serialVersionUID = -1691198710165902910L;

    @Column(name = "ID_UTENTE")
    private Long idUtente;

    @Column(name = "ID_PROFILO")
    private Long idProfilo;


    public Long getIdUtente() {
        return idUtente;
    }
    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }
    public Long getIdProfilo() {
        return idProfilo;
    }
    public void setIdProfilo(Long idProfilo) {
        this.idProfilo = idProfilo;
    }
}
