package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the A4GT_AZIENDA_AGRICOLA database table.
 * 
 */
@Entity
@Table(name="A4GT_AZIENDA_AGRICOLA")
public class A4gtAziendaAgricola extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -5761774518229824436L;

	@Column(nullable=false, length=16)
	private String cuaa;

	//bi-directional many-to-one association to A4gtDichiarazioneAntimafia
	@OneToMany(mappedBy="a4gtAziendaAgricola")
	private Set<A4gtDichiarazioneAntimafia> a4gtDichiarazioneAntimafias;

	public A4gtAziendaAgricola() {
		//Default empty constructor
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Set<A4gtDichiarazioneAntimafia> getA4gtDichiarazioneAntimafias() {
		return this.a4gtDichiarazioneAntimafias;
	}

	public void setA4gtDichiarazioneAntimafias(Set<A4gtDichiarazioneAntimafia> a4gtDichiarazioneAntimafias) {
		this.a4gtDichiarazioneAntimafias = a4gtDichiarazioneAntimafias;
	}

	public A4gtDichiarazioneAntimafia addA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		getA4gtDichiarazioneAntimafias().add(a4gtDichiarazioneAntimafia);
		a4gtDichiarazioneAntimafia.setA4gtAziendaAgricola(this);

		return a4gtDichiarazioneAntimafia;
	}

	public A4gtDichiarazioneAntimafia removeA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		getA4gtDichiarazioneAntimafias().remove(a4gtDichiarazioneAntimafia);
		a4gtDichiarazioneAntimafia.setA4gtAziendaAgricola(null);

		return a4gtDichiarazioneAntimafia;
	}

}