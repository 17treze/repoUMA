package it.tndigitale.a4gistruttoria.dto;

public class AZCountResult {

	private Long richiesti;
	private Long integrazioniAmmisibilita;
	private Long controlliSuperati;
	private Long controlliNonSuperati;
	private Long controlliLiquidabilitaNonSuperati;
	private Long nonAmmissibili;
	private Long liquidabili;
	private Long nonLiquidabili;
	private Long pagamentoNonAutorizzato;
	private Long pagamentoAutorizzato;
	private Long controlliIntersostegnoSuperati;

	public Long getRichiesti() {
		return richiesti;
	}

	public Long getIntegrazioniAmmisibilita() {
		return integrazioniAmmisibilita;
	}

	public Long getControlliSuperati() {
		return controlliSuperati;
	}

	public Long getControlliNonSuperati() {
		return controlliNonSuperati;
	}

	public Long getNonAmmissibili() {
		return nonAmmissibili;
	}

	public void setRichiesti(Long richiesti) {
		this.richiesti = richiesti;
	}

	public void setIntegrazioniAmmisibilita(Long integrazioniAmmisibilita) {
		this.integrazioniAmmisibilita = integrazioniAmmisibilita;
	}

	public void setControlliSuperati(Long controlliSuperati) {
		this.controlliSuperati = controlliSuperati;
	}

	public void setControlliNonSuperati(Long controlliNonSuperati) {
		this.controlliNonSuperati = controlliNonSuperati;
	}

	public void setNonAmmissibili(Long nonAmmissibili) {
		this.nonAmmissibili = nonAmmissibili;
	}

	public Long getLiquidabili() {
		return liquidabili;
	}

	public Long getNonLiquidabili() {
		return nonLiquidabili;
	}

	public Long getPagamentoNonAutorizzato() {
		return pagamentoNonAutorizzato;
	}

	public Long getPagamentoAutorizzato() {
		return pagamentoAutorizzato;
	}

	public Long getControlliIntersostegnoSuperati() {
		return controlliIntersostegnoSuperati;
	}

	public void setLiquidabili(Long liquidabili) {
		this.liquidabili = liquidabili;
	}

	public void setNonLiquidabili(Long nonLiquidabili) {
		this.nonLiquidabili = nonLiquidabili;
	}

	public void setPagamentoNonAutorizzato(Long pagamentoNonAutorizzato) {
		this.pagamentoNonAutorizzato = pagamentoNonAutorizzato;
	}

	public void setPagamentoAutorizzato(Long pagamentoAutorizzato) {
		this.pagamentoAutorizzato = pagamentoAutorizzato;
	}

	public void setControlliIntersostegnoSuperati(Long controlliIntersostegnoSuperati) {
		this.controlliIntersostegnoSuperati = controlliIntersostegnoSuperati;
	}

	public Long getControlliLiquidabilitaNonSuperati() {
		return controlliLiquidabilitaNonSuperati;
	}

	public void setControlliLiquidabilitaNonSuperati(Long controlliLiquidabilitaNonSuperati) {
		this.controlliLiquidabilitaNonSuperati = controlliLiquidabilitaNonSuperati;
	}

}
