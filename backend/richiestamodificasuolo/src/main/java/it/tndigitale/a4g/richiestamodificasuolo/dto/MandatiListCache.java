package it.tndigitale.a4g.richiestamodificasuolo.dto;

import java.time.LocalDateTime;
import java.util.List;

import it.tndigitale.a4g.fascicolo.client.model.Fascicolo;

public class MandatiListCache {

	private LocalDateTime ultimoAggiornamento;
	
	private List<Fascicolo> fascicoli;

	public LocalDateTime getUltimoAggiornamento() {
		return ultimoAggiornamento;
	}

	public void setUltimoAggiornamento(LocalDateTime ultimoAggiornamento) {
		this.ultimoAggiornamento = ultimoAggiornamento;
	}

	public List<Fascicolo> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(List<Fascicolo> fascicoli) {
		this.fascicoli = fascicoli;
	}

}
