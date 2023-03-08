package it.tndigitale.a4g.proxy.repository.esiti.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="ANTIMAFIA_DESC_ESITI")
@NamedQuery(name="AntimafiaDescrizioneEsitiModel.findAll", query="SELECT a FROM AntimafiaDescrizioneEsitiModel a")
public class AntimafiaDescrizioneEsitiModel {
	@Id
	@Column(name="CODICE")
	private String codice;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;
	
	@Column(name="ZONA")
	private String zona;
	
	@Column(name="DESCRIZIONE_ZONA")
	private String descrizioneZona;
	
	@Column(name="ESITO_INVIO_AGEA")
	private String esitoInvioAgea;
	
	@Column(name="ESITO_INVIO_BDNA")
	private String esitoInvioBdna;

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

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getDescrizioneZona() {
		return descrizioneZona;
	}

	public void setDescrizioneZona(String descrizioneZona) {
		this.descrizioneZona = descrizioneZona;
	}

	public String getEsitoInvioAgea() {
		return esitoInvioAgea;
	}

	public void setEsitoInvioAgea(String esitoInvioAgea) {
		this.esitoInvioAgea = esitoInvioAgea;
	}

	public String getEsitoInvioBdna() {
		return esitoInvioBdna;
	}

	public void setEsitoInvioBdna(String esitoInvioBdna) {
		this.esitoInvioBdna = esitoInvioBdna;
	}
}
