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
public class AnomaliaValidazioneRelModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ANOMALIA")
	private AnomaliaValidazioneModel anomaliaValidazioneModel;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_WORKSPACE")
	private WorkspaceLavSuoloModel workspaceLavSuoloModel;

	public AnomaliaValidazioneModel getAnomaliaValidazioneModel() {
		return anomaliaValidazioneModel;
	}

	public void setAnomaliaValidazioneModel(AnomaliaValidazioneModel anomaliaValidazioneModel) {
		this.anomaliaValidazioneModel = anomaliaValidazioneModel;
	}

	public WorkspaceLavSuoloModel getWorkspaceLavSuoloModel() {
		return workspaceLavSuoloModel;
	}

	public void setWorkspaceLavSuoloModel(WorkspaceLavSuoloModel workspaceLavSuoloModel) {
		this.workspaceLavSuoloModel = workspaceLavSuoloModel;
	}

}
