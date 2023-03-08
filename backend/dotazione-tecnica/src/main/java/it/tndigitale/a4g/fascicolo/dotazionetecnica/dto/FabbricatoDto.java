package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

/**
 * Dto di comodo per visualizzare i dati generici di un fabbricato
 * 
 * @author G.De Vincentiis
 *
 */
public class FabbricatoDto {

	private Long id;
	private String tipologia; // descrizione tipologia fabbricato (macro)
	private String comune;
	private Long volume;
	private Long superficie;
	
	public Long getId() {
		return id;
	}

	public FabbricatoDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getTipologia() {
		return tipologia;
	}

	public FabbricatoDto setTipologia(String tipologia) {
		this.tipologia = tipologia;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public FabbricatoDto setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public Long getVolume() {
		return volume;
	}

	public FabbricatoDto setVolume(Long volume) {
		this.volume = volume;
		return this;
	}

	public Long getSuperficie() {
		return superficie;
	}

	public FabbricatoDto setSuperficie(Long superficie) {
		this.superficie = superficie;
		return this;
	}
}
