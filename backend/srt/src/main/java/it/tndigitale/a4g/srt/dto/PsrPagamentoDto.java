package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PsrPagamentoDto {
	private LocalDate data;
	private String socNumeroDomanda;
	
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public String getSocNumeroDomanda() {
		return socNumeroDomanda;
	}
	public void setSocNumeroDomanda(String socNumeroDomanda) {
		this.socNumeroDomanda = socNumeroDomanda;
	}

	public static BigDecimal addIfNotNull(BigDecimal valueBase, final BigDecimal valueToAdd) {
		if (valueBase == null) {
			valueBase = BigDecimal.ZERO;
		}
		if (valueToAdd != null) {
			valueBase = valueBase.add(valueToAdd);			
		}
		return valueBase;
	}
}
