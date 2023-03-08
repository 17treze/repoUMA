package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(description = "Periodo temporale")
public class PeriodoDtoV2 {
    @ApiModelProperty(value = "Inizio del periodo", required = true)
    private LocalDate dataInizio;

    @ApiModelProperty(value = "Termine del periodo")
    private LocalDate dataFine;

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}
}
