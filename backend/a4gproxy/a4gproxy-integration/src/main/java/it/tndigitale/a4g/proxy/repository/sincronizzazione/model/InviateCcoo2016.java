package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the INVIATE_CCOO_2016 database table.
 * 
 */
@Entity
@Table(name = "INVIATE_CCOO_2016")
@NamedQuery(name = "InviateCcoo2016.findAll", query = "SELECT i FROM InviateCcoo2016 i")
public class InviateCcoo2016 implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InviateCcoo2016PK id;

	@Column(name = "COD_OP")
	private String codOp;

	@Column(name = "CODI_ESIT")
	private BigDecimal codiEsit;

	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FINE_VALI")
	private Date dataFineVali;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_RILASCIO")
	private Date dataRilascio;

	@Column(name = "DECO_TIPO_DOCU")
	private BigDecimal decoTipoDocu;

	@Column(name = "DESC_ORG_DIF")
	private String descOrgDif;

	@Column(name = "ID_ATTO_AMMI")
	private BigDecimal idAttoAmmi;

	private String nominativo;

	@Column(name = "NUM_DOM_MODIFICATO")
	private String numDomModificato;

	@Column(name = "NUMERO_DOM")
	private String numeroDom;

	@Column(name = "UFFICIO_RIL")
	private String ufficioRil;

	public InviateCcoo2016() {
	}

	public InviateCcoo2016PK getId() {
		return this.id;
	}

	public void setId(InviateCcoo2016PK id) {
		this.id = id;
	}

	public String getCodOp() {
		return this.codOp;
	}

	public void setCodOp(String codOp) {
		this.codOp = codOp;
	}

	public BigDecimal getCodiEsit() {
		return this.codiEsit;
	}

	public void setCodiEsit(BigDecimal codiEsit) {
		this.codiEsit = codiEsit;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Date getDataFineVali() {
		return this.dataFineVali;
	}

	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}

	public Date getDataRilascio() {
		return this.dataRilascio;
	}

	public void setDataRilascio(Date dataRilascio) {
		this.dataRilascio = dataRilascio;
	}

	public BigDecimal getDecoTipoDocu() {
		return this.decoTipoDocu;
	}

	public void setDecoTipoDocu(BigDecimal decoTipoDocu) {
		this.decoTipoDocu = decoTipoDocu;
	}

	public String getDescOrgDif() {
		return this.descOrgDif;
	}

	public void setDescOrgDif(String descOrgDif) {
		this.descOrgDif = descOrgDif;
	}

	public BigDecimal getIdAttoAmmi() {
		return this.idAttoAmmi;
	}

	public void setIdAttoAmmi(BigDecimal idAttoAmmi) {
		this.idAttoAmmi = idAttoAmmi;
	}

	public String getNominativo() {
		return this.nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}

	public String getNumDomModificato() {
		return this.numDomModificato;
	}

	public void setNumDomModificato(String numDomModificato) {
		this.numDomModificato = numDomModificato;
	}

	public String getNumeroDom() {
		return this.numeroDom;
	}

	public void setNumeroDom(String numeroDom) {
		this.numeroDom = numeroDom;
	}

	public String getUfficioRil() {
		return this.ufficioRil;
	}

	public void setUfficioRil(String ufficioRil) {
		this.ufficioRil = ufficioRil;
	}

}