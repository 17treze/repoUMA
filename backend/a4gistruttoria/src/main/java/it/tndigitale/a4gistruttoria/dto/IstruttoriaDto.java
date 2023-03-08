package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public class IstruttoriaDto {
	
	private Long id;
	private Sostegno sostegno;
	private String statoIstruttoria;
	private Boolean bloccataBool;
	private Boolean erroreCalcolo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Sostegno getSostegno() {
		return sostegno;
	}
	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}
	public String getStatoIstruttoria() {
		return statoIstruttoria;
	}
	public void setStatoIstruttoria(String statoIstruttoria) {
		this.statoIstruttoria = statoIstruttoria;
	}
	public Boolean getBloccataBool() {
		return bloccataBool;
	}
	public void setBloccataBool(Boolean bloccataBool) {
		this.bloccataBool = bloccataBool;
	}
	public Boolean getErroreCalcolo() {
		return this.erroreCalcolo;
	}
	public void setErroreCalcolo(Boolean erroreCalcolo) {
		this.erroreCalcolo = erroreCalcolo;
	}
}
