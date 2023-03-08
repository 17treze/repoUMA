package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "sintesiCalcolo", "dettaglioCalcolo" })
public class DatiDomandaAccoppiato {

	private Map<String, String> sintesiCalcolo;
	private IDettaglioCalcolo dettaglioCalcolo;
	
	public Map<String, String> getSintesiCalcolo() {
		return sintesiCalcolo;
	}
	public void setSintesiCalcolo(Map<String, String> sintesiCalcolo) {
		this.sintesiCalcolo = sintesiCalcolo;
	}
	
	public IDettaglioCalcolo getDettaglioCalcolo() {
		return dettaglioCalcolo;
	}
	public void setDettaglioCalcolo(IDettaglioCalcolo dettaglioCalcolo) {
		this.dettaglioCalcolo = dettaglioCalcolo;
	}
}