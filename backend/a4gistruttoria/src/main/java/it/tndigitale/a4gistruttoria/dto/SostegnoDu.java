package it.tndigitale.a4gistruttoria.dto;

public class SostegnoDu {
	private Long id;
	private String identificativo;
	private String descrizione;

	public Long getId() {
		return id;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}