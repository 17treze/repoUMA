package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the A4GT_PICCOLO_AGRICOLTORE database table.
 * 
 */
@Entity
@Table(name = "A4GT_PICCOLO_AGRICOLTORE")
@NamedQuery(name = "A4gtPiccoloAgricoltore.findAll", query = "SELECT a FROM A4gtPiccoloAgricoltore a")
public class A4gtPiccoloAgricoltore extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 6804463655506092818L;

	/* @Column(name = "ANNO_RIFERIMENTO")
	private BigDecimal annoRiferimento; */

	@Column(name = "CUAA")
	private String cuaa;

	@Column(name = "NOMINATIVO")
	private String nominativo;
	
	@Column(name = "ANNO_INIZIO")
	private Integer annoInizio;
	
	@Column(name = "ANNO_FINE")
	private Integer annoFine;

	public A4gtPiccoloAgricoltore() {
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getNominativo() {
		return this.nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	
	public Integer getAnnoInizio() {
		return annoInizio;
	}

	public void setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
	}

	public Integer getAnnoFine() {
		return annoFine;
	}

	public void setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
	}

}