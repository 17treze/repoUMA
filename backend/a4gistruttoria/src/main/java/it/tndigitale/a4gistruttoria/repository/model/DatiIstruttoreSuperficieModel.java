package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the A4GT_ISTRUTTORE_SUPERFICIE database table.
 * 
 */
@Entity
@Table(name="A4GT_ISTRUTTORE_SUPERFICIE")
@NamedQuery(name="DatiIstruttoreSuperficieModel.findAll", query="SELECT d FROM DatiIstruttoreSuperficieModel d")
public class DatiIstruttoreSuperficieModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 5075137184341080679L;

	@Column(name="CONTROLLO_ANTIMAFIA")
	private Boolean controlloAntimafia;

	@Column(name="CONTROLLO_SIGECO_LOCO")
	private Boolean controlloSigecoLoco;

	@Column(name="SUP_FRUMENTO_M9")
	private BigDecimal supFrumentoM9;

	@Column(name="SUP_LEGUMINOSE_M11")
	private BigDecimal supLeguminoseM11;

	@Column(name="SUP_OLIVOPENDENZA_M16")
	private BigDecimal supOlivopendenzaM16;

	@Column(name="SUP_OLIVOQUALITA_M17")
	private BigDecimal supOlivoqualitaM17;

	@Column(name="SUP_OLIVOSTANDARD_M15")
	private BigDecimal supOlivostandardM15;

	@Column(name="SUP_POMODORO_M14")
	private BigDecimal supPomodoroM14;

	@Column(name="SUP_PROTEAGINOSE_M10")
	private BigDecimal supProteaginoseM10;

	@Column(name="SUP_SOIA_M8")
	private BigDecimal supSoiaM8;

	@Column(name = "ANNULLO_RIDUZIONE")
	private Boolean annulloRiduzione;

	//bi-directional many-to-one association to A4gtLavorazioneSostegno
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;

	public DatiIstruttoreSuperficieModel() {
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

	public BigDecimal getSupFrumentoM9() {
		return this.supFrumentoM9;
	}

	public void setSupFrumentoM9(BigDecimal supFrumentoM9) {
		this.supFrumentoM9 = supFrumentoM9;
	}

	public BigDecimal getSupLeguminoseM11() {
		return this.supLeguminoseM11;
	}

	public void setSupLeguminoseM11(BigDecimal supLeguminoseM11) {
		this.supLeguminoseM11 = supLeguminoseM11;
	}

	public BigDecimal getSupOlivopendenzaM16() {
		return this.supOlivopendenzaM16;
	}

	public void setSupOlivopendenzaM16(BigDecimal supOlivopendenzaM16) {
		this.supOlivopendenzaM16 = supOlivopendenzaM16;
	}

	public BigDecimal getSupOlivoqualitaM17() {
		return this.supOlivoqualitaM17;
	}

	public void setSupOlivoqualitaM17(BigDecimal supOlivoqualitaM17) {
		this.supOlivoqualitaM17 = supOlivoqualitaM17;
	}

	public BigDecimal getSupOlivostandardM15() {
		return this.supOlivostandardM15;
	}

	public void setSupOlivostandardM15(BigDecimal supOlivostandardM15) {
		this.supOlivostandardM15 = supOlivostandardM15;
	}

	public BigDecimal getSupPomodoroM14() {
		return this.supPomodoroM14;
	}

	public void setSupPomodoroM14(BigDecimal supPomodoroM14) {
		this.supPomodoroM14 = supPomodoroM14;
	}

	public BigDecimal getSupProteaginoseM10() {
		return this.supProteaginoseM10;
	}

	public void setSupProteaginoseM10(BigDecimal supProteaginoseM10) {
		this.supProteaginoseM10 = supProteaginoseM10;
	}

	public BigDecimal getSupSoiaM8() {
		return this.supSoiaM8;
	}

	public void setSupSoiaM8(BigDecimal supSoiaM8) {
		this.supSoiaM8 = supSoiaM8;
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
