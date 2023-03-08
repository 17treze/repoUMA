package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

@Deprecated
public class Pagina<T> implements Serializable {

	private static final long serialVersionUID = -8946985138368760697L;

	private List<T> risultati;
	
	private long elementiTotali;

	public List<T> getRisultati() {
		return risultati;
	}

	public void setRisultati(List<T> risultati) {
		this.risultati = risultati;
	}

	public long getElementiTotali() {
		return elementiTotali;
	}

	public void setElementiTotali(long elementiTotali) {
		this.elementiTotali = elementiTotali;
	}
}
