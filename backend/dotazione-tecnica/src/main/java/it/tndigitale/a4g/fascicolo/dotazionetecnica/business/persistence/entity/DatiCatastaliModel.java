package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.EsitoValidazioneParticellaCatastoEnum;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name="A4GT_DATI_CATASTALI")
@Inheritance(strategy = InheritanceType.JOINED)
public class DatiCatastaliModel extends EntitaDominioFascicolo {

	private static final long serialVersionUID = 7602867370254031177L;

	@Column(name="TIPOLOGIA", length = 100)
	@Enumerated(EnumType.STRING)
	private TipologiaParticellaCatastale tipologia;
	
	@Column(name="PARTICELLA", length = 50)
	private String particella;
	
	@Column(name="DENOMINATORE", length = 50)
	private String denominatore;
	
	@Column(name="IN_TRENTINO", length = 50)
	private Boolean inTrentino;
	
	@Column(name="SEZIONE", length = 50)
	private String sezione;
	
	@Column(name="FOGLIO", length = 100)
	private Integer foglio;
	
	@Column(name="COMUNE", length = 50)
	private String comune;
	
	@Column(name="SUB", length = 50)
	private String sub;
	
	@Column(name="INDIRIZZO", length = 50)
	private String indirizzo;
	
	@Column(name="SUPERFICIE", length = 50)
	private Long superficie;
	
	@Column(name="CONSISTENZA", length = 100)
	private String consistenza;
	
	@Column(name="CATEGORIA", length = 50)
	private String categoria;
	
	@Column(name="NOTE", length = 50)
	private String note;
	
	@Column(name="ESITO", length = 20)
	@Enumerated(EnumType.STRING)
	private EsitoValidazioneParticellaCatastoEnum esito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FABBRICATO_ID", referencedColumnName = "ID")
	@JoinColumn(name="FABBRICATO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FabbricatoModel fabbricato;

	public TipologiaParticellaCatastale getTipologia() {
		return tipologia;
	}

	public void setTipologia(TipologiaParticellaCatastale tipologia) {
		this.tipologia = tipologia;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getDenominatore() {
		return denominatore;
	}

	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}

	public Boolean getInTrentino() {
		return inTrentino;
	}

	public void setInTrentino(Boolean inTrentino) {
		this.inTrentino = inTrentino;
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

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Long getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Long superficie) {
		this.superficie = superficie;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public EsitoValidazioneParticellaCatastoEnum getEsito() {
		return esito;
	}

	public void setEsito(EsitoValidazioneParticellaCatastoEnum esito) {
		this.esito = esito;
	}

	public FabbricatoModel getFabbricato() {
		return fabbricato;
	}

	public void setFabbricato(FabbricatoModel fabbricato) {
		this.fabbricato = fabbricato;
	}
	
	

}
