package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ProduzioneLatte {
	private Map<String, Boolean> mesiProduzione;
	private Map<String, Boolean> mesiAnalisi;
	private BigDecimal tenoreCellule;
	private BigDecimal tenoreBatteri;
	private BigDecimal contenutoProteine;

	public BigDecimal getTenoreCellule() {
		return tenoreCellule;
	}

	public BigDecimal getTenoreBatteri() {
		return tenoreBatteri;
	}

	public BigDecimal getContenutoProteine() {
		return contenutoProteine;
	}

	public void setTenoreCellule(BigDecimal tenoreCellule) {
		this.tenoreCellule = tenoreCellule;
	}

	public void setTenoreBatteri(BigDecimal tenoreBatteri) {
		this.tenoreBatteri = tenoreBatteri;
	}

	public void setContenutoProteine(BigDecimal contenutoProteine) {
		this.contenutoProteine = contenutoProteine;
	}

	public Map<String, Boolean> getMesiProduzione() {
		return mesiProduzione;
	}

	public Map<String, Boolean> getMesiAnalisi() {
		return mesiAnalisi;
	}

	public void setMesiProduzione(Map<String, Boolean> mesiProduzione) {
		this.mesiProduzione = mesiProduzione;
	}

	public void setMesiAnalisi(Map<String, Boolean> mesiAnalisi) {
		this.mesiAnalisi = mesiAnalisi;
	}

}
