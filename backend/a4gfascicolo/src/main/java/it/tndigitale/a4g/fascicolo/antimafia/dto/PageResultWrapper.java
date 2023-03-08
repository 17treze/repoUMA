package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.List;

public class PageResultWrapper<T> {
	
	private List<T> results;
	private Long total;
	private Integer pagSize;
	private Long pagStart;
	
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPagSize() {
		return pagSize;
	}
	public void setPagSize(Integer pagSize) {
		this.pagSize = pagSize;
	}
	public Long getPagStart() {
		return pagStart;
	}
	public void setPagStart(Long pagStart) {
		this.pagStart = pagStart;
	}
	

}
