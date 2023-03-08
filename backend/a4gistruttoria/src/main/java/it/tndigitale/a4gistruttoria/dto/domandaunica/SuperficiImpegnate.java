package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.io.Serializable;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;


public class SuperficiImpegnate implements Serializable {

	private static final long serialVersionUID = -3770481069935601232L;

	private Double supImpegnataLorda;
	private Double supImpegnataNetta;
	private RisultatiPaginati<RichiestaSuperficie> paginaSuperfici;
	
	public Double getSupImpegnataLorda() {
		return supImpegnataLorda;
	}
	public Double getSupImpegnataNetta() {
		return supImpegnataNetta;
	}
	public RisultatiPaginati<RichiestaSuperficie> getPaginaSuperfici() {
		return paginaSuperfici;
	}
	public void setSupImpegnataLorda(Double supImpegnataLorda) {
		this.supImpegnataLorda = supImpegnataLorda;
	}
	public void setSupImpegnataNetta(Double supImpegnataNetta) {
		this.supImpegnataNetta = supImpegnataNetta;
	}
	public void setPaginaSuperfici(RisultatiPaginati<RichiestaSuperficie> paginaSuperfici) {
		this.paginaSuperfici = paginaSuperfici;
	}

}
