package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "metadati di supporto al contratto")
public class InfoMetadatiDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "mittente del mandato", required = true)
	private MittenteDto mittente;

	@ApiModelProperty(value = "tipologia documento principale del mandato", required = false)
	private String tipologiaDocumentoPrincipale;

	@ApiModelProperty(value = "oggetto del mandato", required = true)
	private String oggetto;

	@ApiModelProperty(value = "codice fascicolo del mandato", required = true)
	private String codiceFascicolo;

	@ApiModelProperty(value = "codice fascicolo del mandato", required = true)
	private String nomeFile;

	@ApiModelProperty(value = "Mappa per i dati del template pitre", required = true)
	private Map<String, String> metadatiTemplate = new HashMap<String, String>();

	public MittenteDto getMittente() {
		return mittente;
	}

	public void setMittente(MittenteDto mittente) {
		this.mittente = mittente;
	}

	public String getTipologiaDocumentoPrincipale() {
		return tipologiaDocumentoPrincipale;
	}

	public void setTipologiaDocumentoPrincipale(String tipologiaDocumentoPrincipale) {
		this.tipologiaDocumentoPrincipale = tipologiaDocumentoPrincipale;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getCodiceFascicolo() {
		return codiceFascicolo;
	}

	public void setCodiceFascicolo(String codiceFascicolo) {
		this.codiceFascicolo = codiceFascicolo;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Map<String, String> getMetadatiTemplate() {
		return metadatiTemplate;
	}

	public void setMetadatiTemplate(Map<String, String> metadatiTemplate) {
		this.metadatiTemplate = metadatiTemplate;
	}

}
