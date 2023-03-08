package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class PassoLavorazioneStampa {

	private String tipoPassoLavorazione;
	private List<VariabileStampa> variabiliInput;
	private List<VariabileStampa> variabiliOutput;
	private int ordine;

	public String getTipoPassoLavorazione() {
		return tipoPassoLavorazione;
	}

	public void setTipoPassoLavorazione(String tipoPassoLavorazione) {
		this.tipoPassoLavorazione = tipoPassoLavorazione;
	}

	public List<VariabileStampa> getVariabiliInput() {
		return variabiliInput;
	}

	public void setVariabiliInput(List<VariabileStampa> variabiliInput) {
		this.variabiliInput = variabiliInput;
	}

	public List<VariabileStampa> getVariabiliOutput() {
		return variabiliOutput;
	}

	public void setVariabiliOutput(List<VariabileStampa> variabiliOutput) {
		this.variabiliOutput = variabiliOutput;
	}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

}
