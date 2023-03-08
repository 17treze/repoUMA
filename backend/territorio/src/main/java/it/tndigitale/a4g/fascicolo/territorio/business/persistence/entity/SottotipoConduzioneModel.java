package it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4g.territorio.business.persistence.entity.SottotipoDocumentoModel;

@Entity
@Table(name = "A4GD_SOTTOTIPO_CONDUZIONE", uniqueConstraints = {@UniqueConstraint(columnNames={"ID", "CODICE"})})
public class SottotipoConduzioneModel extends EntitaDominio {
	@Column(name = "CODICE", length = 16, nullable = false)
	private String codice;

	@Column(name = "DESCRIZIONE", length = 100, nullable = false)
	private String descrizione;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_TIPO_CONDUZIONE")
	private TipoConduzioneModel tipoConduzione;


	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sottotipoConduzione")
	private List<SottotipoDocumentoModel> sottotipodocumenti;

    public List<SottotipoDocumentoModel> getSottotipodocumenti() {
		return sottotipodocumenti;
	}

	public void setSottotipodocumenti(List<SottotipoDocumentoModel> sottotipodocumenti) {
		this.sottotipodocumenti = sottotipodocumenti;
	}

	public TipoConduzioneModel getTipoConduzione() {
		return tipoConduzione;
	}

	public void setTipoConduzione(TipoConduzioneModel tipoConduzione) {
		this.tipoConduzione = tipoConduzione;
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
}
