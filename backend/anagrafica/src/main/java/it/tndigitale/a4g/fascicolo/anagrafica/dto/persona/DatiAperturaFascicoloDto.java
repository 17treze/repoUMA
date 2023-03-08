package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dati di anagrafe tributaria che possono essere visualizzati in fase di apertura del fascicolo")
public class DatiAperturaFascicoloDto {

	private DatiAnagraficiDto datiAnagraficiRappresentante;

	private IndirizzoDto domicilioFiscaleRappresentante;

	@ApiModelProperty(value = "Codice fiscale della persona giuridica o della ditta individuale")
	private String codiceFiscale;

	private String partitaIva;

	private String denominazione;

	private String naturaGiuridica;

	private IndirizzoDto ubicazioneDitta;
	
	private String denominazioneFascicolo;

	public DatiAnagraficiDto getDatiAnagraficiRappresentante() {
		return datiAnagraficiRappresentante;
	}

	public DatiAperturaFascicoloDto setDatiAnagraficiRappresentante(DatiAnagraficiDto datiAnagraficiRappresentante) {
		this.datiAnagraficiRappresentante = datiAnagraficiRappresentante;
		return this;
	}

	public IndirizzoDto getDomicilioFiscaleRappresentante() {
		return domicilioFiscaleRappresentante;
	}

	public DatiAperturaFascicoloDto setDomicilioFiscaleRappresentante(IndirizzoDto domicilioFiscaleRappresentante) {
		this.domicilioFiscaleRappresentante = domicilioFiscaleRappresentante;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DatiAperturaFascicoloDto setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public DatiAperturaFascicoloDto setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public DatiAperturaFascicoloDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public String getNaturaGiuridica() {
		return naturaGiuridica;
	}

	public DatiAperturaFascicoloDto setNaturaGiuridica(String naturaGiuridica) {
		this.naturaGiuridica = naturaGiuridica;
		return this;
	}

	public IndirizzoDto getUbicazioneDitta() {
		return ubicazioneDitta;
	}

	public DatiAperturaFascicoloDto setUbicazioneDitta(IndirizzoDto ubicazioneDitta) {
		this.ubicazioneDitta = ubicazioneDitta;
		return this;
	}
	
	public String getDenominazioneFascicolo() {
		return denominazioneFascicolo;
	}

	public DatiAperturaFascicoloDto setDenominazioneFascicolo(String denominazioneFascicolo) {
		this.denominazioneFascicolo = denominazioneFascicolo;
		return this;
	}
}
