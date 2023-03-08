package it.tndigitale.a4g.fascicolo.antimafia.dto;

/**
 * The dao class for the STATO_DICHIARAZIONE_ANTIMAFIA database table.
 * 
 */
public class StatoDic {
	private Long id;
	private String identificativo;
	private String descrizione;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}