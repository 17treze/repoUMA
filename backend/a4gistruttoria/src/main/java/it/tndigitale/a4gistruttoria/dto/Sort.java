package it.tndigitale.a4gistruttoria.dto;

public class Sort {
	
	public Sort(String[] sortBy) {
		super();
		this.sortBy = sortBy;
	}

	private String[] sortBy;

	public String[] getSortBy() {
		return sortBy;
	}

	public void setSortBy(String[] sortBy) {
		this.sortBy = sortBy;
	}

}
