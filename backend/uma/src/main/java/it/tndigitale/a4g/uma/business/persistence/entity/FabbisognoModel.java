package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


@Entity
@Table(name="TAB_AGRI_UMAL_FABBISOGNO")
@Inheritance(strategy = InheritanceType.JOINED)
public class FabbisognoModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 5553741090158402913L;

	@Column(name="CARBURANTE", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoCarburante carburante;

	@Column(name="QUANTITA")
	private BigDecimal quantita;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneModel lavorazioneModel;

	public TipoCarburante getCarburante() {
		return carburante;
	}
	public FabbisognoModel setCarburante(TipoCarburante carburante) {
		this.carburante = carburante;
		return this;
	}
	public BigDecimal getQuantita() {
		return this.quantita;
	}
	public FabbisognoModel setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
		return this;
	}
	public LavorazioneModel getLavorazioneModel() {
		return lavorazioneModel;
	}
	public FabbisognoModel setLavorazioneModel(LavorazioneModel lavorazioneModel) {
		this.lavorazioneModel = lavorazioneModel;
		return this;
	}
	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public FabbisognoModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
}