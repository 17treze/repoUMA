package it.tndigitale.a4g.fascicolo.territorio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloParcelleRiferimentoDto {

	private String codiceParcella;
	private String codiceSuolo;
	private String descrizioneSuolo;
	private Integer superficie;

	public String getCodiceParcella() {
		return codiceParcella;
	}
	public void setCodiceParcella(String codiceParcella) {
		this.codiceParcella = codiceParcella;
	}
	public String getCodiceSuolo() {
		return codiceSuolo;
	}
	public void setCodiceSuolo(String codiceSuolo) {
		this.codiceSuolo = codiceSuolo;
	}
	public String getDescrizioneSuolo() {
		return descrizioneSuolo;
	}
	public void setDescrizioneSuolo(String descrizioneSuolo) {
		this.descrizioneSuolo = descrizioneSuolo;
	}
	public Integer getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}
	public ReportValidazioneFascicoloParcelleRiferimentoDto(String codiceParcella,
			String codiceSuolo, String descrizioneSuolo, Integer superficie) {
		super();
		this.codiceParcella = codiceParcella;
		this.codiceSuolo = codiceSuolo;
		this.descrizioneSuolo = descrizioneSuolo;
		this.superficie = superficie;
	}

}
