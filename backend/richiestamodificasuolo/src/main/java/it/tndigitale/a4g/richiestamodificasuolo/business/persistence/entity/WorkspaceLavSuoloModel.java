package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_WORKSPACE_LAV_SUOLO")
public class WorkspaceLavSuoloModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7226172395164674693L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel idLavorazioneWorkspaceLavSuolo;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_USO_SUOLO", referencedColumnName = "COD_USO_SUOLO")
	private UsoSuoloModel codUsoSuoloWorkspaceLavSuolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO_COLT", referencedColumnName = "STATO_COLT")
	private StatoColtModel statoColtWorkspaceLavSuolo;

	private String note;

	@Column(name = "DATA_ULTIMA_MODIFICA")
	private LocalDateTime dataUltimaModifica;

	@Column(name = "AREA")
	private Double area;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "anomaliaWorkspaceRel")
	private Set<AnomaliaValidazioneModel> anomaliaValidazione;

	private String istatp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRID")
	private GrigliaSuoloModel idGridWorkspace;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE_ORIG")
	private LavorazioneSuoloModel idLavorazioneOrigWorkspaceLavSuolo;

	private String sorgente;

	// @OneToMany(mappedBy = "workspaceLavSuoloModel")
	// private Set<AnomaliaValidazioneRelModel> workspaceAnomaliaRel;

	public LavorazioneSuoloModel getIdLavorazioneWorkspaceLavSuolo() {
		return idLavorazioneWorkspaceLavSuolo;
	}

	public void setIdLavorazioneWorkspaceLavSuolo(LavorazioneSuoloModel idLavorazioneWorkspaceLavSuolo) {
		this.idLavorazioneWorkspaceLavSuolo = idLavorazioneWorkspaceLavSuolo;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public UsoSuoloModel getCodUsoSuoloWorkspaceLavSuolo() {
		return codUsoSuoloWorkspaceLavSuolo;
	}

	public void setCodUsoSuoloWorkspaceLavSuolo(UsoSuoloModel codUsoSuoloWorkspaceLavSuolo) {
		this.codUsoSuoloWorkspaceLavSuolo = codUsoSuoloWorkspaceLavSuolo;
	}

	public StatoColtModel getStatoColtWorkspaceLavSuolo() {
		return statoColtWorkspaceLavSuolo;
	}

	public void setStatoColtWorkspaceLavSuolo(StatoColtModel statoColtWorkspaceLavSuolo) {
		this.statoColtWorkspaceLavSuolo = statoColtWorkspaceLavSuolo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDateTime getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Set<AnomaliaValidazioneModel> getAnomaliaValidazione() {
		return anomaliaValidazione;
	}

	public void setAnomaliaValidazione(Set<AnomaliaValidazioneModel> anomaliaValidazione) {
		this.anomaliaValidazione = anomaliaValidazione;
	}

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public GrigliaSuoloModel getIdGridWorkspace() {
		return idGridWorkspace;
	}

	public void setIdGridWorkspace(GrigliaSuoloModel idGridWorkspace) {
		this.idGridWorkspace = idGridWorkspace;
	}

	public LavorazioneSuoloModel getIdLavorazioneOrigWorkspaceLavSuolo() {
		return idLavorazioneOrigWorkspaceLavSuolo;
	}

	public void setIdLavorazioneOrigWorkspaceLavSuolo(LavorazioneSuoloModel idLavorazioneOrigWorkspaceLavSuolo) {
		this.idLavorazioneOrigWorkspaceLavSuolo = idLavorazioneOrigWorkspaceLavSuolo;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkspaceLavSuoloModel))
			return false;
		return id != null && id.equals(((WorkspaceLavSuoloModel) o).getId());
	}

	@Override
	public int hashCode() {
		return Optional.ofNullable(id).map(i -> i.hashCode()).orElseGet(() -> getClass().hashCode());
	}
}
