package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.locationtech.jts.geom.Geometry;

@Entity
@Immutable
@Table(name = "A4SV_SUOLO_DICH_LAVORAZIONE")
public class SuoloDichiaratoLavorazioneModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4494449467669305291L;

	@Id
	@Column(name = "ID_SUOLO_DICH")
	private Long id;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@Column(name = "ID_RICHIESTA")
	private Long idRichiesta;

	@Column(name = "STATO_RICHIESTA")
	@Enumerated(EnumType.STRING)
	private StatoRichiestaModificaSuolo statoRichiesta;

	private String cuaa;

	private String azienda;

	private Integer campagna;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_SUOLO_DICHIARATO")
	private TagDichiarato tipoSuoloDichiarato;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_SUOLO_RILEVATO")
	private TagRilevato tipoSuoloRilevato;

	@Column(name = "AREA_ORI")
	private Double areaOri;

	@Column(name = "CODI_RILE_DICHIARATO")
	private String codiRileDichiarato;

	@Column(name = "DESC_RILE_DICHIARATO")
	private String descRileDichiarato;

	@Column(name = "CODI_PROD_RILE_DICHIARATO")
	private String codiProdRileDichiarato;

	@Column(name = "DESC_PROD_RILE_DICHIARATO")
	private String descProdRileDichiarato;

	@Column(name = "CODI_RILE_PREVALENTE_DICH")
	private String codiRilePrevalenteDich;

	@Column(name = "DESC_RILE_PREVALENTE_DICH")
	private String descRilePrevalenteDich;

	@Column(name = "CODI_RILE_RILEVATO")
	private String codiRileRilevato;

	@Column(name = "DESC_RILE_RILEVATO")
	private String descRileRilevato;

	@Column(name = "CODI_PROD_RILE_RILEVATO")
	private String codiProdRileRilevato;

	@Column(name = "DESC_PROD_RILE_RILEVATO")
	private String descProdRileRilevato;

	@Column(name = "CODI_RILE_PREVALENTE_RIL")
	private String codiRilePrevalenteRil;

	@Column(name = "DESC_RILE_PREVALENTE_RIL")
	private String descRilePrevalenteRil;

	@Column(name = "COD_SEZIONE")
	private String codSezione;

	@Column(name = "ID_LAVORAZIONE")
	private Long idLavorazione;

	@Column(name = "STATO_LAVORAZIONE")
	@Enumerated(EnumType.STRING)
	private StatoLavorazioneSuolo statoLavorazione;

	@Column(name = "UTENTE_LAVORAZIONE")
	private String utenteLavorazione;

	@Column(name = "ESITO_DICHIARATO")
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public Geometry getShape() {
		return shape;
	}

	public Long getIdRichiesta() {
		return idRichiesta;
	}

	public StatoRichiestaModificaSuolo getStatoRichiesta() {
		return statoRichiesta;
	}

	public String getCuaa() {
		return cuaa;
	}

	public String getAzienda() {
		return azienda;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public TagDichiarato getTipoSuoloDichiarato() {
		return tipoSuoloDichiarato;
	}

	public TagRilevato getTipoSuoloRilevato() {
		return tipoSuoloRilevato;
	}

	public Double getAreaOri() {
		return areaOri;
	}

	public String getCodiRileDichiarato() {
		return codiRileDichiarato;
	}

	public String getDescRileDichiarato() {
		return descRileDichiarato;
	}

	public String getCodiProdRileDichiarato() {
		return codiProdRileDichiarato;
	}

	public String getDescProdRileDichiarato() {
		return descProdRileDichiarato;
	}

	public String getCodiRilePrevalenteDich() {
		return codiRilePrevalenteDich;
	}

	public String getDescRilePrevalenteDich() {
		return descRilePrevalenteDich;
	}

	public String getCodiRileRilevato() {
		return codiRileRilevato;
	}

	public String getDescRileRilevato() {
		return descRileRilevato;
	}

	public String getCodiProdRileRilevato() {
		return codiProdRileRilevato;
	}

	public String getDescProdRileRilevato() {
		return descProdRileRilevato;
	}

	public String getCodiRilePrevalenteRil() {
		return codiRilePrevalenteRil;
	}

	public String getDescRilePrevalenteRil() {
		return descRilePrevalenteRil;
	}

	public String getCodSezione() {
		return codSezione;
	}

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public StatoLavorazioneSuolo getStatoLavorazione() {
		return statoLavorazione;
	}

	public EsitoLavorazioneDichiarato getEsito() {
		return esito;
	}

	public String getUtenteLavorazione() {
		return utenteLavorazione;
	}

	public void setUtenteLavorazione(String utenteLavorazione) {
		this.utenteLavorazione = utenteLavorazione;
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
}
