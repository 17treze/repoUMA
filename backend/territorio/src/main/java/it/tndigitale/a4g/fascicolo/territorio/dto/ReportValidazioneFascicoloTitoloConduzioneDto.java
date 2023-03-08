package it.tndigitale.a4g.fascicolo.territorio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloTitoloConduzioneDto {

	private String tipologia;
	private String descrizione;
	private String protocollo;
	private String dataSottoscrizione;
	private String dataScadenza;

	public ReportValidazioneFascicoloTitoloConduzioneDto(TipoConduzioneEnum tipologia, String descrizione,
			String protocollo, String dataSottoscrizione, String dataScadenza) {
		super();
		this.tipologia = tipologia.label;
		this.descrizione = descrizione;
		this.protocollo = protocollo;
		this.dataSottoscrizione = dataSottoscrizione;
		this.dataScadenza = dataScadenza;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(String dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
	public String getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
}
