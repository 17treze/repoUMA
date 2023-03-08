package it.tndigitale.a4g.uma.dto.distributori;

public class DistributoreDto {
	
	private Long id;
	private String denominazione;
	private String comune;
	private String indirizzo;
	private String provincia;
	private Long identificativo;

	public String getDenominazione() {
		return denominazione;
	}
	public DistributoreDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public DistributoreDto setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public DistributoreDto setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}
	public String getProvincia() {
		return provincia;
	}
	public DistributoreDto setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}
	public Long getIdentificativo() {
		return identificativo;
	}
	public DistributoreDto setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
		return this;
	}
	public Long getId() {
		return id;
	}
	public DistributoreDto setId(Long id) {
		this.id = id;
		return this;
	}
}
