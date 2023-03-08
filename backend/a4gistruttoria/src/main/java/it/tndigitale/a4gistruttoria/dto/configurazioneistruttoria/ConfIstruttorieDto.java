package it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria;

import java.time.LocalDate;

public class ConfIstruttorieDto {

	private Long id;
	private Integer campagna;
	private LocalDate dtScadenzaDomandeIniziali;
	private Double percentualePagamento;
	private Double percentualeDisciplinaFinanziaria;
	private LocalDate dtScadenzaDomandeModifica;

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

	public LocalDate getDtScadenzaDomandeIniziali() {
		return dtScadenzaDomandeIniziali;
	}

	public void setDtScadenzaDomandeIniziali(LocalDate dtScadenzaDomandeIniziali) {
		this.dtScadenzaDomandeIniziali = dtScadenzaDomandeIniziali;
	}

	public Double getPercentualePagamento() {
		return percentualePagamento;
	}

	public void setPercentualePagamento(Double percentualePagamento) {
		this.percentualePagamento = percentualePagamento;
	}

	public Double getPercentualeDisciplinaFinanziaria() {
		return percentualeDisciplinaFinanziaria;
	}

	public void setPercentualeDisciplinaFinanziaria(Double percentualeDisciplinaFinanziaria) {
		this.percentualeDisciplinaFinanziaria = percentualeDisciplinaFinanziaria;
	}
	
	public LocalDate getDtScadenzaDomandeModifica() {
		return dtScadenzaDomandeModifica;
	}

	public void setDtScadenzaDomandeModifica(LocalDate dtScadenzaDomandeModifica) {
		this.dtScadenzaDomandeModifica = dtScadenzaDomandeModifica;
	}
}
