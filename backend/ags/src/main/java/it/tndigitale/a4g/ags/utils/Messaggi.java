package it.tndigitale.a4g.ags.utils;

public enum Messaggi {

	OK_MOVIMENTO("Movimento eseguito correttamente"), E_SALVATAGGIO_MOVIMENTO("Errore nel salvataggio del movimento"), E_MOVIMENTO_DOMANDA(
			"La domanda non può eseguire il movimento il movimento richiesto"), E_CARICAMENTO_DATI_DOMANDA("Impossibile caricare i dati di domanda");

	private String messaggio;

	Messaggi(String text) {
		this.messaggio = text;

	}

	public String getMessaggi() {
		return messaggio;
	}

}
