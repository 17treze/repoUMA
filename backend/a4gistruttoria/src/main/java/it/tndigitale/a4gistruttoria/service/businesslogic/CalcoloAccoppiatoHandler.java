package it.tndigitale.a4gistruttoria.service.businesslogic;

import it.tndigitale.a4gistruttoria.dto.InfoIstruttoriaDomanda;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;

public class CalcoloAccoppiatoHandler {

	private MapVariabili variabiliInput;
	private MapVariabili variabiliOutput;
	private InfoIstruttoriaDomanda infoIstruttoriaDomanda;

	public CalcoloAccoppiatoHandler() {
		super();
		this.variabiliInput = new MapVariabili();
		this.variabiliOutput = new MapVariabili();
	}

	public CalcoloAccoppiatoHandler(MapVariabili variabiliInput, MapVariabili variabiliOutput) {
		super();
		this.variabiliInput = variabiliInput;
		this.variabiliOutput = variabiliOutput;
	}

	public MapVariabili getVariabiliInput() {
		return variabiliInput;
	}

	public void setVariabiliInput(MapVariabili variabiliInput) {
		this.variabiliInput = variabiliInput;
	}

	public MapVariabili getVariabiliOutput() {
		return variabiliOutput;
	}

	public void setVariabiliOutput(MapVariabili variabiliOutput) {
		this.variabiliOutput = variabiliOutput;
	}

	public InfoIstruttoriaDomanda getInfoIstruttoriaDomanda() {
		return infoIstruttoriaDomanda;
	}

	public void setInfoIstruttoriaDomanda(InfoIstruttoriaDomanda infoIstruttoriaDomanda) {
		this.infoIstruttoriaDomanda = infoIstruttoriaDomanda;
	}

	public Boolean hasVariabiliInput() {
		return this.getVariabiliInput() != null
				&& !this.getVariabiliInput().getVariabiliCalcolo().isEmpty();
	}

	public Boolean hasVariabiliOutput() {
		return this.getVariabiliOutput() != null
				&& !this.getVariabiliOutput().getVariabiliCalcolo().isEmpty();
	}
}
