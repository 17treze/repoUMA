package it.tndigitale.a4g.territorio.dto;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

public class FascicoloDto implements Serializable {

	private String cuaa;
	
	public String getCuaa() {
		return cuaa;
	}

	public FascicoloDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

}
