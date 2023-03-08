/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class CalcoloAccoppiatiJobDto {

	private Long annoCampagna;
	private List<Long> idsDomande;

	public Long getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	public List<Long> getIdsDomande() {
		return idsDomande;
	}

	public void setIdsDomande(List<Long> idsDomande) {
		this.idsDomande = idsDomande;
	}
}
