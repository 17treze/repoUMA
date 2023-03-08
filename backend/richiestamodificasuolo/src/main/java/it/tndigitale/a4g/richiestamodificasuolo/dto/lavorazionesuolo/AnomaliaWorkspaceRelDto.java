package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;

public class AnomaliaWorkspaceRelDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnomaliaValidazioneDto anomaliaDto;
	private WorkspaceLavSuoloDto workspaceDto;

	public AnomaliaValidazioneDto getAnomaliaDto() {
		return anomaliaDto;
	}

	public void setAnomaliaDto(AnomaliaValidazioneDto anomaliaDto) {
		this.anomaliaDto = anomaliaDto;
	}

	public WorkspaceLavSuoloDto getWorkspaceDto() {
		return workspaceDto;
	}

	public void setWorkspaceDto(WorkspaceLavSuoloDto workspaceDto) {
		this.workspaceDto = workspaceDto;
	}

}
