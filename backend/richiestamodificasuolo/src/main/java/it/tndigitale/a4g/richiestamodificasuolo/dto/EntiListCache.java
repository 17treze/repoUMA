package it.tndigitale.a4g.richiestamodificasuolo.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class EntiListCache {

	private LocalDateTime ultimoAggiornamento;
	
	private List<String> enti;

	public LocalDateTime getUltimoAggiornamento() {
		return ultimoAggiornamento;
	}

	public void setUltimoAggiornamento(LocalDateTime ultimoAggiornamento) {
		this.ultimoAggiornamento = ultimoAggiornamento;
	}

	public List<String> getEnti() {
		return enti;
	}

	public void setEnti(List<String> enti) {
		this.enti = enti;
	}
}
