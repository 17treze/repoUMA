package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;

public class PsrAnticipoDto extends PsrPagamentoDto {
	private Integer idDomandaPagamento;
	private BigDecimal anticipoRichiesto;
	private BigDecimal anticipoLiquidabile;
	
	public Integer getIdDomandaPagamento() {
		return idDomandaPagamento;
	}
	public void setIdDomandaPagamento(Integer idDomandaPagamento) {
		this.idDomandaPagamento = idDomandaPagamento;
	}
	public BigDecimal getAnticipoRichiesto() {
		return anticipoRichiesto;
	}
	public void setAnticipoRichiesto(BigDecimal anticipoRichiesto) {
		this.anticipoRichiesto = anticipoRichiesto;
	}
	public BigDecimal getAnticipoLiquidabile() {
		return anticipoLiquidabile;
	}
	public void setAnticipoLiquidabile(BigDecimal anticipoLiquidabile) {
		this.anticipoLiquidabile = anticipoLiquidabile;
	}
}