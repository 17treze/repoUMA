package it.tndigitale.a4gistruttoria.strategy;

import java.util.List;
import java.util.Map;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public final class SuperficiAccertateInputData extends SincronizzazioneInputData {
	
	private StatoIstruttoria statoLavorazioneSostegno;
	private List<PassoTransizioneModel> passiLavorazioneEntities;
	private Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo;

	public StatoIstruttoria getStatoLavorazioneSostegno() {
		return statoLavorazioneSostegno;
	}
	
	public void setStatoLavorazioneSostegno(StatoIstruttoria statoLavorazioneSostegno) {
		this.statoLavorazioneSostegno = statoLavorazioneSostegno;
	}
	
	public List<PassoTransizioneModel> getPassiLavorazioneEntities() {
		return passiLavorazioneEntities;
	}
	
	public void setPassiLavorazioneEntities(List<PassoTransizioneModel> passiLavorazioneEntities) {
		this.passiLavorazioneEntities = passiLavorazioneEntities;
	}
	
	public Map<TipoVariabile, VariabileCalcolo> getVariabiliCalcolo() {
		return variabiliCalcolo;
	}
	
	public void setVariabiliCalcolo(Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo) {
		this.variabiliCalcolo = variabiliCalcolo;
	}
}
