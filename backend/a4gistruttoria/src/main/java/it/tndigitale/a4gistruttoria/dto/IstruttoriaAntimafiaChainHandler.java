/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author S.DeLuca
 *
 */
public class IstruttoriaAntimafiaChainHandler {

	private JsonNode datiDichiarazione;
	private String cuaa;
	private JsonNode dettaglioImpresa;
	private JsonNode soggettiParix;
	private IstruttoriaAntimafiaEsito istruttoriaAntimafiaEsito;
	private Boolean isParixOK;
	private Boolean isSiapOK;
	private Boolean areSoggettiOK;
	
	public IstruttoriaAntimafiaChainHandler() {
		super();
	}
	
	public IstruttoriaAntimafiaChainHandler(JsonNode datiDichiarazione, String cuaa, JsonNode dettaglioImpresa, JsonNode soggettiParix, IstruttoriaAntimafiaEsito istruttoriaAntimafiaEsito, Boolean isParixOK,
			Boolean isSiapOK, Boolean areSoggettiOK) {
		super();
		this.datiDichiarazione = datiDichiarazione;
		this.cuaa = cuaa;
		this.dettaglioImpresa = dettaglioImpresa;
		this.soggettiParix = soggettiParix;
		this.istruttoriaAntimafiaEsito = istruttoriaAntimafiaEsito;
		this.isParixOK = isParixOK;
		this.isSiapOK = isSiapOK;
		this.areSoggettiOK = areSoggettiOK;
	}

	public JsonNode getDatiDichiarazione() {
		return datiDichiarazione;
	}
	
	public void setDatiDichiarazione(JsonNode datiDichiarazione) {
		this.datiDichiarazione = datiDichiarazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	
	public JsonNode getDettaglioImpresa() {
		return dettaglioImpresa;
	}

	public void setDettaglioImpresa(JsonNode dettaglioImpresa) {
		this.dettaglioImpresa = dettaglioImpresa;
	}

	public JsonNode getSoggettiParix() {
		return soggettiParix;
	}

	public void setSoggettiParix(JsonNode soggettiParix) {
		this.soggettiParix = soggettiParix;
	}

	public IstruttoriaAntimafiaEsito getIstruttoriaAntimafiaEsito() {
		return istruttoriaAntimafiaEsito;
	}

	public void setIstruttoriaAntimafiaEsito(IstruttoriaAntimafiaEsito istruttoriaAntimafiaEsito) {
		this.istruttoriaAntimafiaEsito = istruttoriaAntimafiaEsito;
	}

	public Boolean getIsParixOK() {
		return istruttoriaAntimafiaEsito.getParixMessage() == null
				|| istruttoriaAntimafiaEsito.getParixMessage().isEmpty();
	}

	public Boolean getIsSiapOK() {
		return istruttoriaAntimafiaEsito.getSiapMessage() == null
				|| istruttoriaAntimafiaEsito.getSiapMessage().isEmpty();
	}

	public Boolean getAreSoggettiOK() {
		return istruttoriaAntimafiaEsito.getSoggettiMessages() == null
				|| istruttoriaAntimafiaEsito.getSoggettiMessages().isEmpty();
	}
	
	public void setParixErrorMessage(String message) {
		istruttoriaAntimafiaEsito.setParixMessage(message);
	}

	public void setSiapErrorMessage(String message) {
		istruttoriaAntimafiaEsito.setSiapMessage(message);
	}
	
	public void addSoggettiErrorMessage(String message) {
		if(istruttoriaAntimafiaEsito.getSoggettiMessages() == null) {
			istruttoriaAntimafiaEsito.setSoggettiMessages(new ArrayList<>());
		}
		istruttoriaAntimafiaEsito.getSoggettiMessages().add(message);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IstruttoriaAntimafiaChain [datiDichiarazione=");
		builder.append(datiDichiarazione);
		builder.append(", cuaa=");
		builder.append(cuaa);
		builder.append(", dettaglioImpresa=");
		builder.append(dettaglioImpresa);
		builder.append(", soggettiParix=");
		builder.append(soggettiParix);
		builder.append(", istruttoriaAntimafiaEsito=");
		builder.append(istruttoriaAntimafiaEsito);
		builder.append(", isParixOK=");
		builder.append(isParixOK);
		builder.append(", isSiapOK=");
		builder.append(isSiapOK);
		builder.append(", areSoggettiOK=");
		builder.append(areSoggettiOK);
		builder.append("]");
		return builder.toString();
	}
}
