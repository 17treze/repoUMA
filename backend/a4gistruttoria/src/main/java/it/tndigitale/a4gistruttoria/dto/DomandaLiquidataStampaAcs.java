package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.tndigitale.a4gistruttoria.config.ImportoBigDecimalJsonSerializer;

@JsonPropertyOrder({ "counter", "numeroDomanda", "numeroProtocollo", "nome", "cuaa", "domicilio", "quote",
		"dataPresentazione", "quotaPremioAcsM8", "quotaPremioAcsM9", "quotaPremioAcsM10", 
		"quotaPremioAcsM11", "quotaPremioAcsM14", "quotaPremioAcsM15", "quotaPremioAcsM16", "quotaPremioAcsM17", "totalePremio" })
public class DomandaLiquidataStampaAcs extends DomandaLiquidataStampa {

	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM8;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM9;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM10;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM11;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM14;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM15;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM16;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcsM17;

	public BigDecimal getQuotaPremioAcsM8() {
		return quotaPremioAcsM8;
	}

	public void setQuotaPremioAcsM8(BigDecimal quotaPremioAcsM8) {
		this.quotaPremioAcsM8 = quotaPremioAcsM8;
	}

	public BigDecimal getQuotaPremioAcsM9() {
		return quotaPremioAcsM9;
	}

	public void setQuotaPremioAcsM9(BigDecimal quotaPremioAcsM9) {
		this.quotaPremioAcsM9 = quotaPremioAcsM9;
	}
	
	public BigDecimal getQuotaPremioAcsM10() {
		return quotaPremioAcsM10;
	}

	public void setQuotaPremioAcsM10(BigDecimal quotaPremioAcsM10) {
		this.quotaPremioAcsM10 = quotaPremioAcsM10;
	}

	public BigDecimal getQuotaPremioAcsM11() {
		return quotaPremioAcsM11;
	}

	public void setQuotaPremioAcsM11(BigDecimal quotaPremioAcsM11) {
		this.quotaPremioAcsM11 = quotaPremioAcsM11;
	}
	
	public BigDecimal getQuotaPremioAcsM14() {
		return quotaPremioAcsM14;
	}

	public void setQuotaPremioAcsM14(BigDecimal quotaPremioAcsM14) {
		this.quotaPremioAcsM14 = quotaPremioAcsM14;
	}
	
	public BigDecimal getQuotaPremioAcsM15() {
		return quotaPremioAcsM15;
	}

	public void setQuotaPremioAcsM15(BigDecimal quotaPremioAcsM15) {
		this.quotaPremioAcsM15 = quotaPremioAcsM15;
	}
	
	public BigDecimal getQuotaPremioAcsM16() {
		return quotaPremioAcsM16;
	}

	public void setQuotaPremioAcsM16(BigDecimal quotaPremioAcsM16) {
		this.quotaPremioAcsM16 = quotaPremioAcsM16;
	}
	
	public BigDecimal getQuotaPremioAcsM17() {
		return quotaPremioAcsM17;
	}

	public void setQuotaPremioAcsM17(BigDecimal quotaPremioAcsM17) {
		this.quotaPremioAcsM17 = quotaPremioAcsM17;
	}
}
