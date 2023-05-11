package it.tndigitale.a4g.uma.dto;

import java.math.BigDecimal;

public class CoefficienteDto {
	
	private Long id;
	private BigDecimal coefficiente;
	private Integer annoInizio;
	private Integer annoFine;
	private Long idLavorazione;
	
	public Long getId() {
		return this.id;
	}
	
	public CoefficienteDto setId(Long id) {
		this.id = id;
		return this;
	}
	
	public BigDecimal getCoefficiente() {
		return coefficiente;
	}
	
	public CoefficienteDto setCoefficiente(BigDecimal coefficiente) {
		this.coefficiente = coefficiente;
		return this;
	}
	
	public Long getIdLavorazione() {
		return idLavorazione;
	}
	
	public CoefficienteDto setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
		return this;
	}
	
	public Integer getAnnoFine() {
		return this.annoFine;
	}
	
	public CoefficienteDto setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	
	public Integer getAnnoInizio() {
		return this.annoInizio;
	}
	
	public CoefficienteDto setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	
}