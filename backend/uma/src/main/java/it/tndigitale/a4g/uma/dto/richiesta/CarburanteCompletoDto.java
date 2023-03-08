package it.tndigitale.a4g.uma.dto.richiesta;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;

public class CarburanteCompletoDto extends CarburanteDto {

	private Integer gasolioTerzi;

	public Integer getGasolioTerzi() {
		return gasolioTerzi;
	}
	public CarburanteCompletoDto setGasolioTerzi(Integer gasolioTerzi) {
		this.gasolioTerzi = gasolioTerzi;
		return this;
	}

	@Override
	public CarburanteCompletoDto setBenzina(Integer benzina) {
		super.setBenzina(benzina);
		return this;
	}

	@Override
	public CarburanteCompletoDto setGasolio(Integer gasolio) {
		super.setGasolio(gasolio);
		return this;
	}

	@Override
	public CarburanteCompletoDto setGasolioSerre(Integer gasolioSerre) {
		super.setGasolioSerre(gasolioSerre);
		return this;
	}

	public CarburanteDto toCarburanteDto() {
		return new CarburanteDto()
		.setBenzina(this.getBenzina())
		.setGasolio(this.getGasolio() + this.getGasolioTerzi())
		.setGasolioSerre(this.getGasolioSerre());
	}
}
