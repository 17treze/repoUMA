package it.tndigitale.a4g.fascicolo.territorio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.business.service.TipoConduzioneEnum;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloSchedarioFrutticoloDto {

	/*
	 *  Schedario frutticolo
	 *	Lista di unità arboree. Per ogni unità:
	 */
	
	private String provincia;
	private String comune;
	private Integer foglio;
	private String particella;
	private String subalterno;
	private String tipoUnitaArborea;
	private Integer progressivoUnitaArborea;
	private Integer superficie;
	private String varieta;
	private String annoImpianto;
	private String sestoImpianto;
	private String formaAllevamento;
	private String numeroPiante;
	private String copertura;
	private String antibrina;
	private String antigrandine;
	
	public ReportValidazioneFascicoloSchedarioFrutticoloDto(String provincia, String comune, Integer foglio,
			String particella, String subalterno, String tipoUnitaArborea, Integer progressivoUnitaArborea,
			Integer superficie, String varieta, String annoImpianto, String sestoImpianto, String formaAllevamento,
			String numeroPiante, String copertura, String antibrina, String antigrandine) {
		super();
		this.provincia = provincia;
		this.comune = comune;
		this.foglio = foglio;
		this.particella = particella;
		this.subalterno = subalterno;
		this.tipoUnitaArborea = tipoUnitaArborea;
		this.progressivoUnitaArborea = progressivoUnitaArborea;
		this.superficie = superficie;
		this.varieta = varieta;
		this.annoImpianto = annoImpianto;
		this.sestoImpianto = sestoImpianto;
		this.formaAllevamento = formaAllevamento;
		this.numeroPiante = numeroPiante;
		this.copertura = copertura;
		this.antibrina = antibrina;
		this.antigrandine = antigrandine;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
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
	public String getTipoUnitaArborea() {
		return tipoUnitaArborea;
	}
	public void setTipoUnitaArborea(String tipoUnitaArborea) {
		this.tipoUnitaArborea = tipoUnitaArborea;
	}
	public Integer getProgressivoUnitaArborea() {
		return progressivoUnitaArborea;
	}
	public void setProgressivoUnitaArborea(Integer progressivoUnitaArborea) {
		this.progressivoUnitaArborea = progressivoUnitaArborea;
	}
	public Integer getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}
	public String getVarieta() {
		return varieta;
	}
	public void setVarieta(String varieta) {
		this.varieta = varieta;
	}
	public String getAnnoImpianto() {
		return annoImpianto;
	}
	public void setAnnoImpianto(String annoImpianto) {
		this.annoImpianto = annoImpianto;
	}
	public String getSestoImpianto() {
		return sestoImpianto;
	}
	public void setSestoImpianto(String sestoImpianto) {
		this.sestoImpianto = sestoImpianto;
	}
	public String getFormaAllevamento() {
		return formaAllevamento;
	}
	public void setFormaAllevamento(String formaAllevamento) {
		this.formaAllevamento = formaAllevamento;
	}
	public String getNumeroPiante() {
		return numeroPiante;
	}
	public void setNumeroPiante(String numeroPiante) {
		this.numeroPiante = numeroPiante;
	}
	public String getCopertura() {
		return copertura;
	}
	public void setCopertura(String copertura) {
		this.copertura = copertura;
	}
	public String getAntibrina() {
		return antibrina;
	}
	public void setAntibrina(String antibrina) {
		this.antibrina = antibrina;
	}
	public String getAntigrandine() {
		return antigrandine;
	}
	public void setAntigrandine(String antigrandine) {
		this.antigrandine = antigrandine;
	}
		
}
