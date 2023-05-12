package it.tndigitale.a4g.uma.dto;

public class FabbricatoGruppiDto {
	
	private String codiceFabbricato;
	private String tipoFabbricato;
	private GruppoLavorazioneDto gruppoLavorazione;
	private Long id;
	
	public Long getId() {
		return this.id;
	}
	
	public FabbricatoGruppiDto setId(Long id) {
		this.id = id;
		return this;
	}
	
	public String getCodiceFabbricato() {
		return this.codiceFabbricato;
	}
	
	public FabbricatoGruppiDto setCodiceFabbricato(String codiceFabbricato) {
		this.codiceFabbricato = codiceFabbricato;
		return this;
	}
	
	public String getTipoFabbricato() {
		return this.tipoFabbricato;
	}
	
	public FabbricatoGruppiDto setTipoFabbricato(String tipoFabbricato) {
		this.tipoFabbricato = tipoFabbricato;
		return this;
	}
	
	public GruppoLavorazioneDto getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	
	public FabbricatoGruppiDto setGruppoLavorazione(GruppoLavorazioneDto gruppoLavorazione) {
		this.gruppoLavorazione = gruppoLavorazione;
		return this;
	}
	
}