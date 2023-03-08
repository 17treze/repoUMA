package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="A4GT_TRANSIZIONE_ISTRUTTORIA")
@NamedQuery(name="TransizioneIstruttoriaModel.findAll", query="SELECT a FROM TransizioneIstruttoriaModel a")
public class TransizioneIstruttoriaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_ESECUZIONE")
	private Date dataEsecuzione;

	@OneToMany(mappedBy="transizioneIstruttoria", cascade = CascadeType.REMOVE)
	private Set<PassoTransizioneModel> passiTransizione;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_STATO_FINALE")
	private A4gdStatoLavSostegno a4gdStatoLavSostegno1;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_STATO_INIZIALE")
	private A4gdStatoLavSostegno a4gdStatoLavSostegno2;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;

	public Date getDataEsecuzione() {
		return this.dataEsecuzione;
	}

	public void setDataEsecuzione(Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	public Set<PassoTransizioneModel> getPassiTransizione() {
		return this.passiTransizione;
	}

	public void setPassiTransizione(Set<PassoTransizioneModel> passiTransizione) {
		this.passiTransizione = passiTransizione;
	}

	public PassoTransizioneModel addPassoTransizione(PassoTransizioneModel passoTransizione) {
		getPassiTransizione().add(passoTransizione);
		passoTransizione.setTransizioneIstruttoria(this);

		return passoTransizione;
	}

	public PassoTransizioneModel removePassoTransizione(PassoTransizioneModel passoTransizione) {
		getPassiTransizione().remove(passoTransizione);
		passoTransizione.setTransizioneIstruttoria(null);

		return passoTransizione;
	}

	public A4gdStatoLavSostegno getA4gdStatoLavSostegno1() {
		return this.a4gdStatoLavSostegno1;
	}

	public void setA4gdStatoLavSostegno1(A4gdStatoLavSostegno a4gdStatoLavSostegno1) {
		this.a4gdStatoLavSostegno1 = a4gdStatoLavSostegno1;
	}

	public A4gdStatoLavSostegno getA4gdStatoLavSostegno2() {
		return this.a4gdStatoLavSostegno2;
	}

	public void setA4gdStatoLavSostegno2(A4gdStatoLavSostegno a4gdStatoLavSostegno2) {
		this.a4gdStatoLavSostegno2 = a4gdStatoLavSostegno2;
	}

	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}

	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

}