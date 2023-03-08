package it.tndigitale.a4g.ags.dto;

import java.time.LocalDate;

public class IstruttoriaGraficaDuDto {
	
	private Long idDomanda;
	private String cuaa;
	private LocalDate dataIstruttoriaGrafica;
	private LocalDate dataRiferimento;
	private Integer anno;

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public LocalDate getDataIstruttoriaGrafica() {
		return dataIstruttoriaGrafica;
	}

	public void setDataIstruttoriaGrafica(LocalDate dataIstruttoriaGrafica) {
		this.dataIstruttoriaGrafica = dataIstruttoriaGrafica;
	}

	public LocalDate getDataRiferimento() {
		return dataRiferimento;
	}

	public void setDataRiferimento(LocalDate dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}
	
	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
}
