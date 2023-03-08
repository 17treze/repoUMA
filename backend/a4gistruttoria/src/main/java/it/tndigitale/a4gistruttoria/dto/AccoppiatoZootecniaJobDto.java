package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class AccoppiatoZootecniaJobDto {

	private Long campagna;
	private List<Long> idsDomande;
	private List<Long> idsDomandeDaEscludere;
	private String statoLavorazione;
	private AzRicercaFilter azRicercaFilter;

	public Long getCampagna() {
		return campagna;
	}

	public void setCampagna(Long campagna) {
		this.campagna = campagna;
	}

	public List<Long> getIdsDomande() {
		return idsDomande;
	}

	public void setIdsDomande(List<Long> idsDomande) {
		this.idsDomande = idsDomande;
	}

	public List<Long> getIdsDomandeDaEscludere() {
		return idsDomandeDaEscludere;
	}

	public void setIdsDomandeDaEscludere(List<Long> idsDomandeDaEscludere) {
		this.idsDomandeDaEscludere = idsDomandeDaEscludere;
	}

	public String getStatoLavorazione() {
		return statoLavorazione;
	}

	public void setStatoLavorazione(String statoLavorazione) {
		this.statoLavorazione = statoLavorazione;
	}

	public AzRicercaFilter getAzRicercaFilter() {
		return azRicercaFilter;
	}

	public void setAzRicercaFilter(AzRicercaFilter azRicercaFilter) {
		this.azRicercaFilter = azRicercaFilter;
	}

}
