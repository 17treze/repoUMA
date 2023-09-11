package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_CONSUMI_CLIENTI")
public class ConsumiClienteModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -2552184220467899875L;

	@Column(name="CARBURANTE", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoCarburante carburante;

	@Column(name="QUANTITA")
	private BigDecimal quantita;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE")
	private ClienteModel cliente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneModel lavorazioneModel;

	public TipoCarburante getCarburante() {
		return carburante;
	}
	public ConsumiClienteModel setCarburante(TipoCarburante carburante) {
		this.carburante = carburante;
		return this;
	}
	public BigDecimal getQuantita() {
		return quantita;
	}
	public ConsumiClienteModel setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
		return this;
	}
	public ClienteModel getCliente() {
		return cliente;
	}
	public ConsumiClienteModel setCliente(ClienteModel cliente) {
		this.cliente = cliente;
		return this;
	}
	public LavorazioneModel getLavorazioneModel() {
		return lavorazioneModel;
	}
	public ConsumiClienteModel setLavorazioneModel(LavorazioneModel lavorazioneModel) {
		this.lavorazioneModel = lavorazioneModel;
		return this;
	}
}