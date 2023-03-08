package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.tndigitale.a4gistruttoria.config.ImportoBigDecimalJsonSerializer;

@JsonPropertyOrder({ "counter", "numeroDomanda", "numeroProtocollo", "nome", "cuaa", "domicilio", "quote",
		"dataPresentazione", 
		"quotaPremioAcz310", "quotaPremioAcz311", "quotaPremioAcz313", 
		"quotaPremioAcz322", "quotaPremioAcz315", "quotaPremioAcz316", 
		"quotaPremioAcz318", "quotaPremioAcz320", "quotaPremioAcz321", 
		"totalePremio" })
public class DomandaLiquidataStampaAcz extends DomandaLiquidataStampa {

	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz310;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz311;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz313;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz322;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz315;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz316;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz318;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz320;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioAcz321;

	public BigDecimal getQuotaPremioAcz310() {
		return quotaPremioAcz310;
	}

	public void setQuotaPremioAcz310(BigDecimal quotaPremioAcz310) {
		this.quotaPremioAcz310 = quotaPremioAcz310;
	}

	public BigDecimal getQuotaPremioAcz311() {
		return quotaPremioAcz311;
	}

	public void setQuotaPremioAcz311(BigDecimal quotaPremioAcz311) {
		this.quotaPremioAcz311 = quotaPremioAcz311;
	}
	
	public BigDecimal getQuotaPremioAcz313() {
		return quotaPremioAcz313;
	}

	public void setQuotaPremioAcz313(BigDecimal quotaPremioAcz313) {
		this.quotaPremioAcz313 = quotaPremioAcz313;
	}

	public BigDecimal getQuotaPremioAcz322() {
		return quotaPremioAcz322;
	}

	public void setQuotaPremioAcz322(BigDecimal quotaPremioAcz322) {
		this.quotaPremioAcz322 = quotaPremioAcz322;
	}
	
	public BigDecimal getQuotaPremioAcz315() {
		return quotaPremioAcz315;
	}

	public void setQuotaPremioAcz315(BigDecimal quotaPremioAcz315) {
		this.quotaPremioAcz315 = quotaPremioAcz315;
	}
	
	public BigDecimal getQuotaPremioAcz316() {
		return quotaPremioAcz316;
	}

	public void setQuotaPremioAcz316(BigDecimal quotaPremioAcz316) {
		this.quotaPremioAcz316 = quotaPremioAcz316;
	}
	
	public BigDecimal getQuotaPremioAcz318() {
		return quotaPremioAcz318;
	}

	public void setQuotaPremioAcz318(BigDecimal quotaPremioAcz318) {
		this.quotaPremioAcz318 = quotaPremioAcz318;
	}
	
	public BigDecimal getQuotaPremioAcz320() {
		return quotaPremioAcz320;
	}

	public void setQuotaPremioAcz320(BigDecimal quotaPremioAcz320) {
		this.quotaPremioAcz320 = quotaPremioAcz320;
	}
	
	public BigDecimal getQuotaPremioAcz321() {
		return quotaPremioAcz321;
	}

	public void setQuotaPremioAcz321(BigDecimal quotaPremioAcz321) {
		this.quotaPremioAcz321 = quotaPremioAcz321;
	}
}
