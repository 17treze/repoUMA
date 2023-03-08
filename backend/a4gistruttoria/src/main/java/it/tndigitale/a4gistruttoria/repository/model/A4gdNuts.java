package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "A4GD_NUTS")
@NamedQuery(name = "A4gdNuts.findAll", query = "SELECT a FROM A4gdNuts a")
public class A4gdNuts extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "NUTS_1")
	private String nuts1;
	
	@Column(name = "CODICE_1")
	private String codice1;
	
	@Column(name = "NUTS_2")
	private String nuts2;
	
	@Column(name = "CODICE_2")
	private String codice2;
	
	@Column(name = "NUTS_3")
	private String nuts3;
	
	@Column(name = "CODICE_3")
	private String codice3;
	
	@Column(name = "SIGLA_PROVINCIA")
	private String siglaProvincia;

	public A4gdNuts() {
	}

	public String getNuts1() {
		return nuts1;
	}

	public void setNuts1(String nuts1) {
		this.nuts1 = nuts1;
	}

	public String getCodice1() {
		return codice1;
	}

	public void setCodice1(String codice1) {
		this.codice1 = codice1;
	}

	public String getNuts2() {
		return nuts2;
	}

	public void setNuts2(String nuts2) {
		this.nuts2 = nuts2;
	}

	public String getCodice2() {
		return codice2;
	}

	public void setCodice2(String codice2) {
		this.codice2 = codice2;
	}

	public String getNuts3() {
		return nuts3;
	}

	public void setNuts3(String nuts3) {
		this.nuts3 = nuts3;
	}

	public String getCodice3() {
		return codice3;
	}

	public void setCodice3(String codice3) {
		this.codice3 = codice3;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
}