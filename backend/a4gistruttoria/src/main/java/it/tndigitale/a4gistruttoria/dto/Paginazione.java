package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

@Deprecated
public class Paginazione implements Serializable {

	private static final long serialVersionUID = 4268922379632858818L;

	private static final int NUMERO_ELEMENTI_PAGINA_DEFAULT = 20;
	private static final int PAGINA_DEFAULT = 1;
	
	private Integer numeroElementiPagina;
	private int pagina = PAGINA_DEFAULT;

	public Paginazione() {
		super();
	}
	
	public Paginazione(Integer numeroElementiPagina, int pagina) {
		super();
		this.numeroElementiPagina = numeroElementiPagina;
		this.pagina = pagina;
	}

	public Integer getNumeroElementiPagina() {
		return numeroElementiPagina;
	}

	public void setNumeroElementiPagina(Integer numeroElementiPagina) {
		this.numeroElementiPagina = numeroElementiPagina;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
	public static Paginazione of() {
		return new Paginazione(NUMERO_ELEMENTI_PAGINA_DEFAULT, PAGINA_DEFAULT);
	}

}

