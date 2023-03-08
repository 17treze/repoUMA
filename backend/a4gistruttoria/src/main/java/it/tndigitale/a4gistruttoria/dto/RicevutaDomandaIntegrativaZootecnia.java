package it.tndigitale.a4gistruttoria.dto;

public class RicevutaDomandaIntegrativaZootecnia {

	private byte[] ricevuta;

	private Long idDomanda;

	public byte[] getRicevuta() {
		return ricevuta;
	}

	public void setRicevuta(byte[] ricevuta) {
		this.ricevuta = ricevuta;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
}
