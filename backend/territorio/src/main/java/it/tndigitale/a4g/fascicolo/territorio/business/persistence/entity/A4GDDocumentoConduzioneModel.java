package it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4g.territorio.business.persistence.entity.SottotipoDocumentoModel;


@Entity
@Table(name = "A4GD_DOCUMENTO_CONDUZIONE", uniqueConstraints = {@UniqueConstraint(columnNames={"ID", "CODICE"})})
public class A4GDDocumentoConduzioneModel extends EntitaDominio {
	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Column(name = "CODICE")
	private String codice;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "documentoConduzione")
	private List<SottotipoDocumentoModel> sottotipodocumenti;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public List<SottotipoDocumentoModel> getSottotipodocumenti() {
		return sottotipodocumenti;
	}

	public void setSottotipodocumenti(List<SottotipoDocumentoModel> sottotipodocumenti) {
		this.sottotipodocumenti = sottotipodocumenti;
	}
}
