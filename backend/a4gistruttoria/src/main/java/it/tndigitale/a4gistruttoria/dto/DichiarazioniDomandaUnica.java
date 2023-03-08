package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.Date;

public class DichiarazioniDomandaUnica implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codDocumento;
	private String idCampo;
	private String descCampo;
	private Boolean valCheck;
	private Date valDate;
	private Long valNumber;
	private String valString;
	private Long ordine;

	public String getCodDocumento() {
		return codDocumento;
	}

	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}

	public String getIdCampo() {
		return idCampo;
	}

	public void setIdCampo(String idCampo) {
		this.idCampo = idCampo;
	}

	public String getDescCampo() {
		return descCampo;
	}

	public void setDescCampo(String descCampo) {
		this.descCampo = descCampo;
	}

	public Boolean getValCheck() {
		return valCheck;
	}

	public void setValCheck(Boolean valCheck) {
		this.valCheck = valCheck;
	}

	public Date getValDate() {
		return valDate;
	}

	public void setValDate(Date valDate) {
		this.valDate = valDate;
	}

	public Long getValNumber() {
		return valNumber;
	}

	public void setValNumber(Long valNumber) {
		this.valNumber = valNumber;
	}

	public String getValString() {
		return valString;
	}

	public void setValString(String valString) {
		this.valString = valString;
	}

	public Long getOrdine() {
		return ordine;
	}

	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}

}
