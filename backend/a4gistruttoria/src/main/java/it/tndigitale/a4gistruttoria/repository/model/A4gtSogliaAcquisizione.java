package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the A4GT_SOGLIA_ACQUISIZIONE database table.
 * 
 */
@Entity
@Table(name="A4GT_SOGLIA_ACQUISIZIONE")
@NamedQuery(name="A4gtSogliaAcquisizione.findAll", query="SELECT a FROM A4gtSogliaAcquisizione a")
public class A4gtSogliaAcquisizione extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_APPLICAZIONE", nullable=false)
	private Date dataFineApplicazione;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO_APPLICAZIONE", nullable=false)
	private Date dataInizioApplicazione;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_PRESENTAZIONE", nullable=false)
	private Date dataPresentazione;

	@Column(nullable=false, length=50)
	private String settore;

	@Column(nullable=false, precision=20)
	private BigDecimal soglia;

	public A4gtSogliaAcquisizione() {
	}

	public Date getDataFineApplicazione() {
		return this.dataFineApplicazione;
	}

	public void setDataFineApplicazione(Date dataFineApplicazione) {
		this.dataFineApplicazione = dataFineApplicazione;
	}

	public Date getDataInizioApplicazione() {
		return this.dataInizioApplicazione;
	}

	public void setDataInizioApplicazione(Date dataInizioApplicazione) {
		this.dataInizioApplicazione = dataInizioApplicazione;
	}

	public Date getDataPresentazione() {
		return this.dataPresentazione;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public String getSettore() {
		return this.settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public BigDecimal getSoglia() {
		return this.soglia;
	}

	public void setSoglia(BigDecimal soglia) {
		this.soglia = soglia;
	}
}