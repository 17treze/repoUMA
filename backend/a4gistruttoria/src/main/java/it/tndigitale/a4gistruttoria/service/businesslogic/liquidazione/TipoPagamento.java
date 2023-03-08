package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

enum TipoPagamento {

	// Pagamento Unico
	PAGAMENTO_UNICO("01"), 
	// Anticipo
	ANTICIPO("02"), 
	// Stato avanzamento
	STATO_AVANZAMENTO("03"), 
	// Saldo
	SALDO("04"),
	
	// Integrazione
	INTEGRAZIONE("05");

	private String codice;

	private TipoPagamento(String codice) {
		this.codice = codice;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public static TipoPagamento getTipoPagamentoByTipoIstruttoria(
			TipoIstruttoria tipoIstruttoria) {
		switch (tipoIstruttoria) {
		case ANTICIPO:
			return TipoPagamento.ANTICIPO;
		case SALDO:
			return TipoPagamento.SALDO;
		case INTEGRAZIONE:
			return TipoPagamento.INTEGRAZIONE;
		default:
			return TipoPagamento.SALDO;
		}
	}
}
