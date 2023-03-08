package it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria;

import java.time.LocalDate;

public class ConfIstruttoriaRicevibilitaDto {

	private Long id;
	private Integer campagna;
	private LocalDate dataRicevibilita;
	private LocalDate dataScadenzaDomandaInizialeInRitardo;
	private LocalDate dataScadenzaDomandaRitiroParziale;
	private LocalDate dataScadenzaDomandaModificaInRitardo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public LocalDate getDataRicevibilita() {
		return dataRicevibilita;
	}

	public void setDataRicevibilita(LocalDate dataRicevibilita) {
		this.dataRicevibilita = dataRicevibilita;
	}

	public LocalDate getDataScadenzaDomandaInizialeInRitardo() {
		return dataScadenzaDomandaInizialeInRitardo;
	}

	public void setDataScadenzaDomandaInizialeInRitardo(LocalDate dataScadenzaDomandaInizialeInRitardo) {
		this.dataScadenzaDomandaInizialeInRitardo = dataScadenzaDomandaInizialeInRitardo;
	}

	public LocalDate getDataScadenzaDomandaRitiroParziale() {
		return dataScadenzaDomandaRitiroParziale;
	}

	public void setDataScadenzaDomandaRitiroParziale(LocalDate dataScadenzaDomandaRitiroParziale) {
		this.dataScadenzaDomandaRitiroParziale = dataScadenzaDomandaRitiroParziale;
	}
	
	public LocalDate getDataScadenzaDomandaModificaInRitardo() {
		return dataScadenzaDomandaModificaInRitardo;
	}

	public void setDataScadenzaDomandaModificaInRitardo(LocalDate dataScadenzaDomandaModificaInRitardo) {
		this.dataScadenzaDomandaModificaInRitardo = dataScadenzaDomandaModificaInRitardo;
	}
}
