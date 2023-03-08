package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_TRASFERIMENTI")
public class TrasferimentoCarburanteModel extends EntitaDominio {

	private static final long serialVersionUID = -6159539346373950883L;

	@Column(name="GASOLIO", length = 7, nullable = true)
	private Integer gasolio;

	@Column(name="GASOLIO_SERRE", length = 7, nullable = true)
	private Integer gasolioSerre;

	@Column(name="BENZINA", length = 7, nullable = true)
	private Integer benzina;

	@Column(name="DATA", nullable = false)
	private LocalDateTime data;

	@Column(name="CUAA_DESTINATARIO", length = 16 , nullable = false)
	private String cuaaDestinatario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	public Integer getGasolio() {
		return gasolio;
	}
	public TrasferimentoCarburanteModel setGasolio(Integer gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public Integer getGasolioSerre() {
		return gasolioSerre;
	}
	public TrasferimentoCarburanteModel setGasolioSerre(Integer gasolioSerre) {
		this.gasolioSerre = gasolioSerre;
		return this;
	}
	public Integer getBenzina() {
		return benzina;
	}
	public TrasferimentoCarburanteModel setBenzina(Integer benzina) {
		this.benzina = benzina;
		return this;
	}
	public LocalDateTime getData() {
		return data;
	}
	public TrasferimentoCarburanteModel setData(LocalDateTime data) {
		this.data = data;
		return this;
	}
	public String getCuaaDestinatario() {
		return cuaaDestinatario;
	}
	public TrasferimentoCarburanteModel setCuaaDestinatario(String cuaaDestinatario) {
		this.cuaaDestinatario = cuaaDestinatario;
		return this;
	}
	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public TrasferimentoCarburanteModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
}
