package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.io.Serializable;

public class SintesiPagamento implements Serializable{

	public SintesiPagamento(Double importoCalcolato, Double importoLiquidato) {
		super();
		this.importoCalcolato = importoCalcolato;
		this.importoLiquidato = importoLiquidato;
	}
	private static final long serialVersionUID = 7904915164388146435L;
	
	private Double importoCalcolato;
	private Double importoLiquidato;
	
	public Double getImportoCalcolato() {
		return importoCalcolato;
	}
	public void setImportoCalcolato(Double importoCalcolato) {
		this.importoCalcolato = importoCalcolato;
	}
	public Double getImportoLiquidato() {
		return importoLiquidato;
	}
	public void setImportoLiquidato(Double importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}
	

}
