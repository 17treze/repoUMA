package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

public class TipologiaDto {

	private Long id;
	private String descrizione;

	public Long getId() {
		return id;
	}
	public TipologiaDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public TipologiaDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
}
