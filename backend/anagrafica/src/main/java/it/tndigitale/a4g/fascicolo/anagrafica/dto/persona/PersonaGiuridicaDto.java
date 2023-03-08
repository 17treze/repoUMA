package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;

public class PersonaGiuridicaDto extends PersonaDto {
	private static final long serialVersionUID = 1L;

	private String formaGiuridica;

    private String denominazione;

    private String partitaIva;
    
    private String oggettoSociale;
    
    @ApiModelProperty(value = "Rappresentante legale dell'impresa (valorizzato solo per anagrafe tributaria)")
    private RappresentanteLegaleDto rappresentanteLegale;

    private SedeDto sedeLegale;

//    private List<PersonaFisicaConCaricaDto> cariche;
    
    private LocalDate dataCostituzione;
    
    private LocalDate dataTermine;
    
    private Double capitaleSocialeDeliberato;

	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getOggettoSociale() {
		return oggettoSociale;
	}

	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}

	public SedeDto getSedeLegale() {
		return sedeLegale;
	}

	public void setSedeLegale(SedeDto sedeLegale) {
		this.sedeLegale = sedeLegale;
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

	public RappresentanteLegaleDto getRappresentanteLegale() {
		return rappresentanteLegale;
	}

	public void setRappresentanteLegale(RappresentanteLegaleDto rappresentanteLegale) {
		this.rappresentanteLegale = rappresentanteLegale;
	}
}
