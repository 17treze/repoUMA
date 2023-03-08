package it.tndigitale.a4g.fascicolo.territorio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloParticellaCatastaleDto {

	private String comuneAmministrativo;
	private String codiceCatastale;
	private String sezione;
	private Integer foglio;
	private String particella;
	private String subalterno;
	private Integer superficieGraficaCondotta;
	private Integer percentualeConduzione;
	private String protocolloDocumentoConduzione;
	
	public ReportValidazioneFascicoloParticellaCatastaleDto(String comuneAmministrativo, 
			String codiceCatastale, String sezione, Integer foglio, String particella,
			String subalterno, Integer superficieGraficaCondotta, Integer percentualeConduzione,
			String protocolloDocumentoConduzione) {
		super();
		this.comuneAmministrativo = comuneAmministrativo;
		this.codiceCatastale = codiceCatastale;
		this.sezione = sezione;
		this.foglio = foglio;
		this.particella = particella;
		this.subalterno = subalterno;
		this.superficieGraficaCondotta = superficieGraficaCondotta;
		this.percentualeConduzione = percentualeConduzione;
		this.protocolloDocumentoConduzione = protocolloDocumentoConduzione;
	}
	
	public String getComuneAmministrativo() {
		return comuneAmministrativo;
	}
	public void setComuneAmministrativo(String comuneAmministrativo) {
		this.comuneAmministrativo = comuneAmministrativo;
	}
	public String getCodiceCatastale() {
		return codiceCatastale;
	}
	public void setCodiceCatastale(String codiceCatastale) {
		this.codiceCatastale = codiceCatastale;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public Integer getFoglio() {
		return foglio;
	}
	public void setFoglio(Integer foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public Integer getSuperficieGraficaCondotta() {
		return superficieGraficaCondotta;
	}
	public void setSuperficieGraficaCondotta(Integer superficieGraficaCondotta) {
		this.superficieGraficaCondotta = superficieGraficaCondotta;
	}
	public Integer getPercentualeConduzione() {
		return percentualeConduzione;
	}
	public void setPercentualeConduzione(Integer percentualeConduzione) {
		this.percentualeConduzione = percentualeConduzione;
	}
	public String getProtocolloDocumentoConduzione() {
		return protocolloDocumentoConduzione;
	}
	public void setProtocolloDocumentoConduzione(String protocolloDocumentoConduzione) {
		this.protocolloDocumentoConduzione = protocolloDocumentoConduzione;
	}

}
