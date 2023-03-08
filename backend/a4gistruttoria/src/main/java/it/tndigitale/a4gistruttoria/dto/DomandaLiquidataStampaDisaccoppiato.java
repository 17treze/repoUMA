package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.tndigitale.a4gistruttoria.config.ImportoBigDecimalJsonSerializer;

@JsonPropertyOrder({ "counter", "numeroDomanda", "numeroProtocollo", "nome", "cuaa", "domicilio", "quote", "dataPresentazione", "quotaPremioBps", "quotaPremioGreening", "quotaPremioGiovane","totalePremio" })
public class DomandaLiquidataStampaDisaccoppiato extends DomandaLiquidataStampa{

	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioBps;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioGreening;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal quotaPremioGiovane;

	public BigDecimal getQuotaPremioBps() {
		return quotaPremioBps;
	}

	public void setQuotaPremioBps(BigDecimal quotaPremioBps) {
		this.quotaPremioBps = quotaPremioBps;
	}

	public BigDecimal getQuotaPremioGreening() {
		return quotaPremioGreening;
	}

	public void setQuotaPremioGreening(BigDecimal quotaPremioGreening) {
		this.quotaPremioGreening = quotaPremioGreening;
	}

	public BigDecimal getQuotaPremioGiovane() {
		return quotaPremioGiovane;
	}

	public void setQuotaPremioGiovane(BigDecimal quotaPremioGiovane) {
		this.quotaPremioGiovane = quotaPremioGiovane;
	}

}
