package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="A4GT_SERRE")
public class SerreModel extends FabbricatoModel {
	private static final long serialVersionUID = 341049743239638739L;

	@Column(name="IMPIANTO_RISCALDAMENTO")
	private boolean impiantoRiscaldamento;
	
	@Column(name="ANNO_COSTRUZIONE", length = 4)
	private Long annoCostruzione;
	
	@Column(name="TIPOLOGIA_MATERIALE", length = 200)
	private String tipologiaMateriale;
	
	@Column(name="ANNO_ACQUISTO", length = 4)
	private Long annoAcquisto;
	
	@Column(name="TITOLO_CONFORMITA_URBANISTICA", length = 100)
	private String titoloConformitaUrbanistica;

	public Long getAnnoCostruzione() {
		return annoCostruzione;
	}

	public SerreModel setAnnoCostruzione(Long annoCostruzione) {
		this.annoCostruzione = annoCostruzione;
		return this;
	}

	public String getTipologiaMateriale() {
		return tipologiaMateriale;
	}

	public SerreModel setTipologiaMateriale(String tipologiaMateriale) {
		this.tipologiaMateriale = tipologiaMateriale;
		return this;
	}

	public Long getAnnoAcquisto() {
		return annoAcquisto;
	}

	public SerreModel setAnnoAcquisto(Long annoAcquisto) {
		this.annoAcquisto = annoAcquisto;
		return this;
	}

	public String getTitoloConformitaUrbanistica() {
		return titoloConformitaUrbanistica;
	}

	public SerreModel setTitoloConformitaUrbanistica(String titoloConformitaUrbanistica) {
		this.titoloConformitaUrbanistica = titoloConformitaUrbanistica;
		return this;
	}

	public boolean isImpiantoRiscaldamento() {
		return impiantoRiscaldamento;
	}

	public SerreModel setImpiantoRiscaldamento(boolean impiantoRiscaldamento) {
		this.impiantoRiscaldamento = impiantoRiscaldamento;
		return this;
	}

}
