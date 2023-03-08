package it.tndigitale.a4g.proxy.dto.persona;

import java.util.List;

public class ImpresaIndividualeDto implements Impresa {
    private String partitaIva;

    private String denominazione;

    private SedeDto sedeLegale;

    private String formaGiuridica;
    
    private String oggettoSociale;
    
    private List<UnitaLocaleDto> unitaLocali;

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
    public SedeDto getSedeLegale() {
        return this.sedeLegale;
    }

    public void setSedeLegale(SedeDto sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    @Override
    public String getFormaGiuridica() {
        return this.formaGiuridica;
    }

    public void setFormaGiuridica(String formaGiuridica) {
        this.formaGiuridica = formaGiuridica;
    }

	@Override
	public String getOggettoSociale() {
		return this.oggettoSociale;
	}

	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}
	
	public List<UnitaLocaleDto> getUnitaLocali() {
		return unitaLocali;
	}

	public void setUnitaLocali(List<UnitaLocaleDto> unitaLocali) {
		this.unitaLocali = unitaLocali;
	}
	
}
