package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

public class AggiornaStatoDomandeFilter {

	private List<Long> idsDomande;
	private String identificativoSostegno;
	private String identificativoStatoLavSostegno;
	private Long annoCampagna;
	private List<Long> idsDomandeDaEscludere;

	public List<Long> getIdsDomandeDaEscludere() {
		return idsDomandeDaEscludere;
	}

	public void setIdsDomandeDaEscludere(List<Long> idsDomandeDaEscludere) {
		this.idsDomandeDaEscludere = idsDomandeDaEscludere;
	}

	public List<Long> getIdsDomande() {
		return idsDomande;
	}

	public String getIdentificativoSostegno() {
		return identificativoSostegno;
	}

	public String getIdentificativoStatoLavSostegno() {
		return identificativoStatoLavSostegno;
	}

	public Long getAnnoCampagna() {
		return annoCampagna;
	}

	public void setIdsDomande(List<Long> idsDomande) {
		this.idsDomande = idsDomande;
	}

	public void setIdentificativoSostegno(String identificativoSostegno) {
		this.identificativoSostegno = identificativoSostegno;
	}

	public void setIdentificativoStatoLavSostegno(String identificativoStatoLavSostegno) {
		this.identificativoStatoLavSostegno = identificativoStatoLavSostegno;
	}

	public void setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

}
