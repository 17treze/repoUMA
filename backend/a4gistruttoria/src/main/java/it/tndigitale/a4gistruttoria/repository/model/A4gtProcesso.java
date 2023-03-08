package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name="A4GT_PROCESSO")
@NamedQuery(name="A4gtProcesso.findAll", query="SELECT a FROM A4gtProcesso a")
public class A4gtProcesso extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 880306828354946440L;

	@Lob
	@Column(name="DATI_ELABORAZIONE")
	private String datiElaborazione;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE")
	private Date dtFine;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO")
	private Date dtInizio;

	@Column(name="PERCENTUALE_AVANZAMENTO")
	private BigDecimal percentualeAvanzamento;

	@Column(name="TIPO")
	@Enumerated(EnumType.STRING)
	private TipoProcesso tipo;

	@Column(name="STATO")
	@Enumerated(EnumType.STRING)
	private StatoProcesso stato;

	public String getDatiElaborazione() {
		return this.datiElaborazione;
	}

	public void setDatiElaborazione(String datiElaborazione) {
		this.datiElaborazione = datiElaborazione;
	}

	public Date getDtFine() {
		return this.dtFine;
	}

	public void setDtFine(Date dtFine) {
		this.dtFine = dtFine;
	}

	public Date getDtInizio() {
		return this.dtInizio;
	}

	public void setDtInizio(Date dtInizio) {
		this.dtInizio = dtInizio;
	}

	public BigDecimal getPercentualeAvanzamento() {
		return this.percentualeAvanzamento;
	}

	public void setPercentualeAvanzamento(BigDecimal percentualeAvanzamento) {
		this.percentualeAvanzamento = percentualeAvanzamento;
	}

	public TipoProcesso getTipo() {
		return tipo;
	}

	public void setTipo(TipoProcesso tipo) {
		this.tipo = tipo;
	}

	public StatoProcesso getStato() {
		return stato;
	}

	public void setStato(StatoProcesso stato) {
		this.stato = stato;
	}
}