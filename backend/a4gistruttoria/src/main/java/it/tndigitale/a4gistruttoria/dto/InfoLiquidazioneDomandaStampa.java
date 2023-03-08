package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class InfoLiquidazioneDomandaStampa {

	private String codiceElencoLiquidazione;
	private Boolean domandaNonRicevibile;
	private Boolean domandaNonAmmissibile;
	private Boolean domandaNonLiquidabile;

	public String getCodiceElencoLiquidazione() {
		return codiceElencoLiquidazione;
	}

	public void setCodiceElencoLiquidazione(String codiceElencoLiquidazione) {
		this.codiceElencoLiquidazione = codiceElencoLiquidazione;
	}

	public Boolean getDomandaNonRicevibile() {
		return domandaNonRicevibile;
	}

	public void setDomandaNonRicevibile(Boolean domandaNonRicevibile) {
		this.domandaNonRicevibile = domandaNonRicevibile;
	}

	public Boolean getDomandaNonAmmissibile() {
		return domandaNonAmmissibile;
	}

	public void setDomandaNonAmmissibile(Boolean domandaNonAmmissibile) {
		this.domandaNonAmmissibile = domandaNonAmmissibile;
	}

	public Boolean getDomandaNonLiquidabile() {
		return domandaNonLiquidabile;
	}

	public void setDomandaNonLiquidabile(Boolean domandaNonLiquidabile) {
		this.domandaNonLiquidabile = domandaNonLiquidabile;
	}

}
