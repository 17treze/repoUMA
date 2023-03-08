package it.tndigitale.a4g.client.anagrafica.persona.dto;

public class CaricaDtoV2 {
    private String descrizione;

    private String codiceFiscale;

    private AnagraficaDtoV2 anagrafica;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public AnagraficaDtoV2 getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(AnagraficaDtoV2 anagrafica) {
		this.anagrafica = anagrafica;
	}
}
