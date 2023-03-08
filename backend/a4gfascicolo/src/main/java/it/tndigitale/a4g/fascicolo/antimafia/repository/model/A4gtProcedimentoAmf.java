package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the A4GT_PROCEDIMENTO_AMF database table.
 * 
 */
@Entity
@Table(name="A4GT_PROCEDIMENTO_AMF")
@NamedQuery(name="A4gtProcedimentoAmf.findAll", query="SELECT a FROM A4gtProcedimentoAmf a")
public class A4gtProcedimentoAmf extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -5944839549908970262L;

	private String procedimento;

	//bi-directional many-to-one association to A4gtDichiarazioneAntimafia
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DICHIARAZIONE")
	private A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia;


	public String getProcedimento() {
		return this.procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public A4gtDichiarazioneAntimafia getA4gtDichiarazioneAntimafia() {
		return this.a4gtDichiarazioneAntimafia;
	}

	public void setA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		this.a4gtDichiarazioneAntimafia = a4gtDichiarazioneAntimafia;
	}

}