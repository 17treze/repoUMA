package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "A4GT_DOMANDA_INTEGRATIVA")
@NamedQuery(name = "A4gtDomandaIntegrativa.findAll", query = "SELECT a FROM A4gtDomandaIntegrativa a")
public class A4gtDomandaIntegrativa extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DT_ULTIMO_AGGIORNAMENTO")
	private LocalDateTime dtUltimoAggiornamento;

	@Column(length = 50)
	private String stato;

	@Column
	private Boolean duplicato;
	
	@Column
	private Boolean controlloSuperato;
	
	@Column
	private String identificativo;

	// bi-directional many-to-one association to EsitoCalcoloCapoModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICH_ALLEVAM_ESITO", nullable = false)
	private EsitoCalcoloCapoModel esitoCalcoloCapo;

	public A4gtDomandaIntegrativa() {
	}

	public LocalDateTime getDtUltimoAggiornamento() {
		return this.dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(LocalDateTime dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public EsitoCalcoloCapoModel getEsitoCalcoloCapo() {
		return this.esitoCalcoloCapo;
	}

	public void setEsitoCalcoloCapo(EsitoCalcoloCapoModel esitoCalcoloCapo) {
		this.esitoCalcoloCapo = esitoCalcoloCapo;
	}
	
	public Boolean getDuplicato() {
		return duplicato;
	}

	public void setDuplicato(Boolean duplicato) {
		this.duplicato = duplicato;
	}

	public Boolean getControlloSuperato() {
		return controlloSuperato;
	}

	public void setControlloSuperato(Boolean controlloSuperato) {
		this.controlloSuperato = controlloSuperato;
	}
	
	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}


}