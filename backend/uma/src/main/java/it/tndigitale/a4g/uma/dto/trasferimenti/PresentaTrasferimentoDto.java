package it.tndigitale.a4g.uma.dto.trasferimenti;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;

public class PresentaTrasferimentoDto {

	private Long idRichiestaMittente;
	private Long idRichiestaDestinatario;
	private CarburanteDto carburanteTrasferito;

	public Long getIdRichiestaMittente() {
		return idRichiestaMittente;
	}
	public PresentaTrasferimentoDto setIdRichiestaMittente(Long idRichiestaMittente) {
		this.idRichiestaMittente = idRichiestaMittente;
		return this;
	}
	public Long getIdRichiestaDestinatario() {
		return idRichiestaDestinatario;
	}
	public PresentaTrasferimentoDto setIdRichiestaDestinatario(Long idRichiestaDestinatario) {
		this.idRichiestaDestinatario = idRichiestaDestinatario;
		return this;
	}
	public CarburanteDto getCarburanteTrasferito() {
		return carburanteTrasferito;
	}
	public PresentaTrasferimentoDto setCarburanteTrasferito(CarburanteDto carburanteTrasferito) {
		this.carburanteTrasferito = carburanteTrasferito;
		return this;
	}



}
