package it.tndigitale.a4g.uma.dto.richiesta;

public class PresentaRichiestaDto {
	private String cuaa;
	private String codiceFiscaleRichiedente;

	public String getCuaa() {
		return cuaa;
	}
	public PresentaRichiestaDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getCodiceFiscaleRichiedente() {
		return codiceFiscaleRichiedente;
	}
	public PresentaRichiestaDto setCodiceFiscaleRichiedente(String codiceFiscaleRichiedente) {
		this.codiceFiscaleRichiedente = codiceFiscaleRichiedente;
		return this;
	}
}
