package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the A4GT_ISTRUTTORE_ZOOTECNIA database table.
 * 
 */
@Entity
@Table(name="A4GT_ISTRUTTORE_ZOOTECNIA")
@NamedQuery(name="DatiIstruttoreZootecniaModel.findAll", query="SELECT d FROM DatiIstruttoreZootecniaModel d")
public class DatiIstruttoreZootecniaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = -8467948628358995695L;

	@Column(name="CONTROLLO_ANTIMAFIA")
	private Boolean controlloAntimafia;

	@Column(name="CONTROLLO_SIGECO_LOCO")
	private Boolean controlloSigecoLoco;
	
	@Column(name="CUAA_SUBENTRANTE")
	private String cuaaSubentrante;

	@Column(name = "ANNULLO_RIDUZIONE")
	private Boolean annulloRiduzione;

	//bi-directional many-to-one association to A4gtLavorazioneSostegno
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;

	public DatiIstruttoreZootecniaModel() {
	}

	public Boolean getControlloAntimafia() {
		return this.controlloAntimafia;
	}

	public void setControlloAntimafia(Boolean controlloAntimafia) {
		this.controlloAntimafia = controlloAntimafia;
	}

	public Boolean getControlloSigecoLoco() {
		return this.controlloSigecoLoco;
	}

	public void setControlloSigecoLoco(Boolean controlloSigecoLoco) {
		this.controlloSigecoLoco = controlloSigecoLoco;
	}
	
	public String getCuaaSubentrante() {
		return this.cuaaSubentrante;
	}

	public void setCuaaSubentrante(String cuaaSubentrante) {
		this.cuaaSubentrante = cuaaSubentrante;
	}

	public Boolean getAnnulloRiduzione() {
		return annulloRiduzione;
	}

	public void setAnnulloRiduzione(Boolean annulloRiduzione) {
		this.annulloRiduzione = annulloRiduzione;
	}

	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}

	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

}
