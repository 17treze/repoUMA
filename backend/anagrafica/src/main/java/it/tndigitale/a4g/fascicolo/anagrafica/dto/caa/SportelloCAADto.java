package it.tndigitale.a4g.fascicolo.anagrafica.dto.caa;

import java.io.Serializable;

public class SportelloCAADto implements Serializable {
	private static final long serialVersionUID = -5420306595679137287L;
	
	private Long id;
	private Long identificativo;
	private String denominazione;
	private String indirizzo;
	private String comune;
	private String cap;
	private String provincia;
	private String telefono;
	private String email;
	
	public Long getIdentificativo() {
		return identificativo;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
