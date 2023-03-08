package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the A4GD_STATO_LAV_SOSTEGNO database table.
 * 
 */
@Entity
@Table(name="A4GD_STATO_LAV_SOSTEGNO")
@NamedQuery(name="A4gdStatoLavSostegno.findAll", query="SELECT a FROM A4gdStatoLavSostegno a")
public class A4gdStatoLavSostegno extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	
	private static final long serialVersionUID = -5217542364158951555L;

	private String descrizione;

	private String identificativo;

	//bi-directional many-to-one association to A4gtLavorazioneSostegno
	@OneToMany(mappedBy="a4gdStatoLavSostegno")
	private Set<IstruttoriaModel> a4gtLavorazioneSostegnos;

	public A4gdStatoLavSostegno() {
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getIdentificativo() {
		return this.identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public Set<IstruttoriaModel> getA4gtLavorazioneSostegnos() {
		return this.a4gtLavorazioneSostegnos;
	}

	public void setA4gtLavorazioneSostegnos(Set<IstruttoriaModel> a4gtLavorazioneSostegnos) {
		this.a4gtLavorazioneSostegnos = a4gtLavorazioneSostegnos;
	}

	public IstruttoriaModel addA4gtLavorazioneSostegno(IstruttoriaModel a4gtLavorazioneSostegno) {
		getA4gtLavorazioneSostegnos().add(a4gtLavorazioneSostegno);
		a4gtLavorazioneSostegno.setA4gdStatoLavSostegno(this);

		return a4gtLavorazioneSostegno;
	}

	public IstruttoriaModel removeA4gtLavorazioneSostegno(IstruttoriaModel a4gtLavorazioneSostegno) {
		getA4gtLavorazioneSostegnos().remove(a4gtLavorazioneSostegno);
		a4gtLavorazioneSostegno.setA4gdStatoLavSostegno(null);

		return a4gtLavorazioneSostegno;
	}
}
