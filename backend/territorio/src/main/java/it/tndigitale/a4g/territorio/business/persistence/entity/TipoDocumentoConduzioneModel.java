package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "A4GD_TIPO_DOCUMENTO_CONDUZIONE")
public class TipoDocumentoConduzioneModel extends EntitaDominio {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SOTTOTIPO_ID", referencedColumnName = "ID")
	private SottotipoModel sottotipo;

	@Column(name = "DESCRIZIONE", length = 2000, nullable = false)
	private String descrizione;

	@OneToMany(mappedBy = "tipoDocumentoConduzione", fetch = FetchType.LAZY)
	private List<DocumentoConduzioneModel> documenti;

	public SottotipoModel getSottotipo() {
		return sottotipo;
	}

	public TipoDocumentoConduzioneModel setSottotipo(SottotipoModel sottotipo) {
		this.sottotipo = sottotipo;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public TipoDocumentoConduzioneModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public List<DocumentoConduzioneModel> getDocumenti() {
		return documenti;
	}

	public TipoDocumentoConduzioneModel setDocumenti(List<DocumentoConduzioneModel> documenti) {
		this.documenti = documenti;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TipoDocumentoConduzioneModel)) return false;
		TipoDocumentoConduzioneModel that = (TipoDocumentoConduzioneModel) o;
		return Objects.equals(descrizione, that.descrizione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione);
	}
}
