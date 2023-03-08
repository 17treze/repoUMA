package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;

public class PersonaGiuridicaDto extends PersonaDto implements Impresa {
    private static final long serialVersionUID = -1344976774533572955L;

    private String formaGiuridica;

    private String denominazione;

    private String partitaIva;
    
    private String oggettoSociale;

    @ApiModelProperty(value = "Rappresentante legale dell'impresa (valorizzato solo per anagrafe tributaria)")
    private RappresentanteLegaleDto rappresentanteLegale;

    private SedeDto sedeLegale;

    private LocalDate dataCostituzione;
    
    private LocalDate dataTermine;
    
    private Double capitaleSocialeDeliberato;
    
    private List<PersonaFisicaConCaricaDto> personeFisicheConCarica;
    
    private List<PersonaGiuridicaConCaricaDto> personeGiuridicheConCarica;
    
    private List<UnitaLocaleDto> unitaLocali;

    @Override
    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getFormaGiuridica() {
        return formaGiuridica;
    }

    @Override
    public SedeDto getSedeLegale() {
        return this.sedeLegale;
    }

    public void setSedeLegale(SedeDto sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    public void setFormaGiuridica(String formaGiuridica) {
        this.formaGiuridica = formaGiuridica;
    }

    @Override
    public String getPartitaIva() {
        return this.partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public RappresentanteLegaleDto getRappresentanteLegale() {
        return rappresentanteLegale;
    }

    public void setRappresentanteLegale(RappresentanteLegaleDto rappresentanteLegale) {
        this.rappresentanteLegale = rappresentanteLegale;
    }

	@Override
	public String getOggettoSociale() {
		return oggettoSociale;
	}

	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}
	
	public LocalDate getDataCostituzione() {
		return dataCostituzione;
	}

	public void setDataCostituzione(LocalDate dataCostituzione) {
		this.dataCostituzione = dataCostituzione;
	}

	public LocalDate getDataTermine() {
		return dataTermine;
	}

	public void setDataTermine(LocalDate dataTermine) {
		this.dataTermine = dataTermine;
	}

	public Double getCapitaleSocialeDeliberato() {
		return capitaleSocialeDeliberato;
	}

	public void setCapitaleSocialeDeliberato(Double capitaleSocialeDeliberato) {
		this.capitaleSocialeDeliberato = capitaleSocialeDeliberato;
	}
	
	public List<PersonaFisicaConCaricaDto> getPersoneFisicheConCarica() {
		return personeFisicheConCarica;
	}

	public void setPersoneFisicheConCarica(List<PersonaFisicaConCaricaDto> personeFisicheConCarica) {
		this.personeFisicheConCarica = personeFisicheConCarica;
	}

	public List<PersonaGiuridicaConCaricaDto> getPersoneGiuridicheConCarica() {
		return personeGiuridicheConCarica;
	}

	public void setPersoneGiuridicheConCarica(List<PersonaGiuridicaConCaricaDto> personeGiuridicheConCarica) {
		this.personeGiuridicheConCarica = personeGiuridicheConCarica;
	}

	public List<UnitaLocaleDto> getUnitaLocali() {
		return unitaLocali;
	}

	public void setUnitaLocali(List<UnitaLocaleDto> unitaLocali) {
		this.unitaLocali = unitaLocali;
	}

}
