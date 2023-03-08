package it.tndigitale.a4gistruttoria.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestDomandePerStatoPaged {

	@JsonProperty
	private Integer page;

	@JsonProperty
	private Integer size;

	@JsonProperty
	private Long idDatiSettore;

	@JsonProperty
	private String stato;

	@JsonProperty
	private boolean unpaged;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getIdDatiSettore() {
		return idDatiSettore;
	}

	public void setIdDatiSettore(Long idDatiSettore) {
		this.idDatiSettore = idDatiSettore;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public boolean isUnpaged() {
		return unpaged;
	}

	public void setUnpaged(boolean unpaged) {
		this.unpaged = unpaged;
	}

}
