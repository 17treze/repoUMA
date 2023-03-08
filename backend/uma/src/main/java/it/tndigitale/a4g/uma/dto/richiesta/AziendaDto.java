package it.tndigitale.a4g.uma.dto.richiesta;

public class AziendaDto {

	private String cuaa;
	private String denominazione;

	public String getCuaa() {
		return cuaa;
	}
	public AziendaDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public AziendaDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
}
