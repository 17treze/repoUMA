package it.tndigitale.a4g.fascicolo.territorio.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GD_TIPO_CONDUZIONE", uniqueConstraints = {@UniqueConstraint(columnNames={"ID", "CODICE"})})
public class TipoConduzioneModel extends EntitaDominio {
 
	private static final long serialVersionUID = 5434941794078730642L;
 
	@Column(name = "CODICE", length = 16, nullable = false)
	private String codice;

	@Column(name = "DESCRIZIONE", length = 100, nullable = false)
	private String descrizione;
	
	@OneToMany(mappedBy = "tipoConduzione", fetch = FetchType.LAZY)
	private List<SottotipoConduzioneModel> sottotipi;
 	  
    public List<SottotipoConduzioneModel> getSottotipi() {
		return sottotipi;
	}

	public void setSottotipi(List<SottotipoConduzioneModel> sottotipi) {
		this.sottotipi = sottotipi;
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
