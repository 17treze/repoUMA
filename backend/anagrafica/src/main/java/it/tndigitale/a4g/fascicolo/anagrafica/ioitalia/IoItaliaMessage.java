package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import java.time.Period;

public class IoItaliaMessage {
	private String codiceFiscale;
	private String oggetto;
	private String messaggio;
	private Period period;
	private Integer numMaxTentativi;
	
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

	public Period getPeriod() {
		return period;
	}

	public IoItaliaMessage setPeriod(Period period) {
		this.period = period;
		return this;
	}

	public Integer getNumMaxTentativi() {
		return numMaxTentativi;
	}

	public IoItaliaMessage setNumMaxTentativi(Integer numMaxTentativi) {
		this.numMaxTentativi = numMaxTentativi;
		return this;
	}

}
