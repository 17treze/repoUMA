package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "A4GT_PASCOLO_PARTICELLA")
@NamedQuery(name = "A4gtPascoloParticella.findAll", query = "SELECT a FROM A4gtPascoloParticella a")
public class A4gtPascoloParticella extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_PASCOLO")
	private String codPascolo;

	@Column(name = "DESC_PASCOLO")
	private String descPascolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	@Column(name = "ID_PASCOLO")
	private BigDecimal idPascolo;

	@Lob
	@Column(name = "INFO_PART_PASCOLO")
	private String infoPartPascolo;

	@Column(name = "SUP_NETTA_PASCOLO")
	private BigDecimal supNettaPascolo;

	@OneToMany(mappedBy = "pascoloParticella", cascade = CascadeType.REMOVE)
	private List<EsitoMantenimentoPascolo> esitiMantenimentoPascolo;

	public List<EsitoMantenimentoPascolo> getEsitiMantenimentoPascolo() {
		return esitiMantenimentoPascolo;
	}

	public A4gtPascoloParticella setEsitiMantenimentoPascolo(List<EsitoMantenimentoPascolo> esitiMantenimentoPascolo) {
		this.esitiMantenimentoPascolo = esitiMantenimentoPascolo;
		return this;
	}

	public String getCodPascolo() {
		return codPascolo;
	}

	public A4gtPascoloParticella setCodPascolo(String codPascolo) {
		this.codPascolo = codPascolo;
		return this;
	}

	public String getDescPascolo() {
		return descPascolo;
	}

	public A4gtPascoloParticella setDescPascolo(String descPascolo) {
		this.descPascolo = descPascolo;
		return this;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return domandaUnicaModel;
	}

	public A4gtPascoloParticella setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
		return this;
	}

	public BigDecimal getIdPascolo() {
		return idPascolo;
	}

	public A4gtPascoloParticella setIdPascolo(BigDecimal idPascolo) {
		this.idPascolo = idPascolo;
		return this;
	}

	public String getInfoPartPascolo() {
		return infoPartPascolo;
	}

	public A4gtPascoloParticella setInfoPartPascolo(String infoPartPascolo) {
		this.infoPartPascolo = infoPartPascolo;
		return this;
	}

	public BigDecimal getSupNettaPascolo() {
		return supNettaPascolo;
	}

	public A4gtPascoloParticella setSupNettaPascolo(BigDecimal supNettaPascolo) {
		this.supNettaPascolo = supNettaPascolo;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		A4gtPascoloParticella that = (A4gtPascoloParticella) o;
		return Objects.equals(codPascolo, that.codPascolo) &&
				Objects.equals(descPascolo, that.descPascolo) &&
				Objects.equals(domandaUnicaModel, that.domandaUnicaModel) &&
				Objects.equals(idPascolo, that.idPascolo) &&
				Objects.equals(infoPartPascolo, that.infoPartPascolo) &&
				Objects.equals(supNettaPascolo, that.supNettaPascolo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codPascolo, descPascolo, domandaUnicaModel, idPascolo, infoPartPascolo, supNettaPascolo);
	}
}
