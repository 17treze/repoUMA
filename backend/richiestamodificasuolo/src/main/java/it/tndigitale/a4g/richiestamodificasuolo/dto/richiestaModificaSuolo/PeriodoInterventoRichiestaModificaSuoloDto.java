package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PeriodoInterventoRichiestaModificaSuoloDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDateTime getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}
}
