package it.tndigitale.a4g.fascicolo.mediator.dto;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ReportValidazioneTerreniAgsDto;

public class ReportValidazioneFascicoloDto {

	private TipoSchedaValidazioneEnum tipoSchedaValidazione;
	private ReportValidazioneDto reportValidazioneAnagrafica;
	private it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneDto reportValidazioneZootecnia;
	private it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto reportValidazioneDotazioneTecnica;
	private ReportValidazioneTerreniAgsDto reportValidazioneTerreni;
	private it.tndigitale.a4g.utente.client.model.ReportValidazioneDto reportValidazioneUtente;

	public TipoSchedaValidazioneEnum getTipoSchedaValidazione() {
		return tipoSchedaValidazione;
	}

	public ReportValidazioneFascicoloDto setTipoSchedaValidazione(TipoSchedaValidazioneEnum tipoSchedaValidazione) {
		this.tipoSchedaValidazione = tipoSchedaValidazione;
		return this;
	}

	public ReportValidazioneDto getReportValidazioneAnagrafica() {
		return reportValidazioneAnagrafica;
	}
	public ReportValidazioneFascicoloDto setReportValidazioneAnagrafica(ReportValidazioneDto reportValidazioneAnagrafica) {
		this.reportValidazioneAnagrafica = reportValidazioneAnagrafica;
		return this;
	}
	public it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneDto getReportValidazioneZootecnia() {
		return reportValidazioneZootecnia;
	}
	public ReportValidazioneFascicoloDto setReportValidazioneZootecnia(it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneDto reportValidazioneZootecnia) {
		this.reportValidazioneZootecnia = reportValidazioneZootecnia;
		return this;
	}

	public ReportValidazioneTerreniAgsDto getReportValidazioneTerreni() {
		return reportValidazioneTerreni;
	}

	public ReportValidazioneFascicoloDto setReportValidazioneTerreni(ReportValidazioneTerreniAgsDto reportValidazioneTerreni) {
		this.reportValidazioneTerreni = reportValidazioneTerreni;
		return this;
	}

	public it.tndigitale.a4g.utente.client.model.ReportValidazioneDto getReportValidazioneUtente() {
		return reportValidazioneUtente;
	}

	public ReportValidazioneFascicoloDto setReportValidazioneUtente(it.tndigitale.a4g.utente.client.model.ReportValidazioneDto reportValidazioneUtente) {
		this.reportValidazioneUtente = reportValidazioneUtente;
		return this;
	}

	public it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto getReportValidazioneDotazioneTecnica() {
		return reportValidazioneDotazioneTecnica;
	}

	public ReportValidazioneFascicoloDto setReportValidazioneDotazioneTecnica(it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto reportValidazioneDotazioneTecnica) {
		this.reportValidazioneDotazioneTecnica = reportValidazioneDotazioneTecnica;
		return this;
	}
	
	
}
