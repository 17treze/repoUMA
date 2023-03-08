package it.tndigitale.a4gistruttoria.util;

public class OrdineVariabile {

	private TipologiaPassoTransizione passo;
	private Integer ordine;

	public OrdineVariabile(TipologiaPassoTransizione passo, Integer ordine) {
		this.passo = passo;
		this.ordine = ordine;
	}

	public TipologiaPassoTransizione getPasso() {
		return passo;
	}

	public void setPasso(TipologiaPassoTransizione passo) {
		this.passo = passo;
	}

	public Integer getOrdine() {
		return ordine;
	}

	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

}
