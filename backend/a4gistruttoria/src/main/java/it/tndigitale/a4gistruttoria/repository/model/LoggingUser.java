package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the A4GISTR_LOGGING database table.
 * 
 */
public class LoggingUser implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Long id;

	protected Integer version;
	
	private LocalDateTime dtEvento;

	private Long idEntita;

	private String tabella;

	private String tipoEvento;

	private String utente;

	public LoggingUser() {
		//Default empty constructor
	}

	public LocalDateTime getDtEvento() {
		return this.dtEvento;
	}

	public void setDtEvento(LocalDateTime dtEvento) {
		this.dtEvento = dtEvento;
	}

	public Long getIdEntita() {
		return this.idEntita;
	}

	public void setIdEntita(Long idEntita) {
		this.idEntita = idEntita;
	}

	public String getTabella() {
		return this.tabella;
	}

	public void setTabella(String tabella) {
		this.tabella = tabella;
	}

	public String getTipoEvento() {
		return this.tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getUtente() {
		return this.utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

}