package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ite3279
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class DomandaRegistrazioneUtenteDto implements Serializable {
	
	private Long idDomanda;
	private ByteArrayResource documentoPrincipale;
	private ObjectNode jsonProtocollazione;
	private List<ByteArrayResource> allegati = new ArrayList<ByteArrayResource>();
	
	public DomandaRegistrazioneUtenteDto() {
		super();
	}
	
	public ByteArrayResource getDocumentoPrincipale() {
		return this.documentoPrincipale;
	}

	public void setDocumentoPrincipale(ByteArrayResource documentoPrincipale) {
		this.documentoPrincipale = documentoPrincipale;
	}

	public Long getIdDomanda() {
		return this.idDomanda;
	}

	public DomandaRegistrazioneUtenteDto setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
		return this;
	}
	
	public void setJsonProtocollazione(ObjectNode jsonProtocollazione) {
		this.jsonProtocollazione = jsonProtocollazione;
	}
	
	public ObjectNode getJsonProtocollazione() {
		return jsonProtocollazione;
	}

	public List<ByteArrayResource> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ByteArrayResource> allegati) {
		this.allegati = allegati;
	}
}
