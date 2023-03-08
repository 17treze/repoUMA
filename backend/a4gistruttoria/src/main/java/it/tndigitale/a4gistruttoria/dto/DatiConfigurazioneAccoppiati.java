package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class DatiConfigurazioneAccoppiati {

	private ConfigurazioneInstruttoriaZootecnicaFilter configurazioneInstruttoriaZootecnicaFilter;
	private List<InterventoDuPremio> interventoDuPremi;

	public ConfigurazioneInstruttoriaZootecnicaFilter getConfigurazioneInstruttoriaZootecnicaFilter() {
		return configurazioneInstruttoriaZootecnicaFilter;
	}

	public List<InterventoDuPremio> getInterventoDuPremi() {
		return interventoDuPremi;
	}

	public void setConfigurazioneInstruttoriaZootecnicaFilter(ConfigurazioneInstruttoriaZootecnicaFilter configurazioneInstruttoriaZootecnicaFilter) {
		this.configurazioneInstruttoriaZootecnicaFilter = configurazioneInstruttoriaZootecnicaFilter;
	}

	public void setInterventoDuPremi(List<InterventoDuPremio> interventoDuPremi) {
		this.interventoDuPremi = interventoDuPremi;
	}
}
