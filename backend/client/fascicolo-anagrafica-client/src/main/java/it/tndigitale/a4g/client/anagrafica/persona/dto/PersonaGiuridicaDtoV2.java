package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;

public class PersonaGiuridicaDtoV2 extends PersonaDtoV2 implements ImpresaV2 {
    private static final long serialVersionUID = -1344976774533572955L;

    private String formaGiuridica;

    private String denominazione;

    private String partitaIva;
    
    private String oggettoSociale;

    @ApiModelProperty(value = "Rappresentante legale dell'impresa (valorizzato solo per anagrafe tributaria)")
    private RappresentanteLegaleDtoV2 rappresentanteLegale;

    private SedeDtoV2 sedeLegale;

    private List<CaricaDtoV2> cariche;
    
    private LocalDate dataCostituzione;
    
    private LocalDate dataTermine;
    
    private Long capitaleSocialeDeliberato;

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
    public SedeDtoV2 getSedeLegale() {
        return this.sedeLegale;
    }

    public void setSedeLegale(SedeDtoV2 sedeLegale) {
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

    public RappresentanteLegaleDtoV2 getRappresentanteLegale() {
        return rappresentanteLegale;
    }

    public void setRappresentanteLegale(RappresentanteLegaleDtoV2 rappresentanteLegale) {
        this.rappresentanteLegale = rappresentanteLegale;
    }

    public List<CaricaDtoV2> getCariche() {
        return cariche;
    }

    public void setCariche(List<CaricaDtoV2> cariche) {
        this.cariche = cariche;
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

	public Long getCapitaleSocialeDeliberato() {
		return capitaleSocialeDeliberato;
	}

	public void setCapitaleSocialeDeliberato(Long capitaleSocialeDeliberato) {
		this.capitaleSocialeDeliberato = capitaleSocialeDeliberato;
	}

}
