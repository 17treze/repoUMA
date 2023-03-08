package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class ImpresaIndividualeDto implements ImpresaV2 {
    private String partitaIva;

    private String denominazione;

    private SedeDtoV2 sedeLegale;

    private static String FORMA_GIURIDICA = "Impresa individuale";
    
    private String oggettoSociale;

    @Override
    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    @Override
    public String getDenominazione() {
        return this.denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    @Override
    public SedeDtoV2 getSedeLegale() {
        return this.sedeLegale;
    }

    public void setSedeLegale(SedeDtoV2 sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    @Override
    public String getFormaGiuridica() {
        return FORMA_GIURIDICA;
    }

	@Override
	public String getOggettoSociale() {
		return this.oggettoSociale;
	}

	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}
	
	
}
