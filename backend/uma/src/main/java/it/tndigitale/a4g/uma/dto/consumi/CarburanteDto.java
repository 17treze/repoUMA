package it.tndigitale.a4g.uma.dto.consumi;

public class CarburanteDto {

	private Integer gasolio;
	private Integer benzina;
	private Integer gasolioSerre;
	
	public CarburanteDto() {
		this.benzina = 0;
		this.gasolio = 0;
		this.gasolioSerre = 0;
	}
	
	public CarburanteDto(Integer gasolio, Integer benzina, Integer gasolioSerre) {
		this.gasolio = gasolio;
		this.benzina = benzina;
		this.gasolioSerre = gasolioSerre;
	}
	
	public Integer getGasolio() {
		return gasolio;
	}
	public CarburanteDto setGasolio(Integer gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public Integer getGasolioSerre() {
		return gasolioSerre;
	}
	public CarburanteDto setGasolioSerre(Integer gasolioSerre) {
		this.gasolioSerre = gasolioSerre;
		return this;
	}
	public Integer getBenzina() {
		return benzina;
	}
	public CarburanteDto setBenzina(Integer benzina) {
		this.benzina = benzina;
		return this;
	}
	@Override
	public String toString() {
		return "CarburanteDto [gasolio=" + gasolio + ", benzina=" + benzina + ", gasolioSerre=" + gasolioSerre + "]";
	}
}

