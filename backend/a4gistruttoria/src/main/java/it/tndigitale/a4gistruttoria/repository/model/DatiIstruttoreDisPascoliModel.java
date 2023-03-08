package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the A4GT_ISTRUTTORE_DIS_PASCOLI database table.
 * 
 */
@Entity
@Table(name="A4GT_ISTRUTTORE_DIS_PASCOLI")
@NamedQuery(name="DatiIstruttoreDisPascoliModel.findAll", query="SELECT d FROM DatiIstruttoreDisPascoliModel d")
public class DatiIstruttoreDisPascoliModel  extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 4699367786976350385L;

	@Column(name="CUAA_RESPONSABILE")
	private String cuaaResponsabile;

	@Column(name="DESCRIZIONE_PASCOLO")
	private String descrizionePascolo;

	@Column(name="ESITO_CONTROLLO_MANTENIMENTO")
	private String esitoControlloMantenimento;

	@Column(name="SUPERFICIE_DETERMINATA")
	private BigDecimal superficieDeterminata;

	@Column(name="VERIFICA_DOCUMENTAZIONE")
	private String verificaDocumentazione;

	//bi-directional many-to-one association to A4gtLavorazioneSostegno
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;

	public DatiIstruttoreDisPascoliModel() {
	}

	public String getCuaaResponsabile() {
		return this.cuaaResponsabile;
	}

	public void setCuaaResponsabile(String cuaaResponsabile) {
		this.cuaaResponsabile = cuaaResponsabile;
	}

	public String getDescrizionePascolo() {
		return this.descrizionePascolo;
	}

	public void setDescrizionePascolo(String descrizionePascolo) {
		this.descrizionePascolo = descrizionePascolo;
	}

	public String getEsitoControlloMantenimento() {
		return this.esitoControlloMantenimento;
	}

	public void setEsitoControlloMantenimento(String esitoControlloMantenimento) {
		this.esitoControlloMantenimento = esitoControlloMantenimento;
	}

	public BigDecimal getSuperficieDeterminata() {
		return this.superficieDeterminata;
	}

	public void setSuperficieDeterminata(BigDecimal superficieDeterminata) {
		this.superficieDeterminata = superficieDeterminata;
	}

	public String getVerificaDocumentazione() {
		return this.verificaDocumentazione;
	}

	public void setVerificaDocumentazione(String verificaDocumentazione) {
		this.verificaDocumentazione = verificaDocumentazione;
	}

	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}

	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

}