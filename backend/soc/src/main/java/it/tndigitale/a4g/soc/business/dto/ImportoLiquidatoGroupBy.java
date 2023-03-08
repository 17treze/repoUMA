package it.tndigitale.a4g.soc.business.dto;

import java.util.Objects;

public class ImportoLiquidatoGroupBy {
	
	public ImportoLiquidatoGroupBy(String tipoBilancio, Integer anno, Long progressivo) {
		super();
		this.tipoBilancio = tipoBilancio;
		this.anno = anno;
		this.progressivo = progressivo;
	}
	
	private String tipoBilancio;
	private Integer anno;
	private Long progressivo;
	
	public String getTipoBilancio() {
		return tipoBilancio;
	}
	public void setTipoBilancio(String tipoBilancio) {
		this.tipoBilancio = tipoBilancio;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public Long getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Long progressivo) {
		this.progressivo = progressivo;
	}
	@Override
	public int hashCode() {
		return Objects
			.hash(anno, progressivo, tipoBilancio);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
			.getClass())
			return false;
		ImportoLiquidatoGroupBy other = (ImportoLiquidatoGroupBy) obj;
		return Objects
			.equals(anno, other.anno)
				&& Objects
					.equals(progressivo, other.progressivo)
				&& Objects
					.equals(tipoBilancio, other.tipoBilancio);
	}

}
