package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="A4GD_COEFFICIENTI")
public class CoefficienteModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1543374526522006579L;

	@Column(name="COEFFICIENTE", nullable = false , precision = 8)
	private BigDecimal coefficiente;

	@Column(name="ANNO_INIZIO", nullable = false, length = 4)
	private Integer annoInizio;

	@Column(name="ANNO_FINE", nullable = true, length = 4)
	private Integer annoFine;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE", referencedColumnName = "ID")
	private LavorazioneModel lavorazioneModel;

	public BigDecimal getCoefficiente() {
		return coefficiente;
	}
	public CoefficienteModel setCoefficiente(BigDecimal coefficiente) {
		this.coefficiente = coefficiente;
		return this;
	}
	public Integer getAnnoInizio() {
		return annoInizio;
	}
	public CoefficienteModel setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	public Integer getAnnoFine() {
		return annoFine;
	}
	public CoefficienteModel setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	public LavorazioneModel getLavorazioneModel() {
		return lavorazioneModel;
	}
	public CoefficienteModel setLavorazioneModel(LavorazioneModel lavorazioneModel) {
		this.lavorazioneModel = lavorazioneModel;
		return this;
	}

}
