package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@MappedSuperclass
public class TipologiaAbstractModel extends EntitaDominio {

	private static final long serialVersionUID = -7433206476826310938L;

	@Column(name="DESCRIZIONE", length = 50)
	private String descrizione;

	@Column(name="DATA_INIZIO")
	private LocalDateTime dataInizio;

	@Column(name="DATA_FINE")
	private LocalDateTime dataFine;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
