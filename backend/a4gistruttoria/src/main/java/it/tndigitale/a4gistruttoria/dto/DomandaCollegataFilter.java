package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

public class DomandaCollegataFilter {

	private String cuaa;
	private TipoDomandaCollegata tipoDomanda;
	private Long idDomanda;
	private Long campagna;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	
	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public Long getCampagna() {
		return campagna;
	}

	public void setCampagna(Long campagna) {
		this.campagna = campagna;
	}
	
	public String validate() {
		StringBuilder sb = new StringBuilder();
		if (this.cuaa == null || this.cuaa.isEmpty()) {
			sb.append("Il cuaa non può essere vuoto. ");
		}
		return sb.toString();
	}

	public TipoDomandaCollegata getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(TipoDomandaCollegata tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}	

	
}
