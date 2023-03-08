package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import it.tndigitale.a4g.proxy.dto.SiNoEnum;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "APPO_SUPE_ACCERT")
@NamedQuery(name = "AppoSupeAccert.findAll", query = "SELECT a FROM AppoSupeAccert a WHERE current_date() >= DATA_INIZIO_VAL AND current_date() < DATA_FINE_VAL")
@SQLDelete(sql ="UPDATE AppoSupeAccert t SET t.dataFineVal = current_date() WHERE id = ?")
@Where(clause = "current_date >= DATA_INIZIO_VAL AND current_date < DATA_FINE_VAL")
public class AppoSupeAccert implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_SUPE_ACCERT")
	@SequenceGenerator(name = "APPO_SUPE_ACCERT_GENERATOR", sequenceName = "APPO_SUPE_ACCERT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPO_SUPE_ACCERT_GENERATOR")		
	private long idSupeAccert;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	@Column(name="CUAA")
	private String cuaa;

	@Column(name="NUME_CAMP")
	private Long numeCamp;

	@Column(name="ID_UFFI_ORPA")
	private Long idUffiOrpa;

	@Column(name="CODI_ATTO_OPR")
	private String codiAttoOpr;

	@Column(name="CODI_INTE")
	private String codiInte;
	
	@Column(name="SUPE_ACCE_AMMI")
	private BigDecimal supeAcceAmmi;
	
	@Column(name="SUPE_DETE")
	private BigDecimal supeDete;

	@Column(name="MOTIVAZIONE_A1")
	@Enumerated(EnumType.STRING)
	private SiNoEnum motivazioneA1;
	
	@Column(name="MOTIVAZIONE_A2")
	@Enumerated(EnumType.STRING)
	private SiNoEnum motivazioneA2;
	
	@Column(name="MOTIVAZIONE_A3")
	@Enumerated(EnumType.STRING)
	private SiNoEnum motivazioneA3;

	@Column(name="MOTIVAZIONE_B0")
	@Enumerated(EnumType.STRING)
	private SiNoEnum motivazioneB0;

	@Column(name="MOTIVAZIONE_B1")
	@Enumerated(EnumType.STRING)
	private SiNoEnum motivazioneB1;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO_VAL")
	private Date dataInizioVal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VAL")
	private Date dataFineVal;
	
	@Column(name="FONTE")
	private String fonte;

	public long getIdSupeAccert() {
		return idSupeAccert;
	}

	public void setIdSupeAccert(long idSupeAccert) {
		this.idSupeAccert = idSupeAccert;
	}

	public Integer getVersione() {
		return versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Long getNumeCamp() {
		return numeCamp;
	}

	public void setNumeCamp(Long numeCamp) {
		this.numeCamp = numeCamp;
	}

	public Long getIdUffiOrpa() {
		return idUffiOrpa;
	}

	public void setIdUffiOrpa(Long idUffiOrpa) {
		this.idUffiOrpa = idUffiOrpa;
	}

	public String getCodiAttoOpr() {
		return codiAttoOpr;
	}

	public void setCodiAttoOpr(String codiAttoOpr) {
		this.codiAttoOpr = codiAttoOpr;
	}

	public String getCodiInte() {
		return codiInte;
	}

	public void setCodiInte(String codiInte) {
		this.codiInte = codiInte;
	}

	public BigDecimal getSupeAcceAmmi() {
		return supeAcceAmmi;
	}

	public void setSupeAcceAmmi(BigDecimal supeAcceAmmi) {
		this.supeAcceAmmi = supeAcceAmmi;
	}

	public BigDecimal getSupeDete() {
		return supeDete;
	}

	public void setSupeDete(BigDecimal supeDete) {
		this.supeDete = supeDete;
	}

	public SiNoEnum getMotivazioneA1() {
		return motivazioneA1;
	}

	public void setMotivazioneA1(SiNoEnum motivazioneA1) {
		this.motivazioneA1 = motivazioneA1;
	}

	public SiNoEnum getMotivazioneA2() {
		return motivazioneA2;
	}

	public void setMotivazioneA2(SiNoEnum motivazioneA2) {
		this.motivazioneA2 = motivazioneA2;
	}

	public SiNoEnum getMotivazioneA3() {
		return motivazioneA3;
	}

	public void setMotivazioneA3(SiNoEnum motivazioneA3) {
		this.motivazioneA3 = motivazioneA3;
	}

	public SiNoEnum getMotivazioneB0() {
		return motivazioneB0;
	}

	public void setMotivazioneB0(SiNoEnum motivazioneB0) {
		this.motivazioneB0 = motivazioneB0;
	}

	public Date getDataInizioVal() {
		return dataInizioVal;
	}

	public void setDataInizioVal(Date dataInizioVal) {
		this.dataInizioVal = dataInizioVal;
	}

	public Date getDataFineVal() {
		return dataFineVal;
	}

	public void setDataFineVal(Date dataFineVal) {
		this.dataFineVal = dataFineVal;
	}

	public SiNoEnum getMotivazioneB1() {
		return motivazioneB1;
	}

	public void setMotivazioneB1(SiNoEnum motivazioneB1) {
		this.motivazioneB1 = motivazioneB1;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
}