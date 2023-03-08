package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

import java.io.Serializable;
import java.math.BigDecimal;

public class Processo implements Serializable {
	private static final long serialVersionUID = 7957311852606029516L;

	private BigDecimal percentualeAvanzamento;
	private StatoProcesso statoProcesso;
	private TipoProcesso tipoProcesso;
	private Long idProcesso;

	public BigDecimal getPercentualeAvanzamento() {
		return percentualeAvanzamento;
	}

	public void setPercentualeAvanzamento(BigDecimal percentualeAvanzamento) {
		this.percentualeAvanzamento = percentualeAvanzamento;
	}

	public StatoProcesso getStatoProcesso() {
		return statoProcesso;
	}

	public void setStatoProcesso(StatoProcesso statoProcesso) {
		this.statoProcesso = statoProcesso;
	}

	public TipoProcesso getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(TipoProcesso tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}

}
