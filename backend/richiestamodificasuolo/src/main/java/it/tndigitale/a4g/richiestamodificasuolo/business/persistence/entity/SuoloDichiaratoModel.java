package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

/**
 * The persistent class for the A4ST_SUOLO_DICHIARATO database table.
 * 
 */
@Entity
@Table(name = "A4ST_SUOLO_DICHIARATO")
public class SuoloDichiaratoModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaModificaSuoloModel richiestaModificaSuolo;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel lavorazioneSuolo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "A4SR_SUPPORTO_TAG_TIPO_SUOLO", joinColumns = { @JoinColumn(name = "id_suolo_dichiarato", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "id_suolo_rilevato", referencedColumnName = "id") })
	private List<SuoloRilevatoModel> suoliRilevati;

	@Column(name = "ID_ISOLA")
	private Long idIsola;

	@Column(name = "COD_ISOLA")
	private String codIsola;

	@Column(name = "COD_SEZIONE")
	private String codSezione;

	@Column(name = "ID_SUOL_GIS")
	private Long idSuolGis;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_SUOLO_DICHIARATO")
	private TagDichiarato tipoSuoloDichiarato;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_SUOLO_RILEVATO")
	private TagRilevato tipoSuoloRilevato;

	@Column(name = "NOTE_CARICAMENTO")
	private String note;

	@Column(name = "ESITO")
	@Enumerated(EnumType.STRING)
	private EsitoLavorazioneDichiarato esito;

	@Column(name = "NUOVA_MODIFICA_BO")
	private Boolean nuovaModificaBo;

	@Column(name = "NUOVA_MODIFICA_CAA")
	private Boolean nuovaModificaCaa;

	@Column(name = "VISIBILE_IN_ORTOFOTO")
	private Boolean visibileInOrtofoto;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_INTERVENTO_COLTURALE")
	private TipoInterventoColturale tipoInterventoColturale;

	@Column(name = "INTERVENTO_INIZIO")
	private LocalDateTime interventoInizio;

	@Column(name = "INTERVENTO_FINE")
	private LocalDateTime interventoFine;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "CODI_RILE_DICHIARATO", referencedColumnName = "CODI_RILE"), @JoinColumn(name = "CODI_PROD_RILE_DICHIARATO", referencedColumnName = "CODI_PROD_RILE"), })
	private CodiRilePcgModel codiRilePcgDichiarato;

	public RichiestaModificaSuoloModel getRichiestaModificaSuolo() {
		return richiestaModificaSuolo;
	}

	public void setRichiestaModificaSuolo(RichiestaModificaSuoloModel richiestaModificaSuolo) {
		this.richiestaModificaSuolo = richiestaModificaSuolo;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public LavorazioneSuoloModel getLavorazioneSuolo() {
		return lavorazioneSuolo;
	}

	public void setLavorazioneSuolo(LavorazioneSuoloModel lavorazioneSuolo) {
		this.lavorazioneSuolo = lavorazioneSuolo;
	}

	public List<SuoloRilevatoModel> getSuoliRilevati() {
		return suoliRilevati;
	}

	public void setSuoliRilevati(List<SuoloRilevatoModel> suoliRilevati) {
		this.suoliRilevati = suoliRilevati;
	}

	public Long getIdIsola() {
		return idIsola;
	}

	public void setIdIsola(Long idIsola) {
		this.idIsola = idIsola;
	}

	public String getCodIsola() {
		return codIsola;
	}

	public void setCodIsola(String codIsola) {
		this.codIsola = codIsola;
	}

	public String getCodSezione() {
		return codSezione;
	}

	public void setCodSezione(String codSezione) {
		this.codSezione = codSezione;
	}

	public Long getIdSuolGis() {
		return idSuolGis;
	}

	public void setIdSuolGis(Long idSuolGis) {
		this.idSuolGis = idSuolGis;
	}

	public TagDichiarato getTipoSuoloDichiarato() {
		return tipoSuoloDichiarato;
	}

	public void setTipoSuoloDichiarato(TagDichiarato tipoSuoloDichiarato) {
		this.tipoSuoloDichiarato = tipoSuoloDichiarato;
	}

	public TagRilevato getTipoSuoloRilevato() {
		return tipoSuoloRilevato;
	}

	public void setTipoSuoloRilevato(TagRilevato tipoSuoloRilevato) {
		this.tipoSuoloRilevato = tipoSuoloRilevato;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getNuovaModificaBo() {
		return nuovaModificaBo;
	}

	public void setNuovaModificaBo(Boolean nuovaModificaBo) {
		this.nuovaModificaBo = nuovaModificaBo;
	}

	public Boolean getNuovaModificaCaa() {
		return nuovaModificaCaa;
	}

	public void setNuovaModificaCaa(Boolean nuovaModificaCaa) {
		this.nuovaModificaCaa = nuovaModificaCaa;
	}

	public Boolean getVisibileInOrtofoto() {
		return visibileInOrtofoto;
	}

	public void setVisibileInOrtofoto(Boolean visibileInOrtofoto) {
		this.visibileInOrtofoto = visibileInOrtofoto;
	}

	public TipoInterventoColturale getTipoInterventoColturale() {
		return tipoInterventoColturale;
	}

	public void setTipoInterventoColturale(TipoInterventoColturale tipoInterventoColturale) {
		this.tipoInterventoColturale = tipoInterventoColturale;
	}

	public LocalDateTime getInterventoInizio() {
		return interventoInizio;
	}

	public void setInterventoInizio(LocalDateTime interventoInizio) {
		this.interventoInizio = interventoInizio;
	}

	public LocalDateTime getInterventoFine() {
		return interventoFine;
	}

	public void setInterventoFine(LocalDateTime interventoFine) {
		this.interventoFine = interventoFine;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SuoloDichiaratoModel))
			return false;
		return id != null && id.equals(((SuoloDichiaratoModel) o).getId());
	}

	@Override
	public int hashCode() {
		return Optional.ofNullable(id).map(i -> i.hashCode()).orElseGet(() -> getClass().hashCode());
	}

	public EsitoLavorazioneDichiarato getEsito() {
		return esito;
	}

	public void setEsito(EsitoLavorazioneDichiarato esito) {
		this.esito = esito;
	}
}
