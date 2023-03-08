package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Alimentazione;

public class MotoreDto {

	private String marca;
	private String tipo;
	private Alimentazione alimentazione;
	private Double potenza;
	private String matricola;

	public String getMarca() {
		return marca;
	}
	public MotoreDto setMarca(String marca) {
		this.marca = marca;
		return this;
	}
	public String getTipo() {
		return tipo;
	}
	public MotoreDto setTipo(String tipo) {
		this.tipo = tipo;
		return this;
	}
	public Alimentazione getAlimentazione() {
		return alimentazione;
	}
	public MotoreDto setAlimentazione(Alimentazione alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}
	public Double getPotenza() {
		return potenza;
	}
	public MotoreDto setPotenza(Double potenza) {
		this.potenza = potenza;
		return this;
	}
	public String getMatricola() {
		return matricola;
	}
	public MotoreDto setMatricola(String matricola) {
		this.matricola = matricola;
		return this;
	}
}
