package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

public class DomandaFilter {

	private List<StatoDomanda> stati;
	private List<String> codiciAgea;
	private List<Long> idsDomande;
	private Integer campagna;

	public List<StatoDomanda> getStati() {
		return stati;
	}
	
	public void setStati(List<StatoDomanda> stati) {
		this.stati = stati;
	}

	public List<String> getCodiciAgea() {
		return codiciAgea;
	}

	public void setCodiciAgea(List<String> codiciAgea) {
		this.codiciAgea = codiciAgea;
	}

	public List<Long> getIdsDomande() {
		return idsDomande;
	}

	public void setIdsDomande(List<Long> idsDomande) {
		this.idsDomande = idsDomande;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
}
