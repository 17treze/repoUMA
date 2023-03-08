package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "PAGAMENTI")
@NamedQuery(name = "Pagamenti.findAll", query = "SELECT a FROM Pagamenti a WHERE current_date() >= DATA_INIZ_VALI AND current_date() < DATA_FINE_VALI")
@SQLDelete(sql ="UPDATE Pagamenti t SET t.dataFineVali = current_date(), t.decoStat = 93 WHERE id = ?")
@Where(clause = "current_date >= DATA_INIZ_VALI AND current_date < DATA_FINE_VALI")
public class Pagamenti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PK_ID_PAGAM")
	@SequenceGenerator(name = "PAGAMENTI_GENERATOR", sequenceName = "PAGAMENTI_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGAMENTI_GENERATOR")		
	private long pkIdPagam;

	@Version
	@Column(name="VERSIONE")
	private Integer versione;
	
	@Column(name="NUME_PROG_DECR")
	private Long numeProgDecr;

	@Column(name="NUME_CAMP_RIFE")
	private Long numeCampRife;
	
	@Column(name="ID_ENTE")
	private Long idEnte;

	@Column(name="DESC_ENTE")
	private String descEnte;
	
	@Column(name="BC_ATTO_AMMI")
	private String bcAttoAmmi;

	@Column(name="ID_ATTO_AMMI")
	private Long idAttoAmmi;

	@Column(name="CODI_FISC")
	private String codiFisc;

	@Column(name="CODI_ATTO_OPR")
	private String codiAttoOpr;
	
	@Column(name="ID_INTE")
	private Long idInte;
	
	@Column(name="CODI_INTE")
	private Long codiInte;
	
	@Column(name="IMPO_DETE")
	private BigDecimal impoDete;
	
	@Column(name="IMPO_LIQUI")
	private BigDecimal impoLiqui;
	
	@Column(name="IMPO_RICHIESTO")
	private BigDecimal impoRichiesto;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGGI")
	private Date dataAggi;
	
	@Column(name="DECO_STAT_PAGA")
	private Long decoStatPaga;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private Date dataInizVali;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;
	
	@Column(name="DECO_STAT")
	private Long decoStat;
	
	@Column(name="FONTE")
	private String fonte;

	public long getIdPagamenti() {
		return pkIdPagam;
	}

	public void setIdPagamenti(long idPagamenti) {
		this.pkIdPagam = idPagamenti;
	}

	public Integer getVersione() {
		return versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public Long getNumeProgDecr() {
		return numeProgDecr;
	}

	public void setNumeProgDecr(Long numeProgDecr) {
		this.numeProgDecr = numeProgDecr;
	}

	public Long getNumeCampRife() {
		return numeCampRife;
	}

	public void setNumeCampRife(Long numeCampRife) {
		this.numeCampRife = numeCampRife;
	}

	public Long getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}

	public String getDescEnte() {
		return descEnte;
	}

	public void setDescEnte(String descEnte) {
		this.descEnte = descEnte;
	}

	public String getBcAttoAmmi() {
		return bcAttoAmmi;
	}

	public void setBcAttoAmmi(String bcAttoAmmi) {
		this.bcAttoAmmi = bcAttoAmmi;
	}

	public Long getIdAttoAmmi() {
		return idAttoAmmi;
	}

	public void setIdAttoAmmi(Long idAttoAmmi) {
		this.idAttoAmmi = idAttoAmmi;
	}

	public String getCodiFisc() {
		return codiFisc;
	}

	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}

	public String getCodiAttoOpr() {
		return codiAttoOpr;
	}

	public void setCodiAttoOpr(String codiAttoOpr) {
		this.codiAttoOpr = codiAttoOpr;
	}

	public Long getIdInte() {
		return idInte;
	}

	public void setIdInte(Long idInte) {
		this.idInte = idInte;
	}

	public Long getCodiInte() {
		return codiInte;
	}

	public void setCodiInte(Long codiInte) {
		this.codiInte = codiInte;
	}

	public BigDecimal getImpoDete() {
		return impoDete;
	}

	public void setImpoDete(BigDecimal impoDete) {
		this.impoDete = impoDete;
	}

	public BigDecimal getImpoLiqui() {
		return impoLiqui;
	}

	public void setImpoLiqui(BigDecimal impoLiqui) {
		this.impoLiqui = impoLiqui;
	}

	public BigDecimal getImpoRichiesto() {
		return impoRichiesto;
	}

	public void setImpoRichiesto(BigDecimal impoRichiesto) {
		this.impoRichiesto = impoRichiesto;
	}

	public Date getDataAggi() {
		return dataAggi;
	}

	public void setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
	}

	public Long getDecoStatPaga() {
		return decoStatPaga;
	}

	public void setDecoStatPaga(Long decoStatPaga) {
		this.decoStatPaga = decoStatPaga;
	}

	public Date getDataInizVali() {
		return dataInizVali;
	}

	public void setDataInizVali(Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}

	public Date getDataFineVali() {
		return dataFineVali;
	}

	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}

	public Long getDecoStat() {
		return decoStat;
	}

	public void setDecoStat(Long decoStat) {
		this.decoStat = decoStat;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
}