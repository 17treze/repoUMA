package framework.main.src.main.java.it.tndigitale.a4g.framework.security.dto;

import java.io.Serializable;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import java.util.List;

public class RuoloAppDto implements Serializable {

	private String applicazione;
	private List<RuoloDto> ruoli;
	
	public String getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(String applicazione) {
		this.applicazione = applicazione;
	}
	public List<RuoloDto> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<RuoloDto> ruoli) {
		this.ruoli = ruoli;
	}
	@Override
	public String toString() {
		return "RuoloAppDto [applicazione=" + applicazione + ", ruoli=" + ruoli + "]";
	}

}
