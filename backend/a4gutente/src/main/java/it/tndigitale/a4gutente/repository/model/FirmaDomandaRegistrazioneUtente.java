package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_FIRMA_DOMANDA_REG")
public class FirmaDomandaRegistrazioneUtente extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 2753522338345477695L;

	private Long idDomanda;
	
	private String xml;
	
	@Lob
	private byte[] pdf;

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}	
}
