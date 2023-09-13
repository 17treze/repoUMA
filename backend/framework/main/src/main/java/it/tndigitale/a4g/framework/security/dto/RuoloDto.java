package framework.main.src.main.java.it.tndigitale.a4g.framework.security.dto;

import java.io.Serializable;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class RuoloDto implements Serializable {

	private String id;
	private String descrizione;
	private String applicazione;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getApplicazione() {
		return applicazione;
	}
	public void setApplicazione(String applicazione) {
		this.applicazione = applicazione;
	}
	@Override
	public String toString() {
		return "RuoloDto [id=" + id + ", descrizione=" + descrizione + ", applicazione=" + applicazione + "]";
	}
	
	
}
