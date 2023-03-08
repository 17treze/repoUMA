package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_TMP_WORKSPACE")
public class WorkspaceTmpModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971138772862757787L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel idLavorazioneWorkspaceTmp;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_USO_SUOLO", referencedColumnName = "COD_USO_SUOLO")
	private UsoSuoloModel codUsoSuoloWorkspaceTmp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO_COLT", referencedColumnName = "STATO_COLT")
	private StatoColtModel statoColtWorkspaceTmp;

	private String note;

	@Column(name = "DATA_ULTIMA_MODIFICA")
	private LocalDateTime dataUltimaModifica;

	@Column(name = "AREA")
	private Double area;

	private String istatp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRID")
	private GrigliaSuoloModel idGridWorkspace;

	public LavorazioneSuoloModel getIdLavorazioneWorkspaceTmp() {
		return idLavorazioneWorkspaceTmp;
	}

	public void setIdLavorazioneWorkspaceTmp(LavorazioneSuoloModel idLavorazioneWorkspaceTmp) {
		this.idLavorazioneWorkspaceTmp = idLavorazioneWorkspaceTmp;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public UsoSuoloModel getCodUsoSuoloWorkspaceTmp() {
		return codUsoSuoloWorkspaceTmp;
	}

	public void setCodUsoSuoloWorkspaceTmp(UsoSuoloModel codUsoSuoloWorkspaceTmp) {
		this.codUsoSuoloWorkspaceTmp = codUsoSuoloWorkspaceTmp;
	}

	public StatoColtModel getStatoColtWorkspaceTmp() {
		return statoColtWorkspaceTmp;
	}

	public void setStatoColtWorkspaceTmp(StatoColtModel statoColtWorkspaceTmp) {
		this.statoColtWorkspaceTmp = statoColtWorkspaceTmp;
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
