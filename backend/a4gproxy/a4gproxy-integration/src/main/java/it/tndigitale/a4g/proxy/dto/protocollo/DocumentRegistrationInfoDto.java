package it.tndigitale.a4g.proxy.dto.protocollo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentRegistrationInfoDto {

	private String oggetto;

	private CorrespondentDto mittente;

	private String tipologiaDocumentoPrincipale;

	private Map<String, String> metadatiTemplate = new HashMap<String, String>();

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public CorrespondentDto getMittente() {
		return mittente;
	}

	public void setMittente(CorrespondentDto mittente) {
		this.mittente = mittente;
	}
	
	public String getTipologiaDocumentoPrincipale() {
		return tipologiaDocumentoPrincipale;
	}

	public void setTipologiaDocumentoPrincipale(String tipologiaDocumentoPrincipale) {
		this.tipologiaDocumentoPrincipale = tipologiaDocumentoPrincipale;
	}

	public Map<String, String> getMetadatiTemplate() {
		return metadatiTemplate;
	}

	public void setMetadatiTemplate(Map<String, String> metadatiTemplate) {
		this.metadatiTemplate = metadatiTemplate;
	}
}
