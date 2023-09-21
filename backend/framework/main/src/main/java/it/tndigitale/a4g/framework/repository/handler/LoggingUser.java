package it.tndigitale.a4g.framework.repository.handler;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoggingUser implements Serializable {
	private static final long serialVersionUID = 3757330499284388249L;
	
	@SuppressWarnings("unused")
	protected Long id;
	
	@SuppressWarnings("unused")
	protected Integer version;
	
	private LocalDateTime dtEvento;
	
	private Long idEntita;
	
	private String tabella;
	
	private String tipoEvento;
	
	private String utente;
	
	public LoggingUser() {
		super();
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
		return (this.utente == null ? "N.D." : this.utente);
	}
	
	public void setUtente(String utente) {
		this.utente = utente;
	}
}