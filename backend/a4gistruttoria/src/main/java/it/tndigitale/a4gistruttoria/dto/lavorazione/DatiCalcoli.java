package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatiCalcoli {
	private List<VariabileCalcolo> variabiliCalcolo;

	public DatiCalcoli() {
		this.variabiliCalcolo = new ArrayList<>();
	}

	public List<VariabileCalcolo> getVariabiliCalcolo() {
		return variabiliCalcolo;
	}

	public List<VariabileCalcolo> getVariabiliCalcoloDaStampare() {
		return variabiliCalcolo.stream().filter(x -> x.getTipoVariabile().getStampa().equals(true)).collect(Collectors.toList());
	}

	public void setVariabiliCalcolo(List<VariabileCalcolo> variabiliCalcolo) {
		if (variabiliCalcolo != null) {
			this.variabiliCalcolo = variabiliCalcolo;
		}
	}
}
