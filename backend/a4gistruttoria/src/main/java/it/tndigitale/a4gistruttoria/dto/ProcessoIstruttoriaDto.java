package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

public class ProcessoIstruttoriaDto implements Serializable {

	private static final long serialVersionUID = -950562946872622673L;
	private Long idDatiSettore;
	private BigDecimal percentualeAvanzamento;
	private StatoProcesso statoProcesso;
	private TipoProcesso tipoProcesso;
	private Long idProcesso;
	private DatiElaborazioneProcesso datiElaborazioneProcesso;

	public Long getIdDatiSettore() {
		return idDatiSettore;
	}

	public void setIdDatiSettore(Long idDatiSettore) {
		this.idDatiSettore = idDatiSettore;
	}

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

	public DatiElaborazioneProcesso getDatiElaborazioneProcesso() {
		return datiElaborazioneProcesso;
	}

	public void setDatiElaborazioneProcesso(DatiElaborazioneProcesso datiElaborazioneProcesso) {
		this.datiElaborazioneProcesso = datiElaborazioneProcesso;
	}

}
