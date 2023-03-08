package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TITOLI_PAC database table.
 * 
 */
@Entity
@Table(name="TITOLI_PAC")
@NamedQuery(name="TitoliPac.findAll", query="SELECT t FROM TitoliPac t")
public class TitoliPac implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIT")
	private long idTit;

	private String blocco;

	@Column(name="COD_OP")
	private String codOp;

	private String cuaa;

	@Column(name="CUAA_PROP")
	private String cuaaProp;

	@Column(name="DATA_FINE_POSS")
	private String dataFinePoss;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_REC")
	private Date dataFineRec;

	@Column(name="DATA_INIZ")
	private String dataIniz;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INS_REC")
	private Date dataInsRec;

	@Column(name="DECO_MOVI")
	private String decoMovi;

	@Column(name="DECO_ORIG")
	private String decoOrig;

	@Column(name="DECO_SALA")
	private BigDecimal decoSala;

	@Column(name="DECO_SALA_VALI")
	private BigDecimal decoSalaVali;

	@Column(name="DECO_TIPO")
	private String decoTipo;

	@Column(name="NUME_CAMP_FINE")
	private BigDecimal numeCampFine;

	@Column(name="NUME_CAMP_INIZ")
	private BigDecimal numeCampIniz;

	@Column(name="NUME_TITO")
	private String numeTito;

	@Column(name="PRESENZA_PEGNO")
	private String presenzaPegno;

	@Column(name="PRESENZA_VINCOLO")
	private String presenzaVincolo;

	@Column(name="SUPE_TITO")
	private BigDecimal supeTito;

	private String utilizzo;

	@Column(name="VALO_TITO_2015")
	private BigDecimal valoTito2015;

	@Column(name="VALO_TITO_2016")
	private BigDecimal valoTito2016;

	@Column(name="VALO_TITO_2017")
	private BigDecimal valoTito2017;

	@Column(name="VALO_TITO_2018")
	private BigDecimal valoTito2018;

	@Column(name="VALO_TITO_2019")
	private BigDecimal valoTito2019;

	@Column(name="VALO_TITO_2020")
	private BigDecimal valoTito2020;
	
	@Column(name="VALO_TITO_2021")
	private BigDecimal valoTito2021;

	@Column(name="VALO_TITO_2022")
	private BigDecimal valoTito2022;

	public TitoliPac() {
	}

	public long getIdTit() {
		return this.idTit;
	}

	public void setIdTit(long idTit) {
		this.idTit = idTit;
	}

	public String getBlocco() {
		return this.blocco;
	}

	public void setBlocco(String blocco) {
		this.blocco = blocco;
	}

	public String getCodOp() {
		return this.codOp;
	}

	public void setCodOp(String codOp) {
		this.codOp = codOp;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getCuaaProp() {
		return this.cuaaProp;
	}

	public void setCuaaProp(String cuaaProp) {
		this.cuaaProp = cuaaProp;
	}

	public String getDataFinePoss() {
		return this.dataFinePoss;
	}

	public void setDataFinePoss(String dataFinePoss) {
		this.dataFinePoss = dataFinePoss;
	}

	public Date getDataFineRec() {
		return this.dataFineRec;
	}

	public void setDataFineRec(Date dataFineRec) {
		this.dataFineRec = dataFineRec;
	}

	public String getDataIniz() {
		return this.dataIniz;
	}

	public void setDataIniz(String dataIniz) {
		this.dataIniz = dataIniz;
	}

	public Date getDataInsRec() {
		return this.dataInsRec;
	}

	public void setDataInsRec(Date dataInsRec) {
		this.dataInsRec = dataInsRec;
	}

	public String getDecoMovi() {
		return this.decoMovi;
	}

	public void setDecoMovi(String decoMovi) {
		this.decoMovi = decoMovi;
	}

	public String getDecoOrig() {
		return this.decoOrig;
	}

	public void setDecoOrig(String decoOrig) {
		this.decoOrig = decoOrig;
	}

	public BigDecimal getDecoSala() {
		return this.decoSala;
	}

	public void setDecoSala(BigDecimal decoSala) {
		this.decoSala = decoSala;
	}

	public BigDecimal getDecoSalaVali() {
		return this.decoSalaVali;
	}

	public void setDecoSalaVali(BigDecimal decoSalaVali) {
		this.decoSalaVali = decoSalaVali;
	}

	public String getDecoTipo() {
		return this.decoTipo;
	}

	public void setDecoTipo(String decoTipo) {
		this.decoTipo = decoTipo;
	}

	public BigDecimal getNumeCampFine() {
		return this.numeCampFine;
	}

	public void setNumeCampFine(BigDecimal numeCampFine) {
		this.numeCampFine = numeCampFine;
	}

	public BigDecimal getNumeCampIniz() {
		return this.numeCampIniz;
	}

	public void setNumeCampIniz(BigDecimal numeCampIniz) {
		this.numeCampIniz = numeCampIniz;
	}

	public String getNumeTito() {
		return this.numeTito;
	}

	public void setNumeTito(String numeTito) {
		this.numeTito = numeTito;
	}

	public String getPresenzaPegno() {
		return this.presenzaPegno;
	}

	public void setPresenzaPegno(String presenzaPegno) {
		this.presenzaPegno = presenzaPegno;
	}

	public String getPresenzaVincolo() {
		return this.presenzaVincolo;
	}

	public void setPresenzaVincolo(String presenzaVincolo) {
		this.presenzaVincolo = presenzaVincolo;
	}

	public BigDecimal getSupeTito() {
		return this.supeTito;
	}

	public void setSupeTito(BigDecimal supeTito) {
		this.supeTito = supeTito;
	}

	public String getUtilizzo() {
		return this.utilizzo;
	}

	public void setUtilizzo(String utilizzo) {
		this.utilizzo = utilizzo;
	}

	public BigDecimal getValoTito2015() {
		return this.valoTito2015;
	}

	public void setValoTito2015(BigDecimal valoTito2015) {
		this.valoTito2015 = valoTito2015;
	}

	public BigDecimal getValoTito2016() {
		return this.valoTito2016;
	}

	public void setValoTito2016(BigDecimal valoTito2016) {
		this.valoTito2016 = valoTito2016;
	}

	public BigDecimal getValoTito2017() {
		return this.valoTito2017;
	}

	public void setValoTito2017(BigDecimal valoTito2017) {
		this.valoTito2017 = valoTito2017;
	}

	public BigDecimal getValoTito2018() {
		return this.valoTito2018;
	}

	public void setValoTito2018(BigDecimal valoTito2018) {
		this.valoTito2018 = valoTito2018;
	}

	public BigDecimal getValoTito2019() {
		return this.valoTito2019;
	}

	public void setValoTito2019(BigDecimal valoTito2019) {
		this.valoTito2019 = valoTito2019;
	}

	public BigDecimal getValoTito2020() {
		return this.valoTito2020;
	}

	public void setValoTito2020(BigDecimal valoTito2020) {
		this.valoTito2020 = valoTito2020;
	}

	public BigDecimal getValoTito2021() {
		return valoTito2021;
	}

	public void setValoTito2021(BigDecimal valoTito2021) {
		this.valoTito2021 = valoTito2021;
	}

	public BigDecimal getValoTito2022() {
		return valoTito2022;
	}

	public void setValoTito2022(BigDecimal valoTito2022) {
		this.valoTito2022 = valoTito2022;
	}

}