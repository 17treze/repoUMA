package it.tndigitale.a4g.fascicolo.mediator.dto;

import org.springframework.core.io.ByteArrayResource;

public class SchedaValidazioneFascicoloDto {

	private String codiceFiscale;

	private ByteArrayResource report;

	private Integer nextIdValidazione;
//	DEFAULT
	private TipoDetenzioneEnum tipoDetenzione = TipoDetenzioneEnum.MANDATO;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public SchedaValidazioneFascicoloDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}
	public ByteArrayResource getReport() {
		return report;
	}
	public SchedaValidazioneFascicoloDto setReport(ByteArrayResource report) {
		this.report = report;
		return this;
	}
	public Integer getNextIdValidazione() {
		return nextIdValidazione;
	}
	public void setNextIdValidazione(Integer nextIdValidazione) {
		this.nextIdValidazione = nextIdValidazione;
	}
	public TipoDetenzioneEnum getTipoDetenzione() {
		return tipoDetenzione;
	}
	public void setTipoDetenzione(TipoDetenzioneEnum tipoDetenzione) {
		this.tipoDetenzione = tipoDetenzione;
	}

}
