package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class DatiElaborazioneProcesso {
	private String totale;
	private String parziale;
	private String daElaborare;
	private List<String> gestite;
	private List<String> conProblemi;
	private String esito;
	
	public String getTotale() {
		return totale;
	}
	public void setTotale(String totale) {
		this.totale = totale;
	}
	public String getParziale() {
		return parziale;
	}
	public void setParziale(String parziale) {
		this.parziale = parziale;
	}
	public String getDaElaborare() {
		return daElaborare;
	}
	public void setDaElaborare(String daElaborare) {
		this.daElaborare = daElaborare;
	}
	public List<String> getGestite() {
		return gestite;
	}
	public void setGestite(List<String> gestite) {
		this.gestite = gestite;
	}
	public List<String> getConProblemi() {
		return conProblemi;
	}
	public void setConProblemi(List<String> conProblemi) {
		this.conProblemi = conProblemi;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
}
