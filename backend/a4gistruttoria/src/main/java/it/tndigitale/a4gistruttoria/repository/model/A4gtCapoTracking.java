package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the A4GT_CAPO_TRACKING database table.
 * 
 */
@Entity
@Table(name="A4GT_CAPO_TRACKING")
@NamedQuery(name="A4gtCapoTracking.findAll", query="SELECT a FROM A4gtCapoTracking a")
public class A4gtCapoTracking extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	@Column(nullable=false)
	private String dati;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ULTIMO_AGGIORNAMENTO")
	private Date dtUltimoAggiornamento;

	//bi-directional one-to-one association to EsitoCalcoloCapoModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_RICH_ALLEVAM_ESITO", nullable=false)
	private EsitoCalcoloCapoModel esitoCalcoloCapo;

	public A4gtCapoTracking() {
	}

	public String getDati() {
		return this.dati;
	}

	public void setDati(String dati) {
		this.dati = dati;
	}

	public Date getDtUltimoAggiornamento() {
		return this.dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public EsitoCalcoloCapoModel getEsitoCalcoloCapo() {
		return this.esitoCalcoloCapo;
	}

	public void setEsitoCalcoloCapo(EsitoCalcoloCapoModel esitoCalcoloCapo) {
		this.esitoCalcoloCapo = esitoCalcoloCapo;
	}

}