package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class FiltroRicercaDomandeIstruttoriaRid {
	private String pascoli;
	private String campione;
	private String giovane;
	private String riserva;
	private String anomalie;

	public String getAnomalie() {
		return anomalie;
	}

	public void setAnomalie(String anomalie) {
		this.anomalie = anomalie;
	}

	private List<String> anomalieERROR;
	private List<String> anomalieINFO;
	private List<String> anomalieWARNING;

	public void fixDati() {
		pascoli = fixValue(pascoli);
		campione = fixValue(campione);
		giovane = fixValue(giovane);
		riserva = fixValue(riserva);
		anomalie = fixValue(anomalie);
	}

	private String fixValue(String value) {
		String newValue;
		if (value != null && !value.isEmpty()) {
			newValue = value.trim().toUpperCase();
			if (value.equals("TUTTI"))
				newValue = null;
		} else
			newValue = null;

		return newValue;
	}

	public String getPascoli() {
		return pascoli;
	}

	public void setPascoli(String pascoli) {
		this.pascoli = pascoli;
	}

	public String getCampione() {
		return campione;
	}

	public void setCampione(String campione) {
		this.campione = campione;
	}

	public String getGiovane() {
		return giovane;
	}

	public void setGiovane(String giovane) {
		this.giovane = giovane;
	}

	public String getRiserva() {
		return riserva;
	}

	public void setRiserva(String riserva) {
		this.riserva = riserva;
	}

	public List<String> getAnomalieERROR() {
		return anomalieERROR;
	}

	public void setAnomalieERROR(List<String> anomalieERROR) {
		this.anomalieERROR = anomalieERROR;
	}

	public List<String> getAnomalieINFO() {
		return anomalieINFO;
	}

	public void setAnomalieINFO(List<String> anomalieINFO) {
		this.anomalieINFO = anomalieINFO;
	}

	public List<String> getAnomalieWARNING() {
		return anomalieWARNING;
	}

	public void setAnomalieWARNING(List<String> anomalieWARNING) {
		this.anomalieWARNING = anomalieWARNING;
	}

}
