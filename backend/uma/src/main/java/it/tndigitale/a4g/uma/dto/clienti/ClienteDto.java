package it.tndigitale.a4g.uma.dto.clienti;

public class ClienteDto {
	private Long idDichiarazioneConsumi;
	private String cuaa;
	private String denominazione;
	private Long  idFascicolo;
	private Long id;

	public Long getId() {
		return id;
	}
	public ClienteDto setId(Long id) {
		this.id = id;
		return this;
	}
	public Long getIdDichiarazioneConsumi() {
		return idDichiarazioneConsumi;
	}
	public ClienteDto setIdDichiarazioneConsumi(Long idDichiarazioneConsumi) {
		this.idDichiarazioneConsumi = idDichiarazioneConsumi;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public ClienteDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public ClienteDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Long getIdFascicolo() {
		return idFascicolo;
	}
	public ClienteDto setIdFascicolo(Long idFascicolo) {
		this.idFascicolo = idFascicolo;
		return this;
	}
}
