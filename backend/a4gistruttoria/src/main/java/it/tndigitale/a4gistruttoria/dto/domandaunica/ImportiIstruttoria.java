package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ImportiIstruttoria {
	//	IMPCALCFIN
	private BigDecimal importoCalcolato;
	//	DFIMPLIQDIS 
	private BigDecimal importoAutorizzato;
	private BigDecimal importoDisciplina;
	private List<Debito> debitiRecuperati;
	//IMPORTO LIQUIDATO
	private Integer annoEsercizio;
	private Long progressivoCredito;
	private String numeroAutorizzazione;
	private LocalDateTime dataAutorizzazione;
	private Long progressivoPagamento;
	private BigDecimal importoLiquidato;
	
	public BigDecimal getImportoCalcolato() {
		return importoCalcolato;
	}
	public ImportiIstruttoria setImportoCalcolato(BigDecimal importoCalcolato) {
		this.importoCalcolato = importoCalcolato;
		return this;
	}
	public BigDecimal getImportoAutorizzato() {
		return importoAutorizzato;
	}
	public ImportiIstruttoria setImportoAutorizzato(BigDecimal importoAutorizzato) {
		this.importoAutorizzato = importoAutorizzato;
		return this;
	}
	public List<Debito> getDebitiRecuperati() {
		return debitiRecuperati;
	}
	public ImportiIstruttoria setDebitiRecuperati(List<Debito> debitiRecuperati) {
		this.debitiRecuperati = debitiRecuperati;
		return this;
	}
	public BigDecimal getImportoDisciplina() {
		return importoDisciplina;
	}
	public void setImportoDisciplina(BigDecimal importoDisciplina) {
		this.importoDisciplina = importoDisciplina;
	}
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public Long getProgressivoCredito() {
		return progressivoCredito;
	}
	public void setProgressivoCredito(Long progressivoCredito) {
		this.progressivoCredito = progressivoCredito;
	}
	public String getNumeroAutorizzazione() {
		return numeroAutorizzazione;
	}
	public void setNumeroAutorizzazione(String numeroAutorizzazione) {
		this.numeroAutorizzazione = numeroAutorizzazione;
	}
	public LocalDateTime getDataAutorizzazione() {
		return dataAutorizzazione;
	}
	public void setDataAutorizzazione(LocalDateTime dataAutorizzazione) {
		this.dataAutorizzazione = dataAutorizzazione;
	}
	public Long getProgressivoPagamento() {
		return progressivoPagamento;
	}
	public void setProgressivoPagamento(Long progressivoPagamento) {
		this.progressivoPagamento = progressivoPagamento;
	}
	public BigDecimal getImportoLiquidato() {
		return importoLiquidato;
	}
	public void setImportoLiquidato(BigDecimal importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}
}
