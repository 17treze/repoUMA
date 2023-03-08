package it.tndigitale.a4g.ags.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_ISTRUTTORIA_GRAFICA_DU")
public class ViewIstruttoriaGraficaDuEntity {
	
	@Id
	@Column(name = "ID_DOMANDA")
	private Long idDomanda;
	
	@Column(name = "CUAA")
	private String cuaa;
	
	@Column(name = "DATA_ISTRUTTORIA_GRAFICA")
	private LocalDate dataIstruttoriaGrafica;

	@Column(name = "DATA_RIFERIMENTO")
	private LocalDate dataRiferimento;
	
	@Column(name = "ANNO")
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
