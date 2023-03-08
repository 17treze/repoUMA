package it.tndigitale.a4g.territorio.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity.A4GDDocumentoConduzioneModel;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity.SottotipoConduzioneModel;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


@Entity
@Table(name = "A4GR_SOTTOTIPO_DOCUMENTO", uniqueConstraints = {@UniqueConstraint(columnNames={"ID"})})
public class SottotipoDocumentoModel extends EntitaDominio {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ID_DOCUMENTO_CONDUZIONE")
	private A4GDDocumentoConduzioneModel documentoConduzione;

	@ManyToOne()
	@JoinColumn(name="ID_SOTTOTIPO_CONDUZIONE")
	private SottotipoConduzioneModel sottotipoConduzione;
 
	@Column(name = "TIPO")
	private String tipo;
 
	@Column(name = "OBBLIGATORIO")
    private int obbligatorio;

	@ManyToMany()
	@JoinTable(
		name = "A4GR_DOCUMENTO_DIPENDENZA", 
		joinColumns = @JoinColumn(name = "ID_SOTTOTIPO_DOCUMENTO_S"), 
		inverseJoinColumns = @JoinColumn(name = "ID_SOTTOTIPO_DOCUMENTO_P")
	)
	private List<SottotipoDocumentoModel> sottotipoDocumentoSecondario;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "A4GR_DOCUMENTO_DIPENDENZA", 
		joinColumns = @JoinColumn(name = "ID_SOTTOTIPO_DOCUMENTO_P"), 
		inverseJoinColumns = @JoinColumn(name = "ID_SOTTOTIPO_DOCUMENTO_S")
		
	)
	private List<SottotipoDocumentoModel> documentoDipendenzaPrimario;

	public List<SottotipoDocumentoModel> getDocumentoDipendenzaPrimario() {
		return documentoDipendenzaPrimario;
	}

	public void setDocumentoDipendenzaPrimario(List<SottotipoDocumentoModel> documentoDipendenzaPrimario) {
		this.documentoDipendenzaPrimario = documentoDipendenzaPrimario;
	}

	public List<SottotipoDocumentoModel> getSottotipoDocumentoSecondario() {
		return sottotipoDocumentoSecondario;
	}

	public void setSottotipoDocumentoSecondario(List<SottotipoDocumentoModel> sottotipoDocumentoSecondario) {
		this.sottotipoDocumentoSecondario = sottotipoDocumentoSecondario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(int obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public A4GDDocumentoConduzioneModel getDocumentoConduzione() {
		return documentoConduzione;
	}

	public void setDocumentoConduzione(A4GDDocumentoConduzioneModel documentoConduzione) {
		this.documentoConduzione = documentoConduzione;
	}

	public SottotipoConduzioneModel getSottotipoConduzione() {
		return sottotipoConduzione;
	}

	public void setSottotipoConduzione(SottotipoConduzioneModel sottotipoConduzione) {
		this.sottotipoConduzione = sottotipoConduzione;
	}
     
}
