package it.tndigitale.a4g.fascicolo.antimafia.dto;

public class AllegatoFamiliariConviventiResult {

	private AllegatoFamiliariConviventi allegatoFamiliariConviventi;
	private Carica carica;
	private String esito;
	
	public AllegatoFamiliariConviventi getAllegatoFamiliariConviventi() {
		return allegatoFamiliariConviventi;
	}
	public String getEsito() {
		return esito;
	}
	public void setAllegatoFamiliariConviventi(AllegatoFamiliariConviventi allegatoFamiliariConviventi) {
		this.allegatoFamiliariConviventi = allegatoFamiliariConviventi;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public Carica getCarica() {
		return carica;
	}
	public void setCarica(Carica carica) {
		this.carica = carica;
	}
	
	
}
