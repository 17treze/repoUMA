package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "A4GT_CONDUZIONE")
public class ConduzioneModel extends EntitaDominioFascicolo {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FASCICOLO_ID", referencedColumnName = "ID")
	@JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SOTTOTIPO_ID", referencedColumnName = "ID")
	private SottotipoModel sottotipo;

	@OneToMany(mappedBy = "conduzione", fetch = FetchType.LAZY)
	private List<ParticelleFondiarieModel> particelle;

	@OneToMany(mappedBy = "conduzione", fetch = FetchType.LAZY)
	private List<DocumentoConduzioneModel> documenti;

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public ConduzioneModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}

	public SottotipoModel getSottotipo() {
		return sottotipo;
	}

	public ConduzioneModel setSottotipo(SottotipoModel sottotipo) {
		this.sottotipo = sottotipo;
		return this;
	}

	public List<ParticelleFondiarieModel> getParticelle() {
		return particelle;
	}

	public ConduzioneModel setParticelle(List<ParticelleFondiarieModel> particelle) {
		this.particelle = particelle;
		return this;
	}

	public List<DocumentoConduzioneModel> getDocumenti() {
		return documenti;
	}

	public ConduzioneModel setDocumenti(List<DocumentoConduzioneModel> documenti) {
		this.documenti = documenti;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConduzioneModel)) return false;
		ConduzioneModel that = (ConduzioneModel) o;
		return Objects.equals(fascicolo, that.fascicolo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fascicolo);
	}
}
