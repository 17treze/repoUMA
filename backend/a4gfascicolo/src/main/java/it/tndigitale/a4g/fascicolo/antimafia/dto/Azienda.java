package it.tndigitale.a4g.fascicolo.antimafia.dto;

/**
 * The dao class for the AZIENDA_AGRICOLA database table.
 * 
 */

public class Azienda {
	private Long id;
	private String cuaa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	@Override
	public java.lang.String toString() {
		return "Azienda [id=" + id + ", cuaa=" + cuaa + "]";
	}
}
