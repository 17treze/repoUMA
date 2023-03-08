package it.tndigitale.a4g.uma.dto.consumi;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CarburanteDtoBuilder {

	private CarburanteDto carburanteDto = new CarburanteDto().setBenzina(0).setGasolio(0).setGasolioSerre(0);

	public CarburanteDtoBuilder newDto() {
		carburanteDto = new CarburanteDto().setBenzina(0).setGasolio(0).setGasolioSerre(0);
		return this;
	}

	public CarburanteDtoBuilder from(List<CarburanteDto> carburanti) {
		carburanteDto = carburanti.stream().map(t -> new CarburanteDto()
				.setBenzina(t.getBenzina())
				.setGasolio(t.getGasolio())
				.setGasolioSerre(t.getGasolioSerre()))
				.reduce((a,b) -> b
						.setBenzina(b.getBenzina() + a.getBenzina())
						.setGasolio(b.getGasolio() + a.getGasolio())
						.setGasolioSerre(b.getGasolioSerre() + a.getGasolioSerre()))
				.orElse(new CarburanteDto().setBenzina(0).setGasolio(0).setGasolioSerre(0));
		return this;
	}

	public CarburanteDtoBuilder add(CarburanteDto carburante) {
		carburanteDto
		.setBenzina(carburanteDto.getBenzina() + carburante.getBenzina())
		.setGasolio(carburanteDto.getGasolio() + carburante.getGasolio())
		.setGasolioSerre(carburanteDto.getGasolioSerre() + carburante.getGasolioSerre());
		return this;
	}

	public CarburanteDtoBuilder subtract(CarburanteDto carburante) {
		carburanteDto
		.setBenzina(carburanteDto.getBenzina() - carburante.getBenzina())
		.setGasolio(carburanteDto.getGasolio() - carburante.getGasolio())
		.setGasolioSerre(carburanteDto.getGasolioSerre() - carburante.getGasolioSerre());
		return this;
	}

	public boolean isLesserOrEqual(CarburanteDto carburante) {
		return 
				carburanteDto.getBenzina() <= carburante.getBenzina() &&
				carburanteDto.getGasolio() <= carburante.getGasolio() &&
				carburanteDto.getGasolioSerre() <= carburante.getGasolioSerre();
	}

	public boolean isGreaterThen(CarburanteDto carburante) {
		return !isLesserOrEqual(carburante);
	}

	public CarburanteDto build() {
		return carburanteDto;
	}
}
