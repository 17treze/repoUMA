package it.tndigitale.a4g.framework.repository.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "MY_TEST_TABLE")
class MyTestEntity extends EntitaDominio {
	@Column(name="TEST")
	String test;

	@Override
	public String toString() {
		return "MyTestEntity [test=" + test + ", id=" + id + ", version=" + version + "]";
	}

}
