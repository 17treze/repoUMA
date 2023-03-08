package it.tndigitale.a4g.uma.dto.richiesta;

import java.math.BigDecimal;

import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;

public class FabbisognoDto {
	private BigDecimal quantita;
	private TipoCarburante carburante;

	public BigDecimal getQuantita() {
		return quantita;
	}
	public FabbisognoDto setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
		return this;
	}
	public TipoCarburante getCarburante() {
		return carburante;
	}
	public FabbisognoDto setCarburante(TipoCarburante carburante) {
		this.carburante = carburante;
		return this;
	}
}
