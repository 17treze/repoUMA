package it.tndigitale.a4g.framework.client.custom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MetadatiDto implements Serializable {
	private String oggetto;

	private MittenteDto mittente;

	private TipologiaDocumentoPrincipale tipologiaDocumentoPrincipale;

	private Map<String, String> metadatiTemplate = new HashMap<String, String>();

	public String getOggetto() {
		return oggetto;
	}

	public MetadatiDto setOggetto(String oggetto) {
		this.oggetto = oggetto;
		return this;
	}

	public MittenteDto getMittente() {
		return mittente;
	}

	public MetadatiDto setMittente(MittenteDto mittente) {
		this.mittente = mittente;
		return this;
	}

	public TipologiaDocumentoPrincipale getTipologiaDocumentoPrincipale() {
		return tipologiaDocumentoPrincipale;
	}

	public MetadatiDto setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale tipologiaDocumentoPrincipale) {
		this.tipologiaDocumentoPrincipale = tipologiaDocumentoPrincipale;
		return this;
	}

	public Map<String, String> getMetadatiTemplate() {
		return metadatiTemplate;
	}

	public MetadatiDto setMetadatiTemplate(Map<String, String> metadatiTemplate) {
		this.metadatiTemplate = metadatiTemplate;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MetadatiDto that = (MetadatiDto) o;
		return Objects.equals(oggetto, that.oggetto) &&
				Objects.equals(mittente, that.mittente) &&
				tipologiaDocumentoPrincipale == that.tipologiaDocumentoPrincipale &&
				Objects.equals(metadatiTemplate, that.metadatiTemplate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(oggetto, mittente, tipologiaDocumentoPrincipale, metadatiTemplate);
	}

	public enum TipologiaDocumentoPrincipale {
		PRIVACY, ANTIMAFIA, ACCESSO_SISTEMA, MANDATO, MANDATO_REVOCA_IMMEDIATA, SCHEDA_VALIDAZIONE, RICHIESTA_CARBURANTE;
	}
}
