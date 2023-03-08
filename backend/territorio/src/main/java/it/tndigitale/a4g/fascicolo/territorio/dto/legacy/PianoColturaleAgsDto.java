package it.tndigitale.a4g.fascicolo.territorio.dto.legacy;

import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;

public class PianoColturaleAgsDto {
	
	private Long idColtura;
	private Integer superficieAccertata;
	private Integer superficieDichiarata;
	private String foglio;
	private String particella;
	private String subalterno;
	private Long idParticella;
	private String codiceNazionale;
	private String codiceProdotto;
	private String codiceDestinazioneUso;
	private String codiceUso;
	private String codiceQualita;
	private String codiceVarieta;
	private TitoloConduzione titoloConduzione;
	private Integer tipoAtto;
	private String descrizioneAtto;
	private CriterioMantenimento criterioMantenimento;

	public Long getIdColtura() {
		return idColtura;
	}
	public PianoColturaleAgsDto setIdColtura(Long idColtura) {
		this.idColtura = idColtura;
		return this;
	}
	public Integer getSuperficieAccertata() {
		return superficieAccertata;
	}
	public PianoColturaleAgsDto setSuperficieAccertata(Integer superficieAccertata) {
		this.superficieAccertata = superficieAccertata;
		return this;
	}
	public String getFoglio() {
		return foglio;
	}
	public PianoColturaleAgsDto setFoglio(String foglio) {
		this.foglio = foglio;
		return this;
	}
	public String getParticella() {
		return particella;
	}
	public PianoColturaleAgsDto setParticella(String particella) {
		this.particella = particella;
		return this;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public PianoColturaleAgsDto setSubalterno(String subalterno) {
		this.subalterno = subalterno;
		return this;
	}
	public Long getIdParticella() {
		return idParticella;
	}
	public PianoColturaleAgsDto setIdParticella(Long idParticella) {
		this.idParticella = idParticella;
		return this;
	}
	public String getCodiceProdotto() {
		return codiceProdotto;
	}
	public PianoColturaleAgsDto setCodiceProdotto(String codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
		return this;
	}
	public String getCodiceDestinazioneUso() {
		return codiceDestinazioneUso;
	}
	public PianoColturaleAgsDto setCodiceDestinazioneUso(String codiceDestinazioneUso) {
		this.codiceDestinazioneUso = codiceDestinazioneUso;
		return this;
	}
	public String getCodiceUso() {
		return codiceUso;
	}
	public PianoColturaleAgsDto setCodiceUso(String codiceUso) {
		this.codiceUso = codiceUso;
		return this;
	}
	public String getCodiceQualita() {
		return codiceQualita;
	}
	public PianoColturaleAgsDto setCodiceQualita(String codiceQualita) {
		this.codiceQualita = codiceQualita;
		return this;
	}
	public String getCodiceVarieta() {
		return codiceVarieta;
	}
	public PianoColturaleAgsDto setCodiceVarieta(String codiceVarieta) {
		this.codiceVarieta = codiceVarieta;
		return this;
	}
	public String getCodiceNazionale() {
		return codiceNazionale;
	}
	public PianoColturaleAgsDto setCodiceNazionale(String codiceNazionale) {
		this.codiceNazionale = codiceNazionale;
		return this;
	}
	public TitoloConduzione getTitoloConduzione() {
		return titoloConduzione;
	}
	public PianoColturaleAgsDto setTitoloConduzione(TitoloConduzione titoloConduzione) {
		this.titoloConduzione = titoloConduzione;
		return this;
	}
	public Integer getTipoAtto() {
		return tipoAtto;
	}
	public PianoColturaleAgsDto setTipoAtto(Integer tipoAtto) {
		this.tipoAtto = tipoAtto;
		return this;
	}
	public String getDescrizioneAtto() {
		return descrizioneAtto;
	}
	public PianoColturaleAgsDto setDescrizioneAtto(String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
		return this;
	}
	public CriterioMantenimento getCriterioMantenimento() {
		return criterioMantenimento;
	}
	public PianoColturaleAgsDto setCriterioMantenimento(CriterioMantenimento criterioMantenimento) {
		this.criterioMantenimento = criterioMantenimento;
		return this;
	}
	public Integer getSuperficieDichiarata() {
		return superficieDichiarata;
	}
	public PianoColturaleAgsDto setSuperficieDichiarata(Integer superficieDichiarata) {
		this.superficieDichiarata = superficieDichiarata;
		return this;
	}
}
