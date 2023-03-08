package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the A4GD_STATO_DIC_ANTIMAFIA database table.
 * 
 */
@Entity
@Table(name = "A4GD_STATO_DIC_ANTIMAFIA")
public class A4gdStatoDicAntimafia extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -9146724509249246774L;

	@Column(nullable = false, length = 4000)
	private String descrizione;

	@Column(nullable = false, length = 500)
	private String identificativo;

	// bi-directional many-to-one association to A4gtDichiarazioneAntimafia
	@OneToMany(mappedBy = "a4gdStatoDicAntimafia")
	private Set<A4gtDichiarazioneAntimafia> a4gtDichiarazioneAntimafias;

	public A4gdStatoDicAntimafia() {
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

	public Set<A4gtDichiarazioneAntimafia> getA4gtDichiarazioneAntimafias() {
		return this.a4gtDichiarazioneAntimafias;
	}

	public void setA4gtDichiarazioneAntimafias(Set<A4gtDichiarazioneAntimafia> a4gtDichiarazioneAntimafias) {
		this.a4gtDichiarazioneAntimafias = a4gtDichiarazioneAntimafias;
	}

	public A4gtDichiarazioneAntimafia addA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		getA4gtDichiarazioneAntimafias().add(a4gtDichiarazioneAntimafia);
		a4gtDichiarazioneAntimafia.setA4gdStatoDicAntimafia(this);

		return a4gtDichiarazioneAntimafia;
	}

	public A4gtDichiarazioneAntimafia removeA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		getA4gtDichiarazioneAntimafias().remove(a4gtDichiarazioneAntimafia);
		a4gtDichiarazioneAntimafia.setA4gdStatoDicAntimafia(null);

		return a4gtDichiarazioneAntimafia;
	}

}