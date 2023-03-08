package it.tndigitale.a4g.uma.dto.clienti;

public class ClienteConsumiDto extends ClienteDto {
	private boolean gasolio;
	private boolean benzina;

	public boolean isGasolio() {
		return gasolio;
	}
	public ClienteConsumiDto setGasolio(boolean gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public boolean isBenzina() {
		return benzina;
	}
	public ClienteConsumiDto setBenzina(boolean benzina) {
		this.benzina = benzina;
		return this;
	}

	public ClienteConsumiDto buildFrom(ClienteDto clienteDto) {
		this.setCuaa(clienteDto.getCuaa())
		.setDenominazione(clienteDto.getDenominazione())
		.setId(clienteDto.getId())
		.setIdDichiarazioneConsumi(clienteDto.getIdDichiarazioneConsumi())
		.setIdFascicolo(clienteDto.getIdFascicolo());
		return this;
	}
}
