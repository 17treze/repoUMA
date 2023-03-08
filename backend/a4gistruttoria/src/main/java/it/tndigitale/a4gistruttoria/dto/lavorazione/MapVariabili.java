package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public class MapVariabili {
	private Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo;

	public MapVariabili() {
		variabiliCalcolo = new HashMap<TipoVariabile, VariabileCalcolo>();
	}

	public Map<TipoVariabile, VariabileCalcolo> getVariabiliCalcolo() {
		return variabiliCalcolo;
	}

	public void setVariabiliCalcolo(Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo) {
		this.variabiliCalcolo = variabiliCalcolo;
	}

	public void addElements(TipoVariabile... tipi) {
		if (variabiliCalcolo == null)
			variabiliCalcolo = new HashMap<TipoVariabile, VariabileCalcolo>();

		for (TipoVariabile tipo : tipi) {
			this.variabiliCalcolo.put(tipo, new VariabileCalcolo(tipo));
		}
	}

	public VariabileCalcolo get(TipoVariabile tipo) {
		return this.variabiliCalcolo.get(tipo);
	}

	// TODO: da togliere
	public void addElement(TipoVariabile tipo) {
		this.variabiliCalcolo.put(tipo, new VariabileCalcolo(tipo));
	}

	public void add(TipoVariabile tipo, VariabileCalcolo value) {
		this.variabiliCalcolo.put(tipo, value);
	}

	public void add(VariabileCalcolo value) {
		this.variabiliCalcolo.put(value.getTipoVariabile(), value);
	}

	// A2AC0147 se la variabile non è presente la inserisco
	public void setVal(TipoVariabile tipo, BigDecimal value) {
		if (!variabiliCalcolo.containsKey(tipo))
			this.addElement(tipo);
		variabiliCalcolo.get(tipo).setValNumber(value);
	}

	public void setVal(TipoVariabile tipo, Boolean value) {
		if (!variabiliCalcolo.containsKey(tipo))
			this.addElement(tipo);
		variabiliCalcolo.get(tipo).setValBoolean(value);
	}

	public void setVal(TipoVariabile tipo, String value) {
		if (!variabiliCalcolo.containsKey(tipo))
			this.addElement(tipo);
		variabiliCalcolo.get(tipo).setValString(value);
	}

	public void setVal(TipoVariabile tipo, ArrayList<ParticellaColtura> value) {
		if (!variabiliCalcolo.containsKey(tipo))
			this.addElement(tipo);
		variabiliCalcolo.get(tipo).setValList(value);
	}

	public BigDecimal min(TipoVariabile... tipi) {
		BigDecimal res = variabiliCalcolo.get(tipi[0]).getValNumber();

		for (TipoVariabile tipo : tipi) {
			res = res.min(variabiliCalcolo.get(tipo).getValNumber());
		}
		return res;
	}

	public BigDecimal min(TipoVariabile tipo1, BigDecimal value) {
		return variabiliCalcolo.get(tipo1).getValNumber().min(value);
	}

	public BigDecimal max(TipoVariabile tipo1, BigDecimal value2) {
		return variabiliCalcolo.get(tipo1).getValNumber().max(value2);
	}

	public BigDecimal max(TipoVariabile... tipi) {
		BigDecimal res = variabiliCalcolo.get(tipi[0]).getValNumber();

		for (TipoVariabile tipo : tipi) {
			res = res.max(variabiliCalcolo.get(tipo).getValNumber());
		}
		return res;
	}

	public BigDecimal multiply(TipoVariabile tipo1, BigDecimal value) {
		return variabiliCalcolo.get(tipo1).getValNumber().multiply(value);
	}

	public BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
		return value1.multiply(value2);
	}

	public BigDecimal multiply(TipoVariabile... tipi) {
		BigDecimal res = new BigDecimal(1);

		for (TipoVariabile tipo : tipi) {
			res = res.multiply(variabiliCalcolo.get(tipo).getValNumber());
		}
		return res;
	}

	public BigDecimal add(BigDecimal value1, BigDecimal value2) {
		return value1.add(value2);
	}

	public BigDecimal add(TipoVariabile... tipi) {
		BigDecimal res = new BigDecimal(0);

		for (TipoVariabile tipo : tipi) {
			if (variabiliCalcolo.get(tipo) != null && variabiliCalcolo.get(tipo).getValNumber() != null) {
				res = res.add(variabiliCalcolo.get(tipo).getValNumber());
			}
		}
		return res;
	}

	public BigDecimal subtract(TipoVariabile tipo1, BigDecimal value) {
		return variabiliCalcolo.get(tipo1).getValNumber().subtract(value);
	}

	public BigDecimal subtract(BigDecimal value, TipoVariabile tipo1) {
		return value.subtract(variabiliCalcolo.get(tipo1).getValNumber());
	}

	public BigDecimal divide(TipoVariabile tipo1, TipoVariabile tipo2) {
		return variabiliCalcolo.get(tipo1).getValNumber().divide(variabiliCalcolo.get(tipo2).getValNumber(), RoundingMode.HALF_UP);
	}

	public BigDecimal divide(TipoVariabile tipo1, BigDecimal value) {
		BigDecimal value2 = variabiliCalcolo.get(tipo1).getValNumber();
		return value2.divide(value, RoundingMode.HALF_UP);
	}

	public BigDecimal divide(BigDecimal value, TipoVariabile tipo1) {
		BigDecimal value2 = variabiliCalcolo.get(tipo1).getValNumber();
		return value.divide(value2, RoundingMode.HALF_UP);
	}

	public Boolean equals(TipoVariabile tipo1, TipoVariabile tipo2) {
		return variabiliCalcolo.get(tipo1).getValNumber().equals(variabiliCalcolo.get(tipo2).getValNumber());
	}

	public Boolean equals(TipoVariabile tipo1, BigDecimal value) {
		return variabiliCalcolo.get(tipo1).getValNumber().equals(value);
	}

	public BigDecimal subtract(TipoVariabile... tipo) {
		Stream<TipoVariabile> stream = StreamSupport.stream(Spliterators.spliterator(tipo, 0), false);
		List<TipoVariabile> tipoVariabiles = stream.collect(Collectors.toList());
		Optional<BigDecimal> returnVal = tipoVariabiles.stream().map(tipoIn -> variabiliCalcolo.get(tipoIn).getValNumber()).reduce(BigDecimal::subtract);
		return returnVal.orElseThrow(() -> new ArithmeticException("Errore nella sottrazione delle variabili calcolo"));
	}

	public void remove(TipoVariabile tipo) {
		this.variabiliCalcolo.remove(tipo);
	}
}
