package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.util.List;

@Deprecated
public class AzRicercaFilter extends DomandaUnicaRicercaFilter implements Serializable {
	
	private static final long serialVersionUID = -459189769986869093L;
	
	private Long idDatiSettore;
	
	private List<String> interventi;
	
	public Long getIdDatiSettore() {
		return idDatiSettore;
	}
	
	public void setIdDatiSettore(final Long idDatiSettore) {
		this.idDatiSettore = idDatiSettore;
	}
	
	public List<String> getInterventi() {
		return interventi;
	}
	
	public void setInterventi(final List<String> interventi) {
		this.interventi = interventi;
	}
}
