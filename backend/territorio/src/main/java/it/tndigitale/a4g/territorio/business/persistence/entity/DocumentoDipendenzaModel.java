package it.tndigitale.a4g.territorio.business.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
@Entity
@Table(name = "A4GR_DOCUMENTO_DIPENDENZA")
public class DocumentoDipendenzaModel  extends EntitaDominio {
	@ManyToOne()
	@JoinColumn(name="ID_SOTTOTIPO_DOCUMENTO_P")
	private SottotipoDocumentoModel sottotipoDocumentoPrimario;

	@ManyToOne()
	@JoinColumn(name="ID_SOTTOTIPO_DOCUMENTO_S")
	private SottotipoDocumentoModel sottotipoDocumentoSecondario;

	public SottotipoDocumentoModel getSottotipoDocumentoPrimario() {
		return sottotipoDocumentoPrimario;
	}

	public void setSottotipoDocumentoPrimario(SottotipoDocumentoModel sottotipoDocumentoPrimario) {
		this.sottotipoDocumentoPrimario = sottotipoDocumentoPrimario;
	}

	public SottotipoDocumentoModel getSottotipoDocumentoSecondario() {
		return sottotipoDocumentoSecondario;
	}

	public void setSottotipoDocumentoSecondario(SottotipoDocumentoModel sottotipoDocumentoSecondario) {
		this.sottotipoDocumentoSecondario = sottotipoDocumentoSecondario;
	}
}
