package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "A4GT_CONTROLLO_COMPLETEZZA", uniqueConstraints = {@UniqueConstraint(columnNames={"ID", "CUAA"})})
public class ControlloCompletezzaModel extends EntitaDominio {

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

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getTipoControllo() {
		return tipoControllo;
	}

	public void setTipoControllo(String tipoControllo) {
		this.tipoControllo = tipoControllo;
	}

	public Integer getEsito() {
		return esito;
	}

	public void setEsito(Integer esito) {
		this.esito = esito;
	}

	public Integer getIdControllo() {
		return idControllo;
	}

	public void setIdControllo(Integer idControllo) {
		this.idControllo = idControllo;
	}

	public LocalDateTime getDataEsecuzione() {
		return dataEsecuzione;
	}

	public void setDataEsecuzione(LocalDateTime dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}
}
