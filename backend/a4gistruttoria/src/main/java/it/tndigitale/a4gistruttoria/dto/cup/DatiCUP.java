package it.tndigitale.a4gistruttoria.dto.cup;

public class DatiCUP {

	private String idProgetto;
	private String totaleImportoRichiesto;
	private String tipologiaCopertura;
	private String contributoRichiesto;
	private String cuaa;
	private String descrizioneCodice;
	private String naturaCup;
	private String tipologiaCup;
	private String settoreCup;
	private String sottosettoreCup;
	private String categoriaCup;
	
	public String getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(String idProgetto) {
		this.idProgetto = idProgetto;
	}
	public String getTotaleImportoRichiesto() {
		return totaleImportoRichiesto;
	}
	public void setTotaleImportoRichiesto(String totaleImportoRichiesto) {
		this.totaleImportoRichiesto = totaleImportoRichiesto;
	}
	public String getTipologiaCopertura() {
		return tipologiaCopertura;
	}
	public void setTipologiaCopertura(String tipologiaCopertura) {
		this.tipologiaCopertura = tipologiaCopertura;
	}
	public String getContributoRichiesto() {
		return contributoRichiesto;
	}
	public void setContributoRichiesto(String contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getDescrizioneCodice() {
		return descrizioneCodice;
	}
	public void setDescrizioneCodice(String descrizioneCodice) {
		this.descrizioneCodice = descrizioneCodice;
	}
	public String getNaturaCup() {
		return naturaCup;
	}
	public void setNaturaCup(String naturaCup) {
		this.naturaCup = naturaCup;
	}
	public String getTipologiaCup() {
		return tipologiaCup;
	}
	public void setTipologiaCup(String tipologiaCup) {
		this.tipologiaCup = tipologiaCup;
	}
	public String getSettoreCup() {
		return settoreCup;
	}
	public void setSettoreCup(String settoreCup) {
		this.settoreCup = settoreCup;
	}
	public String getSottosettoreCup() {
		return sottosettoreCup;
	}
	public void setSottosettoreCup(String sottosettoreCup) {
		this.sottosettoreCup = sottosettoreCup;
	}
	public String getCategoriaCup() {
		return categoriaCup;
	}
	public void setCategoriaCup(String categoriaCup) {
		this.categoriaCup = categoriaCup;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiCUP [idProgetto=");
		builder.append(idProgetto);
		builder.append(", totaleImportoRichiesto=");
		builder.append(totaleImportoRichiesto);
		builder.append(", contributoRichiesto=");
		builder.append(contributoRichiesto);
		builder.append(", cuaa=");
		builder.append(cuaa);
		builder.append(", descrizioneCodice=");
		builder.append(descrizioneCodice);
		builder.append(", naturaCup=");
		builder.append(naturaCup);
		builder.append(", tipologiaCup=");
		builder.append(tipologiaCup);
		builder.append(", settoreCup=");
		builder.append(settoreCup);
		builder.append(", sottosettoreCup=");
		builder.append(sottosettoreCup);
		builder.append(", categoriaCup=");
		builder.append(categoriaCup);
		builder.append("]");
		return builder.toString();
	}
}
