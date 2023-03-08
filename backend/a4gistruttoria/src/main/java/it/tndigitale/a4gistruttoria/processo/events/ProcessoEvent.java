package it.tndigitale.a4gistruttoria.processo.events;

import java.math.BigDecimal;
import java.util.Date;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

public class ProcessoEvent {
	private Long id;
	private BigDecimal percentualeAvanzamento;
	private StatoProcesso statoProcesso;
	private TipoProcesso tipoProcesso;
	private DatiElaborazioneProcesso datiElaborazioneProcesso;
	private Date dtFine;
	private Date dtInizio;

	public Long getId() {
		return id;
	}

	public BigDecimal getPercentualeAvanzamento() {
		return percentualeAvanzamento;
	}

	public StatoProcesso getStatoProcesso() {
		return statoProcesso;
	}

	public TipoProcesso getTipoProcesso() {
		return tipoProcesso;
	}

	public DatiElaborazioneProcesso getDatiElaborazioneProcesso() {
		return datiElaborazioneProcesso;
	}

	public Date getDtFine() {
		return dtFine;
	}

	public Date getDtInizio() {
		return dtInizio;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPercentualeAvanzamento(BigDecimal percentualeAvanzamento) {
		this.percentualeAvanzamento = percentualeAvanzamento;
	}

	public void setStatoProcesso(StatoProcesso statoProcesso) {
		this.statoProcesso = statoProcesso;
	}

	public void setTipoProcesso(TipoProcesso tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public void setDatiElaborazioneProcesso(DatiElaborazioneProcesso datiElaborazioneProcesso) {
		this.datiElaborazioneProcesso = datiElaborazioneProcesso;
	}

	public void setDtFine(Date dtFine) {
		this.dtFine = dtFine;
	}

	public void setDtInizio(Date dtInizio) {
		this.dtInizio = dtInizio;
	}

}
