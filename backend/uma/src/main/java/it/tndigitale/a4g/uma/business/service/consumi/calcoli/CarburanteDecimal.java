package it.tndigitale.a4g.uma.business.service.consumi.calcoli;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;


public class CarburanteDecimal {

	private BigDecimal benzina = BigDecimal.ZERO;
	private BigDecimal gasolio = BigDecimal.ZERO;
	private BigDecimal gasolioSerre = BigDecimal.ZERO;

	public BigDecimal getBenzina() {
		return benzina;
	}
	public CarburanteDecimal setBenzina(BigDecimal benzina) {
		this.benzina = benzina;
		return this;
	}
	public BigDecimal getGasolio() {
		return gasolio;
	}
	public CarburanteDecimal setGasolio(BigDecimal gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public BigDecimal getGasolioSerre() {
		return gasolioSerre;
	}
	public CarburanteDecimal setGasolioSerre(BigDecimal gasolioSerre) {
		this.gasolioSerre = gasolioSerre;
		return this;
	}

	public CarburanteDecimal add(CarburanteDecimal carburante) {
		benzina = benzina.add(carburante.getBenzina());
		gasolio = gasolio.add(carburante.getGasolio());
		gasolioSerre = gasolioSerre.add(carburante.getGasolioSerre());
		return this;
	}

	public CarburanteDecimal round() {
		benzina = benzina.setScale(0, RoundingMode.HALF_UP);
		gasolio = gasolio.setScale(0, RoundingMode.HALF_UP);
		gasolioSerre = gasolioSerre.setScale(0, RoundingMode.HALF_UP);
		return this;
	}

	public CarburanteDto build() {
		return new CarburanteDto()
				.setBenzina(benzina.intValue())
				.setGasolio(gasolio.intValue())
				.setGasolioSerre(gasolioSerre.intValue());
	}

}
