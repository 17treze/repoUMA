package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "A4GD_SOTTOTIPO")
public class SottotipoModel extends EntitaDominio {

	@Column(name = "AMBITO", length = 100, nullable = false)
	private String ambito;

	@Column(name = "DESCRIZIONE", length = 100, nullable = false)
	private String descrizione;

	@OneToMany(mappedBy = "sottotipo", fetch = FetchType.LAZY)
	private List<ConduzioneModel> conduzioni;

	@OneToMany(mappedBy = "sottotipo", fetch = FetchType.LAZY)
	private List<TipoDocumentoConduzioneModel> tipiDocumentoConduzione;

	public String getAmbito() {
		return ambito;
	}

	public SottotipoModel setAmbito(String ambito) {
		this.ambito = ambito;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public SottotipoModel setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public List<ConduzioneModel> getConduzioni() {
		return conduzioni;
	}

	public SottotipoModel setConduzioni(List<ConduzioneModel> conduzioni) {
		this.conduzioni = conduzioni;
		return this;
	}

	public List<TipoDocumentoConduzioneModel> getTipiDocumentoConduzione() {
		return tipiDocumentoConduzione;
	}

	public SottotipoModel setTipiDocumentoConduzione(List<TipoDocumentoConduzioneModel> tipiDocumentoConduzione) {
		this.tipiDocumentoConduzione = tipiDocumentoConduzione;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SottotipoModel)) return false;
		SottotipoModel that = (SottotipoModel) o;
		return Objects.equals(ambito, that.ambito) && Objects.equals(descrizione, that.descrizione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ambito, descrizione);
	}
}
