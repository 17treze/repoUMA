package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

public class FabbricatoAgsDto {

	private Long idAgs;
	private Integer volume;
	private String tipoFabbricato;
	private String tipoFabbricatoCodice;
	private String titoloConduzione;
	private String particella;
	private String subalterno;
	private String comune;
	private String provincia;
	private String siglaProvincia;
	private Integer superficie;
	private Integer foglio;
	private String comuneCatastale;
	private String sezione;
	private String descrizione;
	private String note;

	public Integer getSuperficie() {
		return superficie;
	}
	public FabbricatoAgsDto setSuperficie(Integer superficie) {
		this.superficie = superficie;
		return this;
	}
	public Integer getFoglio() {
		return foglio;
	}
	public FabbricatoAgsDto setFoglio(Integer foglio) {
		this.foglio = foglio;
		return this;
	}
	public String getComuneCatastale() {
		return comuneCatastale;
	}
	public FabbricatoAgsDto setComuneCatastale(String comuneCatastale) {
		this.comuneCatastale = comuneCatastale;
		return this;
	}
	public String getSezione() {
		return sezione;
	}
	public FabbricatoAgsDto setSezione(String sezione) {
		this.sezione = sezione;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public FabbricatoAgsDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public Long getIdAgs() {
		return idAgs;
	}
	public FabbricatoAgsDto setIdAgs(Long idAgs) {
		this.idAgs = idAgs;
		return this;
	}
	public String getTipoFabbricato() {
		return tipoFabbricato;
	}
	public FabbricatoAgsDto setTipoFabbricato(String tipoFabbricato) {
		this.tipoFabbricato = tipoFabbricato;
		return this;
	}
	public Integer getVolume() {
		return volume;
	}
	public FabbricatoAgsDto setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}
	public String getTipoFabbricatoCodice() {
		return tipoFabbricatoCodice;
	}
	public FabbricatoAgsDto setTipoFabbricatoCodice(String tipoFabbricatoCodice) {
		this.tipoFabbricatoCodice = tipoFabbricatoCodice;
		return this;
	}
	public String getParticella() {
		return particella;
	}
	public FabbricatoAgsDto setParticella(String particella) {
		this.particella = particella;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public FabbricatoAgsDto setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public FabbricatoAgsDto setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		return this;
	}
	public String getTitoloConduzione() {
		return titoloConduzione;
	}
	public FabbricatoAgsDto setTitoloConduzione(String titoloConduzione) {
		this.titoloConduzione = titoloConduzione;
		return this;
	}
	public String getProvincia() {
		return provincia;
	}
	public FabbricatoAgsDto setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public FabbricatoAgsDto setSubalterno(String subalterno) {
		this.subalterno = subalterno;
		return this;
	}
	public String getNote() {
		return note;
	}
	public FabbricatoAgsDto setNote(String note) {
		this.note = note;
		return this;
	}
}
