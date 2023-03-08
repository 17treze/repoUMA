package it.tndigitale.a4gistruttoria.dto;

/**
 * Classe di modellazione di un semplice e generico controllo (check) del vecchio sistema.
 * 
 * @author Francesco M.
 *
 */
public class Controllo {
	
	private String descrizioneControllo;
	private Boolean esitoControllo;

	public String getDescrizioneControllo() {
		return descrizioneControllo;
	}

	public void setDescrizioneControllo(String descrizioneControllo) {
		this.descrizioneControllo = descrizioneControllo;
	}

	public Boolean getEsitoControllo() {
		return esitoControllo;
	}

	public void setEsitoControllo(Boolean esitoControllo) {
		this.esitoControllo = esitoControllo;
	}

}
