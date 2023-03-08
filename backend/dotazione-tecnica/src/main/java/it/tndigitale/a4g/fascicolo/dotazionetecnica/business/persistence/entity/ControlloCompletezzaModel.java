package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_CONTROLLO_COMPLETEZZA", uniqueConstraints = {@UniqueConstraint(columnNames={"ID", "CUAA"})})
public class ControlloCompletezzaModel extends EntitaDominio {

	private static final long serialVersionUID = 8155026194496573899L;

	@Column(name = "CUAA", length = 16, nullable = false)
	private String cuaa;

	@Column(name = "TIPO_CONTROLLO", length = 100, nullable = false)
	private String tipoControllo;

	@Column(name = "ESITO", nullable = false)
	private Integer esito;

	@Column(name = "ID_CONTROLLO")
	private Integer idControllo;

	@Column(name = "DATA_ESECUZIONE")
	private LocalDateTime dataEsecuzione;

	@Column(name = "UTENTE", length = 100, nullable = false)
	private String utente;

	public String getCuaa() {
		return cuaa;
	}

	public ControlloCompletezzaModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public String getTipoControllo() {
		return tipoControllo;
	}

	public ControlloCompletezzaModel setTipoControllo(String tipoControllo) {
		this.tipoControllo = tipoControllo;
		return this;
	}

	public Integer getEsito() {
		return esito;
	}

	public ControlloCompletezzaModel setEsito(Integer esito) {
		this.esito = esito;
		return this;
	}

	public Integer getIdControllo() {
		return idControllo;
	}

	public ControlloCompletezzaModel setIdControllo(Integer idControllo) {
		this.idControllo = idControllo;
		return this;
	}

	public LocalDateTime getDataEsecuzione() {
		return dataEsecuzione;
	}

	public ControlloCompletezzaModel setDataEsecuzione(LocalDateTime dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
		return this;
	}

	public String getUtente() {
		return utente;
	}

	public ControlloCompletezzaModel setUtente(String utente) {
		this.utente = utente;
		return this;
	}
}
