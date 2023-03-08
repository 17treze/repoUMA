package it.tndigitale.a4g.territorio.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "A4GT_PARTICELLE_FONDIARIE")
public class ParticelleFondiarieModel extends EntitaDominioFascicolo {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CONDUZIONE_ID", referencedColumnName = "ID")
	@JoinColumn(name="CONDUZIONE_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private ConduzioneModel conduzione;

	@Column(name = "SUPERFICIE_CONDOTTA", nullable = false)
	private Long superficieCondotta;

	@Column(name = "PARTICELLA", length = 10, nullable = false)
	private String particella;

	@Column(name = "SUB", length = 10)
	private String sub;

	@Column(name = "SEZIONE", length = 10)
	private String sezione;

	@Column(name = "COMUNE", length = 50, nullable = false)
	private String comune;

	@Column(name = "FOGLIO")
	private Integer foglio;

	public ConduzioneModel getConduzione() {
		return conduzione;
	}

	public ParticelleFondiarieModel setConduzione(ConduzioneModel conduzione) {
		this.conduzione = conduzione;
		return this;
	}

	public Long getSuperficieCondotta() {
		return superficieCondotta;
	}

	public ParticelleFondiarieModel setSuperficieCondotta(Long superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
		return this;
	}

	public String getParticella() {
		return particella;
	}

	public ParticelleFondiarieModel setParticella(String particella) {
		this.particella = particella;
		return this;
	}

	public String getSub() {
		return sub;
	}

	public ParticelleFondiarieModel setSub(String sub) {
		this.sub = sub;
		return this;
	}

	public String getSezione() {
		return sezione;
	}

	public ParticelleFondiarieModel setSezione(String sezione) {
		this.sezione = sezione;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public ParticelleFondiarieModel setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public Integer getFoglio() {
		return foglio;
	}

	public void setFoglio(Integer foglio) {
		this.foglio = foglio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ParticelleFondiarieModel)) return false;
		ParticelleFondiarieModel that = (ParticelleFondiarieModel) o;
		return Objects.equals(particella, that.particella) && Objects.equals(sub, that.sub) && Objects.equals(sezione, that.sezione) && Objects.equals(comune, that.comune) && Objects.equals(foglio, that.foglio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(particella, sub, sezione, comune, foglio);
	}
}
