package it.tndigitale.a4g.uma.dto.consumi;

import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;

public class StampaPrelievoDto {
	private DistributoreDto distributore;
	private CarburanteDto carburante;
	private String data;


	public DistributoreDto getDistributore() {
		return distributore;
	}
	public StampaPrelievoDto setDistributore(DistributoreDto distributore) {
		this.distributore = distributore;
		return this;
	}
	public CarburanteDto getCarburante() {
		return carburante;
	}
	public StampaPrelievoDto setCarburante(CarburanteDto carburante) {
		this.carburante = carburante;
		return this;
	}
	public String getData() {
		return data;
	}
	public StampaPrelievoDto setData(String data) {
		this.data = data;
		return this;
	}
}
