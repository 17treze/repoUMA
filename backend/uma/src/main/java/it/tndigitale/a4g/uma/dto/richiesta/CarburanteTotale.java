package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;

public class CarburanteTotale<T> {

	public CarburanteTotale() {
		this.dati = new ArrayList<>();
		this.totale = new CarburanteDtoBuilder().build();
	}

	private List<T> dati;
	private CarburanteDto totale;

	public List<T> getDati() {
		return dati;
	}
	public CarburanteTotale<T> setDati(List<T> dati) {
		this.dati = dati;
		return this;
	}
	public CarburanteDto getTotale() {
		return totale;
	}
	public CarburanteTotale<T> setTotale(CarburanteDto totale) {
		this.totale = totale;
		return this;
	}
}
