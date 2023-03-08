package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Entity
@Table(name = "A4GT_DOMANDE_COLLEGATE")
@NamedQuery(name = "A4gtDomandeCollegate.findAll", query = "SELECT a FROM A4gtDomandeCollegate a")
public class A4gtDomandeCollegate extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CUAA")
	private String cuaa;

	@Column(name = "TIPO_DOMANDA")
	@Enumerated(EnumType.STRING)
	private TipoDomandaCollegata tipoDomanda;

	@Column(name = "ID_DOMANDA")
	private Long idDomanda;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_DOMANDA")
	private Date dtDomanda;

	@Column(name = "CAMPAGNA")
	private Integer campagna;

	@Column(name = "IMPORTO_RICHIESTO")
	private BigDecimal importoRichiesto;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_BDNA_OP")
	private Date dtBdnaOp;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_BDNA")
	private Date dtBdna;

	@Column(name = "PROTOCOLLO")
	private String protocollo;

	@Column(name = "STATO_BDNA")
	@Enumerated(EnumType.STRING)
	private StatoDomandaCollegata statoBdna;

	@Column(name = "MISURE_PSR")
	private String misurePsr;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INIZIO_SILENZIO_ASSENSO")
	private Date dtInizioSilenzioAssenso;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINE_SILENZIO_ASSENSO")
	private Date dtFineSilenzioAssenso;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INIZIO_ESITO_NEGATIVO")
	private Date dtInizioEsitoNegativo;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINE_ESITO_NEGATIVO")
	private Date dtFineEsitoNegativo;

	@Column(name = "STATO_BDNA_AGGIORNATO")
	@Enumerated(EnumType.STRING)
	private StatoDomandaCollegata statoBdnaAggiornato;
	
	//bi-directional many-to-one association to A4gtTrasmissioneBdna
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TRASMISSIONE_BDNA")
	private A4gtTrasmissioneBdna a4gtTrasmissioneBdna;
	
	
	public A4gtTrasmissioneBdna getA4gtTrasmissioneBdna() {
		return a4gtTrasmissioneBdna;
	}

	public void setA4gtTrasmissioneBdna(A4gtTrasmissioneBdna a4gtTrasmissioneBdna) {
		this.a4gtTrasmissioneBdna = a4gtTrasmissioneBdna;
	}
	public String getCuaa() {
		return cuaa;
	}

	public TipoDomandaCollegata getTipoDomanda() {
		return tipoDomanda;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public Date getDtDomanda() {
		return dtDomanda;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public Date getDtBdnaOp() {
		return dtBdnaOp;
	}

	public Date getDtBdna() {
		return dtBdna;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public StatoDomandaCollegata getStatoBdna() {
		return statoBdna;
	}

	public String getMisurePsr() {
		return misurePsr;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setTipoDomanda(TipoDomandaCollegata tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public void setDtDomanda(Date dtDomanda) {
		this.dtDomanda = dtDomanda;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public void setDtBdnaOp(Date dtBdnaOp) {
		this.dtBdnaOp = dtBdnaOp;
	}

	public void setDtBdna(Date dtBdna) {
		this.dtBdna = dtBdna;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public void setStatoBdna(StatoDomandaCollegata statoBdna) {
		this.statoBdna = statoBdna;
	}

	public void setMisurePsr(String misurePsr) {
		this.misurePsr = misurePsr;
	}

	public Date getDtInizioSilenzioAssenso() {
		return dtInizioSilenzioAssenso;
	}

	public Date getDtFineSilenzioAssenso() {
		return dtFineSilenzioAssenso;
	}

	public Date getDtInizioEsitoNegativo() {
		return dtInizioEsitoNegativo;
	}

	public Date getDtFineEsitoNegativo() {
		return dtFineEsitoNegativo;
	}

	public StatoDomandaCollegata getStatoBdnaAggiornato() {
		return statoBdnaAggiornato;
	}

	public void setDtInizioSilenzioAssenso(Date dtInizioSilenzioAssenso) {
		this.dtInizioSilenzioAssenso = dtInizioSilenzioAssenso;
	}

	public void setDtFineSilenzioAssenso(Date dtFineSilenzioAssenso) {
		this.dtFineSilenzioAssenso = dtFineSilenzioAssenso;
	}

	public void setDtInizioEsitoNegativo(Date dtInizioEsitoNegativo) {
		this.dtInizioEsitoNegativo = dtInizioEsitoNegativo;
	}

	public void setDtFineEsitoNegativo(Date dtFineEsitoNegativo) {
		this.dtFineEsitoNegativo = dtFineEsitoNegativo;
	}

	public void setStatoBdnaAggiornato(StatoDomandaCollegata statoBdnaAggiornato) {
		this.statoBdnaAggiornato = statoBdnaAggiornato;
	}
}
