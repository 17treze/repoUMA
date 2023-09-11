package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_LOGGING")
public class LoggingModel extends EntitaDominio {
	private static final long serialVersionUID = 2834135809795870573L;
	
	@Column(name="UTENTE", length = 500)
	private String utente;
	@Column(name="TIPO_EVENTO", length = 500)
	private String tipoEvento;
	@Column(name="TABELLA", length = 4000)
	private String tabella;
	@Column(name="ID_ENTITA", length = 20)
	private Long idEntita;
	@Column(name="DT_EVENTO")
	private LocalDateTime dtEvento;
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getTabella() {
		return tabella;
	}
	public void setTabella(String tabella) {
		this.tabella = tabella;
	}
	public Long getIdEntita() {
		return idEntita;
	}
	public void setIdEntita(Long idEntita) {
		this.idEntita = idEntita;
	}
	public LocalDateTime getDtEvento() {
		return dtEvento;
	}
	public void setDtEvento(LocalDateTime dtEvento) {
		this.dtEvento = dtEvento;
	}
}
