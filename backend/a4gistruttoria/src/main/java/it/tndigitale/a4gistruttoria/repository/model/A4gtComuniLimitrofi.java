package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the A4GT_COMUNI_LIMITROFI database table.
 * 
 */
@Entity
@Table(name="A4GT_COMUNI_LIMITROFI")
@NamedQuery(name="A4gtComuniLimitrofi.findAll", query="SELECT a FROM A4gtComuniLimitrofi a")
public class A4gtComuniLimitrofi extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="COD_NAZ_COMUNE_CONFINANTE")
	private String codNazComuneConfinante;

	@Column(name="COD_NAZIONALE_COMUNE")
	private String codNazionaleComune;

	@Column(name="CODICE_COMUNE")
	private BigDecimal codiceComune;

	@Column(name="CODICE_COMUNE_CONFINANTE")
	private BigDecimal codiceComuneConfinante;

	@Column(name="CODICE_PROVINCIA")
	private BigDecimal codiceProvincia;

	@Column(name="CODICE_PROVINCIA_CONFINANTE")
	private BigDecimal codiceProvinciaConfinante;

	@Column(name="NOME_COMUNE")
	private String nomeComune;

	@Column(name="NOME_COMUNE_CONFINANTE")
	private String nomeComuneConfinante;

	public A4gtComuniLimitrofi() {
	}

	public String getCodNazComuneConfinante() {
		return this.codNazComuneConfinante;
	}

	public void setCodNazComuneConfinante(String codNazComuneConfinante) {
		this.codNazComuneConfinante = codNazComuneConfinante;
	}

	public String getCodNazionaleComune() {
		return this.codNazionaleComune;
	}

	public void setCodNazionaleComune(String codNazionaleComune) {
		this.codNazionaleComune = codNazionaleComune;
	}

	public BigDecimal getCodiceComune() {
		return this.codiceComune;
	}

	public void setCodiceComune(BigDecimal codiceComune) {
		this.codiceComune = codiceComune;
	}

	public BigDecimal getCodiceComuneConfinante() {
		return this.codiceComuneConfinante;
	}

	public void setCodiceComuneConfinante(BigDecimal codiceComuneConfinante) {
		this.codiceComuneConfinante = codiceComuneConfinante;
	}

	public BigDecimal getCodiceProvincia() {
		return this.codiceProvincia;
	}

	public void setCodiceProvincia(BigDecimal codiceProvincia) {
		this.codiceProvincia = codiceProvincia;
	}

	public BigDecimal getCodiceProvinciaConfinante() {
		return this.codiceProvinciaConfinante;
	}

	public void setCodiceProvinciaConfinante(BigDecimal codiceProvinciaConfinante) {
		this.codiceProvinciaConfinante = codiceProvinciaConfinante;
	}

	public String getNomeComune() {
		return this.nomeComune;
	}

	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}

	public String getNomeComuneConfinante() {
		return this.nomeComuneConfinante;
	}

	public void setNomeComuneConfinante(String nomeComuneConfinante) {
		this.nomeComuneConfinante = nomeComuneConfinante;
	}

}