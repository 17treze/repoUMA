package it.tndigitale.a4g.uma.dto.consumi;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;

public class DichiarazioneConsumiPagedFilter extends DichiarazioneConsumiFilter {

	// Paginazione
	private Integer numeroElementiPagina;
	private Integer pagina;
	// Ordinamento
	private String proprieta;
	private Ordine ordine;

	public Integer getNumeroElementiPagina() {
		return numeroElementiPagina;
	}
	public DichiarazioneConsumiPagedFilter setNumeroElementiPagina(Integer numeroElementiPagina) {
		this.numeroElementiPagina = numeroElementiPagina;
		return this;
	}
	public Integer getPagina() {
		return pagina;
	}
	public DichiarazioneConsumiPagedFilter setPagina(Integer pagina) {
		this.pagina = pagina;
		return this;
	}
	public String getProprieta() {
		return proprieta;
	}
	public DichiarazioneConsumiPagedFilter setProprieta(String proprieta) {
		this.proprieta = proprieta;
		return this;
	}
	public Ordine getOrdine() {
		return ordine;
	}
	public DichiarazioneConsumiPagedFilter setOrdine(Ordine ordine) {
		this.ordine = ordine;
		return this;
	}
}
