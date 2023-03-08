package it.tndigitale.a4g.proxy.repository.esiti.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="ANTIMAFIA_ESITI")
@NamedQuery(name="AntimafiaEsitiModel.findAll", query="SELECT a FROM AntimafiaEsitiModel a")
public class AntimafiaEsitiModel {
	// CUAA + Data Elaborazione
	@EmbeddedId
	private AntimafiaEsitiId id;
	
	@Column(name="ESITO")
	private String esito;
	
	@Column(name="CODICE")
	private String codice;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public AntimafiaEsitiId getId() {
		return id;
	}

	public void setId(AntimafiaEsitiId id) {
		this.id = id;
	}

}
