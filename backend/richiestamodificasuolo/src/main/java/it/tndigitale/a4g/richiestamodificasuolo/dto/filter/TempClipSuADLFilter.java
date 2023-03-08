package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;

public class TempClipSuADLFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;

	private Long idLavorazione;
	private String posizionePoligono;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}

	@Override
	public String toString() {
		return String.format("SuoloFilter [id=%s,idLavorazione=%s,,posizionePoligono=%s]", id, idLavorazione, posizionePoligono);
	}

	public String getPosizionePoligono() {
		return posizionePoligono;
	}

	public void setPosizionePoligono(String posizionePoligono) {
		this.posizionePoligono = posizionePoligono;
	}
}