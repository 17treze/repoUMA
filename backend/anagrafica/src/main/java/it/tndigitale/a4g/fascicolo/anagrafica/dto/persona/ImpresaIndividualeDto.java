package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;

public class ImpresaIndividualeDto implements Serializable {
	private static final long serialVersionUID = -886109950832428152L;

	private String partitaIva;

    private String denominazione;

    private SedeDto sedeLegale;

    private String formaGiuridica;
    
    private String oggettoSociale;

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public SedeDto getSedeLegale() {
		return sedeLegale;
	}

	public void setSedeLegale(SedeDto sedeLegale) {
		this.sedeLegale = sedeLegale;
	}

	public String getOggettoSociale() {
		return oggettoSociale;
	}

	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}

	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}
}
