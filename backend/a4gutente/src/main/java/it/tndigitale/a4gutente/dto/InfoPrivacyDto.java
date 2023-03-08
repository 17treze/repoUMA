package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

//@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class InfoPrivacyDto implements Serializable {
	 private String infoIn; 
	 private ByteArrayResource documento; 
	 private List<ByteArrayResource> allegati = new ArrayList<ByteArrayResource>();
	 private String richiedenteCodiceFiscale;
	 
	 public InfoPrivacyDto() {
		 super();
	 }

	 public String getRichiedenteCodiceFiscale() {
		return this.richiedenteCodiceFiscale;
	}

	public InfoPrivacyDto setRichiedenteCodiceFiscale(String codiceFiscale) {
		this.richiedenteCodiceFiscale = codiceFiscale;
		return this;
	}

	public String getInfoIn() {
		return infoIn;
	}

	public InfoPrivacyDto setInfoIn(String infoIn) {
		this.infoIn = infoIn;
		return this;
	}

	public ByteArrayResource getDocumento() {
		return documento;
	}

	public InfoPrivacyDto setDocumento(ByteArrayResource documento) {
		this.documento = documento;
		return this;
	}

	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}

	public InfoPrivacyDto setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
		return this;
	}
}
