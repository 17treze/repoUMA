package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

public class InterventoSuperficieDto {
	
	public String intervento;
	public BigDecimal superficie;
	
	public String getIntervento() {
		return intervento;
	}
	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}
	public BigDecimal getSuperficie() {
		return superficie;
	}
	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}
	
	public static InterventoSuperficieDto build(final String intervento, final BigDecimal superficie) {
		InterventoSuperficieDto ret = new InterventoSuperficieDto();
		ret.intervento = intervento;
		ret.superficie = superficie;
		return ret;
	}
}
