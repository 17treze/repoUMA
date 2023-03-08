package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloCciaaDto {
	private String dataIscrizione;
	private String codiceRea;
	private String provinciaRea;
//	private List<ReportValidazioneFascicoloIscrizioneSezioneDto> iscrizioni;
	
	public String getDataIscrizione() {
		return dataIscrizione;
	}
	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
	public String getCodiceRea() {
		return codiceRea;
	}
	public void setCodiceRea(String codiceRea) {
		this.codiceRea = codiceRea;
	}
	public String getProvinciaRea() {
		return provinciaRea;
	}
	public void setProvinciaRea(String provinciaRea) {
		this.provinciaRea = provinciaRea;
	}
//	public List<ReportValidazioneFascicoloIscrizioneSezioneDto> getIscrizioni() {
//		return iscrizioni;
//	}
//	public void setIscrizioni(List<ReportValidazioneFascicoloIscrizioneSezioneDto> iscrizioni) {
//		this.iscrizioni = iscrizioni;
//	}
	
}