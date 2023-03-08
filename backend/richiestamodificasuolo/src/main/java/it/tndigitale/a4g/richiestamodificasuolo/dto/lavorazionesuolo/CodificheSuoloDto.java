package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.util.List;

public class CodificheSuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private List<CodDescCodificaSuoloDto> statoColtSuolo;
	private List<CodDescCodificaSuoloDto> codUsoSuolo;
	public List<CodDescCodificaSuoloDto> getStatoColtSuolo() {
		return statoColtSuolo;
	}
	public void setStatoColtSuolo(List<CodDescCodificaSuoloDto> statoColtSuolo) {
		this.statoColtSuolo = statoColtSuolo;
	}
	public List<CodDescCodificaSuoloDto> getCodUsoSuolo() {
		return codUsoSuolo;
	}
	public void setCodUsoSuolo(List<CodDescCodificaSuoloDto> codUsoSuolo) {
		this.codUsoSuolo = codUsoSuolo;
	}
		

}
