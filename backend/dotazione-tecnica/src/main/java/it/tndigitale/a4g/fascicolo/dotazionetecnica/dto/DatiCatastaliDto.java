package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.EsitoValidazioneParticellaCatastoEnum;

public class DatiCatastaliDto {

	private TipologiaParticellaCatastale tipologia;
	private String particella;
	private String denominatore;
	private Boolean inTrentino;
	private String sezione;
	private Integer foglio; // fuori provincia
	private String comune;
	private String sub;
	private String indirizzo;
	private Long superficie;
	private String consistenza;
	private String categoria;
	private String note; // al momento inutilizzato
	private EsitoValidazioneParticellaCatastoEnum esito;

	public TipologiaParticellaCatastale getTipologia() {
		return tipologia;
	}
	public DatiCatastaliDto setTipologia(TipologiaParticellaCatastale tipologia) {
		this.tipologia = tipologia;
		return this;
	}
	public String getParticella() {
		return particella;
	}
	public DatiCatastaliDto setParticella(String particella) {
		this.particella = particella;
		return this;
	}
	public String getDenominatore() {
		return denominatore;
	}
	public DatiCatastaliDto setDenominatore(String denominatore) {
		this.denominatore = denominatore;
		return this;
	}
	public Boolean getInTrentino() {
		return inTrentino;
	}
	public DatiCatastaliDto setInTrentino(Boolean inTrentino) {
		this.inTrentino = inTrentino;
		return this;
	}
	public String getSezione() {
		return sezione;
	}
	public DatiCatastaliDto setSezione(String sezione) {
		this.sezione = sezione;
		return this;
	}
	public Integer getFoglio() {
		return foglio;
	}
	public DatiCatastaliDto setFoglio(Integer foglio) {
		this.foglio = foglio;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public DatiCatastaliDto setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getSub() {
		return sub;
	}
	public DatiCatastaliDto setSub(String sub) {
		this.sub = sub;
		return this;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public DatiCatastaliDto setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}
	public Long getSuperficie() {
		return superficie;
	}
	public DatiCatastaliDto setSuperficie(Long superficie) {
		this.superficie = superficie;
		return this;
	}
	public String getConsistenza() {
		return consistenza;
	}
	public DatiCatastaliDto setConsistenza(String consistenza) {
		this.consistenza = consistenza;
		return this;
	}
	public String getCategoria() {
		return categoria;
	}
	public DatiCatastaliDto setCategoria(String categoria) {
		this.categoria = categoria;
		return this;
	}
	public String getNote() {
		return note;
	}
	public DatiCatastaliDto setNote(String note) {
		this.note = note;
		return this;
	}
	public EsitoValidazioneParticellaCatastoEnum getEsito() {
		return esito;
	}
	public DatiCatastaliDto setEsito(EsitoValidazioneParticellaCatastoEnum esito) {
		this.esito = esito;
		return this;
	}

}
