package it.tndigitale.a4g.fascicolo.antimafia.dto;

public class IoItaliaMessage {

	private String codiceFiscale;
	private String oggetto;
	private String messaggio;


	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public IoItaliaMessage setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getOggetto() {
		return oggetto;
	}

	public IoItaliaMessage setOggetto(String oggetto) {
		this.oggetto = oggetto;
		return this;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public IoItaliaMessage setMessaggio(String messaggio) {
		this.messaggio = messaggio;
		return this;
	}

}
