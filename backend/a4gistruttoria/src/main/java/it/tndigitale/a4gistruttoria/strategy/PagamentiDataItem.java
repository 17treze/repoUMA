package it.tndigitale.a4gistruttoria.strategy;

public final class PagamentiDataItem {
	private Double importoDeterminato;
	private Double importoLiquidato;
	private Double importoRichiesto;
	private Boolean pagamentoAutorizzato;
		
	public Double getImportoDeterminato() {
		return importoDeterminato;
	}
	
	public void setImportoDeterminato(Double importoDeterminato) {
		this.importoDeterminato = importoDeterminato;
	}
	
	public Double getImportoLiquidato() {
		return importoLiquidato;
	}
	
	public void setImportoLiquidato(Double importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}
	
	public Double getImportoRichiesto() {
		return importoRichiesto;
	}
	
	public void setImportoRichiesto(Double importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}
	
	public Boolean isPagamentoAutorizzato() {
		return pagamentoAutorizzato;
	}
	
	public void setPagamentoAutorizzato(Boolean isPagamentoAutorizzato) {
		this.pagamentoAutorizzato = isPagamentoAutorizzato;
	}
}
