package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "A4GT_FASCICOLO", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CUAA"})})
public class FascicoloModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = 8421093257879552608L;

	@OneToMany(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private List<ConduzioneModel> conduzioni;
	@Column(name = "CUAA", length = 16, nullable = false)
	private String cuaa;
	
	public String getCuaa() {
		return cuaa;
	}

	public FascicoloModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public List<ConduzioneModel> getConduzioni() {
		return conduzioni;
	}

	public FascicoloModel setConduzioni(List<ConduzioneModel> conduzioni) {
		this.conduzioni = conduzioni;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FascicoloModel)) return false;
		FascicoloModel that = (FascicoloModel) o;
		return Objects.equals(conduzioni, that.conduzioni) && Objects.equals(cuaa, that.cuaa);
	}

	@Override
	public int hashCode() {
		return Objects.hash(conduzioni, cuaa);
	}
}
