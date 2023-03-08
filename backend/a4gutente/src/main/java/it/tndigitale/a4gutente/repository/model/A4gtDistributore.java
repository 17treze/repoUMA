package it.tndigitale.a4gutente.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The persistent class for the A4GT_DISTRIBUTORE database table.
 * 
 */
@Entity
@Table(name="A4GT_DISTRIBUTORE")
@NamedQuery(name="A4gtDistributore.findAll", query="SELECT a FROM A4gtDistributore a")
public class A4gtDistributore extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DENOMINAZIONE_AZIENDA")
	private String denominazioneAzienda;
	@Column(name = "COMUNE_DISTRIBUTORE")
	private String comune;
	private String provincia;
	private String indirizzo;
	@Column(name = "DATA_INIZIO")
	private LocalDateTime dataInizio;
	@Column(name = "DATA_FINE")
	private LocalDateTime dataFine;

	public A4gtDistributore() {
	}

	public String getDenominazioneAzienda() {
		return denominazioneAzienda;
	}

	public void setDenominazioneAzienda(String denominazioneAzienda) {
		this.denominazioneAzienda = denominazioneAzienda;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDateTime getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}
}
