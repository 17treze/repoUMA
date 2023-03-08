package it.tndigitale.a4g.uma.dto.richiesta;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;

public class RichiestaCarburantePagedFilter extends RichiestaCarburanteFilter {

	// Paginazione
	private Integer numeroElementiPagina;
	private Integer pagina;
	// Ordinamento
	private String proprieta;
	private Ordine ordine;

	public Integer getNumeroElementiPagina() {
		return numeroElementiPagina;
	}
	public RichiestaCarburantePagedFilter setNumeroElementiPagina(Integer numeroElementiPagina) {
		this.numeroElementiPagina = numeroElementiPagina;
		return this;
	}
	public Integer getPagina() {
		return pagina;
	}
	public RichiestaCarburantePagedFilter setPagina(Integer pagina) {
		this.pagina = pagina;
		return this;
	}
	public String getProprieta() {
		return proprieta;
	}
	public RichiestaCarburantePagedFilter setProprieta(String proprieta) {
		this.proprieta = proprieta;
		return this;
	}
	public Ordine getOrdine() {
		return ordine;
	}
	public RichiestaCarburantePagedFilter setOrdine(Ordine ordine) {
		this.ordine = ordine;
		return this;
	}
}
