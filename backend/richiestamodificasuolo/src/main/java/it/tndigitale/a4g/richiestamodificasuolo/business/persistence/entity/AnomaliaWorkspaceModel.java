package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4SR_ANOMALIA_WORKSPACE")
public class AnomaliaWorkspaceModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1323700354053935431L;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_WORKSPACE")
	private WorkspaceLavSuoloModel relAnomaliaWorkspace;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ANOMALIA")
	private AnomaliaValidazioneModel relAnomaliaValidazione;

	public WorkspaceLavSuoloModel getRelAnomaliaWorkspace() {
		return relAnomaliaWorkspace;
	}

	public void setRelAnomaliaWorkspace(WorkspaceLavSuoloModel relAnomaliaWorkspace) {
		this.relAnomaliaWorkspace = relAnomaliaWorkspace;
	}

	public AnomaliaValidazioneModel getRelAnomaliaValidazione() {
		return relAnomaliaValidazione;
	}

	public void setRelAnomaliaValidazione(AnomaliaValidazioneModel relAnomaliaValidazione) {
		this.relAnomaliaValidazione = relAnomaliaValidazione;
	}

}
