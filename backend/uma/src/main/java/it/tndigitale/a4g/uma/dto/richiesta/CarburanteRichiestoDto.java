package it.tndigitale.a4g.uma.dto.richiesta;

public class CarburanteRichiestoDto {

	private CarburanteCompletoDto carburanteRichiesto;
	private String note;

	public CarburanteCompletoDto getCarburanteRichiesto() {
		return carburanteRichiesto;
	}
	public CarburanteRichiestoDto setCarburanteRichiesto(CarburanteCompletoDto carburanteRichiesto) {
		this.carburanteRichiesto = carburanteRichiesto;
		return this;
	}
	public String getNote() {
		return note;
	}
	public CarburanteRichiestoDto setNote(String note) {
		this.note = note;
		return this;
	}
}
