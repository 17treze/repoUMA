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
@Table(name="A4GT_PRELIEVI")
public class PrelievoModel extends EntitaDominio {

	private static final long serialVersionUID = 6468885195977338650L;

	@Column(name = "GASOLIO", length = 7, nullable = true)
	private Integer gasolio;

	@Column(name = "BENZINA", length = 7, nullable = true)
	private Integer benzina;

	@Column(name = "GASOLIO_SERRE", length = 7, nullable = true)
	private Integer gasolioSerre;

	@Column(name = "DATA", nullable = false)
	private LocalDateTime data;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISTRIBUTORE")
	private DistributoreModel distributore;

	@Column(name = "CONSEGNATO", nullable = false)
	private Boolean consegnato;

	@Column(name = "ESTREMI_DOC_FISCALE", length = 50)
	private String estremiDocumentoFiscale;

	public Integer getGasolio() {
		return gasolio;
	}
	public PrelievoModel setGasolio(Integer gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public Integer getBenzina() {
		return benzina;
	}
	public PrelievoModel setBenzina(Integer benzina) {
		this.benzina = benzina;
		return this;
	}
	public Integer getGasolioSerre() {
		return gasolioSerre;
	}
	public PrelievoModel setGasolioSerre(Integer gasolioSerre) {
		this.gasolioSerre = gasolioSerre;
		return this;
	}
	public LocalDateTime getData() {
		return data;
	}
	public PrelievoModel setData(LocalDateTime data) {
		this.data = data;
		return this;
	}
	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public PrelievoModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
	public DistributoreModel getDistributore() {
		return distributore;
	}
	public PrelievoModel setDistributore(DistributoreModel distributore) {
		this.distributore = distributore;
		return this;
	}
	public Boolean getConsegnato() {
		return consegnato;
	}
	public PrelievoModel setConsegnato(Boolean consegnato) {
		this.consegnato = consegnato;
		return this;
	}
	public String getEstremiDocumentoFiscale() {
		return estremiDocumentoFiscale;
	}
	public PrelievoModel setEstremiDocumentoFiscale(String estremiDocumentoFiscale) {
		this.estremiDocumentoFiscale = estremiDocumentoFiscale;
		return this;
	}
}
