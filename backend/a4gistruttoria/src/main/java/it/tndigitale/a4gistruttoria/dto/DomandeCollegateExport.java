package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

public class DomandeCollegateExport implements Serializable {

	
	private static final long serialVersionUID = -3168720588944904867L;
	
	private List<String> cuaa;
	private TipoDomandaCollegata tipoDomanda;
	private List<Integer> anniCampagna;
	
	public List<String> getCuaa() {
		return cuaa;
	}
	public void setCuaa(List<String> cuaa) {
		this.cuaa = cuaa;
	}
	public TipoDomandaCollegata getTipoDomanda() {
		return tipoDomanda;
	}
	public void setTipoDomanda(TipoDomandaCollegata tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}
	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (this.cuaa == null || this.cuaa.isEmpty()) {
			sb.append("Il cuaa non può essere vuoto. ");
		}
		if (this.tipoDomanda == null) {
			sb.append("Il tipo domanda collegata non può essere vuota. ");
		}
		if(this.anniCampagna == null || this.anniCampagna.isEmpty()) {
			sb.append("La campagna non può essere vuota. ");
		}
		return sb.toString();
	}
	public List<Integer> getAnniCampagna() {
		return anniCampagna;
	}
	public void setAnniCampagna(List<Integer> anniCampagna) {
		this.anniCampagna = anniCampagna;
	}

}
