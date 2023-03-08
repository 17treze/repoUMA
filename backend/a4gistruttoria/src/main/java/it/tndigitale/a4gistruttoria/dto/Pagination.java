package it.tndigitale.a4gistruttoria.dto;

public class Pagination {
	
	private String pagSize;
	private String  pagStart;
	
	public Pagination(String pagSize, String pagStart) {
		super();
		this.pagSize = pagSize;
		this.pagStart = pagStart;
	}
	
	public String getPagSize() {
		return pagSize;
	}
	public void setPagSize(String pagSize) {
		this.pagSize = pagSize;
	}
	public String getPagStart() {
		return pagStart;
	}
	public void setPagStart(String pagStart) {
		this.pagStart = pagStart;
	}

}
