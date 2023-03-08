package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PsrAccontoSaldoDto extends PsrPagamentoDto {
	private Integer idDomandaPagamento;
	private BigDecimal costoInvestimentoRichiesto;
	private BigDecimal contributoRichiesto;
	private BigDecimal contributoLiquidabile;
	private List<PsrSanzioneDto> sanzioni;

	public Integer getIdDomandaPagamento() {
		return idDomandaPagamento;
	}

	public void setIdDomandaPagamento(Integer idDomandaPagamento) {
		this.idDomandaPagamento = idDomandaPagamento;
	}

	public BigDecimal getCostoInvestimentoRichiesto() {
		return costoInvestimentoRichiesto;
	}

	public void setCostoInvestimentoRichiesto(BigDecimal costoInvestimento) {
		this.costoInvestimentoRichiesto = costoInvestimento;
	}

	public BigDecimal getContributoRichiesto() {
		return contributoRichiesto;
	}

	public void setContributoRichiesto(BigDecimal contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}

	public BigDecimal getContributoLiquidabile() {
		return contributoLiquidabile;
	}

	public void setContributoLiquidabile(BigDecimal contributoLiquidabile) {
		this.contributoLiquidabile = contributoLiquidabile;
	}

	public List<PsrSanzioneDto> getSanzioni() {
		return sanzioni;
	}

	public void setSanzioni(List<PsrSanzioneDto> sanzioni) {
		this.sanzioni = sanzioni;
	}

	public void iterazioneSommatoriaPopolaTipoDomanda(
			final Integer idDomandaPagamento,
			BigDecimal pagamentoImportoRichiesto,
			BigDecimal pagamentoContributoRichiesto,
			BigDecimal pagamentoContributoAmmesso,
			final LocalDate pagamentoData,
			final String socNumeroDomanda) {
		if (pagamentoImportoRichiesto != null) {
			if (costoInvestimentoRichiesto == null) {
				costoInvestimentoRichiesto = BigDecimal.ZERO;
			}
			pagamentoImportoRichiesto = getCostoInvestimentoRichiesto().add(pagamentoImportoRichiesto);
		}
		if (pagamentoContributoRichiesto != null) {
			if (contributoRichiesto == null) {
				contributoRichiesto = BigDecimal.ZERO;
			}
			pagamentoContributoRichiesto = getContributoRichiesto().add(pagamentoContributoRichiesto);
		}
		if (pagamentoContributoAmmesso != null) {
			if (contributoLiquidabile == null) {
				contributoLiquidabile = BigDecimal.ZERO;
			}
			pagamentoContributoAmmesso = getContributoLiquidabile().add(pagamentoContributoAmmesso);
		}
		popolaTipoDomanda(idDomandaPagamento, pagamentoImportoRichiesto, pagamentoContributoRichiesto, pagamentoContributoAmmesso, pagamentoData, socNumeroDomanda);
	}
	
	public void popolaTipoDomanda(
			final Integer idDomandaPagamento,
			final BigDecimal pagamentoImportoRichiesto,
			final BigDecimal pagamentoContributoRichiesto,
			final BigDecimal pagamentoContributoAmmesso,
			final LocalDate pagamentoData,
			final String socNumeroDomanda) {
		setIdDomandaPagamento(idDomandaPagamento);
		setCostoInvestimentoRichiesto(pagamentoImportoRichiesto);
		setContributoRichiesto(pagamentoContributoRichiesto);
		setContributoLiquidabile(pagamentoContributoAmmesso);
		setData(pagamentoData);
		setSocNumeroDomanda(socNumeroDomanda);
	}
}
