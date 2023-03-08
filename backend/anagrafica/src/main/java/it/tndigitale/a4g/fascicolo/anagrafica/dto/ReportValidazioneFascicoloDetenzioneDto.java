package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloDetenzioneDto {
	private TipoDetenzioneEnum tipoDetenzione;
	private ReportValidazioneFascicoloMandatoDto mandato;

	public ReportValidazioneFascicoloMandatoDto getMandato() {
		return mandato;
	}

	public void setMandato(ReportValidazioneFascicoloMandatoDto mandato) {
		this.mandato = mandato;
	}

	public TipoDetenzioneEnum getTipoDetenzione() {
		return tipoDetenzione;
	}

	public void setTipoDetenzione(TipoDetenzioneEnum tipoDetenzione) {
		this.tipoDetenzione = tipoDetenzione;
	}
	
}