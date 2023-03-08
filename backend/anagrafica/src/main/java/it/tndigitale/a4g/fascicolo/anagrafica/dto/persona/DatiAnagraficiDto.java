package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.time.LocalDate;

public class DatiAnagraficiDto {
	private String nominativo;

	private String codiceFiscale;

	private LocalDate dataNascita;

	private String comuneNascita;

	private String provinciaNascita;
	
	private String pec;

	public String getNominativo() {
		return nominativo;
	}

	public DatiAnagraficiDto setNominativo(String nominativo) {
		this.nominativo = nominativo;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DatiAnagraficiDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public DatiAnagraficiDto setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public DatiAnagraficiDto setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
		return this;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public DatiAnagraficiDto setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
		return this;
	}
	
	public String getPec() {
		return pec;
	}
	
	public DatiAnagraficiDto setPec(String pec) {
		this.pec = pec;
		return this;
	}
}
