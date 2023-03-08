package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_ANOMALIA_VALIDAZIONE")
public class AnomaliaValidazioneModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3176076946679386046L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel lavorazioneSuoloInAnomaliaValidazione;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ANOMALIA")
	private TipoAnomaliaValidazione tipoAnomalia;

	@Column(name = "DETTAGLIO_ANOMALIA")
	private String dettaglioAnomalia;

	private Double area;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	// @OneToMany(mappedBy = "anomaliaValidazioneModel")
	// private Set<AnomaliaValidazioneRelModel> anomaliaRel;
	//
	//
	@ManyToMany
	@JoinTable(name = "A4SR_ANOMALIA_WORKSPACE", joinColumns = @JoinColumn(name = "ID_ANOMALIA"), inverseJoinColumns = @JoinColumn(name = "ID_WORKSPACE"))
	private Set<WorkspaceLavSuoloModel> anomaliaWorkspaceRel;

	public LavorazioneSuoloModel getLavorazioneSuoloInAnomaliaValidazione() {
		return lavorazioneSuoloInAnomaliaValidazione;
	}

	public void setLavorazioneSuoloInAnomaliaValidazione(LavorazioneSuoloModel lavorazioneSuoloInAnomaliaValidazione) {
		this.lavorazioneSuoloInAnomaliaValidazione = lavorazioneSuoloInAnomaliaValidazione;
	}

	public TipoAnomaliaValidazione getTipoAnomalia() {
		return tipoAnomalia;
	}

	public void setTipoAnomalia(TipoAnomaliaValidazione tipoAnomalia) {
		this.tipoAnomalia = tipoAnomalia;
	}

	public String getDettaglioAnomalia() {
		return dettaglioAnomalia;
	}

	public void setDettaglioAnomalia(String dettaglioAnomalia) {
		this.dettaglioAnomalia = dettaglioAnomalia;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public Set<WorkspaceLavSuoloModel> getAnomaliaWorkspaceRel() {
		return anomaliaWorkspaceRel;
	}

	public void setAnomaliaWorkspaceRel(Set<WorkspaceLavSuoloModel> anomaliaWorkspaceRel) {
		this.anomaliaWorkspaceRel = anomaliaWorkspaceRel;
	}

}
