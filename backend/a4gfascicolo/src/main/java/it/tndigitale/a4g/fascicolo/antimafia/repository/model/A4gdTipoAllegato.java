package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the A4GD_TIPO_ALLEGATO database table.
 * 
 */
@Entity
@Table(name = "A4GD_TIPO_ALLEGATO")
public class A4gdTipoAllegato extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -5401428925422988787L;

	@Column(nullable = false, length = 4000)
	private String descrizione;

	@Column(nullable = false, length = 500)
	private String identificativo;

	// bi-directional many-to-one association to A4gtAllegatoDicAntimafia
	@OneToMany(mappedBy = "a4gdTipoAllegato", fetch = FetchType.LAZY)
	private Set<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafias;

	@OneToMany(mappedBy = "a4gdTipoAllegato", fetch = FetchType.LAZY)
	private Set<A4gtAllegatoDicFamConv> a4gtAllegatoDicFamConvs;

	public A4gdTipoAllegato() {
		// Default empty constructor
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

	public Set<A4gtAllegatoDicAntimafia> getA4gtAllegatoDicAntimafias() {
		return this.a4gtAllegatoDicAntimafias;
	}

	public void setA4gtAllegatoDicAntimafias(Set<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafias) {
		this.a4gtAllegatoDicAntimafias = a4gtAllegatoDicAntimafias;
	}

	public A4gtAllegatoDicAntimafia addA4gtAllegatoDicAntimafia(A4gtAllegatoDicAntimafia a4gtAllegatoDicAntimafia) {
		getA4gtAllegatoDicAntimafias().add(a4gtAllegatoDicAntimafia);
		a4gtAllegatoDicAntimafia.setA4gdTipoAllegato(this);

		return a4gtAllegatoDicAntimafia;
	}

	public A4gtAllegatoDicAntimafia removeA4gtAllegatoDicAntimafia(A4gtAllegatoDicAntimafia a4gtAllegatoDicAntimafia) {
		getA4gtAllegatoDicAntimafias().remove(a4gtAllegatoDicAntimafia);
		a4gtAllegatoDicAntimafia.setA4gdTipoAllegato(null);

		return a4gtAllegatoDicAntimafia;
	}

	public Set<A4gtAllegatoDicFamConv> getA4gtAllegatoDicFamConvs() {
		return a4gtAllegatoDicFamConvs;
	}

	public void setA4gtAllegatoDicFamConvs(Set<A4gtAllegatoDicFamConv> a4gtAllegatoDicFamConvs) {
		this.a4gtAllegatoDicFamConvs = a4gtAllegatoDicFamConvs;
	}

}