package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipoConduzione;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name="A4GT_FABBRICATO")
@Inheritance(strategy = InheritanceType.JOINED)
public class FabbricatoModel  extends EntitaDominioFascicolo {
	private static final long serialVersionUID = -2773998811221628215L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FASCICOLO", referencedColumnName = "ID")
	@JoinColumn(name="ID_VALIDAZIONE_FASCICOLO", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOTTOTIPO", referencedColumnName = "ID")
	private SottotipoModel sottotipo;

	@Column(name="DENOMINAZIONE", length = 200)
	private String denominazione;

	@Column(name="INDIRIZZO", length = 100)
	private String indirizzo;

	@Column(name="COMUNE", length = 100)
	private String comune;
	
	@Column(name="VOLUME", length = 10)
	private Long volume;
	
	@Column(name="SUPERFICIE", length = 20)
	private Long superficie;
	
	@Column(name="DESCRIZIONE", length = 400)
	private String descrizione;
	
	@Column(name="TIPO_CONDUZIONE", length = 50)
	@Enumerated(EnumType.STRING)
	private TipoConduzione tipoConduzione;
	
	@OneToMany(mappedBy = "fabbricato", fetch = FetchType.LAZY)
	private List<DatiCatastaliModel> datiCatastali;
	
	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public FabbricatoModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}

	public SottotipoModel getSottotipo() {
		return sottotipo;
	}

	public FabbricatoModel setSottotipo(SottotipoModel sottotipo) {
		this.sottotipo = sottotipo;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public FabbricatoModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public FabbricatoModel setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public FabbricatoModel setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public Long getVolume() {
		return volume;
	}

	public FabbricatoModel setVolume(Long volume) {
		this.volume = volume;
		return this;
	}

	public Long getSuperficie() {
		return superficie;
	}

	public FabbricatoModel setSuperficie(Long superficie) {
		this.superficie = superficie;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public FabbricatoModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public List<DatiCatastaliModel> getDatiCatastali() {
		return datiCatastali;
	}

	public void setDatiCatastali(List<DatiCatastaliModel> datiCatastali) {
		this.datiCatastali = datiCatastali;
	}

	public TipoConduzione getTipoConduzione() {
		return tipoConduzione;
	}

	public FabbricatoModel setTipoConduzione(TipoConduzione tipoConduzione) {
		this.tipoConduzione = tipoConduzione;
		return this;
	}
}
