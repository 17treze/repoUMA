package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


@Entity
@Table(name = "A4GT_SPORTELLO")
public class SportelloModel extends EntitaDominio {
	private static final long serialVersionUID = 5205203195945739639L;

	@ManyToOne
	@JoinColumn(name = "ID_CAA")
	private CentroAssistenzaAgricolaModel centroAssistenzaAgricola;

	@Column(name = "IDENTIFICATIVO", unique = true)
	private Long identificativo;

	@Column(name = "DENOMINAZIONE", length = 100, nullable = false)
	private String denominazione;

	@Column(name = "COMUNE", length = 100)
	private String comune;

	@Column(name = "INDIRIZZO", length = 200)
	private String indirizzo;

	@Column(name = "CAP", length = 10)
	private String cap;

	@Column(name = "PROVINCIA", length = 100)
	private String provincia;

	@Column(name = "TELEFONO", length = 30)
	private String telefono;

	@Column(name = "EMAIL", length = 200)
	private String email;

	@OneToMany(mappedBy = "sportello", fetch = FetchType.LAZY)
	private List<RevocaImmediataModel> revocheImmediate;

	public CentroAssistenzaAgricolaModel getCentroAssistenzaAgricola() {
		return centroAssistenzaAgricola;
	}

	public SportelloModel setCentroAssistenzaAgricola(CentroAssistenzaAgricolaModel personaModel) {
		this.centroAssistenzaAgricola = personaModel;
		return this;
	}

	public Long getIdentificativo() {
		return identificativo;
	}

	public SportelloModel setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public SportelloModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
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

	@Override
	public int hashCode() {
		return Objects
				.hash(comune, denominazione, identificativo, centroAssistenzaAgricola, indirizzo, cap, provincia, telefono, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
				.getClass())
			return false;
		SportelloModel other = (SportelloModel) obj;
		return Objects
				.equals(comune, other.comune)
				&& Objects
				.equals(denominazione, other.denominazione)
				&& Objects
				.equals(identificativo, other.identificativo)
				&& Objects
				.equals(centroAssistenzaAgricola, other.centroAssistenzaAgricola)
				&& Objects
				.equals(indirizzo, other.indirizzo)
				&& Objects
				.equals(cap, other.cap)
				&& Objects
				.equals(provincia, other.provincia)
				&& Objects
				.equals(telefono, other.telefono)
				&& Objects
				.equals(email, other.email);
	}
}